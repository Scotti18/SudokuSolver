import java.util.Arrays;
import java.util.ArrayList;

public class FullBoard {
	private static final int[][] sudoku2 = {
			{2, 9, 5, 7, 4, 3, 8, 6, 1},
			{4, 3, 1, 8, 6, 5, 9, 0, 0},
			{8, 7, 6, 1, 9, 2, 5, 4, 3},
			{3, 8, 7, 4, 5, 9, 2, 1, 6},
			{6, 1, 2, 3, 8, 7, 4, 9, 5},
			{5, 4, 9, 2, 1, 6, 7, 3, 8},
			{7, 6, 3, 5, 3, 4, 1, 8, 9},
			{9, 2, 8, 6, 7, 1, 3, 5, 4},
			{1, 5, 4, 9, 3, 8, 6, 0, 0} };
	private static final int[][] sudoku1 = { 
			{3, 0, 6, 5, 0, 8, 4, 0, 0}, 
	        {5, 2, 0, 0, 0, 0, 0, 0, 0}, 
	        {0, 8, 7, 0, 0, 0, 0, 3, 1}, 
	        {0, 0, 3, 0, 1, 0, 0, 8, 0}, 
	        {9, 0, 0, 8, 6, 3, 0, 0, 5}, 
	        {0, 5, 0, 0, 9, 0, 6, 0, 0}, 
	        {1, 3, 0, 0, 0, 0, 2, 5, 0}, 
	        {0, 0, 0, 0, 0, 0, 0, 7, 4}, 
	        {0, 0, 5, 2, 0, 6, 3, 0, 0} };
	
	private final int SIZE = 9;
	private final int SIZE_BOX = 3;

	private int[][] emptyBoard;
	private int[] listValues, row, column, box;
	private int randomIndex, randomValue;
	
	FullBoard() {
		emptyBoard = new int[9][9];
		listValues = new int[9*9];
	}
	
		
	public String toString() {
		String s = "";
		for (int i=0; i<this.emptyBoard.length; i++) {
			for (int j=0; j<this.emptyBoard.length; j++ ) {
				s += this.emptyBoard[i][j];
				s += " ";
				if ((j+1) % 3 == 0) {
					s += " ";
				}
			}
			s += "\n";
			if ((i+1) % 3 == 0) {
				s += "\n";
			}
		}
		return s;
	}
	
	public boolean createBoard() {
		if (this.isFull()) {
			return true;
		}
		
		do {
			this.randomIndex = (int) (Math.random() * 81);
		} while (this.listValues[randomIndex] != 0);
		int rowIndex = randomIndex / 9;
		int columnIndex = randomIndex % 9;
		
		ArrayList<Integer> availableDigits = this.getAvailableDigits(randomIndex);
		int randomDigit = availableDigits.get((int) (Math.random() * availableDigits.size()));
		
		if (availableDigits.size() <= 0) {
			
		} else {
			this.listValues[randomIndex] = randomDigit;
			this.emptyBoard[rowIndex][columnIndex] = randomDigit;
		}
		
		Sudoku test = new Sudoku(this.emptyBoard);
		int sol = test.countSolutions(0, 0, 0, 1);
		// Check for at least one Solution
		if (sol != 1) {
			this.listValues[randomIndex] = 0;
			this.emptyBoard[rowIndex][columnIndex] = 0;
		} 
		return this.createBoard();
	}
	
	private ArrayList<Integer> getAvailableDigits(int index) {
		int rowIndex = index / 9;
		int columnIndex = index % 9;
		
		// Array with used digits
		int[] usedDigits = new int[this.SIZE];

		// Check used digits in row and column
		for (int i=0; i<this.SIZE; i++) {
			usedDigits[this.emptyBoard[rowIndex][i]]++;
			usedDigits[this.emptyBoard[i][columnIndex]]++;
		}
		
		// Check used digits in 3x3 box
		rowIndex -= rowIndex % this.SIZE_BOX;
		columnIndex -= columnIndex % this.SIZE_BOX;
		for(int i=rowIndex; i<rowIndex+this.SIZE_BOX; i++) {
			for (int j=columnIndex; j<columnIndex+3; j++) {
				usedDigits[this.emptyBoard[i][j]]++;
			}
		}
		
		// ArrayList with available digits
		ArrayList<Integer> unusedDigits = new ArrayList<Integer>();
		for (int i=1; i<usedDigits.length; i++) {
			if (usedDigits[i] == 0) {
				unusedDigits.add(i);
			}
		}
		
		return unusedDigits;
		
	}
	
	private boolean isFull() {
		for (int i : this.listValues) {
			if (i == 0) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		
		/*
		Sudoku test = new Sudoku();
		System.out.println(test);
		System.out.println("Anzahl Lsg: " + test.countSolutions(0, 0, 0, 200));
		System.out.println(test);
		
		Sudoku test2 = new Sudoku(sudoku1);
		System.out.println(test2);
		System.out.println("Anzahl Lsg: " + test2.solveSudoku(0, 0, null, false));
		System.out.println(test2);
		
		*/
		
		
		
		
		FullBoard test = new FullBoard();
		System.out.println(test);
		
		System.out.println(test.createBoard());
		
		System.out.println(test);
		 
		
		
	}
	
	
}
