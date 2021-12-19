import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files

public class Graph {
    boolean isMatrix;
    int vertexCount;

    public Graph(boolean representation) {
        isMatrix = representation;
    }

    private void ReadInputFile() {
        try {
            File myObj = new File("Input.txt");
            Scanner myReader = new Scanner(myObj);

            vertexCount = Integer.valueOf(myReader.nextLine()); // Reads vertex count from input file

            while (myReader.hasNextLine()) { // Reads and creates edges from input file
                String edge [] = myReader.nextLine().split(" ");
                AddEdge(Integer.valueOf(edge[0]), Integer.valueOf(edge[0]));
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void AddEdge(int vertex1, int vertex2) {

    }

    public static void main(String[] args) {
        Graph myGraph = new Graph(true);
        myGraph.ReadInputFile();
    }


}