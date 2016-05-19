package ui;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import textures.*;

public class SplashScreen extends JWindow {

    private BorderLayout borderLayout;
    private JLabel       imgLabel;
    private JPanel       southPanel;
    private FlowLayout   southFlow;
    private JProgressBar progressBar;
    private ImageIcon    imgIcon;

    public SplashScreen(Texture texture) {
        this.imgIcon = new ImageIcon(texture.getImage());
        borderLayout = new BorderLayout();
        imgLabel = new JLabel();
        southPanel = new JPanel();
        southFlow = new FlowLayout();
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() throws Exception {
        imgLabel.setIcon(imgIcon);
        getContentPane().setLayout(borderLayout);
        southPanel.setLayout(southFlow);
        southPanel.setBackground(Color.WHITE);
        getContentPane().add(imgLabel, BorderLayout.CENTER);
        getContentPane().add(southPanel, BorderLayout.SOUTH);
        southPanel.add(progressBar, null);
        pack();
    }

    public void setMaxProgress(int maxProgress) {
        progressBar.setMaximum(maxProgress);
    }

    public void setProgress(final int progress) {
        final float percentage = ((float) progress / (float) progressBar.getMaximum()) * 100;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setValue(progress);
                progressBar.setString("Loading: " + (int) percentage + "%");
            }
        });
    }

}