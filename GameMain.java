import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameMain extends JPanel implements MouseListener{
	//Constants for game 
	// number of ROWS by COLS cell constants 
	public static final int ROWS = 3;     
	public static final int COLS = 3;  
	public static final String TITLE = "Tic Tac Toe";

	//constants for dimensions used for drawing
	//cell width and height
	public static final int CELL_SIZE = 100;
	//drawing canvas
	public static final int CANVAS_WIDTH = CELL_SIZE * COLS;
	public static final int CANVAS_HEIGHT = CELL_SIZE * ROWS;
	//Noughts and Crosses are displayed inside a cell, with padding from the border.
	public static final int CELL_PADDING = CELL_SIZE / 6;    
	public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;    
	public static final int SYMBOL_STROKE_WIDTH = 8;
	
	/*declare game object variables*/
	// the game board 
	private Board board;
	 	 
	// create the enumeration for the variable below (GameState currentState)
	//HINT all of the states you require are shown in the code within GameMain
	private GameState currentState; 
	
	// the current player
	private Player currentPlayer; 
	// for displaying game status message
	private JLabel statusBar;       
	

	/** Constructor to setup the UI and game components on the panel */
	public enum GameState {
	    Playing, Draw, Cross_won, Nought_won
	}

	public GameMain() {
        statusBar = new JLabel(" ");
        statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));
        statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
        statusBar.setOpaque(true);
        statusBar.setBackground(Color.LIGHT_GRAY);
        setLayout(new BorderLayout());
        add(statusBar, BorderLayout.SOUTH);

        board = new Board();
        add(board);
        setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT + 30));

        addMouseListener(this);
        initGame();
    }
	
public static void main(String[] args) {
		    // Run GUI code in Event Dispatch thread for thread safety.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
				//create a main window to contain the panel
				JFrame frame = new JFrame(TITLE);
				
				//create the new GameMain panel and add it to the frame
				GameMain gameMain = new GameMain(); 
				
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	            //frame.add(new GameMain()); 
	            frame.add(gameMain); 
	            frame.pack();
	            
		        frame.setLocationRelativeTo(null);
	            frame.setVisible(true);    
				gameMain.initGame();
	         }
		 });
	}
	/** Custom painting codes on this JPanel */
	public void paintComponent(Graphics g) {
		//fill background and set colour to white
		super.paintComponent(g);
		setBackground(Color.WHITE);
		//ask the game board to paint itself
		board.paint(g);
		
		//set status bar message
		if (currentState == GameState.Playing) {          
			statusBar.setForeground(Color.BLACK);          
			if (currentPlayer == Player.Cross) {   
			
				//TODO: use the status bar to display the message "X"'s Turn
				statusBar.setText("'X' Turn");
				
			} else {    
				
				statusBar.setText("'O' Turn");
				
			}       
			} else if (currentState == GameState.Draw) {          
				statusBar.setForeground(Color.RED);          
				statusBar.setText("It's a Draw! Click to play again.");       
			} else if (currentState == GameState.Cross_won) {          
				statusBar.setForeground(Color.RED);          
				statusBar.setText("'X' Won! Click to play again.");       
			} else if (currentState == GameState.Nought_won) {          
				statusBar.setForeground(Color.RED);          
				statusBar.setText("'O' Won! Click to play again.");       
			}
		}
		
	
	  /** Initialise the game-board contents and the current status of GameState and Player) */
	public void initGame() {
		currentState = GameState.Playing;
        currentPlayer = Player.Cross;
        statusBar.setText("X's Turn");
        board.initGame();  // Reinitializing the playing field
    }
		
		
		/**After each turn check to see if the current player hasWon by putting their symbol in that position, 
		 * If they have the GameState is set to won for that player
		 * If no winner then isDraw is called to see if deadlock, if not GameState stays as PLAYING
		 *   
		 */
		public void updateGame(Player thePlayer, int rowSelected, int colSelected) {
		    // Checking for a win after a move
			if (board.hasWon(thePlayer, rowSelected, colSelected)) {  // Check for win
		        currentState = (thePlayer == Player.Cross) ? GameState.Cross_won : GameState.Nought_won;
		        statusBar.setText(thePlayer == Player.Cross ? "X Won! Click to play again." : "O Won! Click to play again.");
		    } else if (board.isDraw()) {  // Checking for a draw
		        currentState = GameState.Draw;
		        statusBar.setText("It's a Draw! Click to play again.");
		    }
		}
				
	
		/** Event handler for the mouse click on the JPanel. If selected cell is valid and Empty then current player is added to cell content.
		 *  UpdateGame is called which will call the methods to check for winner or Draw. if none then GameState remains playing.
		 *  If win or Draw then call is made to method that resets the game board.  Finally a call is made to refresh the canvas so that new symbol appears*/
	
		public void mouseClicked(MouseEvent e) {  
		    // Get click location coordinates
		    int mouseX = e.getX();             
		    int mouseY = e.getY();             
		    // Calculate the row and column that was clicked
		    int rowSelected = mouseY / CELL_SIZE;             
		    int colSelected = mouseX / CELL_SIZE;               			
		    
		    if (currentState == GameState.Playing) {
		        if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS && board.getContent(rowSelected, colSelected) == Player.Empty) {
		            board.setContent(rowSelected, colSelected, currentPlayer); // Make a move
		            updateGame(currentPlayer, rowSelected, colSelected);  // Check for win or draw

		            // Переключить игрока
		            currentPlayer = (currentPlayer == Player.Cross) ? Player.Nought : Player.Cross;
		            statusBar.setText((currentPlayer == Player.Cross) ? "X's Turn" : "O's Turn");
		        }
		    } else {  // If the game is over, click to restart the game
		        initGame();
		    }
		    repaint();          
		}

	@Override
	public void mousePressed(MouseEvent e) {
		//  Auto-generated, event not used
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		//  Auto-generated, event not used
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// Auto-generated,event not used
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// Auto-generated, event not used
		
	}

}
