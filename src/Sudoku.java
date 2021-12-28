import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

public class Sudoku {
	// Test Sudokus
	// Sudoku with one solution
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
	// Sudoku with two solutions
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
	
	
	private final int SIZE = 9; // Sudoku 9 x 9 Matrix
	private final int SIZE_BOX = 3; // 3 x 3 sub Boxes
	
	private oneField[][] sudoku = new oneField[SIZE][SIZE];  // Given sudoku to solve -> does not change
	private oneField[][] solvedSudoku = new oneField[SIZE][SIZE];  // The solved sudoku
	private oneField[][] createSudoku = new oneField[SIZE][SIZE];
	
	private long startNano, startMilli, endNano, endMilli; // Duration of Algorithm 
	private int numberOfSolutions;
	
	// Constructors 
	public Sudoku() {
		for (int i=0; i<SIZE; i++) {
			for (int j=0; j<SIZE; j++) {
				this.sudoku[i][j] = new oneField(0);
				this.solvedSudoku[i][j] = new oneField(0);
				this.createSudoku[i][j] = new oneField(0);
			}
		}
	}
	public Sudoku(int[][] sudokuToSolve) {
		for (int i=0; i<SIZE; i++) {
			for (int j=0; j<SIZE; j++) {
				if (sudokuToSolve[i][j] == 0) {  // field to be solved / changed
					this.sudoku[i][j] = new oneField(0, false);
					this.solvedSudoku[i][j] = new oneField(0, false);
					this.createSudoku[i][j] = new oneField(0);
				} else {  // preset field (unchangable)
					this.sudoku[i][j] = new oneField(sudokuToSolve[i][j], true);
					this.solvedSudoku[i][j] = new oneField(sudokuToSolve[i][j], true);
					this.createSudoku[i][j] = new oneField(0);
				}
			}
		}
	}
	
	
	// ******
	// Sudoku Solving Algorithms 
	// ******
	
	protected void callSolvingAlgorithm() {
		this.startNano = System.nanoTime();
		this.startMilli = System.currentTimeMillis();
		System.out.println(this.solveSudoku(0, 0, null, false));
		this.endNano = System.nanoTime();
		this.endMilli = System.currentTimeMillis();
	}
	
	// If it has more than one solution it wont solve 
	protected boolean solveSudoku(int i, int j, JFormattedTextField[] labels, boolean slowSolve) {
		// Recursion anchors
		// Keep i and j in line
		if (j == 9) {
			j = 0;
			if (++i == 9) {
				return true;
			}
		}
		// Skip preset fields
		if (this.solvedSudoku[i][j].isGiven()) {
			if (slowSolve) {
				labels[i*9+j].setBorder(BorderFactory.createLineBorder(Color.RED, 2));
				labels[i*9+j].paintImmediately(labels[i*9+j].getVisibleRect());
			}
			return solveSudoku(i, j+1, labels, slowSolve);
		}

		// Brute force values from 1 to 9
		for (int value=1; value<=9; value++) {
			if (this.checkIfFree(i, j, value, true)) {
				if (labels != null && slowSolve) {
					changeGUIField(labels[i * 9 + j], value, slowSolve);
				}
				this.solvedSudoku[i][j].setDigit(value);
				solveSudoku(i, j+1, labels, slowSolve);
				
				if (!this.checkSudokuCorrectness()) {
					this.solvedSudoku[i][j].setDigit(0); // Reset if no digit fits
				}
			}
		}

		return false; // 
	}
	
	/*
	// If it has more than one solution it wont solve 
		protected boolean createSudoku(int i, int j) {
			// Recursion anchors
			// Keep i and j in line
			if (this.isFull()) {
				return true;
			}

			// Brute force values from 1 to 9
			for (int value=1; value<=9; value++) {
				if (this.checkIfFree(i, j, value, true)) {
					this.createSudoku[i][j].setDigit(value);
					int randomIndex, rowIndex, columnIndex;
					do {
						randomIndex = (int) (Math.random() * 81);
						rowIndex = randomIndex / 9;
						columnIndex = randomIndex % 9;
					} while (this.createSudoku[rowIndex][columnIndex].getDigit() != 0);
					
					createSudoku(rowIndex, columnIndex);
					
					if (this.countSolutions(0,0,0,1) != 1) {
						this.solvedSudoku[i][j].setDigit(0); // Reset if no digit fits
					}
				}
			}

			return false; // 
		}
		
		*/ //Doesn't work yet
		
		private boolean isFull() {
			for (oneField[] i : this.createSudoku) {
				for (oneField j : i) {
					if (j.getDigit() == 0) {
						return false;
					}
				}
			}
			return true;
		}
	
	
	protected int countSolutions(int i, int j, int countSolutions, int maxCount) {
		// Recursion anchors
		// Keep i and j in line
		if (j == 9) {
			j = 0;
			if (++i == 9) {
				return countSolutions + 1;
			}
		}
		// Skip preset fields
		if (this.solvedSudoku[i][j].isGiven()) {
			return countSolutions(i, j+1, countSolutions, maxCount);
		}

		// Brute force values from 1 to 9
		for (int value=1; value<=9  && countSolutions<maxCount; value++) {
			if (this.checkIfFree(i, j, value, true)) {
				this.solvedSudoku[i][j].setDigit(value);
				countSolutions = countSolutions(i, j+1, countSolutions, maxCount);
			}
		}
		
		this.solvedSudoku[i][j].setDigit(0);  // Reset if not digit fits
		return countSolutions; 
	}
	
	
	// Check whether number is present in row / column / box 
		private boolean checkIfFree(int rowIndex, int columnIndex, int number, boolean check) {
			int[] row = new int[this.SIZE];
			int[] column = new int[this.SIZE];
			int[] box = new int [this.SIZE];
			
			// Create arrays of the row and the column 
			for (int i=0; i<SIZE; i++) {
				row[i] = this.solvedSudoku[rowIndex][i].getDigit();
				column[i] = this.solvedSudoku[i][columnIndex].getDigit();
			}
			
			// Create array of the 3x3 box
			int counter = 0;
			rowIndex -= rowIndex % this.SIZE_BOX;
			columnIndex -= columnIndex % this.SIZE_BOX;
			for(int i=rowIndex; i<rowIndex+this.SIZE_BOX; i++) {
				for (int j=columnIndex; j<columnIndex+3; j++) {
					box[counter] = solvedSudoku[i][j].getDigit();
					counter++;
				}
			}
			
			// If check == true -> check whether number can fit in column / row / box
			// If check == false -> check the correctness of row / column / box -> (only for) checking the correctness of finished Sudoku
			if (check) {
				return isNumberNotInArray(number, row) && isNumberNotInArray(number, column) && isNumberNotInArray(number, box);
			} else {
				return allDigitsInArray(row) && allDigitsInArray(column) && allDigitsInArray(box);
			}

		}
			
	// ******
	// Static Helper Methods 
	// for Solving the Sudoku
	// ******
	
	// Check if same number is not in array 
	private static boolean isNumberNotInArray(int zahl, int[] array) {
		for (int i : array) {
			if (zahl == i) {
				return false;
			}
		}
		// If number is not in array return true
		return true;
	}

	// Check if int array of length 9 contains all values from 1 to 9
	private static boolean allDigitsInArray(int[] array) {
		int[] help = new int[array.length+1];
		for (int i=0; i<array.length; i++) {
			if (array[i]>= 0 && array[i] <=9) {
				help[array[i]]++;
			}
		}
		for (int i=1; i<help.length; i++) {
			if (help[i] != 1) {
				return false;
			}
		}
		return true;
	}
	
	// ******
	// OOP (Getter, Settter, toString, check Correctness)
	// HELPER METHODS 
	// ******
	
	// Get reference to sudoku fields
	public oneField[][] getSudoku() {
		return this.sudoku;
	}
	public oneField[][] getSolvedSudoku() {
		return this.solvedSudoku;
	}
	// if it is not a preset oneField, set value at i, j
	public void setValueSudoku(int i, int j, int value) {
		if (!this.solvedSudoku[i][j].isGiven()) {
			this.solvedSudoku[i][j].setDigit(value);
		}
	}
	// Print unsolved and (to be) solved Sudoku Fields
	public String toString() {
		String unsolved = "";
		String solved = "";
		
		for (int i=0; i<this.SIZE; i++) {
			for (int j=0; j<this.SIZE; j++) {
				
				unsolved += this.sudoku[i][j].getDigit() + " ";
				solved += this.solvedSudoku[i][j].getDigit() + " ";
				if ((j + 1) % this.SIZE_BOX == 0) {
					unsolved += " ";
					solved += " ";
				}

			}
			unsolved += '\n';
			solved += '\n';
			
			if ((i+1) % this.SIZE_BOX == 0) {
				unsolved += '\n';
				solved += '\n';
			}
		}
		unsolved += "--------------------\n";
		solved += "Duration Time in Nano: " + (this.endNano - this.startNano) + '\n';
		solved += "Duration Time in Milli: " + (this.endMilli - this.startMilli) + '\n';
		solved += "Sudoku correct!\n--------------------\n";
			
		if (this.checkSudokuCorrectness()) {
			return "Solved Sudoku: \n"+ solved;
		} else if (this.numberOfSolutions > 1) {
			return "Sudoku has no unique solution";
		} else {
			return "\nUnsolved Sudoku: \n(Sudoku still incorecct!)\n" + unsolved;
		}
			
	}
	
	protected boolean checkSudokuCorrectness() {
		for (int i=0; i<this.SIZE; i++) {
			for (int j=0; j<this.SIZE; j++) {
				if(!checkIfFree(i, j, this.solvedSudoku[i][j].getDigit(), false)) {
					return false;
				}
			}
		}
		return true;
	}
	
	// ******
	// VISUALISATION METHODS
	// ONLY FOR GUI
	// ******
	
	
	// reset the solvedSudkou field 
	protected void resetSudoku() {
		for (int i=0; i<this.SIZE; i++) {
			for (int j=0; j<this.SIZE; j++) {
				this.solvedSudoku[i][j].setDigit(this.sudoku[i][j].getDigit());
			}
		}
	}
	
	private void changeGUIField(JFormattedTextField textField, int value, boolean slowSolve) {
		int ms = 5;
		textField.setText("" + value);
		textField.paintImmediately(textField.getVisibleRect());
		if (slowSolve) {
			textField.setBorder(BorderFactory.createLineBorder(Color.GREEN, 5));
			textField.paintImmediately(textField.getVisibleRect());
			try {
			      Thread.sleep(ms);
			    } catch (InterruptedException e) {
			      e.printStackTrace();
			    }
			textField.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			textField.paintImmediately(textField.getVisibleRect());
		}
	}
	
	// Test Sudokus 
	public static void main(String[] args) {
		Sudoku test = new Sudoku(sudoku1);
		System.out.println(test);
		test.callSolvingAlgorithm();
		System.out.println(test);
		
		Sudoku test0 = new Sudoku(sudoku2);
		System.out.println(test0);
		test0.callSolvingAlgorithm();
		System.out.println(test0);

	}


}