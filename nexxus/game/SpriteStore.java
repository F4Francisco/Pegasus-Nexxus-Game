package game;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * A resource manager for sprites in the game. Its often quite important
 * how and where you get your game resources from. In most cases
 * it makes sense to have a central resource loader that goes away, gets
 * your resources and caches them for future use.
 */
public class SpriteStore {

	private static SpriteStore single       = new SpriteStore();
	private HashMap<String, Sprite> sprites = new HashMap<String, Sprite>();
	
	private GraphicsConfiguration gc;
	private Image image;
	private BufferedImage sourceImage;
	private Sprite sprite;
	private String refPath;
	private URL url;

	/**
	 * Retrieve a sprite from the store
	 * 
	 * @param ref The reference to the image to use for the sprite
	 * @return A sprite instance containing an accelerate image of the request reference
	 */
	public Sprite getSprite(String ref) {
		this.refPath = ref;
		// if we've already got the sprite in the cache
		// then just return the existing version
		if (sprites.get(ref) != null) {
			return (Sprite) sprites.get(refPath);
		}
		
		// otherwise, go away and grab the sprite from the resource
		// loader
		
		try {
			//ensures we get the sprite from the right place
			url         = this.getClass().getClassLoader().getResource(refPath);
			sourceImage = ImageIO.read(url);
		} catch (IOException e) {
			fail("Failed to load: "+ refPath);
		}
		
		// create an accelerated image of the right size to store our sprite in
		gc    = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		image = gc.createCompatibleImage(sourceImage.getWidth(),sourceImage.getHeight(),Transparency.BITMASK);
		
		// draw our source image into the accelerated image
		image.getGraphics().drawImage(sourceImage, 0, 0, null);
		
		// create a sprite, add it the cache then return it
		createSprite();
		
		return sprite;
	}
	

	public void createSprite(){
		sprite = new Sprite(image);
		sprites.put(refPath,sprite);
	}
	
	public static SpriteStore get() {
		return single;
	}

	private void fail(String message) {
		throw new IllegalArgumentException(message);
	}
}