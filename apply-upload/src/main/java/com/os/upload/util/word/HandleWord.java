package com.os.upload.util.word;

import com.os.upload.util.ImageParser;
import com.os.upload.util.WmfUtils;
import com.os.upload.util.word.item.IWordNumber;
import com.os.upload.util.word.item.WordNumberFactory;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public abstract class HandleWord {


    public static void handleParagraph(StringBuilder content, IBodyElement body, ImageParser imageParser){
        XWPFParagraph paragraph = (XWPFParagraph) body;
        if(paragraph.isEmpty() || paragraph.isWordWrap() || paragraph.isPageBreak()) return;

        String tagName = "p";
        content.append("<" + tagName + ">");

        Map<BigInteger, IWordNumber> wordNumberMap = new HashMap<>();
        handleWordNumber(content, wordNumberMap, paragraph);

        ParagraphChildOrderManager runOrMaths = new ParagraphChildOrderManager(paragraph);
        List<Object> childList = runOrMaths.getChildList();
        for(Object child : childList){
            if (child instanceof XWPFRun){
                handleParagraphRun(content, (XWPFRun) child, imageParser);
            }
            else if (child instanceof CTOMath){
                handleParagraphOMath(content, (CTOMath) child, imageParser);
            }
        }
        content.append("</" + tagName + ">");
    }

    /**
     * Cái này áp dụng cho các căn lề 1,2,3,...; a,b,c,... trong file Word.
     * @param sb
     * @param wordNumberMap
     * @param paragraph
     */
    private static void handleWordNumber(StringBuilder sb,
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


    /**
     * Xử lý hình ảnh có trong XWPFRun
     * @param content
     * @param pics
     * @param imageParser
     */
    private static void handleParagraphRunsImage(StringBuilder content, List<XWPFPicture> pics, ImageParser imageParser){
        for(XWPFPicture pic : pics){
            String desc = pic.getDescription();

            String path = imageParser.parse(pic.getPictureData().getData(), pic.getPictureData().getFileName());
            log.debug("----->>> pic.getPictureData().getFileName() == {}", pic.getPictureData().getFileName());

            CTPicture ctPicture = pic.getCTPicture();
            Node domNode = ctPicture.getDomNode();

            Node extNode = W3cNodeUtil.getChildChainNode(domNode, "pic:spPr", "a:ext");
            NamedNodeMap attributes = extNode.getAttributes();

            // todo chưa tạo được capture của hình ảnh.
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

    /**
     * Xử lý các hình ảnh toán học được chèn vào câu văn XWPFRun.
     * Các hình ảnh được lưu ở định dạng svg, để có độ phận giải cao.
     * @param content
     * @param run
     * @param imageParser
     */
    private static void handleParagraphRunsImageMath(StringBuilder content, XWPFRun run, ImageParser imageParser){
        Node runNode = run.getCTR().getDomNode();

        // Tìm cả 4 node có các tên tương ứng để xử lý toán học
        Node objectNode = W3cNodeUtil.getChildNode(runNode, "w:object");
        if (objectNode == null) return;
        // Lấy ra style của hình.
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

        XWPFDocument runDocument = run.getDocument();
        PackagePart imgPart = runDocument.getPartById(imageRid);
        String fullName = imgPart.getPartName().getName();
        log.debug("----->>> pic.Match.file.fullName: {}", fullName);
        String extName = imgPart.getPartName().getExtension();
        Pattern pattern = Pattern.compile("\\w+\\." + extName);
        Matcher matcher = pattern.matcher(fullName);
        // todo cần sửa lại fullname
        if(matcher.find()){
            fullName = matcher.group();
        }
        log.debug("----->>> pic.Match.file.extension: {}", fullName);
        // lưu hình ảnh ở định dạng svg để tiện cho việc hiển thị lên không gian mạng.
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


    /**
     * Xử lý các định dạng của từ trong XWPFRun
     * @param content
     * @param run
     */
    private static void handleParagraphRunsWithText(StringBuilder content, XWPFRun run){
        String c = run.toString();
        // xử lý các ký tự đặc biệt trong html.
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

    /**
     * Các ký tự đặc biệt trong html
     * @param text
     * @return
     */
    private static String escapeHtmlTag(String text){
        text = text.replace("&", "&amp;");
        text = text.replace("<", "&lt;");
        text = text.replace(">", "&gt;");
        return text;
    }

    /**
     * Xử lý các biểu thức toán học (được tạo ra trong file Word chứ không phải hình ảnh chèn vào) OMath.
     * Ta sẽ tiến hành chuyển Omath thành xml utf-16 -> node -> lưu vào thư mục ở định dạng png.
     * @param content
     * @param child
     * @param imageParser
     */
    private static void handleParagraphOMath(StringBuilder content, CTOMath child, ImageParser imageParser){
        String s = OmmlUtils.convertOmathToPng(child, imageParser);
        content.append(s);
    }

    public static String handleTable(StringBuilder content, IBodyElement body, ImageParser imageParser){
        XWPFTable table = (XWPFTable) body;
        List<XWPFTableRow> rows = table.getRows();
        content.append("<table>");
        for(XWPFTableRow row : rows){
            content.append("<tr>");
            List<XWPFTableCell> cells = row.getTableCells();
            for(XWPFTableCell cell : cells){
                content.append("<td>");
                List<XWPFParagraph> paragraphs = cell.getParagraphs();
                for(XWPFParagraph paragraph : paragraphs){
                    handleParagraph(content, paragraph, imageParser);
                }
                content.append("</td>\n");
            }
            content.append("</tr>\n");
        }
        return null;
    }
}
