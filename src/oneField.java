
public class oneField {
	private int digit;   // value from 1 to 9
	private boolean given; // given Sudoku field digits
	
	
	// Constructors
	public oneField(int value) {
		this.setDigit(value);
		this.setGiven(false);
	}
	public oneField(int value, boolean given) {
		this.setDigit(value);
		this.setGiven(given);
	}

	
	// Getter and Setter Methods
	public int getDigit() {
		return digit;
	}
	public void increaseDigit() {
		this.digit++;
	}
	public void setDigit(int digit) {
		this.digit = digit;
	}

	public boolean isGiven() {
		return given;
	}
	public void setGiven(boolean given) {
		this.given = given;
	}
	
	// print method
	public String toString() {
		return this.digit + " given: " + this.given;
	}
	
	
}