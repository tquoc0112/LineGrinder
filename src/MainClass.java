import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainClass {
	
	public static void main(String[] args) {
		final int width = 500;
		final MainGUI gui = new MainGUI(width,width, "LineGrinder");
	
		Board board = new Board(width, 19);
		final LineGrinder game = new LineGrinder(board);
		gui.attachBoard(board.getGUI());
		gui.pack();
		gui.setVisible(true);
		gui.listenGameStartButton(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg0) {
				Object[] settings = gui.fetchSettings();
				int depth = (Integer)(settings[0]);
				boolean computerStarts = (Boolean)(settings[1]);
				
				System.out.println("Depth: " + depth + " AI Makes the first move: " + computerStarts );
				gui.showBoard();
				game.setAIDepth(depth);
				game.setAIStarts(computerStarts);
				game.start();
			}
			
		});
		
		
		
		
		
		
	}
}
