package localapp.mavenrepomanager;

public final class Repository {

    public final static class Entry{
        private String artifact;
        private String group;
        private String version;

        public DirNode toNode(){
            DirNode node = null, next;
            for (String s : group.split(".")){
                if (node == null)
                    node = new DirNode(s);
                else {
                    next = new DirNode(s, node);
                    node = next;
                }
            }

            next = new DirNode(artifact, node);
            node = next;

            next = new DirNode(version, node);
            node = next;

            while (node.hasParent())
                node = node.getParent();

            return node;
        }
    }
}
