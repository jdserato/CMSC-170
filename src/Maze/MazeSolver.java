package src.Maze;

import javax.swing.*;

/**
 * Created by Serato, Jay Vince on October 09, 2017.
 */
public class MazeSolver extends JFrame {
    private JPanel pnlUserInput;
    private JPanel pnlMaze;
    private JPanel pnlMain;
    private JButton part1BasicPathButton;
    private JButton part2SearchWithButton;
    private JTextArea whichOfTheFollowingTextArea;
    private JTextArea whichPartWouldYouTextArea;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton manhattanDistanceButton;
    private JButton straightLineDistanceButton;

    MazeSolver() {
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(pnlMain);
        setSize(400, 400);
    }
}
