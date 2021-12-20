import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.IOException;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;

public class Graph {
    boolean isMatrix;
    Integer[][] matrix;
    int vertexCount;
    int edgeCount;
    int minDegree;
    int maxDegree;
    double averageDegree;
    double medianDegree;
    Integer[] vertexDegrees;

    public Graph(boolean repChoice) {
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
                while (myReader.hasNextLine()) { // Reads and creates edges from input file
                    String edge [] = myReader.nextLine().split(" ");
                    AddEdge(Integer.valueOf(edge[0]), Integer.valueOf(edge[1]));
                }
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

    private Integer[][] CreateMatrix() {
        Integer[][] matrix = new Integer[vertexCount][vertexCount];
        for(int i = 0; i < vertexCount; i++)
            for(int j = 0; j < vertexCount; j++)
                matrix[i][j] = 0;
        return matrix;
    }

    private void PrintMatrix(Integer[][] matrix) {
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
        }
    }

    public void BFS() {

    }

    public void DFS() {
    
    }

    public static void main(String[] args) {
        Graph myGraph = new Graph(true);
    }


}