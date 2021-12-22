import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.IOException;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class Test {
    boolean isMatrix;
    int[][] matrix;
    Integer[][] vector;
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
        CalculateAttributes();
        PrintMatrix(matrix);
        WriteToOutputFile();
    }

    private void ReadInputFile() {
        try {
            File myObj = new File("Input.txt");
            Scanner myReader = new Scanner(myObj);

            vertexCount = Integer.valueOf(myReader.nextLine()); // Reads vertex count from input file

            vertexDegrees = new Integer[vertexCount];
            for (int i = 0; i < vertexCount; i++) vertexDegrees[i] = 0;

            if (isMatrix) {
                matrix = CreateMatrix();
            }

            while (myReader.hasNextLine()) { // Reads and creates edges from input file
                String edge [] = myReader.nextLine().split(" ");
                AddEdge(Integer.valueOf(edge[0]), Integer.valueOf(edge[1]));
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



    private void WriteToOutputFile() {
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

    private int[][] CreateMatrix() {
        int[][] matrix = new int[vertexCount][vertexCount];
        return matrix;
    }

    private void PrintMatrix(int[][] matrix) {
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

    private void AddEdge(int vertex1, int vertex2) {
        if (isMatrix) {
            if (matrix[vertex1 - 1][vertex2 - 1] == 0) {
                matrix[vertex1 - 1][vertex2 - 1] = 1;
                matrix[vertex2 - 1][vertex1 - 1] = 1;
                edgeCount++;
                vertexDegrees[vertex1 - 1]++;
                vertexDegrees[vertex2 - 1]++;
            }
        } else {
           // if (! vector[vertex1 - 1].stream(arr).AnyMatch(x -> x= vertex1)) { // If vertex2 is not connected yet
                //vector[0] = vertex2 //
            //}
        }
    }

    public void BFS(Integer origin) {
        LinkedList<Node> searchTree = new LinkedList<Node>();
        if (isMatrix) {
            markedVertices = new boolean[vertexCount];
            LinkedList<Node> queue = new LinkedList<Node>();

            markedVertices[origin - 1] = true;
            Node currentNode = new Node(origin, null);
            searchTree.add(currentNode);
            queue.add(currentNode);
            

            while (queue.size() != 0) {
                currentNode = queue.poll();
                System.out.println("current vertex = " + currentNode.name);
                for (int i = 0; i < vertexCount; i++) {
                    int n = matrix[currentNode.name - 1][i];
                    if (n != 0 && !markedVertices[i]) {
                        markedVertices[i] = true;
                        Node newNode = new Node(i + 1, currentNode);
                        queue.add(newNode);
                        searchTree.add(newNode);
                    }
                }
            }
        } else {
            
        }
        WriteToBfsFile(searchTree);
    }
    private void WriteToBfsFile(LinkedList<Node> STree) {
        try {
            FileWriter myWriter = new FileWriter("BFS search tree.txt");
            for (Node node : STree) {
                myWriter.write("Node: " + node.name);
                if (node.parent != null) myWriter.write(" Parent: " + node.parent.name);
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
        markedVertices = new boolean[vertexCount];
        ActualDFS(origin, markedVertices);
    }
    private void ActualDFS(Integer origin, boolean[] visited) {
        

        markedVertices[origin - 1] = true;
        System.out.println(origin);

        for (int i = 0; i < vertexCount; i++) {
            int n = matrix[origin - 1][i];
            if (n != 0 && !markedVertices[i]) {
                ActualDFS(i + 1, visited);
            }
        }
    }

    public static void main(String[] args) {
        Test myGraph = new Test(true);
        myGraph.DFS(1);
    }


}