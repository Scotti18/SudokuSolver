import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class SudokuGUI extends JFrame{
	// Test Sudoku
	private final int[][] sudoku1 = { 
			{3, 0, 6, 5, 0, 8, 4, 0, 0}, 
	        {5, 2, 0, 0, 0, 0, 0, 0, 0}, 
	        {0, 8, 7, 0, 0, 0, 0, 3, 1}, 
	        {0, 0, 3, 0, 1, 0, 0, 8, 0}, 
	        {9, 0, 0, 8, 6, 3, 0, 0, 5}, 
	        {0, 5, 0, 0, 9, 0, 6, 0, 0}, 
	        {1, 3, 0, 0, 0, 0, 2, 5, 0}, 
	        {0, 0, 0, 0, 0, 0, 0, 7, 4}, 
	        {0, 0, 5, 2, 0, 6, 3, 0, 0} };
	
	// Almost solved sudoku
	private final int[][] sudoku2 = {
		      { 9, 8, 0, 2, 7, 6, 3, 1, 5 }, 
		      { 2, 5, 7, 1, 3, 8, 4, 9, 6 }, 
		      { 6, 1, 3, 9, 0, 5, 2, 7, 8 },
		      { 4, 2, 9, 3, 6, 7, 0, 5, 0 }, 
		      { 5, 6, 1, 8, 2, 9, 7, 4, 3 }, 
		      { 3, 7, 8, 5, 1, 4, 6, 2, 9 },
		      { 8, 3, 2, 0, 5, 1, 0, 6, 4 }, 
		      { 7, 4, 5, 6, 9, 3, 1, 8, 2 }, 
		      { 0, 0, 6, 4, 8, 2, 5, 3, 0 } };
	
	// Most difficult sudoku in the world
	private final static int[][] sudoku3 = {
			{ 8, 0, 0, 0, 0, 0, 0, 0, 0 },
			  { 0, 0, 3, 6, 0, 0, 0, 0, 0 },
			  { 0, 7, 0, 0, 9, 0, 2, 0, 0 },
			  { 0, 5, 0, 0, 0, 7, 0, 0, 0 },
			  { 0, 0, 0, 0, 4, 5, 7, 0, 0 },
			  { 0, 0, 0, 1, 0, 0, 0, 3, 0 },
			  { 0, 0, 1, 0, 0, 0, 0, 6, 8 },
			  { 0, 0, 8, 5, 0, 0, 0, 1, 0 },
			  { 0, 9, 0, 0, 0, 0, 4, 0, 0 } };
	
	private int[][][] sudokus = {sudoku2, sudoku1, sudoku3};
	
	// Colors and borders
	private final Border border1 = BorderFactory.createLineBorder(Color.BLACK, 1);
	private final Font font1 = new Font(null, Font.BOLD, 18);
	private final Color lightblue = new Color(157, 212, 208);
    private final Color lightred = new Color(228, 171, 171);
    private final Color lightgreen = new Color(179, 245, 184);
    private final Color red = new Color(235, 71, 71);
    private final Color green = new Color(0, 150, 0);
    private final Color yellow = new Color(245, 245, 196);
    private final Color cyan = new Color(113, 181, 176);
    
    // All the main components
	private JFrame fenster;
	private JPanel p, northPanel, centerPanel, gridHelp, southPanel, eastPanel, westPanel;
	private JButton[] difficultyButtons = new JButton[3];
	private JButton[] operationButtons = new JButton[2];
	private JButton checkButton, resetButton;
	private JFormattedTextField[] sudokuLabels = new JFormattedTextField[81];
	private Sudoku newSudoku;
	
	public SudokuGUI() {
		// Basic JFrame Settings
		fenster = new JFrame();
		fenster.setTitle("Sudoku Solver");
		fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fenster.setSize(750, 600);
		
		
		// Initialize the window
		p = new JPanel();
		p.setLayout(new BorderLayout());
		fenster.setContentPane(p);
		
		// Choose Sudoku
		newSudoku = this.chooseSudoku();
		
		// Create Panels 
		northPanel = this.createSouthPanel();
		eastPanel = this.createEastPanel();
		westPanel = this.createWestPanel();
		centerPanel = this.createCenterPanel();
		northPanel = this.createNorthPanel();
		
		this.showSudoku();
	}
	
	private void showSudoku() {
		// At the end show the frame
		
		/*
		northPanel.setBorder(border1);
		flowHelp.setBorder(border1);
		southPanel.setBorder(border1);
		*/
		
		p.add(northPanel, BorderLayout.NORTH);
		p.add(centerPanel, BorderLayout.CENTER);
		p.add(southPanel, BorderLayout.SOUTH);
		p.add(eastPanel, BorderLayout.EAST);
		p.add(westPanel, BorderLayout.WEST);
		
		p.setBackground(lightblue);
		fenster.setVisible(true);
	}

	private JPanel createNorthPanel() {
		operationButtons[0] = new JButton("Quick Solve");
		operationButtons[1] = new JButton("Slow Solve");
		resetButton = new JButton("Reset");
		
		// Quick Solve button
		for (JButton button : operationButtons) {
			button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				newSudoku.resetSudoku();
				newSudoku.solveSudoku();
				
				oneField[][] solvedSudoku = newSudoku.getSolvedSudoku();
				int count = 0;
				for (int i=0; i<solvedSudoku.length; i++) {
					for (int j=0; j<solvedSudoku.length; j++) {
						sudokuLabels[count++].setText("" + solvedSudoku[i][j].getDigit());
					}
				}
				
				for (JButton b : difficultyButtons) {
					b.setEnabled(false);
				}
				for (JButton b : operationButtons) {
					b.setEnabled(false);
				}
				resetButton.setEnabled(true);
				
			}
			});
		}
		
		// Reset Button
		resetButton.setEnabled(false);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				oneField[][] unsolvedSudoku = newSudoku.getSudoku();
				int count = 0;
				for (int i=0; i<unsolvedSudoku.length; i++) {
					for (int j=0; j<unsolvedSudoku.length; j++) {
						if (!unsolvedSudoku[i][j].isGiven()) {
							sudokuLabels[count++].setText("");
						} else {
							sudokuLabels[count++].setText("" + unsolvedSudoku[i][j].getDigit());
						}
					}
				}
				
				for (JButton b : operationButtons) {
					b.setEnabled(true);
				}
				for (JButton b : difficultyButtons) {
					b.setEnabled(true);
				}
				
				resetButton.setEnabled(false);
				newSudoku.resetSudoku();
			}
		});
		
		
		// North Panel -> solve and reset buttons
		northPanel = new JPanel(new FlowLayout());
		northPanel.setOpaque(false);
		for (JButton b : operationButtons) {
			northPanel.add(b);
		}
		northPanel.add(resetButton);
		northPanel.add(Box.createRigidArea(new Dimension(0, 50)));
		
		return northPanel;
	}


	

	private JPanel createEastPanel() {
		// East Panel
		eastPanel = new JPanel();
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));
		eastPanel.add(new JLabel("Choose Difficulty: "));
		eastPanel.add(Box.createRigidArea(new Dimension(0, 40)));
		eastPanel.setOpaque(false);
		
		// Difficulty Buttons 
		difficultyButtons[0] = new JButton("Easy");
		difficultyButtons[0].setBackground(lightgreen);
		difficultyButtons[1] = new JButton("Medium");
		difficultyButtons[1].setBackground(yellow);
		difficultyButtons[2] = new JButton("Hard");
		difficultyButtons[2].setBackground(lightred);
		for (JButton b : difficultyButtons) {
			b.setPreferredSize(new Dimension(100, 30));
		}
		
		// Because we need effectively final int i for ActionListener
		int[] help = new int[difficultyButtons.length];
		for (int i=0; i<difficultyButtons.length; i++) {
			help[i] = i;
		}
		for (int i : help) {
			difficultyButtons[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					newSudoku = getThis().chooseSudoku(sudokus[i]);
					// change Sudoku -> initial = false
					getThis().setSudoku(newSudoku.getSudoku(), false);
				}
			});
			
			JPanel flowButton = new JPanel();
			flowButton.setOpaque(false);
			flowButton.add(difficultyButtons[i]);
			eastPanel.add(flowButton);
		}

		return eastPanel;
	}


	private JPanel createWestPanel() {
		// West Panel
		westPanel = new JPanel();
		westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));
		JLabel title1 = new JLabel("The");
		title1.setFont(font1);
		westPanel.add(title1);
		JLabel title2 = new JLabel("Sudoku");
		title2.setFont(font1);
		westPanel.add(title2);
		JLabel title3 = new JLabel("Solver");
		title3.setFont(font1);
		westPanel.add(title3);
		westPanel.setOpaque(false);
		return westPanel;
	}


	private JPanel createSouthPanel() {
		// South Panel 
		checkButton = new JButton("Check");
		southPanel = new JPanel();
		southPanel.add(checkButton);
		JLabel answer = new JLabel();
		answer.setPreferredSize(new Dimension(230, 30));
		answer.setHorizontalAlignment(JLabel.CENTER);
		southPanel.add(answer);
		southPanel.setOpaque(false);
		
		// Check Button 
		checkButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println(newSudoku.checkSudokuCorrectness());
				getThis().fenster.validate();
				if(newSudoku.checkSudokuCorrectness()) {
					for (JButton b : difficultyButtons) {
						b.setEnabled(false);
					}
					for (JButton b : operationButtons) {
						b.setEnabled(false);
					}
					resetButton.setEnabled(true);
					answer.setText("Sudoku is correct! Congratulations");
					answer.setVisible(true);
				} else {
					answer.setText("Sorry Sudoku is still incorrect");
				}
			}
		});
		
		return southPanel;
	}
	

	private JPanel createCenterPanel() {
		// set Sudoku field for first time -> initial = true
		this.setSudoku(newSudoku.getSudoku(), true);
		centerPanel = new JPanel();
		centerPanel.setPreferredSize(new Dimension(800, 500));
		centerPanel.add(gridHelp);
		centerPanel.setOpaque(false);
		return centerPanel;
	}
	
	
	
	//******
	// HELPER FUNTIONS
	//******
	
	private Sudoku chooseSudoku() {
		return new Sudoku();
	}
	private Sudoku chooseSudoku(int[][] toSolve) {
		return new Sudoku(toSolve);
	}
	
	// Set Sudoku in GUI -> either initial set up or change sudoku field
	private void setSudoku(oneField[][] sudokuF, boolean initial) {
		// Center Panel -> Sudoku Field
		gridHelp = new JPanel(new GridLayout(9, 9, 10, 10));
		int count = 0;
		for (int i=0; i<sudokuF.length; i++) {
			for (int j=0; j<sudokuF[1].length; j++) {
				// If it is set for the first time 
				if (initial) {
					sudokuLabels[count] = new JFormattedTextField();
					sudokuLabels[count].setBackground(new Color(200, 200, 200));
					sudokuLabels[count].setPreferredSize(new Dimension(40, 40));
					sudokuLabels[count].setOpaque(true);
					sudokuLabels[count].setBackground(yellow);
					sudokuLabels[count].setFont(new Font(null,2, 18));
					sudokuLabels[count].setHorizontalAlignment(JTextField.CENTER);
					sudokuLabels[count].setBorder(BorderFactory.createLineBorder(Color.BLACK));
				}
				
				// Set preset fields to uneditable
				if (sudokuF[i][j].getDigit() != 0) {
					sudokuLabels[count].setText("" + sudokuF[i][j].getDigit());
					sudokuLabels[count].setEditable(false);
				} else {
					// add Event Listener to not preset Fields 
					sudokuLabels[count].setEditable(true);
					sudokuLabels[count].setText("");
					final int count_final = count;
					
					// Input Correction for the editable fields 
					sudokuLabels[count].addKeyListener(new KeyListener() {
					    @Override
					    
					    public void keyTyped(KeyEvent arg0) {		    	
					    }		 
					    public void keyPressed(KeyEvent arg0) {
					    }
					    
					    public void keyReleased(KeyEvent arg0) {
					    	int parsed = 0;
					    	try {
					    		parsed = Integer.parseInt(sudokuLabels[count_final].getText());
					    		if (parsed > 9 || parsed < 1) {
					    			sudokuLabels[count_final].setText("");
					    			parsed = 0;
					    		}
					    	} catch (Exception e) {
					    			sudokuLabels[count_final].setText("");
					    	}
					        if(sudokuLabels[count_final].getText().length()>1) 
					        {
					            sudokuLabels[count_final].setText(sudokuLabels[count_final].getText().substring(0,1));
					        }
					        
					        if (parsed != 0 || sudokuLabels[count_final].getText().equals("")) {
					        	// Change value of solvedSudoku array
					        	int position_i = count_final / sudokuF.length;
					        	int position_j = count_final % sudokuF[1].length;
					        	newSudoku.changeSolvedValue(position_i, position_j, parsed);
					        	System.out.println(newSudoku);
					        } 					        
					    }

					});
				}
				if (initial) {
					gridHelp.add(sudokuLabels[count++]);
				} else {
					count++;
				}
			}
		}
		
		// Border for testing
		gridHelp.setBorder(border1);
		gridHelp.setOpaque(true);
		gridHelp.setBackground(cyan);
	}

	// To access this (SudokuGUI) within an Action Listener
	private SudokuGUI getThis() {
		return this;
	}
	
	public static void main(String[] args) {
		SudokuGUI test = new SudokuGUI();
		
	}
	
}
