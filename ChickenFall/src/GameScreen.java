
import java.awt.HeadlessException;
import javax.swing.JFrame;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author utku
 */
public class GameScreen extends JFrame{

    public GameScreen(String title) throws HeadlessException {
        super(title);
    }
    
    public static void main(String[] args) {
            GameScreen screen = new GameScreen("Chicken Fall");
            screen.setResizable(false);
            screen.setFocusable(false);
            screen.setSize(800,800);
            screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Game game = new Game();
            game.requestFocus();
            game.addKeyListener(game);
            game.setFocusable(true);
            game.setFocusTraversalKeysEnabled(false);
            screen.add(game);
            screen.setVisible(true);
    }
}
