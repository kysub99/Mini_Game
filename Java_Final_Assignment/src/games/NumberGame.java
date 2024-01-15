package games;

import base.BaseGame;
import main.GameLauncher;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

public class NumberGame extends BaseGame {
    private JButton[] buttons;
    private int curNumber;
    private ArrayList<Integer> unusedNumbers;
    private int upperBound;
    private int size;
    private String mode;

    public NumberGame(String mode) {
        super("Number Game");
        this.mode = mode;
        size = getSize(mode); 
        curNumber = 1;
        upperBound = size * size * 2;
        unusedNumbers = new ArrayList<>();
        for (int i = size * size + 1; i <= upperBound; i++) {
            unusedNumbers.add(i);
        }
        Collections.shuffle(unusedNumbers);
        setGame(c);
        
    }

    @Override
    protected void setGame(Container c) {

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new GridLayout(size, size, 5, 5));
        buttons = new JButton[size * size];

        ArrayList<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= size * size; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JButton(Integer.toString(numbers.get(i)));
            buttons[i].addMouseListener(new MyMouseAdapter());
            buttonPanel.add(buttons[i]);
        }
        c.add(buttonPanel, BorderLayout.CENTER);
        
        JLabel infoLabel = new JLabel("Press the buttons in order");
        infoLabel.setHorizontalAlignment(JLabel.CENTER); 
        c.add(infoLabel, BorderLayout.SOUTH);
    }

    @Override
    protected void timeUp() {
        if(GameLauncher.updateHighScore(0, mode.equals("normal") ? 0 : 1, score)){
        	JOptionPane.showMessageDialog(null, "New Record!");
        }
        else {
        	JOptionPane.showMessageDialog(null, "Game Over");
        }        	
    }

    private int getSize(String mode) {
        if (mode.equals("normal")) {
            return 3;
        } else if (mode.equals("hard")) {
            return 5;
        }
        return 3; 
    }



    class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            JButton button = (JButton) e.getSource();
            int number = Integer.parseInt(button.getText());

            if (number == curNumber) {
                correctAnswer();
                curNumber++;
                changeButtonColor(button, Color.GREEN, 200);
                updateNumbers(button);
            } else {
                wrongAnswer();
                changeButtonColor(button, Color.RED, 200); 
            }
            scoreLabel.setText("Score: " + score);
        }
        
        private void changeButtonColor(JButton button, Color color, int delay) {
            Color originalColor = button.getBackground();
            button.setBackground(color);


            Timer timer = new Timer(delay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    button.setBackground(originalColor);
                }
            });
            timer.setRepeats(false); 
            timer.start();
        }

        private void updateNumbers(JButton button) {
            if (unusedNumbers.isEmpty()) {
                upperBound += size * size;
                for (int i = upperBound - (size * size) + 1; i <= upperBound; i++) {
                    unusedNumbers.add(i);
                }
                Collections.shuffle(unusedNumbers);
            }
            button.setText(Integer.toString(unusedNumbers.remove(0)));
        }
    }
}
