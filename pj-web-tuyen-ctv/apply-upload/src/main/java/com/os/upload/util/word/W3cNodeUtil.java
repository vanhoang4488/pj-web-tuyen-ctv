package com.os.upload.util.word;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

@Slf4j
public abstract class W3cNodeUtil {

    public static String node2XmlStr(Node node){
        Transformer transformer = null;
        if(node == null)
            throw new IllegalArgumentException("node could not null");

        try{
            transformer = TransformerFactory.newInstance().newTransformer();
        }
        catch (Exception e){
            log.error("=====> transform Xml to Str failed: {}", e.getMessage(), e);
        }

        if(transformer != null){
            try{
                StringWriter sw = new StringWriter();
                transformer.transform(new DOMSource(node), new StreamResult(sw));
                return  sw.toString();
            }
            catch (Exception e){

            }
        }
        return null;
    }

    public static String xml2Xml(String xml, Source XSLSource){
        Transformer transformer = null;
        if(xml == null)
            throw new IllegalArgumentException("xml could not null ...");

        try{
            if(XSLSource == null)
                transformer = TransformerFactory.newInstance().newTransformer();
            else
                transformer = TransformerFactory.newInstance().newTransformer(XSLSource);

            if(transformer != null){
                Source source = new StreamSource(new StringReader(xml));
                StringWriter sw = new StringWriter();
                transformer.transform(source, new StreamResult(sw));
                return sw.toString();
            }
        }
        catch (Exception e){
            log.error("=====> encoding utf-16 xml failed: {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * chuyển đổi xml2Str thành Node.
     * @param xmlString
     * @param encoding
     * @return
     */
    public static Document xmlStr2Node(String xmlString, String encoding){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try{
            InputStream is = new ByteArrayInputStream(xmlString.getBytes(encoding));
            doc = dbf.newDocumentBuilder().parse(is);
            is.close();
        }
        catch (Exception e){
            log.error("=====> transform string to node failed: {}", e.getMessage(), e);
        }
        return doc;
    }

    public static Node getChildNode(Node node, String nodeName){
        if(!node.hasChildNodes()) return null;

        NodeList childNodes = node.getChildNodes();
        for(int i = 0; i < childNodes.getLength(); i++){
            Node childNode = childNodes.item(i);
            if (nodeName.equals(childNode.getNodeName())) return childNode;

            childNode = getChildNode(childNode, nodeName);
            if (childNode != null) return childNode;
        }

        return null;
    }

    public static Node getChildChainNode(Node node, String... nodeName){
        Node childNode = node;
        for(int i = 0; i < nodeName.length; i++){
            String tmp = nodeName[i];
            childNode = getChildNode(childNode, tmp);
            if(childNode == null) return null;
        }
        return childNode;
    }
}
