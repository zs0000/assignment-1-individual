package code;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Minesweeper {
	//https://stackoverflow.com/questions/6802483/how-to-directly-initialize-a-hashmap-in-a-literal-way
	private static final Map<String, List<Integer>> DIRECTIONS_MAP = Map.of(
	        "up-left", Arrays.asList(-1, -1),
	        "up", Arrays.asList(-1, 0),
	        "up-right", Arrays.asList(-1, 1),
	        "left", Arrays.asList(0, -1),
	        "right", Arrays.asList(0, 1),
	        "down-left", Arrays.asList(1, -1),
	        "down", Arrays.asList(1, 0),
	        "down-right", Arrays.asList(1, 1)
	    );
	
	private char[][] myBoard;
	private char[][] myHintsBoard;

	private int myRows;
	private int myCols;
	private int myCount;
	
	public Minesweeper() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		Minesweeper testBoard = new Minesweeper();
		//testBoard.playMinesweeper();
		
		//uncomment these to test from input file.
		//testBoard.getBoardsCountFromFile();
		testBoard.loadAllBoards();

	}

	public final void saveBoard(char[][] board, String boardType) {
		Map<String, String> keyMap = Map.of(
				"hints","my_minesweeper_output.txt",
				"mines","minesweeper_input.txt"
				);
		 try {
			 	String boardKey = keyMap.get(boardType);
	            File file = new File(boardKey);
	            FileWriter writer = new FileWriter(file, true); 

	            
	            int cols = this.getCols();
	            int rows = this.getRows();
	            int count = this.getCount();
	            writer.write("Field #" + count +":"	+ "\n");
	            
	            for (int i = 0; i < rows; i++) {
	                for (int j = 0; j < cols; j++) {
	                    writer.write(board[i][j]);
	                }
	                writer.write("\n");
	            }
	            writer.write("\n");

	            writer.close();
	        } catch (IOException e) {
	            System.out.println("An error occurred while writing to file.");
	            e.printStackTrace();
	        }
		
		
	}
	
	private final boolean inRange(final int theNumber) {
		return theNumber > 0 && theNumber < 101;
	}

	private final boolean isMine(final int theRow, final int theCol) {
		return this.myBoard[theRow][theCol] == '*';
	}
	
	private final void playMinesweeper() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter an int for rows: ");
		int rows = scanner.nextInt();
		System.out.println("Enter an int for cols: ");
		int cols = scanner.nextInt();
		System.out.println("Enter an int for percentage of mines: ");
		int percentage = scanner.nextInt();
		setRows(rows);
		setCols(cols);
		InputGenerator ig = new InputGenerator();
		char[][] field = ig.generateInput(rows, cols, percentage);
		this.setBoard(field);
		this.printBoard(field);
		char[][] currHintsBoard = this.produceHintsBoard(field);
		this.printHintsBoard(currHintsBoard);
		
		this.saveBoard(currHintsBoard, "hints");
		this.saveBoard(field, "mines");
		scanner.close();
	}
	
	// largely inspired by https://www.digitalocean.com/community/tutorials/java-read-file-line-by-line
	private final void loadAllBoards() {

		try {
			
			
			
			Scanner scanner = new Scanner(new File("minesweeper_input.txt"));
			int boardCount = 1;
			int rowsCount = 0;
			int colsCount = 0;
			int rowIdx = 0;
			this.setCount(boardCount);
			char[][] board = new char[1][1];
			while(scanner.hasNextLine()) {
				String currLine = scanner.nextLine();
				String[] tempStrs = currLine.split(" ");
				if(rowsCount == 0 || colsCount == 0) {
					rowsCount = Integer.parseInt(tempStrs[0]);
					colsCount = Integer.parseInt(tempStrs[1]);
					this.setRows(rowsCount);
					this.setCols(colsCount);
					board = new char[rowsCount][colsCount];
				} else {
					if(!currLine.contains("*") && !currLine.contains(".")) {
						
						this.setBoard(board);
						char[][] currHintBoard = this.produceHintsBoard(board);
						this.printBoard();
						this.printHintsBoard();
						
						this.saveBoard(currHintBoard, "hints");
						rowsCount = Integer.parseInt(tempStrs[0]);
						colsCount = Integer.parseInt(tempStrs[1]);
						this.setRows(rowsCount);
						this.setCols(colsCount);
						board = new char[rowsCount][colsCount];
						rowIdx=0;
						this.setCount(++boardCount);
						
					} else {
						board[rowIdx] = currLine.toCharArray();
						rowIdx++;
					}
					

				}
			}

			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	private final boolean checkDirection(final int theRow, final int theCol, String theDirection) {
		boolean result = false;
		int modifiedRow = theRow + DIRECTIONS_MAP.get(theDirection).get(0);
		int modifiedCol = theCol + DIRECTIONS_MAP.get(theDirection).get(1);
		if(modifiedRow < 0 || modifiedRow > myRows-1
		|| modifiedCol < 0 || modifiedCol > myCols-1) {

			result = false;
		} else {
			result = this.isMine(modifiedRow, modifiedCol);
		}
		
		return result;
	}
	
	private final int checkDirections(final int theRow, final int theCol) {
		int total = 0;
		for(String direction: DIRECTIONS_MAP.keySet()) {
			if(this.checkDirection(theRow, theCol, direction)) {
				total ++;
			}
		}
		return total;
	}
	
	
	private final char[][] produceHintsBoard(final char[][] theMinefield){
		int neededRows = theMinefield.length;
		int neededCols = theMinefield[0].length;
		int rowsIdx = 0;
		int colsIdx = 0;
		char[][] hintsBoard = new char[neededRows][neededCols];
		
		for(char[] currRow: theMinefield) {
			for(char currCol: currRow) {
				if(currCol == '*') {
					hintsBoard[rowsIdx][colsIdx] = '*';
				} else {
					//chars get converted to ASCII representation, need to add '0' too offset. 
					//https://stackoverflow.com/questions/17984975/convert-int-to-char-in-java
					hintsBoard[rowsIdx][colsIdx]=(char) (this.checkDirections(rowsIdx, colsIdx) + '0');
				}
				colsIdx++;
			}
			rowsIdx++;
			colsIdx = 0;
		}
		this.setHintsBoard(hintsBoard);
		return hintsBoard;
	}
	
	private final int getBoardsCountFromFile() {
		int result = 0;
		try {
			File file = new File("minesweeper_input.txt");
			Scanner scanner = new Scanner(file);
			
			while (scanner.hasNextLine()) {
				String currLine = scanner.nextLine();
				if(currLine.contains(" 0")) {
					result++;
				} 
			}
			System.out.println(result);
			scanner.close();
			return result;
		} catch (FileNotFoundException error) {
			error.printStackTrace();
		}
		
		return result;
	}
	
	private final int getRows() {
		return Integer.valueOf(this.myRows);
	}
	
	private final int getCols() {
		return Integer.valueOf(this.myCols);
	}
	
	private final int getCount() {
		return Integer.valueOf(this.myCount);
	}
	
	private final void setBoard(final char[][] theBoard) {
		if(!theBoard.equals(null)) {
			this.myBoard = theBoard;
		}
	}
	
	private final void setHintsBoard(final char[][] theHintsBoard) {
		if(!theHintsBoard.equals(null)) {
			this.myHintsBoard = theHintsBoard;
		}
	}
	
	private final void setRows(final int theRows) {
		if(this.inRange(theRows)) {
			this.myRows = theRows;
		}
	}
	
	private final void setCols(final int theCols) {
		if(this.inRange(theCols)) {
			this.myCols = theCols;
		}
	}
	
	private final void setCount(final int theCount) {
		if(theCount > 0) {
			this.myCount = theCount;
		}
	}

	
	private final void printBoard() {
		System.out.println();
		for(char[] currRow: this.myBoard) {
			StringBuilder sb = new StringBuilder();
			for(char currCol: currRow) {
				sb.append(currCol);
			}
			System.out.println(sb);
			sb.setLength(0);
		}
		System.out.println();
	}
	private final void printBoard(char[][] theBoard) {
		System.out.println();
		for(char[] currRow: theBoard) {
			StringBuilder sb = new StringBuilder();
			for(char currCol: currRow) {
				sb.append(currCol);
			}
			System.out.println(sb);
			sb.setLength(0);
		}
		System.out.println();
	}
	private final void printHintsBoard() {
		System.out.println();
		for(char[] currRow: this.myHintsBoard) {
			StringBuilder sb = new StringBuilder();
			for(char currCol: currRow) {
				sb.append(currCol);
			}
			System.out.println(sb);
			sb.setLength(0);
		}
		System.out.println();
	}
	private final void printHintsBoard(char[][] theBoard) {
		System.out.println();
		for(char[] currRow: theBoard) {
			StringBuilder sb = new StringBuilder();
			for(char currCol: currRow) {
				sb.append(currCol);
			}
			System.out.println(sb);
			sb.setLength(0);
		}
		System.out.println();
	}

}
