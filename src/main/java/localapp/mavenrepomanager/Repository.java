package localapp.mavenrepomanager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Repository {

    /**
     * An object to represent a directory of a repository. Each Node has
     * a path associated to it constructed from the names of the parent Nodes.
     */
    public final static class Node{
        private String name;
        private Node parent;
        private List<Node> children;

        private Node(){
            children = new ArrayList<>();
        }

        public Node(String name){
            this();
            setName(name);
        }

        public Node(String name, Node parent){
            this(name);
            setParent(parent);
        }

        /**
         * Adds the given Node as a child Node to this Node. The child's parent is then
         * set to this Node.
         * @param child
         */
        public void addChild(Node child){
            children.add(child);
            child.setParent(this);
        }

        /**
         * @return the list of children of this Node.
         */
        public List<Node> getChildren(){
            return this.children;
        }

        /**
         * @return returns the parent of this Node.
         */
        public Node getParent(){
            return this.parent;
        }

        /**
         * Removes the given Node from the children of this Node. Also unsets
         * the child's parent so that they are no longer linked.
         * @param child
         */
        public void removeChild(Node child){
            children.remove(child);
            child.setParent(null);
        }

        /**
         * @return this Node's name.
         */
        public String getName(){
            return this.name;
        }

        /**
         * Sets this Node's name. The provided name cannot be blank or null.
         * @param name
         */
        public void setName(String name){
            if (name == null || name.isBlank())
                throw new IllegalArgumentException("Node name may not be blank.");
            this.name = name;
        }

        /**
         * A method to get the Path abstraction of this Node.
         * @return the Path object representing the path to this Node in a directory
         * structure. For example, linked Nodes {@code parent} and {@code child}
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

        private void setParent(Node parent){
            if (this.parent != parent){
                this.parent = parent;
                if (parent != null && !parent.getChildren().contains(this))
                    this.parent.addChild(this);
            }
        }
    }
}
