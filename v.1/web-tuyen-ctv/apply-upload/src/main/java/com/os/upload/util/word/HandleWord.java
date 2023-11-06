package com.os.upload.util.word;

import com.os.upload.util.ImageParser;
import com.os.upload.util.WmfUtils;
import com.os.upload.util.word.item.IWordNumber;
import com.os.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath;
import org.openxmlformats.schemas.officeDocument.x2006.sharedTypes.STVerticalAlignRun;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public abstract class HandleWord {

    public static void handleWordNumber(StringBuilder sb,
                                        Map<BigInteger, IWordNumber> wordNumberMap,
                                        XWPFParagraph paragraph){
        String prefix = null;
        if (wordNumberMap != null
                && paragraph.getNumID() != null
                && paragraph.getNumFmt() != null){
            IWordNumber wn = wordNumberMap.get(paragraph.getNumID());
            if(wn != null){
                prefix = wn.nextNum();
            }
            else {
                IWordNumber newWordNumber = WordNumberFactory.getWordNumber(paragraph.getNumFmt(), paragraph.getNumLevelText());
                if(newWordNumber != null){
                    wordNumberMap.put(paragraph.getNumID(), newWordNumber);
                    prefix = newWordNumber.nextNum();
                }
            }
        }

        if(StringUtils.isNotBlank(prefix)) sb.append(prefix);
    }

    public static void handleParagraph(StringBuilder content, IBodyElement body, Map<BigInteger, IWordNumber> wordNUmberMap, ImageParser imageParser){
        XWPFParagraph paragraph = (XWPFParagraph) body;
        if(paragraph.isEmpty() || paragraph.isWordWrap() || paragraph.isPageBreak()) return;

        String tagName = "p";
        content.append("<" + tagName + ">");

        HandleWord.handleWordNumber(content, wordNUmberMap, paragraph);

        ParagraphChildOrderManager runOrMaths = new ParagraphChildOrderManager(paragraph);
        List<Object> childList = runOrMaths.getChildList();
        for(Object child : childList){
            if (child instanceof XWPFRun){
                HandleWord.handleParagraphRun(content, (XWPFRun) child, imageParser);
            }
            else if (child instanceof CTOMath){
                HandleWord.handleParagraphOMath(content, (CTOMath) child, imageParser);
            }
        }
        content.append("</" + tagName + ">");
    }

    public static void handleParagraphRun(StringBuilder content, XWPFRun run, ImageParser imageParser){
        List<XWPFPicture> pics = run.getEmbeddedPictures();
        if(pics != null && pics.size() > 0){
            handleParagraphRunsImage(content, pics, imageParser);
        }
        else if(isMath(run)){
            handleParagraphRunsImageMath(content, run, imageParser);
        }
        else {
            handleParagraphRunsWithText(content, run);
        }
    }

    private static void handleParagraphRunsImage(StringBuilder content, List<XWPFPicture> pics, ImageParser imageParser){
        for(XWPFPicture pic : pics){
            String desc = pic.getDescription();

            String path = imageParser.parse(pic.getPictureData().getData(), pic.getPictureData().getFileName());
            log.debug("=====> pic.getPictureData().getFileName() == {}", pic.getPictureData().getFileName());

            CTPicture ctPicture = pic.getCTPicture();
            Node domNode = ctPicture.getDomNode();

            Node extNode = W3cNodeUtil.getChildChainNode(domNode, "pic:spPr", "a:ext");
            NamedNodeMap attributes = extNode.getAttributes();
            if(attributes != null && attributes.getNamedItem("cx") != null){
                int width = WordMyUnits.emuToPx(new Double(attributes.getNamedItem("cx").getNodeValue()));
                int height = WordMyUnits.emuToPx(new Double(attributes.getNamedItem("cy").getNodeValue()));
                content.append(String.format("<img src=\"%s\" width=\"%d\" height=\"%d\"/>", path, width, height));
            }
            else{
                content.append(String.format("<img src=\"%s\"/>", path));
            }
        }
    }

    private static boolean isMath(XWPFRun run){
        Node runNode = run.getCTR().getDomNode();
        Node objectNode = W3cNodeUtil.getChildNode(runNode, "w:object");
        if(objectNode == null) return false;

        Node shapeNode = W3cNodeUtil.getChildNode(objectNode, "v:shape");
        if(shapeNode == null) return false;

        Node imageNode = W3cNodeUtil.getChildNode(shapeNode, "v:imagedata");
        if(imageNode == null) return false;

        Node binNode = W3cNodeUtil.getChildNode(objectNode, "o:OLEObject");
        if(binNode == null) return false;

        return true;
    }

    private static void handleParagraphRunsImageMath(StringBuilder content, XWPFRun run, ImageParser imageParser){
        Node runNode = run.getCTR().getDomNode();
        XWPFDocument runDocument = run.getDocument();
        Node objectNode = W3cNodeUtil.getChildNode(runNode, "w:object");
        if (objectNode == null) return;

        Node shapeNode = W3cNodeUtil.getChildNode(objectNode, "v:shape");
        if (shapeNode == null) return;

        Node imageNode = W3cNodeUtil.getChildNode(shapeNode, "v:imagedata");
        if (imageNode == null) return;

        Node binNode = W3cNodeUtil.getChildNode(objectNode, "o:OLEObject");
        if (binNode == null) return;

        NamedNodeMap shapeAttrs = shapeNode.getAttributes();

        String style = shapeAttrs.getNamedItem("style").getNodeValue();

        NamedNodeMap imageAttrs = imageNode.getAttributes();

        String imageRid = imageAttrs.getNamedItem("r:id").getNodeValue();

        PackagePart imgPart = runDocument.getPartById(imageRid);

        String fullName = imgPart.getPartName().getName();
        log.debug("=====> pic.Match.fullName: {}", fullName);
        String extName = imgPart.getPartName().getExtension();
        Pattern pattern = Pattern.compile("\\w+\\." + extName);
        Matcher matcher = pattern.matcher(fullName);
        if(matcher.find()){
            fullName = matcher.group();
        }
        log.debug("=====> pic.Match.name: {}", fullName);
        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            WmfUtils.toSvg(imgPart.getInputStream(), out);
            fullName = fullName.replace(extName, "svg");
            String path = imageParser.parse(new ByteArrayInputStream(out.toByteArray()), fullName);
            content.append("<img src=\"").append(path).append("\" style=\"").append(style).append("\"/>");
        }
        catch (IOException e){
            log.error("=====> save image into folder, failed: {}", e.getMessage(), e);
        }
    }

    private static void handleParagraphRunsWithText(StringBuilder content, XWPFRun run){
        String c = run.toString();
        c = escapeHtmlTag(c);
        if (c == null || c.length() == 0) return;

        if(run.getVerticalAlignment() != null){
            STVerticalAlignRun.Enum va = run.getVerticalAlignment();
            if(va.equals(STVerticalAlignRun.SUBSCRIPT)){
                c = "<sub>" + c + "</sub>";
            }
            else if(va.equals(STVerticalAlignRun.SUPERSCRIPT)){
                c = "<sup>" + c + "</sup>";
            }
        }

        if (run.isBold()){
            c = "<b>" + c + "</b>";
        }
        else if (run.isItalic()){
            c = "<i>" + c + "</i>";
        }
        else if (run.isStrikeThrough()){
            c = "<strike>" + c + "</strike>";
        }

        if (run.getUnderline() != null && run.getUnderline() != UnderlinePatterns.NONE){
            c = "<span style = 'text-decoration: underline'>" + c + "</span>";
        }

        if (run.getColor() != null){
            c = "<span style='color:#'>" + run.getColor().toLowerCase() + ";'>" + c + "</span>";
        }

        content.append(c);
    }

    private static String escapeHtmlTag(String text){
        text = text.replace("&", "&amp;");
        text = text.replace("<", "&lt;");
        text = text.replace(">", "&gt;");
        return text;
    }

    private static void handleParagraphOMath(StringBuilder content, CTOMath child, ImageParser imageParser){
        String s = OmmlUtils.convertOmathToPng(child, imageParser);
        content.append(s);
    }

    public static String handleTable(StringBuilder content, IBodyElement body, Map<BigInteger, IWordNumber> wordNumberMap, ImageParser imageParser){
        XWPFTable table = (XWPFTable) body;
        List<XWPFTableRow> rows = table.getRows();
        content.append("<table class=\"paper-table\">");
        for(XWPFTableRow row : rows){
            content.append("<tr>");
            List<XWPFTableCell> cells = row.getTableCells();
            for(XWPFTableCell cell : cells){
                content.append("<td>");
                List<XWPFParagraph> paragraphs = cell.getParagraphs();
                for(XWPFParagraph paragraph : paragraphs){
                    handleParagraph(content, paragraph, wordNumberMap, imageParser);
                }
                content.append("</td>\n");
            }
            content.append("</tr>\n");
        }
        return null;
    }
}
