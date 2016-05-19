package character;

import game.Entity;
import game.Game;
import game.SpriteStore;

/**
 * An entity representing a shot fired by the player's ship
 */
public class CharHitBox extends Entity {
	/** The game in which this entity exists */
	private static Game game      = Game.getInstance();

	private static int characterX = game.getCharacter().getX();
	private static int characterY = game.getCharacter().getY();
	private static int characterW = (game.getCharacter().getSprite().getWidth() / 2) - 4;
	private static int characterH = ((game.getCharacter().getSprite().getHeight() / 2) - 1);

	//path files
	private static String boxPath   = "sprites/charHitBoxempty.png";
	private static String emptyBox  = "sprites/CharHitBoxempty.png";
    private static String hitBox    = "sprites/CharHitBox.png";

	public CharHitBox() {
            super(boxPath, (characterX + characterW), (characterY + characterH));
	}

	/**
	 * Request that this shot moved based on time elapsed
	 * @param delta The time that has elapsed since last move
	 */
	
	public void move(long delta) {
		super.move(delta);
                x      = game.getCharacter().getX() + (game.getCharacter().getSprite().getWidth() / 2) - 4;
                y      = game.getCharacter().getY() + (game.getCharacter().getSprite().getHeight() / 2) - 1;
                sprite = SpriteStore.get().getSprite(emptyBox);
                
                if (game.getKeyPressed(5)){
                    if(!(game.getGameTime() < game.getCharacter().getInvincibility() - 2000)){
                        sprite = SpriteStore.get().getSprite(hitBox);
                    }
                }
                
	}

	/**
	 * Notification that this shot has collided with another
	 * entity
	 * @parma other The other entity with which we've collided
	 */
	public void collidedWith(Entity other) {}
}