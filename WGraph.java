import java.io.File; 
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.*;
import javafx.util.Pair;

public class WGraph {
    boolean isMatrix;
    ArrayList<ArrayList<Boolean>> matrix;
    ArrayList<ArrayList<Pair<Integer, Float>>> array;
    int vertexCount;
    int edgeCount;
    int minDegree;
    int maxDegree;
    double averageDegree;
    double medianDegree;
    Integer[] vertexDegrees;
    boolean[] markedVertices;

    boolean hasNegativeWeight;

    public WGraph(boolean repChoice) {
        isMatrix = repChoice;
        CreateGraph();
        CalculateAttributes();
        WriteToOutputFile();
    }

    private void CreateGraph() {
        try {
            File myObj = new File("Input.txt");
            Scanner myReader = new Scanner(myObj);

            vertexCount = Integer.valueOf(myReader.nextLine()); // Reads vertex count from input file

            vertexDegrees = new Integer[vertexCount];
            for (int i = 0; i < vertexCount; i++) vertexDegrees[i] = 0;

            if (isMatrix) {
                matrix = CreateMatrix();
            } else { 
                array = CreateArray();
            }

            while (myReader.hasNextLine()) { // Reads and creates edges from input file
                String edge [] = myReader.nextLine().split(" ");
                AddEdge(Integer.valueOf(edge[0]), Integer.valueOf(edge[1]), Float.valueOf(edge[2]));
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void CalculateAttributes() {
        minDegree = Collections.min(Arrays.asList(vertexDegrees));
        maxDegree = Collections.max(Arrays.asList(vertexDegrees));
        averageDegree = CalculateAverage();
        medianDegree = CalculateMedian();
    }
    private double CalculateAverage() {
        double sum = 0;

        for(int i=0; i < vertexCount; i++) sum += Double.valueOf(vertexDegrees[i]);

        return sum/vertexCount;
    
    }
    private double CalculateMedian() {
        Integer[] numArray = new Integer[vertexCount];
        for (int index = 0; index < vertexDegrees.length; index++) numArray[index] = vertexDegrees[index];
        Arrays.sort(numArray);
        if (numArray.length % 2 == 0) return ((double)numArray[numArray.length/2] + (double)numArray[numArray.length/2 - 1])/2;
        else return (double) numArray[numArray.length/2];
    }



    public void WriteToOutputFile() {
        try {
            ArrayList<ArrayList<Integer>> components = ConnectedComponents();
            FileWriter myWriter = new FileWriter("Output.txt");
            myWriter.write("Number of vertices: " + vertexCount + "\n");
            myWriter.write("Number of edges: " + edgeCount + "\n");
            myWriter.write("Minimum degree: " + minDegree + "\n");
            myWriter.write("Maximum degree: " + maxDegree + "\n");
            myWriter.write("Average degree: " + averageDegree + "\n");
            myWriter.write("Median degree: " + medianDegree + "\n");
            myWriter.write("Number of connected components: " + components.size() + "\n");
            myWriter.write("Connected components: " + "\n");


            myWriter.write(1 + "st component, size: " + components.get(0).size() + ", vertices:\n");
            myWriter.write("[" + components.get(0).get(0));
            for (int j = 1; j < components.get(0).size(); j++) {
                myWriter.write(", " + components.get(0).get(j));
            }
            myWriter.write("]\n");
            
            for (int i = 1; i < components.size(); i++) {
                ArrayList<Integer> tree = components.get(i);
                myWriter.write(i + 1 + "th component, size: " + tree.size() + ", vertices:\n");
                myWriter.write("[" + tree.get(0));
                for (int j = 1; j < tree.size(); j++) {
                    myWriter.write(", " + tree.get(j));
                }
                myWriter.write("]\n");
            }

            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private ArrayList<ArrayList<Boolean>> CreateMatrix() {
        ArrayList<ArrayList<Boolean>> matrix = new ArrayList<ArrayList<Boolean>>(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            ArrayList<Boolean> inner_array = new ArrayList<Boolean>(vertexCount);
            for (int j = 0; j < vertexCount; j++) {
                inner_array.add(false);
            }
            matrix.add(inner_array);
        }
        return matrix;
    }

    private ArrayList<ArrayList<Pair<Integer, Float>>> CreateArray() { // Create array and set it's size
        ArrayList<ArrayList<Pair<Integer, Float>>> array = new ArrayList<ArrayList<Pair<Integer, Float>>> (vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            ArrayList<Pair<Integer, Float>> inner_array = new ArrayList<Pair<Integer, Float>>();
            array.add(inner_array);
        }
        return array;
    }


    private void PrintMatrix(boolean[][] matrix) {
        System.out.print("\u001B[41m" + "  ");
        for (int i = 0; i < matrix.length; i++) {
            System.out.print(i + 1  + " ");
        }
        System.out.print("\u001B[0m" + "\n");
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("\u001B[41m" + (i + 1) + " " + "\u001B[0m");
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.print("\n");
        }
    }

    private void PrintArray(ArrayList<ArrayList<Integer>> array) {
        for (int i = 0; i < array.size(); i++) {
            System.out.print(i+1 + "-> ");
            for (int j = 0; j < array.get(i).size(); j++) {
                System.out.print(array.get(i).get(j) + " ");
            }
            System.out.print("\n");
        }
    }

    private void AddEdge(int vertex1, int vertex2, float weight) {
        System.out.println(vertex1 + " " + vertex2 + " weight = " + weight);
        if (!hasNegativeWeight && weight < 0) {
            hasNegativeWeight = true; // Tests if the graph has negative weights
            System.out.println("tem neg");
        }

        if (isMatrix) {
            if (matrix.get(vertex1 - 1).get(vertex2 - 1) == false && vertex1 != vertex2) {
                matrix.get(vertex1 - 1).set(vertex2 - 1, true);
                matrix.get(vertex2 - 1).set(vertex1 - 1, true);
                edgeCount++;
                vertexDegrees[vertex1 - 1]++;
                vertexDegrees[vertex2 - 1]++;
            }
        } else {
            if (! array.get(vertex1 - 1).contains(vertex2) && vertex1 != vertex2) { // Tests if v1 and v2 are already connected
                array.get(vertex1 - 1).add(new Pair<Integer, Float>(vertex2, weight));
                array.get(vertex2 - 1).add(new Pair<Integer, Float>(vertex1, weight));
                edgeCount++;
                vertexDegrees[vertex1 - 1]++;
                vertexDegrees[vertex2 - 1]++;
            }
        }
    }

    public LinkedList<Node> BFS(Integer origin) {
        LinkedList<Node> searchTree = new LinkedList<Node>();
        markedVertices = new boolean[vertexCount];
        LinkedList<Node> queue = new LinkedList<Node>();

        markedVertices[origin - 1] = true;
        Node currentNode = new Node(origin, null);
        searchTree.add(currentNode);
        queue.add(currentNode);
        

        while (queue.size() != 0) {
            currentNode = queue.poll();

            if (isMatrix)  {
                for (int i = 0; i < vertexCount; i++) {
                    boolean n = matrix.get(currentNode.value - 1).get(i);
                    if (n != false && !markedVertices[i]) {
                        markedVertices[i] = true;
                        Node newNode = new Node(i + 1, currentNode);
                        queue.add(newNode);
                        searchTree.add(newNode);
                    }
                }
            } else {
                // Collections.sort(array.get(currentNode.value - 1)); RESOLVER DPS
                for (Pair<Integer, Float> i : array.get(currentNode.value - 1)) {
                     if (!markedVertices[i.getKey() - 1]) {
                        markedVertices[i.getKey() - 1] = true;
                        Node newNode = new Node(i.getKey(), currentNode);
                        queue.add(newNode);
                        searchTree.add(newNode);
                    }
                }
            }
                
        }
        WriteToTreeFile(searchTree, "BFS search tree.txt");
        return searchTree;
    }
    private void WriteToTreeFile(LinkedList<Node> STree, String filename) {
        try {
            FileWriter myWriter = new FileWriter(filename);
            for (Node node : STree) {
                myWriter.write("Node: " + node.value);
                if (node.parent != null) myWriter.write(" Parent: " + node.parent.value);
                else myWriter.write(" Parent: " + node.parent);
                myWriter.write(" Depth = " + node.depth + "\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    public LinkedList<Node> DFS(Integer origin) {
        LinkedList<Node> searchTree = new LinkedList<Node>();
        markedVertices = new boolean[vertexCount]; // Desmarco todos os vertices
        Stack<Node> s = new Stack<Node>(); // Crio a pilha
        Node rootNode = new Node(origin, null); // Crio a raiz da arvore
        s.push(rootNode); // Coloco a raiz na pilha

        while (! s.empty()) {

            Node currentNode = s.pop();
            if (!markedVertices[currentNode.value - 1]) {
                markedVertices[currentNode.value - 1] = true;
                searchTree.add(currentNode);
                if (isMatrix){
                    for (int i = 0; i < vertexCount; i++) {
                        boolean n = matrix.get(currentNode.value - 1).get(i);
                        if (n == true && !markedVertices[i]) {
                            Node newNode = new Node(i + 1, currentNode);
                            s.push(newNode);
                        }
                    }
                } else {
                    // Collections.sort(array.get(currentNode.value - 1));
                    for (Pair<Integer, Float> neighbour : array.get(currentNode.value -1)) { // Itera por todos os vizinhos
                        Node newNode = new Node(neighbour.getKey(), currentNode);
                        s.push(newNode);
                    }
                }
            }
        }
        WriteToTreeFile(searchTree, "DFS search tree.txt");
        return searchTree;
    }

    public int Distance(int vertex1, int vertex2) {
        LinkedList<Node> tree = BFS(vertex1);
        for (Node vertex : tree) {
            if (vertex.value == vertex2) {
                return vertex.depth;
            }
        }
        return -1;
    }

    public int Diameter(boolean aprox) {
        int maxDist = 0;
        if (aprox) {
            int k = (int)(Math.log(vertexCount)/Math.log(2));
            Random r = new Random();
            for (int i = 0; i < k; i++) {
                int num = r.nextInt(vertexCount - 1) + 1;
                LinkedList<Node> tree = BFS(num);
                int dist = 0;
                for (Node vertex : tree) {
                    if (vertex.depth > dist) dist = vertex.depth;
                }
                if (dist > maxDist) maxDist = dist;
            }

        } else {
            for (int i = 1; i < vertexCount + 1; i++) {
                LinkedList<Node> tree = BFS(i);
                int dist = 0;
                for (Node vertex : tree) {
                    if (vertex.depth > dist) dist = vertex.depth;
                }
                if (dist > maxDist) maxDist = dist;
            }
        }
        return maxDist;
    }

    public ArrayList<ArrayList<Integer>> ConnectedComponents() {
        ArrayList<ArrayList<Integer>> components = new ArrayList<ArrayList<Integer>>();
        boolean[] markedNodes = new boolean[vertexCount];
        boolean isDone = false;
        int s = 1;
        while (!isDone) {
            ArrayList<Integer> currentComponent = new ArrayList<Integer>();
            components.add(currentComponent);
            
            BFS(s);
            for (int i = 0; i < markedNodes.length; i++) {
                if (markedVertices[i]) {
                    markedNodes[i] = true;
                    currentComponent.add(i + 1);
                }
            }

            isDone = true;
            for (int i = 0; i < markedNodes.length; i++) {
                if (!markedNodes[i]) {
                    isDone = false;
                    s = i + 1;
                }
            }

        }

        Collections.sort(components, new Comparator<ArrayList>(){
            public int compare(ArrayList a1, ArrayList a2) {
                return a2.size() - a1.size();
            }
        });
        return components;
    }

    public void Distance2(int s, int target) {
        if (hasNegativeWeight) {

        } else {
            Dijkstra(s, target);
        }
    }

    public void DistanceAll(int s) {
        if (hasNegativeWeight) {

        } else {
            Dijkstra(s, -1);
        }
    }

    private void Dijkstra(int s, int target) {
        int[] parents = new int[vertexCount]; // Array containing the parent of each node
        s--;
        Boolean explored[] = new Boolean[vertexCount]; // Array that represents what vertices were explored
        float dist[] = new float[vertexCount]; // Array containing the current distance estimate of each vertex

        for (int i = 0; i < vertexCount; i++) { // Sets all vertices as not explored and all the distances to infinite
            explored[i] = false;
            dist[i] = Float.MAX_VALUE;
        }
        dist[s] = 0; // Distance from source to itself is set to 0
        parents[s] = -1;

        for (int i = 0; i < vertexCount; i++) { // Goes through all vertices in the graph
            int u = minDistance(dist, explored); // Chooses the not yet explored vertex with minimum distance
            System.out.println(u);

            explored[u] = true;

            for (Pair<Integer, Float> edge : array.get(u)) {
                if (!explored[edge.getKey()-1] && dist[u] != Float.MAX_VALUE && dist[u] + edge.getValue() < dist[edge.getKey()-1]) {
                    dist[edge.getKey()-1] = dist[u] + edge.getValue();
                    parents[edge.getKey()-1] = u;
                }
            }

        }
        // Debug
        System.out.println("distances: ");
        for (float i : dist) {
            System.out.println(i);
        }
        System.out.println("Parents: ");
        for (int i : parents) {
            System.out.println(i);
        }
        System.out.println("Path: ");
        for (Integer i : GetPath(4, parents)) {
            System.out.println(i);
        }
        //
        if (target != -1) {
            System.out.println("Distance between vertices " + target + " and " + (s+1) + " = " + dist[target-1]);
            System.out.print("Path:");
            for (Integer node : GetPath(target-1, parents)) {
                System.out.print(" > "+ (node+1));
            }
            System.out.print("\n\n");
        } else {
            System.out.println("Distances between vertex " + (s+1) + " and all vertices, along with their paths:\n");
            for (int index = 0; index < vertexCount; index++) {
                System.out.println("Distance to vertex " + (index+1) + " = " + dist[index]);
                System.out.print("Path to vertex " + (index+1) + ": ");
                for (Integer node : GetPath(index, parents)) {
                    System.out.print(" > "+ (node+1));
                }
                System.out.print("\n\n");
            }
        }
        


        
    }

    private int minDistance(float dist[], Boolean explored[]) {
        // Initialize min value
        float min = Float.MAX_VALUE;
        int min_index = -1;
 
        for (int v = 0; v < vertexCount; v++)
            if (explored[v] == false && dist[v] <= min) {
                min = dist[v];
                min_index = v;
            }
 
        return min_index;
    }

    private LinkedList<Integer> GetPath(int target, int[] parents) {
        int currentParent = parents[target];
        LinkedList<Integer> path = new LinkedList<Integer>();
        path.add(target);
        while (currentParent != -1) {
            path.add(currentParent);
            //System.out.println(currentParent);
            currentParent = parents[currentParent];
        }
        return path;
    }
    




    public static void main(String[] args) {
        WGraph myGraph = new WGraph(false);
        // myGraph.PrintMatrix(myGraph.matrix);
        // myGraph.PrintArray(myGraph.array);
        //System.out.println(myGraph.WeightedDistance(1));
        //System.out.println(myGraph.Diameter(false));
        //myGraph.DFS(1);
        //myGraph.BFS(1);
        myGraph.Distance2(1, 5);
        myGraph.DistanceAll(1);
        
    }


}
