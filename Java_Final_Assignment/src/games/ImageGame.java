package games;

import base.BaseGame;
import main.GameLauncher;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ImageGame extends BaseGame {
    private JLabel[] images;
    private int centerIdx;
    private int[] dirIdx;
    private JLabel[][] labels;
    private String mode;

    public ImageGame(String mode) {
        super("Image Game");
        this.mode = mode;
        setGame(c);
    }
    
    private ImageIcon getScaledIcon(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    @Override
    protected void setGame(Container c) {
        images = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            images[i] = new JLabel();
            images[i].setSize(50, 50);
            images[i].setHorizontalAlignment(JLabel.CENTER); 
        }

        JPanel centerPanel = new JPanel(new GridLayout(3, 5));
        centerPanel.setOpaque(false);
        labels = new JLabel[3][5];

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 5; col++) {
                labels[row][col] = new JLabel();
                labels[row][col].setHorizontalAlignment(JLabel.CENTER); 
            }
        }
        labels[1][1].setIcon(getScaledIcon("images/left.png", 50, 50));
        labels[1][3].setIcon(getScaledIcon("images/right.png", 50, 50));
        if(mode.equals("normal")) {
        	labels[1][0] = images[1];
        	labels[1][2] = images[4];
        	labels[1][4] = images[2];
        }
        else if (mode.equals("hard")) {
        	labels[0][0] = images[0];               	
        	labels[0][4] = images[2];
        	labels[1][2] = images[4];
        	labels[2][0] = images[1];
        	labels[2][4] = images[3];
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 5; col++) {
            	centerPanel.add(labels[row][col]);
            }
        }        
        c.add(centerPanel, BorderLayout.CENTER);
        
        JLabel infoLabel = new JLabel("Press the arrow key in the direction of the image in the center");
        infoLabel.setHorizontalAlignment(JLabel.CENTER);
        c.add(infoLabel, BorderLayout.SOUTH);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
            
            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyRelease(e);
            }
        });	       	       

        setRandomImages();
    }

    private void handleKeyPress(KeyEvent e) {
        updateDirectionKeyImage(e.getKeyCode(), true);
        
        if (isCorrect(e.getKeyCode())) {
            correctAnswer();
        } else {
            wrongAnswer();
        }
    }

    
    private void handleKeyRelease(KeyEvent e) {        
        setRandomImages();
    }
    
    private void updateDirectionKeyImage(int keyCode, boolean isPressed) {
        String imagePath;
        int labelCol;

        if (keyCode == KeyEvent.VK_LEFT) {
            if (isPressed) {
                if (isCorrect(keyCode)) {
                    imagePath = "images/left_green.png";
                } else {
                    imagePath = "images/left_red.png";
                }
            } else {
                imagePath = "images/left.png";
            }
            labelCol = 1;
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            if (isPressed) {
                if (isCorrect(keyCode)) {
                    imagePath = "images/right_green.png";
                } else {
                    imagePath = "images/right_red.png";
                }
            } else {
                imagePath = "images/right.png";
            }
            labelCol = 3;
        } else {
            return; // ¹«½Ã
        }

        ImageIcon icon = getScaledIcon(imagePath, 50, 50);
        labels[1][labelCol].setIcon(icon);
    }

    private boolean isCorrect(int keyCode) {
        if (mode.equals("normal")) {
            return (keyCode == KeyEvent.VK_LEFT && centerIdx == dirIdx[1]) || (keyCode == KeyEvent.VK_RIGHT && centerIdx == dirIdx[2]);
        } else if (mode.equals("hard")) {
            return (keyCode == KeyEvent.VK_LEFT && (centerIdx == dirIdx[0] || centerIdx == dirIdx[1])) || 
                   (keyCode == KeyEvent.VK_RIGHT && (centerIdx == dirIdx[2] || centerIdx == dirIdx[3]));
        }
        return false;
    }

    @Override
    protected void timeUp() {
        if(GameLauncher.updateHighScore(1, mode.equals("normal") ? 0 : 1, score)){
        	JOptionPane.showMessageDialog(null, "New Record!");
        }
        else {
        	JOptionPane.showMessageDialog(null, "Game Over");
        }        	
    }


    private void setRandomImages() {
        Random random = new Random();
        ArrayList<Integer> numbers = new ArrayList<>();
        labels[1][1].setIcon(getScaledIcon("images/left.png", 50, 50));
        labels[1][3].setIcon(getScaledIcon("images/right.png", 50, 50));
        if (mode.equals("normal")) {
            for (int i = 0; i < 2; i++) {
                numbers.add(i);
            }
        } else {
            for (int i = 0; i < 4; i++) {
                numbers.add(i);
            }
        }
        Collections.shuffle(numbers);

        dirIdx = new int[4];
        for (int i = 0; i < dirIdx.length; i++) {
            dirIdx[i] = numbers.get(i % numbers.size());
            images[i].setIcon(getScaledIcon("images/image" + numbers.get(i % numbers.size()) + ".png", 50, 50));
        }

        centerIdx = random.nextInt(numbers.size());
        images[4].setIcon(getScaledIcon("images/image" + centerIdx + ".png", 50, 50));
        

    }
}
