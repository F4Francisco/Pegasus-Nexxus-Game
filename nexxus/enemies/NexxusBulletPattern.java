package enemies;
import bullet.*;
import game.AePlayWave;
import game.Game;
import game.SpriteStore;
import math.Math2D;
import math.Point;


public class NexxusBulletPattern {
    /**
     * Spawns bullets in a specific pattern at the enemy's location.
     * @param bulletPattern Number of the bulletPattern the fairy will use
     * bulletPatterns:
     * 1 = Releases bullets in a round circle.
     * 2 = Releases a single bullet down the screen
     * 3 = Releases a single bullet towards the player
     * @param x The X coordinate of the fairy
     * @param y The Y coordinate of the fairy
     * @param side Which side of the screen the pattern should start at, it is
     * not always relevant.
     * side:
     * true = left
     * false = right
     * @param bullets The number of bullets that should be used.
     * @param color Enemy colors: 1=Green 2=Blue 3=Yellow 4=Red
     * @param bulletSpeed The speed the bullets will use.
     * @param direction The direction the bullet will travel in, in degrees (from 0 to x), 0 is right.
     * 360 means that the bullets will go all around the fairy
     * 180 means that the bullets will go all around the bottom of the fairy from right to left
     * -180 means that the bullets will go all around the top of the fairy from right to left
     * @return The start coordinate for the fairy for either X or Y in a int.
     * If movePattern is a number not specified in getStartPos return will be 0.
     */
	
    public int BulletPattern (int bulletPattern, int x, int y, boolean side, int bullets, int color, int bulletSpeed, double direction){
        if(bulletPattern == 1){
            
        	String bulletSprite = "sprites/fairyGBullet_1.png";
            
        	if(color == 2)bulletSprite = "sprites/fairyBBullet_1.png";
            
            double dxValue;
            
            double dyValue;
            
            for(int i = 0; i < bullets; i++){
            
            	dxValue = bulletSpeed * Math.cos(Math.toRadians((double) (direction / bullets) * i));
                
            	dyValue = bulletSpeed * Math.sin(Math.toRadians((double) (direction / bullets) * i));
                
            	Game.getInstance().addEntity(new NexxusBullet(bulletSprite, x + (SpriteStore.get().getSprite(bulletSprite).getWidth() / 3), y, dxValue, dyValue));
            }
            
            new AePlayWave(Game.getInstance().getPath() + "/src/sounds/se_option.wav").start();
        }else if(bulletPattern == 2){
            
        	String bulletSprite = "sprites/fairyGBullet_1.png";
            
            Game.getInstance().addEntity(new NexxusBullet(bulletSprite, x + (SpriteStore.get().getSprite(bulletSprite).getWidth() / 3), y, 0, bulletSpeed));
            
            new AePlayWave(Game.getInstance().getPath() + "/src/sounds/se_kira00.wav").start();
            
        }else if(bulletPattern == 3){
            String bulletSprite = "sprites/fairyGBullet_1.png";
           
            direction = Math2D.direction(new Point(x, y), new Point(Game.getInstance().getHitBox().getX(), Game.getInstance().getHitBox().getY()));
            
            double dxValue = bulletSpeed * Math.cos((double) direction);
            
            double dyValue = bulletSpeed * Math.sin((double) direction);
            
            Game.getInstance().addEntity(new NexxusBullet(bulletSprite, x + (SpriteStore.get().getSprite(bulletSprite).getWidth() / 3), y, dxValue, dyValue));
            
            new AePlayWave(Game.getInstance().getPath() + "/src/sounds/se_kira00.wav").start();
        }
        return 0;
    }
}