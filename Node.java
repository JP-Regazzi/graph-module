public class Node {
    public int name;
    public Node parent;
    public int depth;

    public Node(int Name, Node Parent) {
        name = Name;
        parent = Parent;
        depth = GetDepth();
    }

    private int GetDepth() {
        int Depth = 0;
        if (parent == null) {
            return Depth;
        } else {
            return parent.depth + 1;
        }
    }


}
