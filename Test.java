import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.IOException;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;
//import java.util.List;
import java.util.ArrayList;
//import java.io.*;
import java.util.*;
//import java.util.stream.IntStream;

public class Test {
    boolean isMatrix;
    ArrayList<ArrayList<Boolean>> matrix;
    ArrayList<ArrayList<Integer>> array;
    int vertexCount;
    int edgeCount;
    int minDegree;
    int maxDegree;
    double averageDegree;
    double medianDegree;
    Integer[] vertexDegrees;
    boolean[] markedVertices;

    public Test(boolean repChoice) {
        isMatrix = repChoice;
        ReadInputFile();
        //CalculateAttributes();
        //WriteToOutputFile();
    }

    private void ReadInputFile() {
        try {
            File myObj = new File("Input.txt");
            Scanner myReader = new Scanner(myObj);

            vertexCount = Integer.valueOf(myReader.nextLine()); // Reads vertex count from input file

            //vertexDegrees = new Integer[vertexCount];
            //for (int i = 0; i < vertexCount; i++) vertexDegrees[i] = 0;

            if (isMatrix) {
                matrix = CreateMatrix();
            } else { 
                array = CreateArray();
            }

            while (myReader.hasNextLine()) { // Reads and creates edges from input file
                String edge [] = myReader.nextLine().split(" ");
                AddEdge(Integer.valueOf(edge[0]), Integer.valueOf(edge[1]));
            }
            //liberar espaco de memoria de vertexDegrees
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
        double sum = 0; //average will have decimal point

        for(int i=0; i < vertexDegrees.length; i++) sum += Double.valueOf(vertexDegrees[i]);

        return sum/vertexDegrees.length;
    
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
            FileWriter myWriter = new FileWriter("Output.txt");
            myWriter.write("Number of vertices: " + vertexCount + "\n");
            myWriter.write("Number of edges: " + edgeCount + "\n");
            myWriter.write("Minimum degree: " + minDegree + "\n");
            myWriter.write("Maximum degree: " + maxDegree + "\n");
            myWriter.write("Average degree: " + averageDegree + "\n");
            myWriter.write("Median degree: " + medianDegree + "\n");
            myWriter.write("Connected components: " + "\n");

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

    private ArrayList<ArrayList<Integer>> CreateArray() {
        ArrayList<ArrayList<Integer>> array = new ArrayList<ArrayList<Integer>> (vertexCount); // Create array and set it's size
        for (int i = 0; i < vertexCount; i++) {
            ArrayList<Integer> inner_array = new ArrayList<Integer>();
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

    private void AddEdge(int vertex1, int vertex2) {
        if (isMatrix) {
            if (matrix.get(vertex1 - 1).get(vertex2 - 1) == false && vertex1 != vertex2) {
                matrix.get(vertex1 - 1).set(vertex2 - 1, true);
                matrix.get(vertex2 - 1).set(vertex1 - 1, true);
                edgeCount++;
                //vertexDegrees[vertex1 - 1]++;
                //vertexDegrees[vertex2 - 1]++;
            }
        } else {
            if (! array.get(vertex1 - 1).contains(vertex2) && vertex1 != vertex2) { // test if v1 and v2 are connected
                array.get(vertex1 - 1).add(vertex2);
                array.get(vertex2 - 1).add(vertex1);
                edgeCount++;
                //vertexDegrees[vertex1 - 1]++;
                //vertexDegrees[vertex2 - 1]++;
            }
        }
    }

    public void BFS(Integer origin) {
        LinkedList<Node> searchTree = new LinkedList<Node>();
        markedVertices = new boolean[vertexCount];
        LinkedList<Node> queue = new LinkedList<Node>();

        markedVertices[origin - 1] = true;
        Node currentNode = new Node(origin, null);
        searchTree.add(currentNode);
        queue.add(currentNode);
        

        while (queue.size() != 0) {
            currentNode = queue.poll();
            // System.out.println("current vertex = " + currentNode.value);

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
                //Collections.sort(array.get(currentNode.value - 1));
                for (Integer i : array.get(currentNode.value - 1)) {
                     if (!markedVertices[i - 1]) {
                        markedVertices[i - 1] = true;
                        Node newNode = new Node(i, currentNode);
                        queue.add(newNode);
                        searchTree.add(newNode);
                    }
                }
            }
                
        }
        WriteToTreeFile(searchTree, "BFS search tree.txt");
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
    public void DFS(Integer origin) {
        LinkedList<Node> searchTree = new LinkedList<Node>();
        markedVertices = new boolean[vertexCount];
        markedVertices[origin - 1] = true;
        Node rootNode = new Node(origin, null);
        searchTree.add(rootNode);
        ActualDFS(rootNode, markedVertices, searchTree);
        
        WriteToTreeFile(searchTree, "DFS search tree.txt");
    }
    private void ActualDFS(Node parentNode, boolean[] visited, LinkedList<Node> STree) {

        Node currentNode = parentNode;
        
        System.out.println(currentNode.value); //lolol
        if (isMatrix) {
            for (int i = 0; i < vertexCount; i++) {
                boolean n = matrix.get(currentNode.value - 1).get(i);
                if (n != false && !markedVertices[i]) {
                    markedVertices[i] = true;
                    Node newNode = new Node(i + 1, currentNode);
                    STree.add(newNode);
                    ActualDFS(newNode, markedVertices, STree);
                }
            }
        }else {
            for (Integer i : array.get(currentNode.value - 1)) {
                if (!markedVertices[i - 1]) {
                    markedVertices[i - 1] = true;
                    Node newNode = new Node(i, currentNode);
                    STree.add(newNode);
                    ActualDFS(newNode, markedVertices, STree);
                }
                        
            }
        }
        
    
    }

    public static void main(String[] args) {
        Test myTest = new Test(true);
        //myTest.PrintMatrix(myTest.matrix);
        
        // myTest.PrintArray(myTest.array);
        myTest.BFS(1); 
        
    }


}