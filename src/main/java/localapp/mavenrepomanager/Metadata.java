package localapp.mavenrepomanager;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The Metadata class represents the "maven-metadata-local.xml" file found in local
 * Maven repositories. This class is constructed from a location and an Entry that 
 * this Metadata will describe.
 * <br/><br/>
 * Attributes described: ArtifactId, GroupId, Version, Last Updated Date
 */
public class Metadata extends XmlWriter {
    public final static String DEFAULT_NAME = "maven-metadata-local.xml";
    public final String filename;
    private Document document;
    private Entry entry;
    private Path location;

    /**
     * @param entry the entry represented in this xml
     * @param location the directory location of the file. This may only be a directory,
     * not a filename.
     * @throws IllegalArgumentException when the given location is a filename or 
     * does not exist.
     */
    public Metadata(Entry entry, Path location) throws IllegalArgumentException{
        this.entry = entry;
        this.location = location;
        this.filename = Path.of(this.location.toString(), DEFAULT_NAME).toString();
    }

    /**
     * @return the completed XML document representing the associated {@code Entry}
     * data.
     */
    public Document getDocument(){
        if (this.document == null)
            this.document = createDocument();
        return this.document;
    }

    /**
     * Writes the xml document to the specified location.
     * @param options provides a way to pass a debug flag so that the directory 
     * will not really be written to the filesystem. Used for testing.
     * @throws IOException
     */
    public void write(Option... options) throws IOException{
        Document doc = getDocument();
        validateLocation(this.location);
        for (Option option : options){
            if (option.equals(Option.DEBUG))
                return;
        }
        writeDocument(doc, this.filename);
    }

    private Document createDocument() {
        Document doc = getDocumentBuilder().newDocument();
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
}
