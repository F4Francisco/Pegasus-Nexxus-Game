package ui;

import textures.*;
import ui.*;

public class SplashScreenDriver {
    
    private SplashScreen screen;
    
    public SplashScreenDriver() {
        screen = new SplashScreen(new Texture("Splash"));
        screen.setLocationRelativeTo(null);
        screen.setMaxProgress(3000);
        screen.setVisible(true);
        
        for(int i = 0; i <= 3000; i++){
            for(int j = 0; j <= 50000; j++){
                String t = "ewf" + (i + j);
            }
            screen.setProgress(i);
        }
        
        screen.setVisible(false);
    }
    
}