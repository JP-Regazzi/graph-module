import java.io.Console;
import java.io.File; 
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.*;
// import javafx.util.Pair;

import java.lang.Math;

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
            File myObj = new File("grafo_W_3_1.txt");
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
        //System.out.println(vertex1 + " " + vertex2 + " weight = " + weight);
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

    
    public void Distance2(int s, int target) {
        if (hasNegativeWeight) {
            BellmanFord(s, target);

        } else {
            Dijkstra(s, target);
        }
    }

    public void DistanceAll(int s) {
        if (hasNegativeWeight) {
            BellmanFord(s, -1);
        } else {
            Dijkstra(s, -1);
        }
    }

    private void Dijkstra(int s, int target) {
        int[] parents = new int[vertexCount]; // Array containing the parent of each node
        Boolean explored[] = new Boolean[vertexCount]; // Array that represents what vertices were explored
        float dist[] = new float[vertexCount]; // Array containing the current distance estimate of each vertex

        for (int i = 0; i < vertexCount; i++) { // Sets all vertices as not explored and all the distances to infinite
            explored[i] = false;
            dist[i] = Float.MAX_VALUE;
        }
        dist[s-1] = 0; // Distance from source to itself is set to 0
        parents[s-1] = -1;

        PriorityQueue<Pair<Integer, Float>> queue = new PriorityQueue<>((v1, v2) -> Math.round(10000*v1.getValue()) - Math.round(10000*v2.getValue()));
        queue.add(new Pair<Integer, Float>(s, 0f));

        while (queue.size() > 0) {
            int u = queue.poll().getKey()-1;
            //System.out.println(u);

            explored[u] = true;
            for (Pair<Integer, Float> edge : array.get(u)) {
                if (!explored[edge.getKey()-1] && dist[u] != Float.MAX_VALUE && dist[u] + edge.getValue() < dist[edge.getKey()-1]) {
                    dist[edge.getKey()-1] = dist[u] + edge.getValue();
                    parents[edge.getKey()-1] = u + 1;
                    queue.add(new Pair<Integer, Float>(edge.getKey(), dist[edge.getKey()-1]));
                }
            }
        }


        /*for (int i = 0; i < vertexCount; i++) { // Goes through all vertices in the graph
            int u = MinDistance(dist, explored); // Chooses the not yet explored vertex with minimum distance
            System.out.println(u);

            explored[u] = true;

            for (Pair<Integer, Float> edge : array.get(u)) {
                if (!explored[edge.getKey()-1] && dist[u] != Float.MAX_VALUE && dist[u] + edge.getValue() < dist[edge.getKey()-1]) {
                    dist[edge.getKey()-1] = dist[u] + edge.getValue();
                    parents[edge.getKey()-1] = u + 1;
                }
            }

        }*/
        /*
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
        }*/
        if (target != -1) {
            System.out.println("\nDistance between vertices " + target + " and " + (s) + " = " + dist[target-1]);
            System.out.print("Path:");
            for (Integer node : GetPathDijkstra(target, parents)) {
                System.out.print(" > "+ (node));
            }
            System.out.print("\n\n");
        } else {
            System.out.println("\nDistances between vertex " + (s) + " and all vertices, along with their paths:\n");
            for (int vertex = 1; vertex <= vertexCount; vertex++) {
                System.out.println("Distance to vertex " + (vertex) + " = " + dist[vertex-1]);
                System.out.print("Path to vertex " + (vertex) + ": ");
                for (Integer node : GetPathDijkstra(vertex, parents)) {
                    System.out.print(" > "+ (node));
                }
                System.out.print("\n\n");
            }
        }
        


        
    }

    private float [] BellmanFord(int target, int start) {
        System.out.println("BellmanFord started running!");

        float dist[] = new float[vertexCount]; // Array containing the current distance estimate of each vertex (M no slide)
        for (int i = 0; i < vertexCount; i++) { // Sets all the distances to infinite
            dist[i] = Float.MAX_VALUE;
        }
        dist[target-1] = 0; // Set distance from target to itself to 0

        int[] parents = new int[vertexCount];
        parents[target-1] = -1;

        // Creating an array with all paths
        ArrayList<ArrayList<Integer>> path = new ArrayList<ArrayList<Integer>>(vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            ArrayList<Integer> inner_array = new ArrayList<Integer>();
            path.add(inner_array);
        }
        path.get(target-1).add(target);


        // Iterate through all possible number of edges that OPT can use to find the minimum path between target and v 
        for (int i = 1; i < vertexCount; i++) {  //  1 <= i <= n-1
            // Iterate through all vertices                                               
            for (int v = 1; v <= vertexCount; v++) {  // 1 <= v <= n (OBS THERE IS NO V0, but the position 0 in dist is for V1)
                // Iterate through all edges of v (All pairs are inside of an array in the position v, inside the outer array)
                for (Pair<Integer, Float> edge : array.get(v-1)) {
                    if (dist[edge.getKey() - 1] + edge.getValue() < dist[v-1] && v != target  && parents[edge.getKey()-1] != v) {
                        
                        // Update dist
                        dist[v-1] = dist[edge.getKey() - 1] + edge.getValue();

                        // Update path of target to v
                        ArrayList<Integer> temp = new ArrayList<Integer>(path.get(edge.getKey()-1));
                        path.set(v-1, temp);

                        // Test if there is a negative cycle (in the middle of the path update)
                        if (temp.contains(v)) {
                            System.out.print("VERTEX: "+ v + ", PATH: " + temp + " i: " + i);
                            System.out.println("\nUnknown distance due to negative cycle.");
                            return null;
                        }

                        // Finishes updating path of target to v
                        path.get(v-1).add(v);
                        
                        // Update parent
                        parents[v-1] = edge.getKey();

                        System.out.println(Arrays.toString(path.toArray()) + " after, v = " + v + ", i = " + i);
                    }
                }
            }
        }
        
        /*
        for (int v=1; v <= vertexCount; v++) {
            for (Pair<Integer, Float> edge : array.get(v-1)) {
                if (dist[edge.getKey() - 1] + edge.getValue() < dist[v-1]) { // if dist[neighbour] + c_wv < dist[v]]
                    //if (parents[edge.getKey()-1] != v-1) {
                    System.out.println("Unknown distance due to negative cycle.");
                    return null;
                    //}
                    
                }
            }
        }*/
        // Debug
        if (start != -1) {
            System.out.println("\nDistance between vertices " + start + " and " + target + " = " + dist[start-1]);
            System.out.print("Path:");
            for (int index = 0; index < path.size(); index++) {
                System.out.println(path.get(index));
                
            }
            /*
            for (Integer node : path.get(start-1)) {
                System.out.print(" > "+ (node));
            }*/
            System.out.print("\n\n");
        } else {
            System.out.println("\nDistances between vertex " + (target) + " and all vertices, along with their paths:\n");
            for (int vertex = 1; vertex <= vertexCount; vertex++) {
                System.out.println("Distance to vertex " + (vertex) + " = " + dist[vertex-1]);
                System.out.print("Path to vertex " + (vertex) + ": ");
                for (Integer node : path.get(vertex-1) ) {
                    System.out.print(" > "+ (node));
                }
                System.out.print("\n\n");
            }
        }
        
        /*
        for (int i = 0; i < dist.length; i++) {
            int temp = i+1;
            System.out.println("Distance of vertex " + temp + " to vertex " + target + " is: "+ dist[i]);
        }*/
        System.out.println("BellmanFord algorithm is finished!");
        return dist;
    }

    private int MinDistance(float dist[], Boolean explored[]) {
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

    private LinkedList<Integer> GetPathDijkstra(int vertex, int[] parents) {
        //for (int i = 0; i < parents.length; i++) {
        //    System.out.println(parents[i]);
        //}
        int currentParent = parents[vertex-1];
        LinkedList<Integer> path = new LinkedList<Integer>();
        path.add(vertex);
        while (currentParent != -1) {
            path.add(currentParent);
            currentParent = parents[currentParent-1];
        }
        return path;
    }

    public void MST() {

        int parents[] = new int[vertexCount];
        float key[] = new float[vertexCount];
        Boolean explored[] = new Boolean[vertexCount];

        for (int i = 0; i < vertexCount; i++) {
            key[i] = Integer.MAX_VALUE;
            explored[i] = false;
        }
        key[0] = 0;
        parents[0] = -1;

        for (int i = 0; i < vertexCount-1; i++) {
            int u = MinDistance(key, explored);
            explored[u] = true;

            for (Pair<Integer, Float> edge : array.get(u)) {
                if (!explored[edge.getKey()-1] && edge.getValue() < key[edge.getKey()-1]) {
                    parents[edge.getKey()-1] = u;
                    key[edge.getKey()-1] = edge.getValue();
                }
            }
        }
        /*
        for (int index = 0; index < parents.length; index++) {
            System.out.println("Node: " + (index + 1) + ", Parent: " + (parents[index]+1));
        }
        */
        try {
            FileWriter myWriter = new FileWriter("mst.txt");
            for (int index = 0; index < parents.length; index++) {  
                if (parents[index] != -1) {
                    myWriter.write((index+1) + " ");
                    myWriter.write((parents[index]+1) + "\n");
                }
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    




    public static void main(String[] args) {
        WGraph myGraph = new WGraph(false);
        // myGraph.PrintMatrix(myGraph.matrix);
        // myGraph.PrintArray(myGraph.array);
        //System.out.println(myGraph.WeightedDistance(1));
        //System.out.println(myGraph.Diameter(false));
        //myGraph.DFS(1);
        //myGraph.BFS(1);
        myGraph.DistanceAll(1);
        //myGraph.MST();

        //myGraph.BellmanFord(2);

        myGraph.MST();
        
    }


}
