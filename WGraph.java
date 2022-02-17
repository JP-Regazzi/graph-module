import java.io.Console;
import java.io.File; 
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.*;
import java.util.Random;
import java.lang.Math;

public class WGraph {
    ArrayList<ArrayList<Pair<Integer, Float>>> array;
    int vertexCount;
    boolean hasNegativeWeight;

    public WGraph() {
        CreateGraph();
    }

    private void CreateGraph() {
        try {
            File myObj = new File("Input.txt");
            Scanner myReader = new Scanner(myObj);

            vertexCount = Integer.valueOf(myReader.nextLine()); // Reads vertex count from input file

            array = CreateArray();

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


    private ArrayList<ArrayList<Pair<Integer, Float>>> CreateArray() { // Create array and set it's size
        ArrayList<ArrayList<Pair<Integer, Float>>> array = new ArrayList<ArrayList<Pair<Integer, Float>>> (vertexCount);
        for (int i = 0; i < vertexCount; i++) {
            ArrayList<Pair<Integer, Float>> inner_array = new ArrayList<Pair<Integer, Float>>();
            array.add(inner_array);
        }
        return array;
    }


    private void AddEdge(int vertex1, int vertex2, float weight) {
        //System.out.println(vertex1 + " " + vertex2 + " weight = " + weight);
        if (!hasNegativeWeight && weight < 0) {
            hasNegativeWeight = true; // Tests if the graph has negative weights
        }

        if (! array.get(vertex1 - 1).contains(vertex2) && vertex1 != vertex2) { // Tests if v1 and v2 are already connected
            array.get(vertex1 - 1).add(new Pair<Integer, Float>(vertex2, weight));
            array.get(vertex2 - 1).add(new Pair<Integer, Float>(vertex1, weight));
        }
    }

    
    public void Distance(int s, int target) {
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

            explored[u] = true;
            for (Pair<Integer, Float> edge : array.get(u)) {
                if (!explored[edge.getKey()-1] && dist[u] != Float.MAX_VALUE && dist[u] + edge.getValue() < dist[edge.getKey()-1]) {
                    dist[edge.getKey()-1] = Aprox(dist[u] + edge.getValue());
                    parents[edge.getKey()-1] = u + 1;
                    queue.add(new Pair<Integer, Float>(edge.getKey(), dist[edge.getKey()-1]));
                }
            }
        }

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

    private void LowCostDijkstra(int s, int target) {
        int[] parents = new int[vertexCount]; // Array containing the parent of each node
        Boolean explored[] = new Boolean[vertexCount]; // Array that represents what vertices were explored
        float dist[] = new float[vertexCount]; // Array containing the current distance estimate of each vertex

        for (int i = 0; i < vertexCount; i++) { // Sets all vertices as not explored and all the distances to infinite
            explored[i] = false;
            dist[i] = Float.MAX_VALUE;
        }
        dist[s-1] = 0; // Distance from source to itself is set to 0
        parents[s-1] = -1;

        for (int i = 0; i < vertexCount; i++) { // Goes through all vertices in the graph
            int u = MinDistance(dist, explored); // Chooses the not yet explored vertex with minimum distance

            explored[u] = true;

            for (Pair<Integer, Float> edge : array.get(u)) {
                if (!explored[edge.getKey()-1] && dist[u] != Float.MAX_VALUE && dist[u] + edge.getValue() < dist[edge.getKey()-1]) {
                    dist[edge.getKey()-1] = dist[u] + edge.getValue();
                    parents[edge.getKey()-1] = u + 1;
                }
            }

        }

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
                            //System.out.print("VERTEX: "+ v + ", PATH: " + temp + " i: " + i);
                            System.out.println("\nUnknown distance due to negative cycle.");
                            return null;
                        }

                        // Finishes updating path of target to v
                        path.get(v-1).add(v);
                        
                        // Update parent
                        parents[v-1] = edge.getKey();
                    }
                }
            }
        }
        // Debug
        if (start != -1) {
            System.out.println("\nDistance between vertices " + start + " and " + target + " = " + dist[start-1]);
            System.out.print("Path:");
            
            for (Integer node : path.get(start-1)) {
                System.out.print(" > "+ (node));
            }
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

    public void PrimMST() {
        float totalWeight = 0;
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
            if (i%1000 == 0) System.out.println("(" + i + "/" + vertexCount + "), " + ((float)i/(float)vertexCount)*100 + "%");
            int u = MinDistance(key, explored);
            explored[u] = true;

            for (Pair<Integer, Float> edge : array.get(u)) {
                if (!explored[edge.getKey()-1] && edge.getValue() < key[edge.getKey()-1]) {

                    parents[edge.getKey()-1] = u;
                    key[edge.getKey()-1] = edge.getValue();
                }
            }
        }
        for (int index = 0; index < key.length; index++) {
            totalWeight += Aprox(key[index]);
        }

        System.out.println("Total weight = " + Aprox(totalWeight));
        
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

    private float Aprox(float input) { // Aproximates float values to 2 decimal places
        return Math.round(input * 100.0f) / 100.0f;
    }


    




    public static void main(String[] args) {
        WGraph myGraph = new WGraph();
        //myGraph.DistanceAll(2722);
        //myGraph.PrimMST();
        myGraph.BellmanFord(1, 5);
        /*
        Random gerador = new Random();
        long[] timeTaken = new long[100];
        long totalTime = 0;
        for (int index = 0; index < 100; index++) {
            long time1 = System.nanoTime();
            myGraph.DistanceAll(gerador.nextInt(myGraph.vertexCount+1));
            long time2 = System.nanoTime();
            timeTaken[index] = time2 - time1;
            totalTime += time2 - time1;
            System.out.println("Time taken " + timeTaken[index] + " ns");
            
        }
        System.out.println(totalTime);
        System.out.println(myGraph.hasNegativeWeight);
        */
    }


}
