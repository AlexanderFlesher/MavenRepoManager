package localapp.mavenrepomanager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An object to represent a directory of a repository. Each DirNode has
 * a path associated to it constructed from the names of the parent DirNodes.
 */
public final class DirNode{
    private String name;
    private DirNode parent;
    private List<DirNode> children;

    private DirNode(){
        children = new ArrayList<>();
    }

    public DirNode(String name){
        this();
        setName(name);
    }

    public DirNode(String name, DirNode parent){
        this(name);
        setParent(parent);
    }

    /**
     * Adds the given DirNode as a child DirNode to this DirNode. The child's parent is then
     * set to this DirNode.
     * @param child
     */
    public void addChild(DirNode child){
        children.add(child);
        child.setParent(this);
    }

    /**
     * @return the list of children of this DirNode.
     */
    public List<DirNode> getChildren(){
        return this.children;
    }

    /**
     * @return returns the parent of this DirNode.
     */
    public DirNode getParent(){
        return this.parent;
    }

    /**
     * @return true if the parent is not null, false otherwise.
     */
    public boolean hasParent(){
        return this.parent != null;
    }

    /**
     * Removes the given DirNode from the children of this DirNode. Also unsets
     * the child's parent so that they are no longer linked.
     * @param child
     */
    public void removeChild(DirNode child){
        children.remove(child);
        child.setParent(null);
    }

    /**
     * @return this DirNode's name.
     */
    public String getName(){
        return this.name;
    }

    /**
     * Sets this DirNode's name. The provided name cannot be blank or null.
     * @param name
     */
    public void setName(String name){
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("DirNode name may not be blank.");
        this.name = name;
    }

    /**
     * A method to get the Path abstraction of this DirNode.
     * @return the Path object representing the path to this DirNode in a directory
     * structure. For example, linked DirNodes {@code parent} and {@code child}
     * would produce a Path, "parent/child".
     */
    public Path toPath(){
        List<String> stringPath = this.getStringPath();
        String first = stringPath.get(0);
        List<String> next = stringPath.subList(1, stringPath.size());
        return Path.of(first, next.toArray(new String[next.size()]));
    }

    private List<String> getStringPath(){
        List<String> reverse = getReverseStringPath();
        Collections.reverse(reverse);
        return reverse;
    }

    private List<String> getReverseStringPath(){
        List<String> path = new ArrayList<String>();
        path.add(name);
        if (this.parent != null)
            path.addAll(parent.getReverseStringPath());
        return path;
    }

    private void setParent(DirNode parent){
        if (this.parent != parent){
            this.parent = parent;
            if (parent != null && !parent.getChildren().contains(this))
                this.parent.addChild(this);
        }
    }
}
