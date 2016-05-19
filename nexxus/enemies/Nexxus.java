package enemies;

import bullet.PegasusBullet;
import powerups.SmallPowerUp;
import game.AePlayWave;
import game.Entity;
import game.Game;
import game.SpriteStore;
import character.CharHitBox;
import enemies.NexxusMovePattern;


public class Nexxus extends Entity {
	
    private Game game         = Game.getInstance();
    
    private long startTime    = 0L;
    private long nextSprite   = 0L;
    
    private int health        = 9;
    
    private boolean up        = false;
    private boolean inside    = false;
    private boolean side      = false;

    private int color         = 1;
    private int movePattern   = 1;
    private int bulletPattern = 1;
    private int bullets       = 0;
    private double direction  = 1;
    private int bulletSpeed   = 0;
    
    private SpriteStore loadSprites;
    private static String enemy = "sprites/ene01.png";
    private NexxusMovePattern fairyMove = new NexxusMovePattern();

    /**
     * Creates new enemy
     *
     * @param x Where the enemy spawns on the horizontal plane
     * @param y Where the enemy spawns on the vertical plane
     * @param right If the enemy will start from the right or left
     */
    public Nexxus(boolean side, int color, int movePattern, int bulletPattern, int bullets, double direction, int bulletSpeed) {
        super(enemy);
        
        if(bullets < 0)  bullets = 0;
        if(color > 4 || color < 1) color = 1;


        this.color         = color;
        this.movePattern   = movePattern;
        this.bulletPattern = bulletPattern;
        this.bullets       = bullets;   
        this.direction     = direction;
        this.bulletSpeed   = bulletSpeed;
        this.side 		   = side;
        this.x             = fairyMove.getStartPos(true, movePattern, side);
        this.y             = fairyMove.getStartPos(false, movePattern, side);
        startTime          = Game.getInstance().getGameTime();

        
        //to add more enemies just set image here
        for(int i = 1; i < 5; i++){
        	if(color == i) setImage(enemy);
        }
        
    }

    public void move(long delta) {
        // swap over horizontal movement
        super.move(delta);

        dx = fairyMove.getMove(true, movePattern, bulletPattern, startTime, side, (int) x, (int) y, color, bullets, bulletSpeed, direction);
        dy = fairyMove.getMove(false, movePattern, bulletPattern, startTime, side, (int) x, (int) y, color, bullets, bulletSpeed, direction);

        /**
         * When the enemy is defeated, it will drop a random number of powerups
         */
        if (health <= 0) {
            for(int i = 0; i < 5; i++){
                game.addEntity(new SmallPowerUp((int) x, (int) y));
                if(Math.random() >= 0.3 && i == 0){
                    i = 3;
                }
            }
            game.addScore(1000);
            new AePlayWave(game.getPath() + "/src/sounds/se_enep00.wav").start();
            game.removeEntity(this);
        }

        /**
         * enemy sprite change
         */
        if(game.getGameTime() > nextSprite){
            nextSprite = game.getGameTime() + 80;
            /**
             * enemy colors:
             * 1=Green 2=Blue 3=Yelow 4=Red
             */
            
            // Going to use 4 enemies with
            if(color == 1){
                if(this.sprite == SpriteStore.get().getSprite("sprites/ene01.png")){
                    setImage("sprites/ene01.png");
                    up = false;
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/ene01.png")){
                    if(!up){
                        setImage("sprites/ene01.png");
                    }else{
                        setImage("sprites/ene01.png");
                    }
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/ene01.png")){
                    if(!up){
                        setImage("sprites/ene01.png");
                    }else{
                        setImage("sprites/ene01.png");
                    }
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/ene01.png")){
                    setImage("sprites/ene01.png");
                    up = true;
                }
            }else if(color == 2){
                if(this.sprite == SpriteStore.get().getSprite("sprites/ene01.png")){
                    setImage("sprites/ene01.png");
                    up = false;
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/ene01.png")){
                    if(!up){
                        setImage("sprites/ene01.png");
                    }else{
                        setImage("sprites/ene01.png");
                    }
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/ene01.png")){
                    if(!up){
                        setImage("sprites/ene01.png");
                    }else{
                        setImage("sprites/ene01.png");
                    }
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/ene01.png")){
                    setImage("sprites/ene01.png");
                    up = true;
                }
            }else if(color == 3){
                if(this.sprite == SpriteStore.get().getSprite("sprites/ene01.png")){
                    setImage("sprites/ene01.png");
                    up = false;
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/ene01.png")){
                    if(!up){
                        setImage("sprites/ene01.png");
                    }else{
                        setImage("sprites/ene01.png");
                    }
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/ene01.png")){
                    if(!up){
                        setImage("sprites/ene01.png");
                    }else{
                        setImage("sprites/ene01.png");
                    }
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/ene01.png")){
                    setImage("sprites/ene01.png");
                    up = true;
                }
            }else if(color == 4){
                if(this.sprite == SpriteStore.get().getSprite("sprites/ene01.png")){
                    setImage("sprites/ene01.png");
                    up = false;
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/ene01.png")){
                    if(!up){
                        setImage("sprites/ene01.png");
                    }else{
                        setImage("sprites/ene01.png");
                    }
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/ene01.png")){
                    if(!up){
                        setImage("sprites/ene01.png");
                    }else{
                        setImage("sprites/ene01.png");
                    }
                }else if(this.sprite == SpriteStore.get().getSprite("sprites/ene01.png")){
                    setImage("sprites/ene01.png");
                    up = true;
                }
            }
        }

        /**
         * Checks if the enemy has entered the screen. If it has, remove it
         * if it leaves the screen again
         */
        if((x < game.getWidth() + sprite.getWidth() + 1 && y < game.getHeight() + sprite.getHeight() + 1) && (x >= -sprite.getWidth() - 1 && y >= -sprite.getHeight() - 1) && !inside){
            inside = true;
        }
        if((x > game.getWidth() + sprite.getWidth() + 1 || x <= -sprite.getWidth() - 1 || y > game.getHeight() + sprite.getHeight() + 1 || y <= -sprite.getHeight() - 1) && inside){
            game.removeEntity(this);
        }
    }

    /**
     * Collision logic
     *
     * @param other The other entity,
     */
    public void collidedWith(Entity other) {
        /** If the enemy collides with the players hit box, make the player lose one life */
        if(Game.getInstance().getGameTime() > Game.getInstance().getCharacter().getInvincibility()){
            if (other instanceof CharHitBox) {
                game.getCharacter().loseLife();
            }
        }
        /** If the enemy collides with the player projectile, make the enemy lose health */
        if (other instanceof PegasusBullet) {
            if(inside){
                health -= 1;
                new AePlayWave(game.getPath() + "/src/sounds/se_damage00.wav").start();
                Game.getInstance().addScore(3);
                ((PegasusBullet)other).used();
            }
        }
    }
}