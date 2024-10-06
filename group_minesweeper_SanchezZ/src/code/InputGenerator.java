package code;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Random;

public class InputGenerator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read rows, cols, and percentage from System.in
        System.out.print("Enter number of rows (1-100): ");
        int rows = scanner.nextInt();

        System.out.print("Enter number of columns (1-100): ");
        int cols = scanner.nextInt();

        System.out.print("Enter percentage of mines (0-100): ");
        int percentage = scanner.nextInt();

        // Validate input
        if (rows < 1 || rows > 100) {
            System.out.println("Invalid number of rows. Must be between 1 and 100.");
            return;
        }

        if (cols < 1 || cols > 100) {
            System.out.println("Invalid number of columns. Must be between 1 and 100.");
            return;
        }

        if (percentage < 0 || percentage > 100) {
            System.out.println("Invalid percentage. Must be between 0 and 100.");
            return;
        }

        // Compute number of mines
        int totalCells = rows * cols;
        int numMines = (int) Math.round(totalCells * percentage / 100.0);

        // Initialize the field with safe spots
        char[][] field = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                field[i][j] = '.'; // Safe spot
            }
        }

        // Randomly place mines
        Random rand = new Random();
        int minesPlaced = 0;
        while (minesPlaced < numMines) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            if (field[r][c] != '*') {
                field[r][c] = '*';
                minesPlaced++;
            }
        }

        // Write to "minesweeper_input.txt"
        try {
            File file = new File("minesweeper_input.txt");
            FileWriter writer = new FileWriter(file, true); // Append mode

            // Write rows and cols
            writer.write(rows + " " + cols + "\n");

            // Write the field
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    writer.write(field[i][j]);
                }
                writer.write("\n");
            }

            // Write "0 0" to end the input
            writer.write("0 0\n");

            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to file.");
            e.printStackTrace();
        }

        System.out.println("Minesweeper input generated and saved to minesweeper_input.txt.");
    }
    
    public final char[][] generateInput(final int theRows, final int theCols, final int thePercentage) {


         // Validate input
         if (theRows < 1 || theRows > 100) {
             System.out.println("Invalid number of rows. Must be between 1 and 100.");
             return null;
         }

         if (theCols < 1 || theCols > 100) {
             System.out.println("Invalid number of columns. Must be between 1 and 100.");
             return null;
         }

         if (thePercentage < 0 || thePercentage > 100) {
             System.out.println("Invalid percentage. Must be between 0 and 100.");
             return null;
         }

         // Compute number of mines
         int totalCells = theRows * theCols;
         int numMines = (int) Math.round(totalCells * thePercentage / 100.0);

         // Initialize the field with safe spots
         char[][] field = new char[theRows][theCols];
         for (int i = 0; i < theRows; i++) {
             for (int j = 0; j < theCols; j++) {
                 field[i][j] = '.'; // Safe spot
             }
         }

         // Randomly place mines
         Random rand = new Random();
         int minesPlaced = 0;
         while (minesPlaced < numMines) {
             int r = rand.nextInt(theRows);
             int c = rand.nextInt(theCols);
             if (field[r][c] != '*') {
                 field[r][c] = '*';
                 minesPlaced++;
             }
         }

         return field;
     }
}
