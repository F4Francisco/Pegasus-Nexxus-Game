package powerups;

import game.AePlayWave;
import game.Entity;
import game.Game;
import math.Math2D;
import math.Point;
import character.CharHitBox;

public class LifeUp extends Entity{
    
    private int direction;
    private double playerDirection;
    private double moveSpeed = -200;
    private long startTime = 0L;
    private int collected = 0;
    private boolean startUp = true;

    public LifeUp(int x, int y) {
        super("sprites/LifeUp.png", x, y, false);

        dy = moveSpeed + (Math.random() * 40);
        startTime = Game.getInstance().getGameTime();

        if(x <= (Game.getInstance().getWidth() / 7)){
            direction = -30;
        }else if (x >= Game.getInstance().getWidth() - (Game.getInstance().getWidth() / 5)){
            direction = 30;
        }else{
            if(Math.random() >= 0.5){
                direction = -30;
            }else{
                direction = 30;
            }
        }
        dx = Math.sin(Math.toRadians((Math.random()) * direction)) * moveSpeed;
    }

    public void move(long delta) {
        super.move(delta);

        if(Game.getInstance().getCharacter().getInvincibility() - Game.getInstance().getGameTime() - 2000 > 0){
            collected = 0;
        }
        if(collected == 2){
            playerDirection = Math2D.direction(new Point(x, y), new Point(Game.getInstance().getHitBox().getX(), Game.getInstance().getHitBox().getY()));
            dx = 1500 * Math.cos((double) playerDirection);
            dy = 1500 * Math.sin((double) playerDirection);
        }else if(collected == 1 && !startUp){
            playerDirection = Math2D.direction(new Point(x, y), new Point(Game.getInstance().getHitBox().getX(), Game.getInstance().getHitBox().getY()));
            dx = 500 * Math.cos((double) playerDirection);
            dy = 500 * Math.sin((double) playerDirection);


            if(Game.getInstance().getHitBox().getY() <= Game.getInstance().getHeight() / 5 &&
               Game.getInstance().getCharacter().getPower() == 400){
                collected = 2;
            }
        }else{

            if((x - Game.getInstance().getHitBox().getX() <= 40 &&
                x - Game.getInstance().getHitBox().getX() >= -40) &&
               (y - Game.getInstance().getHitBox().getY() <= 40 &&
                y - Game.getInstance().getHitBox().getY() >= -40)){
                collected = 1;
            }


            if(Game.getInstance().getHitBox().getY() <= Game.getInstance().getHeight() / 5 &&
               Game.getInstance().getCharacter().getPower() == 400){
                collected = 2;
            }


            if(dy > -3 && dy < 3){
                dx = 0;
                startUp = false;
            }


            if(dy < 200){
                dy += 3;
            }


            if (x > Game.getInstance().getWidth() - (this.getSprite().getWidth() / 2)){
                    x = Game.getInstance().getWidth() - (this.getSprite().getWidth() / 2);
            }else if(x < Game.getInstance().getWidth() - Game.getInstance().getWidth() - (this.getSprite().getWidth() / 2)){
                    x = Game.getInstance().getWidth() - Game.getInstance().getWidth() - (this.getSprite().getWidth() / 2);
            }


            if (y > (Game.getInstance().getHeight() + this.sprite.getHeight())) {
                    Game.getInstance().removeEntity(this);
            }
        }
    }

    @Override
    public void collidedWith(Entity other) {
       if (other instanceof CharHitBox){
           if(Game.getInstance().getGameTime() > Game.getInstance().getCharacter().getInvincibility() - 2000){
               if(startTime < Game.getInstance().getGameTime() - 200){
                    Game.getInstance().getCharacter().addLives(1);
                    Game.getInstance().addScore(5000);
                    new AePlayWave(Game.getInstance().getPath() + "/src/sounds/se_item01.wav").start();
                    Game.getInstance().removeEntity(this);
               }
           }
        }
    }
}
