package code;
import java.io.File;
import java.io.FileNotFoundException;
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
	
	public Minesweeper() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		Minesweeper testBoard = new Minesweeper();
		testBoard.playMinesweeper();
		testBoard.getBoardsCountFromFile();
		testBoard.loadAllBoards();
//		testBoard.loadFirstBoard();
//		testBoard.produceHintsBoard(testBoard.myBoard);
//		testBoard.printHintsBoard();

	}
	
	private final boolean inRange(final int theNumber) {
		return theNumber > 0 && theNumber < 101;
	}

	private final boolean isMine(final int theRow, final int theCol) {
		return this.myBoard[theRow][theCol] == '*';
	}
	
	private final void playMinesweeper() {
		System.out.println("Enter an int for rows: ");
		Scanner scanner = new Scanner(System.in);
		final int rows = scanner.nextInt();
		System.out.println("Enter an int for cols: ");
		final int cols = scanner.nextInt();
		
		
	}
	
	private final void loadFirstBoard(){
		try {
			File file = new File("minesweeper_input.txt");
			Scanner scanner = new Scanner(file);
			String[] rowsAndColsAsStr = scanner.nextLine().split(" ");
			int boardRows = Integer.parseInt(rowsAndColsAsStr[0]);
			int boardCols= Integer.parseInt(rowsAndColsAsStr[1]);
			if(this.inRange(boardRows) && this.inRange(boardCols)) {
				setRows(boardRows);
				setCols(boardCols);
			}
			int currRow = 0;
			int currCol = 0;
			char[][] board = new char[boardRows][boardCols];
			while (scanner.hasNextLine()) {
				String currLine = scanner.nextLine();
				String[] checkString = currLine.split(" ");	
				if(checkString.length == 2) {
					//skip, as this is a start or end.
				} else {
					for(char currCell: currLine.toCharArray()) {
						board[currRow][currCol] = currCell;
						currCol++;
					}
					currRow++;
					currCol = 0;
				}
					
				
				//end cond
				if(checkString.length == 2 
				&& checkString[0].equals("0") 
				&& checkString[1].equals("0")
				) {
					this.setBoard(board);
					this.printBoard();
					break;
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	// largely inspired by https://www.digitalocean.com/community/tutorials/java-read-file-line-by-line
	private final void loadAllBoards() {

		try {
			Scanner scanner = new Scanner(new File("minesweeper_input.txt"));
			int rowsCount = 0;
			int colsCount = 0;
			int rowIdx = 0;

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
					if(currLine.contains(" 0")) {
						rowIdx = 0;
						rowsCount = 0;
						colsCount = 0;
						this.setBoard(board);
						this.printBoard();
						this.produceHintsBoard(board);
						this.printHintsBoard();

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
	
	
	private final void produceHintsBoard(final char[][] theMinefield){
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
	

}
