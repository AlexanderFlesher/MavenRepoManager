package localapp.mavenrepomanager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The ClasspathFile class abstracts the Classpath file and records the
 * classpath entires in a map called {@code ClasspathEntries}. The map is 
 * indexed by entry kind corresponding to the content of the "kind" 
 * attribute. The List<String> returned is a list of the contents of the 
 * "path" attribute for all the entries of the key kind.
 * <br/><br/>
 * Throws:<br/><br/>
 * {@code IllegalArgumentException} when the given file does not exist or
 * is not in a classpath file format.
 */
public final class ClasspathFile {
    private Element root;
    private HashMap<String, List<String>> classpathEntries;
    
    public ClasspathFile(InputStream input){
        this.root = getRoot(input);
        if (!validate(root))
            throw new IllegalArgumentException(
                "The given InputStream is not a classpath file or is incorrectly formatted.");

        this.classpathEntries = readClasspathEntries(root);
    }

    public ClasspathFile(File file){
        if (!validate(file))
            throw new IllegalArgumentException(
                String.format("File \"%s\" does not exist.", file));

        this.root = getRoot(file);
        if (!validate(root))
            throw new IllegalArgumentException(
                String.format("File \"%s\" is not a classpath file or is incorrectly formatted.", file));

        this.classpathEntries = readClasspathEntries(root);
    }

    public ClasspathFile(String filepath){
        this(filepath != null ? new File(filepath) : null);
    }

    /**
     * 
     * @return a map that is indexed by entry kind corresponding
     * to the content of the "kind" attribute. The List<String> returned is a list
     * of the contents of the "path" attribute for all the entries of the key kind.
     */
    public Map<String, List<String>> getClasspathEntries(){
        return this.classpathEntries;
    }

    private Element getRoot(InputStream f){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();        
            Document doc = docBuilder.parse(f);
            return doc.getDocumentElement();
        }
        catch (ParserConfigurationException | SAXException | IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    private Element getRoot(File f){
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();        
            Document doc = docBuilder.parse(f);
            return doc.getDocumentElement();
        }
        catch (ParserConfigurationException | SAXException | IOException ex){
            ex.printStackTrace();
            return null;
        }
    }

    private HashMap<String, List<String>> readClasspathEntries(Element e){
        HashMap<String, List<String>> map = new HashMap<>();
        NodeList nodes = e.getElementsByTagName("classpathentry");
        for (int i = 0; i < nodes.getLength(); i++){
            Node node = nodes.item(i);
            final String entryKind = node.getAttributes().getNamedItem("kind").getTextContent();
            if (!map.containsKey(entryKind))
                map.put(entryKind, new ArrayList<String>());

            map.get(entryKind).add(node.getAttributes().getNamedItem("path").getTextContent());
        }
        return map;
    }

    private boolean validate(Element r){
        return r != null && r.getTagName().equals("classpath");
    }
    
    private boolean validate(File f) {
        return f != null && f.exists();
    }
}
