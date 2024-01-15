package base;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public abstract class BaseGame extends JFrame {
    protected int score;
    protected JLabel scoreLabel;
    protected JLabel gaugeLabel;
    protected Timer timer;
    protected JProgressBar gaugeBar;
    protected Container c;
    protected Clip clip;
    private static final int MAX_GAUGE = 10; 
    protected int remainGauge = MAX_GAUGE;
    private int delay = 1000; 
    private double speedRate = 0.99; 
    
    public BaseGame(String title) {
        super(title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        c = getContentPane();
        c.setLayout(new BorderLayout());
        
        setScorePanel(c);
        setTimer(); 
        timer.start();
        setSize(400, 300);
        setVisible(true);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (timer != null) {
                    timer.stop(); 
                }
                dispose(); 
            }
        });
    }
   
    
    private void setScorePanel(Container c) {
        JPanel scorePanel = new JPanel();
        scorePanel.setOpaque(false);

        scoreLabel = new JLabel("Score: " + score);
        scorePanel.add(scoreLabel);

        gaugeBar = new JProgressBar(0, MAX_GAUGE);
        gaugeBar.setValue(MAX_GAUGE);
        gaugeBar.setStringPainted(true); 
        gaugeBar.setString(String.valueOf(MAX_GAUGE));
        scorePanel.add(gaugeBar);

        c.add(scorePanel, BorderLayout.NORTH);
    }

    protected abstract void setGame(Container c);

    private void setTimer() {
        timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainGauge--;
                gaugeBar.setValue(remainGauge);
                gaugeBar.setString(String.valueOf(remainGauge));
                
                updateBackground(); 

                if (remainGauge <= 5) {
                    float intensity = (float) remainGauge / MAX_GAUGE;
                    c.setBackground(new Color(1.0f, intensity, intensity)); 
                }

                if (remainGauge <= 0) {
                    timer.stop();
                    timeUp();
                }

                delay *= speedRate;
                timer.setDelay((int) delay);
            }
        });
    }
    
    protected void loadAudio(String pathName) {
        try {
            File audioFile = new File(pathName);
            final AudioInputStream audioStream =
                    AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }
    
    protected void correctAnswer() {
        score++;
        scoreLabel.setText("Score: " + score); 
        remainGauge = Math.min(remainGauge + 1, MAX_GAUGE);
        gaugeBar.setValue(remainGauge);
        gaugeBar.setString(String.valueOf(remainGauge)); 
        loadAudio("audio/correct.wav");
        updateBackground(); 
    }

    
    protected void wrongAnswer() {
        score--;
        scoreLabel.setText("Score: " + score); 
        remainGauge = Math.max(remainGauge - 1, 0); 
        gaugeBar.setValue(remainGauge);
        gaugeBar.setString(String.valueOf(remainGauge)); 
        loadAudio("audio/wrong.wav");
        updateBackground(); 

        if (remainGauge == 0) {
            timer.stop();
        }
    }
    
    private void updateBackground() {
        if (remainGauge <= 5) {
            float intensity = (float) remainGauge / 10;
            c.setBackground(new Color(1.0f, intensity, intensity));
        } else {
            c.setBackground(getBackground());
        }
    }


    protected abstract void timeUp();
}
