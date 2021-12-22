public class Node {
    public int value;
    public Node parent;
    public int depth;

    public Node(int Value, Node Parent) {
        value = Value;
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
