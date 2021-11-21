public class SudokuIT {
	// Test Sudoku
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
	
	private final int SIZE = 9; // Sudoku 9 x 9 Matrix
	private final int SIZE_BOX = 3; // 3 x 3 sub Boxes
	
	private oneField[][] sudoku = new oneField[SIZE][SIZE];  // Given sudoku to solve -> does not change
	private oneField[][] solvedSudoku = new oneField[SIZE][SIZE];  // The solved sudoku
	
	private long startNano, startMilli, endNano, endMilli; // Duration of Algorithm 
	
	
	// Constructors 
	public SudokuIT() {
		for (int i=0; i<SIZE; i++) {
			for (int j=0; j<SIZE; j++) {
				this.sudoku[i][j] = new oneField(0);
				this.solvedSudoku[i][j] = new oneField(0);
			}
		}
	}
	public SudokuIT(int[][] sudokuToSolve) {
		for (int i=0; i<SIZE; i++) {
			for (int j=0; j<SIZE; j++) {
				if (sudokuToSolve[i][j] == 0) {  // field to be solved / changed
					this.sudoku[i][j] = new oneField(0, false);
					this.solvedSudoku[i][j] = new oneField(0, false);
				} else {  // preset field (unchangable)
					this.sudoku[i][j] = new oneField(sudokuToSolve[i][j], true);
					this.solvedSudoku[i][j] = new oneField(sudokuToSolve[i][j], true);
				}
			}
		}
	}
	
	
	// ******
	// Sudoku Solving Algorithms 
	// ******
	
	
	// Solving algorithm, that implements other helper functions
	protected void solveSudoku() {
		this.startNano = System.nanoTime();
		this.startMilli = System.currentTimeMillis();
		
		
		int[] tmpI_J = new int[2];
		
		// Now iterate over the sudoku field
		for (int i=0; i<this.SIZE;) {	
			for (int j=0; j<this.SIZE;) {
				
				label: {
				// If the field is not pre-filled	
				if (this.sudoku[i][j].isGiven() == false) {					
					do {
						// Increase value of field as long as it doesn't check out
						if(this.solvedSudoku[i][j].getDigit() < 9) {
							this.solvedSudoku[i][j].increaseDigit();
						} else {
							// If value of field is already a 9, set to 0 and go back to previous changeable field
							this.solvedSudoku[i][j].setDigit(0);
							tmpI_J = this.lastZero(i, j);
							i = tmpI_J[0];
							j = tmpI_J[1];
							break label;
						}
					} while (!checkIfFree(i, j, this.solvedSudoku[i][j].getDigit(), true) && this.solvedSudoku[i][j].getDigit() <= 9);
					
					j++;
					
				// If field is pre-filled move on to the next	
				} else {
					j++;
				} 
				} // Label ending bracket
								
			}
			i++;
		}
		
		this.endNano = System.nanoTime();
		this.endMilli = System.currentTimeMillis();
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
				return isNumberInArray(number, row) && isNumberInArray(number, column) && isNumberInArray(number, box);
			} else {
				return allDigitsInArray(row) && allDigitsInArray(column) && allDigitsInArray(box);
			}

		}
		
		// Return column (j) and line (i) of previous zero (look for it in unchanged sudoku array)
		private int[] lastZero(int row, int column) {
			int counter = 0;
			int[] help = {0, 0};
			for (int i=row; i>=0; i--) {
				for (int j=column; j>=0; j--) {
					if (this.sudoku[i][j].getDigit() == 0 && counter > 0) {
						int[] help1 = {i, j};
						return help1;
					}
					counter++;
				}
				column = this.SIZE - 1;
			}
			
			return help;
		}
	
	
	
	// ******
	// Static Helper Methods 
	// for Solving the Sudoku
	// ******
	
	// Check if same number is not in array 
	private static boolean isNumberInArray(int zahl, int[] array) {
		int counter = 0;
		for (int i : array) {
			if (zahl == i) {
				counter++;
			}
		}
		// If number is not in array return true
		return (counter == 1) ? true : false;
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
				if ((j + 1) % 3 == 0) {
					unsolved += " ";
					solved += " ";
				}

			}
			unsolved += '\n';
			solved += '\n';
			
			if ((i+1) % 3 == 0) {
				unsolved += '\n';
				solved += '\n';
			}
		}
		unsolved += "--------------------\n";
		solved += "Duration Time in Nano: " + (this.endNano - this.startNano) + '\n';
		solved += "Duration Time in Milli: " + (this.endMilli - this.startMilli) + '\n';
		solved += "--------------------\n";
		
		if (this.checkSudokuCorrectness()) {
			return "Unsolved Sudoku: \n" + unsolved + "\nSolved Sudoku: \n"+ solved + "Sudoku correct!";
		} else {
			return "Unsolved Sudoku: \n\nSudoku still incorecct!\n" + unsolved + "\n";
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
	
	// Change value of solvedSudoku array
	protected void changeSolvedValue(int i, int j, int value) {
		this.solvedSudoku[i][j].setDigit(value);
	}
	
	// reset the solvedSudkou field 
	protected void resetSudoku() {
		for (int i=0; i<this.SIZE; i++) {
			for (int j=0; j<this.SIZE; j++) {
				this.solvedSudoku[i][j].setDigit(this.sudoku[i][j].getDigit());
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		SudokuIT test = new SudokuIT(sudoku1);
		System.out.println(test);
		test.solveSudoku();
		System.out.println(test);

	}


}