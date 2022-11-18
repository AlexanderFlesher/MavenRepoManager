package localapp.mavenrepomanager;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

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
import org.w3c.dom.Element;

/**
 * The Metadata class represents the "maven-metadata-local.xml" file found in local
 * Maven repositories. This class is constructed from a location and an Entry that 
 * this Metadata will describe.
 * <br/><br/>
 * Attributes described: ArtifactId, GroupId, Version, Last Updated Date
 */
public class Metadata {
    public final static String DEFAULT_NAME = "maven-metadata-local.xml";
    public final String filename;
    private Document document;
    private Repository.Entry entry;
    private Path location;

    /**
     * @param entry the entry represented in this xml
     * @param location the directory location of the file. This may only be a directory,
     * not a filename.
     * @throws IllegalArgumentException when the given location is a filename or 
     * does not exist.
     */
    public Metadata(Repository.Entry entry, Path location) throws IllegalArgumentException{
        this.entry = entry;
        this.location = location;
        validateLocation(location);
        this.filename = Path.of(this.location.toString(), DEFAULT_NAME).toString();
    }

    /**
     * @return the completed XML document representing the associated {@code Repository.Entry}
     * data.
     */
    public Document getDocument(){
        if (this.document == null)
            this.document = createDocument();
        return this.document;
    }

    /**
     * Writes the xml document to the specified location.
     * @throws IOException
     */
    public void write() throws IOException{
        Document doc = getDocument();
        writeDocument(doc);
    }

    private Document createDocument() {
        DocumentBuilder db = getDocumentBuilder();
        Document doc = db.newDocument();
        Element root = doc.createElement("metadata");
        root.appendChild(getGroupNode(doc));
        root.appendChild(getArtifactNode(doc));
        root.appendChild(getVersioningNode(doc));
        doc.appendChild(root);
        return doc;
    }

    private Element getArtifactNode(Document doc) {
        Element artifactIdNode = doc.createElement("artifactId");
        artifactIdNode.appendChild(doc.createTextNode(this.entry.getArtifact()));
        return artifactIdNode;
    }

    private DocumentBuilder getDocumentBuilder(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        return db;
    }

    private Element getGroupNode(Document doc) {
        Element groupIdNode = doc.createElement("groupId");
        groupIdNode.appendChild(doc.createTextNode(this.entry.getGroup()));
        return groupIdNode;
    }    

    private Element getLastUpdatedNode(Document doc){
        Element lastUpdatedNode = doc.createElement("lastUpdated");
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        lastUpdatedNode.appendChild(
            doc.createTextNode(format.format(Date.from(Instant.now()))));
        return lastUpdatedNode;
    }

    private Element getReleaseNode(Document doc) {
        Element releaseNode = doc.createElement("release");
        releaseNode.appendChild(doc.createTextNode(this.entry.getVersion()));
        return releaseNode;
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

    private Element getVersioningNode(Document doc){
        Element versioningNode = doc.createElement("versioning");
        versioningNode.appendChild(getReleaseNode(doc));
        versioningNode.appendChild(getVersionsNode(doc));
        versioningNode.appendChild(getLastUpdatedNode(doc));
        return versioningNode;
    }

    private Element getVersionsNode(Document doc) {
        Element versionsNode = doc.createElement("versions");
        Element versionNode = doc.createElement("version");
        versionNode.appendChild(doc.createTextNode(this.entry.getVersion()));
        versionsNode.appendChild(versionNode);
        return versionsNode;
    }

    private void validateLocation(Path location) {
        if (!location.toFile().isDirectory())
            throw new IllegalArgumentException("The given path is not a directory."); 
        if (!location.toFile().exists())
            throw new IllegalArgumentException("The given path does not exist.");
    }
    
    private void writeDocument(Document doc) throws IOException {
        Transformer transformer = getTransformer();
        DOMSource source = new DOMSource(doc);
        FileWriter writer 
            = new FileWriter(Path.of(this.filename).toFile());
        StreamResult result = new StreamResult(writer);

        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
