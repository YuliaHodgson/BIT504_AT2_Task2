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
	//Noughts and Crosses are displayed inside a cell, with padding from border
	public static final int CELL_PADDING = CELL_SIZE / 6;    
	public static final int SYMBOL_SIZE = CELL_SIZE - CELL_PADDING * 2;    
	public static final int SYMBOL_STROKE_WIDTH = 8;
	
	/*declare game object variables*/
	// the game board 
	private Board board;
	 	 
	//TODO: create the enumeration for the variable below (GameState currentState)
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
		
		// TODO: This JPanel fires a MouseEvent on MouseClicked so add required event listener to 'this'. 
		
		addMouseListener(this);
		board = new Board(); // Создание экземпляра доски
		initGame(); // Инициализация игры
	    
		// Setup the status bar (JLabel) to display status message       
		statusBar = new JLabel("         ");       
		statusBar.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 14));       
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));       
		statusBar.setOpaque(true);       
		statusBar.setBackground(Color.LIGHT_GRAY);  
		
		//layout of the panel is in border layout
		setLayout(new BorderLayout());       
		add(statusBar, BorderLayout.SOUTH);
		// account for statusBar height in overall height
		setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT + 30));
		
		
		// TODO: Create a new instance of the game "Board"class. HINT check the variables above for the correct name

		
		//TODO: call the method to initialise the game board

	}
	
	public static void main(String[] args) {
		    // Run GUI code in Event Dispatch thread for thread safety.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
	         public void run() {
				//create a main window to contain the panel
				JFrame frame = new JFrame(TITLE);
				
				//create the new GameMain panel and add it to the frame
				GameMain gameMain = new GameMain(); // Создаем объект GameMain	
				
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Установка операции закрытия
	            //frame.add(new GameMain()); // Добавление панели в фрейм
	            frame.add(gameMain); // Добавление панели в фрейм
	            frame.pack();
				
				//TODO: set the default close operation of the frame to exit_on_close
	            
		        frame.setLocationRelativeTo(null);
	            frame.setVisible(true);    
				
	         // Вызываем метод initGame() после создания объекта GameMain
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
				
				//TODO: use the status bar to display the message "O"'s Turn
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
			//board = new Board();
		    currentState = GameState.Playing;
		    currentPlayer = Player.Cross;
		    statusBar.setText("'X' Turn");
		    repaint();
		    
			for (int row = 0; row < ROWS; ++row) {          
				for (int col = 0; col < COLS; ++col) {  
					// all cells empty
					board.cells[row][col].content = Player.Empty;           
				}
			}
			board.initGame(); // Вызов метода инициализации доски
			currentState = GameState.Playing; // Начальное состояние игры
			currentPlayer = Player.Cross; // Крестики начинают игру
		}
		
		
		/**After each turn check to see if the current player hasWon by putting their symbol in that position, 
		 * If they have the GameState is set to won for that player
		 * If no winner then isDraw is called to see if deadlock, if not GameState stays as PLAYING
		 *   
		 */
		public void updateGame(Player thePlayer, int rowSelected, int colSelected) {
		    // Проверка на выигрыш после хода
		    if (board.hasWon(thePlayer, rowSelected, colSelected)) {
		        currentState = (thePlayer == Player.Cross) ? GameState.Cross_won : GameState.Nought_won;
		    } else if (board.isDraw()) {
		        currentState = GameState.Draw;
		    }
		    // В противном случае нет изменений в текущем состоянии игры (остаемся в режиме игры)
		}
				
	
		/** Event handler for the mouse click on the JPanel. If selected cell is valid and Empty then current player is added to cell content.
		 *  UpdateGame is called which will call the methods to check for winner or Draw. if none then GameState remains playing.
		 *  If win or Draw then call is made to method that resets the game board.  Finally a call is made to refresh the canvas so that new symbol appears*/
	
		public void mouseClicked(MouseEvent e) {  
		    // Получаем координаты места клика
		    int mouseX = e.getX();             
		    int mouseY = e.getY();             
		    // Вычисляем строку и столбец, по которым произошел клик
		    int rowSelected = mouseY / CELL_SIZE;             
		    int colSelected = mouseX / CELL_SIZE;               			
		    
		    if (currentState == GameState.Playing) {                
		        // Проверяем, что выбранная ячейка находится в пределах игрового поля
		        if (rowSelected >= 0 && rowSelected < ROWS && colSelected >= 0 && colSelected < COLS && board.cells[rowSelected][colSelected].content == Player.Empty) {
		            // Устанавливаем символ текущего игрока в выбранную ячейку
		            board.cells[rowSelected][colSelected].content = currentPlayer; 
		            // Обновляем состояние игры
		            updateGame(currentPlayer, rowSelected, colSelected);
		            
		            // Обновляем текст в статусной строке
		            statusBar.setText((currentPlayer == Player.Cross) ? "'X' Turn" : "'O' Turn");
		            
		            repaint();
		        }
		    } else {        
		        // Если игра окончена, перезапускаем игру
		        initGame();            
		    }   
		    // Перерисовываем графику на UI           
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
