package com.os.upload.util;

import net.arnx.wmf2svg.gdi.svg.SvgGdi;
import net.arnx.wmf2svg.gdi.wmf.WmfParser;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.w3c.dom.Document;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 *  Nếu chúng ta có nhu cầu chuyển đổi các hình ảnh ở định dạng svg, png (độ phân giải cao)
 *  -> các hình ảnh có độ phân giải thấp hơn jpg thì sử dụng lớp này.
 *  Hoặc là chuyển đổi độ phân giải hình ảnh từ thấp lên cao.
 */
public abstract class WmfUtils {

    public static String convert(String path){
        try{
            String svgFile = FileExtNameUtils.replace(path, "wmf", "svg");
            wmfToSvg(path, svgFile);
            String jpgFile = FileExtNameUtils.replace(path, "wmf", "png");
            svgToJpg(svgFile, jpgFile);
            return jpgFile;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static String svgToJpg(String src, String dest) {
        FileOutputStream jpgOut = null;
        FileInputStream svgStream = null;
        ByteArrayOutputStream svgOut = null;
        ByteArrayInputStream svgInputStream = null;
        ByteArrayOutputStream jpg = null;
        try{
            File svg = new File(src);
            svgStream = new FileInputStream(svg);
            svgOut = new ByteArrayOutputStream();

            int noOfByteRead = 0;
            while ((noOfByteRead = svgStream.read()) != -1)
                svgOut.write(noOfByteRead);

            JPEGTranscoder it = new JPEGTranscoder();
            it.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, (float) 1);
            it.addTranscodingHint(ImageTranscoder.KEY_WIDTH, 200f);
            jpg = new ByteArrayOutputStream();
            svgInputStream = new ByteArrayInputStream(svgOut.toByteArray());
            it.transcode(new TranscoderInput(svgInputStream), new TranscoderOutput(jpg));
            jpgOut = new FileOutputStream(dest);
            jpg.write(jpg.toByteArray());
        }
        catch (Exception e){
            try{
                if(svgInputStream != null) svgInputStream.close();

                if(jpg != null) jpg.close();

                if(svgStream != null) svgStream.close();

                if(svgOut != null) svgOut.close();

                if (jpgOut != null) {
                    jpgOut.flush();
                    jpgOut.close();
                }
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
        return dest;
    }

    public static void toSvg(InputStream in, OutputStream out){
        try{
            WmfParser parser = new WmfParser();
            final SvgGdi gdi = new SvgGdi(false);
            Document doc = gdi.getDocument();
            output(doc, out);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void wmfToSvg(String src, String dest){
        InputStream in = null;
        OutputStream out = null;
        try{
            in = new FileInputStream(src);
            out = new FileOutputStream(dest);
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
        }
        toSvg(in, out);
    }

    private static void output(Document doc, OutputStream out) throws Exception {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W#C/DTD SVG 1.0//EN");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM,
                "http://www.w3.org/TR/2001/REC-SVG-20010904/DTD/svg10.dtd");
        transformer.transform(new DOMSource(doc), new StreamResult(out));
        out.flush();
        out.close();
    }

}
