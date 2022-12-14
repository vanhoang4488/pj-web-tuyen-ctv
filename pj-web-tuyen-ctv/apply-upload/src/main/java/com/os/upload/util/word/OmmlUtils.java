package com.os.upload.util.word;

import com.os.upload.util.ImageParser;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.converter.Converter;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;

@Slf4j
public abstract class OmmlUtils {

    public static String getMathMLFromNode(Node node) {
        final String xslFile = "/OMML2MML.XSL";
        StreamSource streamSource = new StreamSource(OmmlUtils.class.getResourceAsStream(xslFile));
        // đã chuyển node sang xml thành công
        // nhận giá trị ở kiểu String để tiến hành thêm các thao tác khác.
        String s = W3cNodeUtil.node2XmlStr(node);

        // encoding utf-16
        String mathML = W3cNodeUtil.xml2Xml(s, streamSource);

        mathML = mathML.replaceAll("xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\"", "");
        mathML = mathML.replaceAll("xmlns:mml", "xmlns");
        mathML = mathML.replaceAll("mml:", "");
        return mathML;
    }

    /**
     * Xử lý node OMath thành định dạng xml utf-16
     * Chuyển đổi lại xml utf-16 thành node
     * lưu hình ảnh vào thư mục ở định dạng png
     * trả về thẻ html chứa hình ảnh.
     * @param xmlObject
     * @param imageParser
     * @return
     */
    public static String convertOmathToPng(XmlObject xmlObject, ImageParser imageParser){
        Document document = null;
        try{
            String mathMLStr = getMathMLFromNode(xmlObject.getDomNode());
            // Lấy hình ảnh ra ở định dạng utf-16
            document = W3cNodeUtil.xmlStr2Node(mathMLStr, "utf-16");
            // thẻ html chứa hình ảnh.
            return documentToImageHTML(document, imageParser);
        }
        catch(Exception e){
            log.error("=====> convert Omath to png failed: {}", e.getMessage(), e);
        }
        return null;
    }

    private static String documentToImageHTML(Document document, ImageParser imageParser){
        try{
            Converter mathMLConvert = Converter.getInstance();

            LayoutContextImpl localLayoutContextImpl =
                    new LayoutContextImpl(LayoutContextImpl.getDefaultLayoutContext());

            localLayoutContextImpl.setParameter(Parameter.MATHSIZE, 18);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            mathMLConvert.convert(document, os, "image/png", localLayoutContextImpl);
            String url = imageParser.parse(os.toByteArray(), "png");
            os.close();
            return "<img src=\"" + url + "\" align=\"absimddle\"/>";
        }
        catch (Exception e){
            log.error("=====> document to image html failed: {}", e.getMessage(), e);
        }
        return null;
    }
}
