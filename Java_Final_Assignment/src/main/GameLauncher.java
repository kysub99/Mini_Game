package main;

import base.BaseGame;
import games.NumberGame;
import games.ImageGame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameLauncher extends JFrame {
    static final int NOG = 2;
    static final int NOM = 2; 
    JButton[] gameButtons = new JButton[NOG];
    JRadioButton[] normalButtons = new JRadioButton[NOG];
    JRadioButton[] hardButtons = new JRadioButton[NOG];
    String[] gameNames = {"Number Game", "Image Game"};
    static int[][] highScores = new int[NOG][NOM];

    public GameLauncher() {
        super("Game Launcher");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container c = getContentPane();
        c.setLayout(new GridLayout(NOG, 1));
        
        createMenu();

        for (int i = 0; i < NOG; i++) {
            JPanel gamePanel = new JPanel();
            gamePanel.setLayout(new FlowLayout());

            gameButtons[i] = new JButton(gameNames[i]);
            gameButtons[i].setPreferredSize(new Dimension(130,30));
            normalButtons[i] = new JRadioButton("Normal", true);
            hardButtons[i] = new JRadioButton("Hard");
            ButtonGroup group = new ButtonGroup();
            group.add(normalButtons[i]);
            group.add(hardButtons[i]);

            for (int j = 0; j < NOM; j++) {
                highScores[i][j] = 0;
            }

            gamePanel.add(gameButtons[i]);
            gamePanel.add(normalButtons[i]);
            gamePanel.add(hardButtons[i]);
            c.add(gamePanel);

            final int gameIdx = i;
            gameButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int modeIdx = normalButtons[gameIdx].isSelected() ? 0 : 1;
                    startGame(gameIdx, modeIdx);
                }
            });
        }

        setSize(300,200);
        setVisible(true);
    }
    
    void startGame(int gameIdx, int modeIdx) {
        String mode = modeIdx == 0 ? "normal" : "hard";
        if(gameIdx == 0) {
            new NumberGame(mode);
        } else if(gameIdx == 1) {
        	new ImageGame(mode);
        }
    }

    void createMenu() {
        JMenuBar mb = new JMenuBar();
        JMenuItem[] menuItem = new JMenuItem[3];
        String[] itemTitle = {"Game Info", "Show Scores", "Exit"};
        JMenu menu = new JMenu("Menu");
                
        MenuActionListener listener = new MenuActionListener();
        for(int i=0;i<menuItem.length;i++) {
        	menuItem[i] = new JMenuItem(itemTitle[i]);
        	menuItem[i].addActionListener(listener);
        	menu.add(menuItem[i]);
        }
        mb.add(menu);
        setJMenuBar(mb);
    }
    
    class MenuActionListener implements ActionListener{
    	public void actionPerformed(ActionEvent e) {
    		String cmd = e.getActionCommand();
    		switch(cmd) {
    		case "Game Info":
    			showGameInfo();
    			break;
    		case "Show Scores":
    			showScores();
    			break;
    		case "Exit":
    			int result = JOptionPane.showConfirmDialog(
    					getContentPane(), "Exit?", "Confirm",
    					JOptionPane.YES_NO_OPTION);
    			if(result == JOptionPane.YES_OPTION)
    				System.exit(0);
    			break;
    		}
    	}
    }
    
    void showGameInfo() {
        JDialog dialog = new JDialog(this, "Game Info", true);
        dialog.setSize(300, 500);

        JTabbedPane tabbedPane = new JTabbedPane();
        
        JPanel numberGamePanel = new JPanel();
        numberGamePanel.setLayout(new BoxLayout(numberGamePanel, BoxLayout.Y_AXIS));
        numberGamePanel.add(new JLabel("Press the buttons in order"));
        numberGamePanel.add(new JLabel(new ImageIcon("images/number_normal.png")));
        numberGamePanel.add(new JLabel(new ImageIcon("images/number_hard.png")));
        tabbedPane.addTab("Number Game", numberGamePanel);
        
        JPanel imageGamePanel = new JPanel();
        imageGamePanel.setLayout(new BoxLayout(imageGamePanel, BoxLayout.Y_AXIS));
        imageGamePanel.add(new JLabel("Press the arrow key in the direction of the image in the center"));
        imageGamePanel.add(new JLabel(new ImageIcon("images/image_normal.png")));
        imageGamePanel.add(new JLabel(new ImageIcon("images/image_hard.png")));
        tabbedPane.addTab("Image Game", imageGamePanel);

        dialog.add(tabbedPane);
        dialog.setVisible(true);
    }

    
    void showScores() {
    	String[] columnNames = {"Game", "Normal Mode", "Hard Mode"};
    	String[][] data = new String[NOG][NOM + 1];
    	
    	for (int i = 0; i < NOG; i++) {
    		data[i][0] = gameNames[i];
    		data[i][1] = String.valueOf(highScores[i][0]);
    		data[i][2] = String.valueOf(highScores[i][1]);
    	}
    	
    	JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 150)); 

        JOptionPane.showMessageDialog(null, scrollPane);
    }

    public static boolean updateHighScore(int gameIdx, int modeIdx, int score) {
    	if(score>highScores[gameIdx][modeIdx]) {
    		highScores[gameIdx][modeIdx] = score;
    		return true;
    	}
    	return false;
    }

    public static void main(String[] args) {
        new GameLauncher();
    }
}
