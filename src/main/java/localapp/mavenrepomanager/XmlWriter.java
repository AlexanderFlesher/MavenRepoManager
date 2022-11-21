package localapp.mavenrepomanager;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public abstract class XmlWriter extends DebugOptions{
    private static DocumentBuilder docBuilder;
    protected DocumentBuilder getDocumentBuilder(){
        if (XmlWriter.docBuilder == null){
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            try {
                docBuilder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
                return null;
            }
        }
        return XmlWriter.docBuilder;
    }

    protected void validateLocation(Path location) throws IllegalArgumentException {
        if (!location.toFile().isDirectory())
            throw new IllegalArgumentException(
                String.format("The given path \'%s\' is not a directory.", location.toString())); 
        if (!location.toFile().exists())
            throw new IllegalArgumentException(
                String.format("The given path \'%s\' does not exist.", location.toString()));
    }

    protected void writeDocument(Document doc, Path filename) throws IOException{
        Transformer transformer = getTransformer();
        DOMSource source = new DOMSource(doc);
        FileWriter writer 
            = new FileWriter(filename.toFile());
        StreamResult result = new StreamResult(writer);

        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    protected void writeDocument(Document doc, String filename) throws IOException {
        writeDocument(doc, Path.of(filename));
    }

    private Transformer getTransformer(){
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        return transformer;
    }
}
