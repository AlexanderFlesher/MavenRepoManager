package localapp.mavenrepomanager;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PomDeps extends XmlWriter {
    Entry[] entries;
    RunSettings settings;
    
    public PomDeps(RunSettings settings, Entry... entries){
        this.entries = entries;
        this.settings = settings;
    }

    public void write(Option... options) throws IOException{
        Document doc = createDocument();
        validateLocation(this.settings.dependencyXmlName.getParent());
        for (Option option : options){
            if (option.equals(Option.DEBUG))
                return;
        }
        writeDocument(doc, this.settings.dependencyXmlName,
        (file) -> {
            tryFormatOutputFile(file);
        });
    }

    private void appendComments(Document doc, String... comments){
        for (String comment : comments){
            doc.appendChild(doc.createComment(comment));
        }
    }

    private Document createDocument() {
        Document doc = getDocumentBuilder().newDocument();
        Element project = doc.createElement("project");
        appendComments(doc, 
            "This document is not intended as a standalone pom.xml file.",
            "Copy this file's project tag contents into a working pom.xml under the project tag.");
        project.appendChild(getRepositoriesElement(doc));
        project.appendChild(getDependenciesElement(doc, entries));
        doc.appendChild(project);
        return doc;
    }

    private Element getDependenciesElement(Document doc, Entry... entries){
        Element dependencies = doc.createElement("dependencies");
        for (Entry entry : this.entries){
            dependencies.appendChild(entry.toXml(doc));
        }
        return dependencies;
    }

    private Element getIdElement(Document doc){
        Element id = doc.createElement("id");
        id.appendChild(doc.createTextNode(this.settings.repoName.replace(' ', '_')));
        return id;
    }

    private Element getNameElement(Document doc){
        Element name = doc.createElement("name");
        name.appendChild(doc.createTextNode(this.settings.repoName));
        return name;
    }

    private Element getRepositoriesElement(Document doc){
        Element repositories = doc.createElement("repositories");
        Element repository = doc.createElement("repository");
        repository.appendChild(getIdElement(doc));
        repository.appendChild(getNameElement(doc));
        repository.appendChild(getUrlElement(doc));
        repositories.appendChild(repository);
        return repositories;
    }

    private Element getUrlElement(Document doc){
        Element url = doc.createElement("url");
        url.appendChild(doc.createTextNode(
            Path.of(this.settings.repoPath.toString(), this.settings.repoName).toString()));
        return url;
    }
    
    private void tryFormatOutputFile(Path file){
        StringBuffer fileBuffer = new StringBuffer();
        try (FileReader reader = new FileReader(file.toFile())){
            while (reader.ready())
                fileBuffer.append(Character.toChars(reader.read()));
        }
        catch (IOException ex){
            ex.printStackTrace();
        }

        try (FileWriter writer = new FileWriter(file.toFile())){
            String output = fileBuffer.toString()
            .replaceAll("--><", "-->" + System.lineSeparator() + "<");
            writer.write(output);
            writer.flush();
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
