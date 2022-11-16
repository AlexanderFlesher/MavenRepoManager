package localapp.mavenrepomanager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class ClasspathFile {
    private Element root;
    private String[] jarPaths;

    public ClasspathFile(InputStream input){
        this.root = getRoot(input);
        if (!validate(root))
            throw new IllegalArgumentException(
                "The given InputStream is not a classpath file or is incorrectly formatted.");
        this.jarPaths = generatePaths(root);

    }

    public ClasspathFile(File file){
        this.root = getRoot(file);
        if (!validate(root))
            throw new IllegalArgumentException(
                String.format("File \"%s\" is not a classpath file or is incorrectly formatted.", file));
        if (!validate(file))
            throw new IllegalArgumentException(
                String.format("File \"%s\" does not exist.", file));

        this.jarPaths = generatePaths(root);
    }

    public ClasspathFile(String filepath){
        this(new File(filepath));
    }

    public String[] getLibPaths(){
        return jarPaths;
    }

    private String[] generatePaths(Element e){
        List<String> paths = new ArrayList<>();
        NodeList nodes = e.getElementsByTagName("classpathentry");
        for (int i = 0; i < nodes.getLength(); i++){
            Node node = nodes.item(i);
            if (node.getAttributes().getNamedItem("kind").getTextContent().equals("lib")){
                paths.add(node.getAttributes().getNamedItem("path").getTextContent());
            }
        }
        return paths.toArray(new String[paths.size()]);
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

    private boolean validate(Element r){
        return r == null || !r.getTagName().equals("classpath") ? false : true;
    }
    
    private boolean validate(File f) {
        return f.exists();
    }
}
