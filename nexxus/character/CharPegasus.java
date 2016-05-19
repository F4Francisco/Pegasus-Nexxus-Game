/**
 * @author Adam Emil Skoog, David Holmquist
 */
package character;

import game.AePlayWave;
import game.Collidable;
import game.Entity;
import game.Game;
import game.SpriteStore;

import java.awt.Point;

import math.Math2D;
import powerups.BigPowerUp;
import powerups.FullPower;
import powerups.SmallPowerUp;


public class CharPegasus extends Entity
 {

    private int     lives        			= 3;
    private int     bombs        			= 2;
    private int     power        			= 0;
    private int     graze        			= 0;
    private float   speed        			= .0f;
    private float   maximalSpeed 			= 10.f;
    private float   direction    			= -400.0f;
    private boolean speedOrDirectionChanged = false;
    private long    nextSprite              = 0L;
    private long    nextHurtSprite 			= 0L;
    private boolean spriteSleep    			= false;
    private long    nextHurt	   			= 0L;
    private long    bombCooldown   			= 0L;
    
    public CharPegasus(final Point position)
     {
        super("sprites/reimuL.png",position.x,position.y,true);
     }


    public CharPegasus setLives(final int lives)
    {
        this.lives = lives;
        if(this.lives > 8){
            this.lives = 8;
            addBombs(1);
        }else if(this.lives < 0){
            this.lives = 0;
        }
        return this;
    }


    public CharPegasus setBombs(final int bombs)
    {
        this.bombs = bombs;
        if(this.bombs > 8){
            this.bombs = 8;
        }else if(this.bombs < 0){
            this.bombs = 0;
        }
        return this;
    }


    public CharPegasus setPower(final int power)
    {
        this.power = power;
        if(this.power > 400){
            this.power = 400;
        }else if(this.power < 0){
            this.power = 0;
        }
        return this;
    }


    public CharPegasus setGraze(final int graze)
    {
        this.graze = graze;
        if(this.graze < 0){
            this.graze = 0;
        }
        return this;
    }

    
    public CharPegasus addLives(final int lives)
    {
        if(lives <= 0){
            return this;
        }
        setLives(getLives() + lives);
        new AePlayWave(Game.getInstance().getPath() + "/src/sounds/se_extend.wav").start();
	return this;
    }

  
    public CharPegasus addBombs(final int bombs)
    {
        if(bombs <= 0){
            return this;
        }
        setBombs(getBombs() + bombs);
        return this;
    }

    
    public CharPegasus addPower(final int power)
    {
        if(power <= 0){
            return this;
        }
        if((getPower() >= 300 && power + getPower() >= 400) ||
           (getPower() >= 200 && getPower() < 300 && power + getPower() >= 300) ||
           (getPower() >= 100 && getPower() < 200 && power + getPower() >= 200) ||
           (getPower() >= 0 && getPower() < 100 && power + getPower() >= 100)){
            new AePlayWave(Game.getInstance().getPath() + "/src/sounds/se_powerup.wav").start();
        }
        setPower(getPower() + power);
        return this;
    }

    public CharPegasus addGraze(final int graze)
    {
        if(graze <= 0){
            return this;
        }
        setGraze(getGraze() + graze);
        new AePlayWave(Game.getInstance().getPath() + "/src/sounds/se_graze.wav").start();
        return this;
    }

  
    public final int getLives()
    {
        return lives;
    }

    public final int getBombs()
    {
        return bombs;
    }


    public final int getPower()
    {
        return power;
    }

  
    public final int getGraze()
    {
        return graze;
    }

  
    public void loseBomb()
    {
        if(!Game.getInstance().getGodMode()){
            if (Game.getInstance().getGameTime() < bombCooldown){
                return;
            }
            setBombs(getBombs() - 1);
            bombCooldown = Game.getInstance().getGameTime() + 2000;
        }
    }

  
    public void loseLife()
    {
        if(!Game.getInstance().getGodMode()){
            if (Game.getInstance().getGameTime() < nextHurt){
		return;
            }
            setLives(getLives() - 1);
            new AePlayWave(Game.getInstance().getPath() + "/src/sounds/se_pldead00.wav").start();
            // Can't be hurt right after just being hurt.
            nextHurt = Game.getInstance().getGameTime() + 3000;
            spriteSleep = true;
            setImage("sprites/reimuempty.png");
            if (getLives() <= 0){
                Game.getInstance().addEntity(new FullPower((int) x, (int) y));
            }
            for(int i = 0; i < 10; i++){
                if(power >= 25 && i == 0){
                    Game.getInstance().addEntity(new BigPowerUp((int) x, (int) y));
                    power -= 25;
                    i += 4;
                }else if(power >= 5){
                    Game.getInstance().addEntity(new SmallPowerUp((int) x, (int) y));
                    power -= 5;
                }else{
                    break;
                }
            }
            x = Game.GAME_WIDTH / 2;
            y = Game.GAME_HEIGHT - (Game.GAME_HEIGHT / 10);
        }
    }

   
    public void setInvincibility(int miliseconds)
    {
        nextHurt = Game.getInstance().getGameTime() + miliseconds;
    }
 
    public final long getInvincibility()
    {
        return nextHurt;
    }

    public CharPegasus setSpeed(final float speed)
     {
        this.speed              = speed;
        speedOrDirectionChanged = true;
        return this;
     }

    public final float getSpeed()
     {
        return speed;
     }

    public CharPegasus setMaximalSpeed(final float speed)
     {
        maximalSpeed = speed;
        return this;
     }

    public final float getMaximalSpeed()
     {
        return maximalSpeed;
     }

    public CharPegasus setDirection(final float direction)
     {
        this.direction          = direction;
        speedOrDirectionChanged = true;
        return this;
     }

    public final float getDirection()
     {
        return direction;
     }

    @Override
    public void move(long delta)
    {
        super.move(delta);

        if (speedOrDirectionChanged)
        {
       
            if (direction>0) {
                direction -= Math.floor(direction/360.f)*360.f;
            }
            else {
                direction = 360.f + direction - (float)Math.floor(direction/360.f)*360.f;
                
            }

            final Point MOVEMENT = Math2D.lengthDir(getSpeed(),getDirection()).toAWT();
            setHorizontalMovement(MOVEMENT.x);
            setVerticalMovement(MOVEMENT.y);
        }

        if(Game.getInstance().getGameTime() > nextHurt - 2000){
            spriteSleep = false;
        }

        if(!spriteSleep){
            if(Game.getInstance().getGameTime() > nextSprite && !Game.getInstance().getKeyPressed(1) && !Game.getInstance().getKeyPressed(4)){
                nextSprite = Game.getInstance().getGameTime() + 80;
                if(this.sprite == SpriteStore.get().getSprite("sprites/reimuR.png")){
                    setImage("sprites/reimuR.png");
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/reimuR.png")){
                    setImage("sprites/reimuR.png");
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/reimuR.png")){
                    setImage("sprites/reimuR.png");
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/reimuR.png")){
                    setImage("sprites/reimuR.png");
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/reimuL.png")){
                    setImage("sprites/reimuL.png");
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/reimuL.png")){
                    setImage("sprites/reimuL.png");
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/reimuL.png") ||
                         this.sprite == SpriteStore.get().getSprite("sprites/reimuL.png") ||
                         this.sprite == SpriteStore.get().getSprite("sprites/reimuL.png")){
                    setImage("sprites/reimuL.png");
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/reimuR.png")){
                    setImage("sprites/reimuR.png");
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/reimuR.png")){
                    setImage("sprites/reimuR.png");
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/reimuR.png")){
                    setImage("sprites/reimuR.png");
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/reimuR.png") ||
                         this.sprite == SpriteStore.get().getSprite("sprites/reimuR.png") ||
                         this.sprite == SpriteStore.get().getSprite("sprites/reimuR.png")){
                    setImage("sprites/reimuR.png");
                }else{
                    setImage("sprites/reimuR.png");
                }

                if(Game.getInstance().getGameTime() < nextHurt && nextHurtSprite < Game.getInstance().getGameTime()){
                    setImage("sprites/reimuempty.png");
                    nextHurtSprite = Game.getInstance().getGameTime() + 160;
                }
            }
        }

        if (getLives() <= 0){
            nextHurt = 0;
            Game.getInstance().notifyDeath();
            return;
        }

        if (y > Game.getInstance().getHeight() - this.getSprite().getHeight()){
                y = Game.getInstance().getHeight() - this.getSprite().getHeight();
        }else if(y < Game.getInstance().getHeight() - Game.getInstance().getHeight()){
                y = Game.getInstance().getHeight() - Game.getInstance().getHeight();
        }

        if (x > Game.getInstance().getWidth() - (this.getSprite().getWidth() / 2)){
                x = Game.getInstance().getWidth() - (this.getSprite().getWidth() / 2);
        }else if(x < Game.getInstance().getWidth() - Game.getInstance().getWidth() - (this.getSprite().getWidth() / 2)){
                x = Game.getInstance().getWidth() - Game.getInstance().getWidth() - (this.getSprite().getWidth() / 2);
        }
    }

    public void collidedWith(Entity other){
        
    }

 }