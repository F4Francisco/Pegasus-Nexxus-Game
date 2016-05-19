package bullet;

import game.Entity;
import game.Game;
import game.SpriteStore;
import character.CharHitBox;
import character.CharPegasus;

/**
 * An entity representing a shot fired by the player's ship
 *
 * @author Daniel
 */
public class NexxusBullet extends Entity {
    /** The game in which this entity exists */
    private Game game = Game.getInstance();
    /** True if this shot has been "used", i.e. its hit something */
    private boolean used = false;
    /** True if this bullet has been grazed once */
    private boolean grazed = false;

    /**
     * Create a new shot from the player
     *
     * @param sprite The sprite representing this shot
     * @param x The initial x location of the shot
     * @param y The initial y location of the shot
     * @param dx The initial speed in x
     * @param dy The initial speed in y
     * @param speed The speed of the shot
     */
    public NexxusBullet(String sprite, int x, int y, double dx, double dy) {
        super(sprite,x,y);
        this.dy = dy;
        this.dx = dx;
    }

    /**
     * Request that this shot moved based on time elapsed
     *
     * @param delta The time that has elapsed since last move
     */
    public void move(long delta) {
            // proceed with normal move
            super.move(delta);
            // if we shot off the screen or if the bullet has hit something, remove ourselfs
            if ((x > game.getWidth() + sprite.getWidth() + 1 || x <= -sprite.getWidth() - 1 || y > game.getHeight() + sprite.getHeight() + 1 || y <= -sprite.getHeight() - 1) || used) {
                    game.removeEntity(this);
            }

            /** If the bullet collides with the players sprite, make the graze count go up by 1 */
            if (!grazed){
                if((x > game.getHitBox().getX() + (SpriteStore.get().getSprite("sprites/CharHitBox.png").getWidth() / 2) - 30  &&
                    x < game.getHitBox().getX() + (SpriteStore.get().getSprite("sprites/CharHitBox.png").getWidth() / 2) + 30) &&
                   (y > game.getHitBox().getY() + (SpriteStore.get().getSprite("sprites/CharHitBox.png").getHeight() / 2) - 30 &&
                    y < game.getHitBox().getY() + (SpriteStore.get().getSprite("sprites/CharHitBox.png").getHeight() / 2) + 30)){
                    game.getCharacter().addGraze(1);
                    game.addScore(10 * game.getCharacter().getGraze());
                    grazed = true;
                }
            }
    }

    public void used(){
        used = true;
    }

    /**
     * Notification that this shot has collided with another
     * entity
     *
     * @parma other The other entity with which we've collided
     */
    public void collidedWith(Entity other) {
        if(Game.getInstance().getGameTime() > Game.getInstance().getCharacter().getInvincibility()){
            /** If the bullet collides with the players hitbox, make the player lose one life */
            if (other instanceof CharHitBox) {
                game.getCharacter().loseLife();
                used = true;
            }
        }
    }
}