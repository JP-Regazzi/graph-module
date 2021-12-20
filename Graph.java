import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Graph {
    boolean isMatrix;
    int vertexCount;
    Integer[][] matrix;

    public Graph(boolean repChoice) {
        isMatrix = repChoice;
        ReadInputFile();
        PrintMatrix(matrix);
    }

    private void ReadInputFile() {
        try {
            File myObj = new File("Input.txt");
            Scanner myReader = new Scanner(myObj);

            vertexCount = Integer.valueOf(myReader.nextLine()); // Reads vertex count from input file

            matrix = CreateMatrix();

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
        matrix[vertex1 - 1][vertex2 - 1] = 1;
        matrix[vertex2 - 1][vertex1 - 1] = 1;
        
        System.out.println("Adding edge from vertex " + vertex1 + " to vertex " + vertex2);
    }

    public static void main(String[] args) {
        Graph myGraph = new Graph(true);
    }


}