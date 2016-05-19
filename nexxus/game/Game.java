package game;

import enemies.Nexxus;
import gui.BombGui;
import gui.ConsoleGui;
import gui.GrazeGui;
import gui.LifeGui;
import gui.MessageHandle;
import gui.PowerGui;
import gui.ScoreGui;
import gui.WaitForKeyPress;
import highscore.Score;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import bullet.PegasusBullet;
import powerups.BigPowerUp;
import powerups.BombUp;
import powerups.FullPower;
import powerups.LifeUp;
import powerups.SmallPowerUp;
import character.CharHitBox;
import character.CharPegasus;
import ui.SplashScreenDriver;

public class Game extends Canvas {

	// ////////////////////////////////////////////////////////////////////////////
	static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int WIDTH = (int) screenSize.getWidth();
	public static final int HEIGHT = (int) screenSize.getHeight();
	// ////////////////////////////////////////////////////////////////////////////

	private static Game instance;

	/**
	 * Gets the one and only instance of the game.
	 * 
	 * @return the instance
	 */
	public static Game getInstance() {
		return instance;
	}

	private BufferStrategy strategy;
	private boolean gameRunning = true;

	private double moveSpeed = 280.0;

	private boolean leftPressed = false;
	private boolean rightPressed = false;
	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean shiftPressed = false;
	private boolean xPressed = false;
	private boolean zPressed = false;

	private long leftPressedMs = 0L;
	private long rightPressedMs = 0L;
	private long shotDelay = 0L;

	private long nextSprite = 0L;
	private long nextHurtSprite = 0L;
	
	int tempWidth  = Game.GAME_WIDTH / 2;
	int tempHeight = Game.GAME_HEIGHT / 2;

	private List<GuiItem> guiitems = Collections
			.synchronizedList(new ArrayList<GuiItem>());
	private List<GuiItem> guiremove = Collections
			.synchronizedList(new ArrayList<GuiItem>());
	private List<Entity> entities = Collections
			.synchronizedList(new ArrayList<Entity>());
	private List<Entity> removeList = Collections
			.synchronizedList(new ArrayList<Entity>());
	private boolean[] keys = { leftPressed, upPressed, downPressed,
			rightPressed, shiftPressed };

	private Sprite lastSprite;

	// path files
	private String path = new java.io.File(".").getCanonicalPath();
	BasePlayer music = new WavPlayer(new File(path + "/src/sounds/1969.wav"));

	private int spawnTime = 1;

	private CharPegasus ply;
	private CharHitBox hbox;

	int sleeptime = 10;
	private float speed = 1.0f; // game speed

	private Score score = new Score();

	private WaitForKeyPress waitForKeyPress = null;

	/**
	 * The console text field GuiItem. We need to store it so we can remove it
	 * when relevant.
	 */
	private ConsoleGui consoleInputField;

	/** The time the game started. Requested by the score group */
	private long startTime;

	/** The message handle that displayes the messages */
	private MessageHandle msgHandle;

	public static int GAME_WIDTH = -1;
	public static int GAME_HEIGHT = -1;

	/** The number of in-game ms(miliseconds) the game has been running */
	private long gameTime = 0L;

	private boolean godmode = false;

	public Game(int width, int height) throws Exception {
		new SplashScreenDriver();
		// So we can allow people to use Game.getInstance() and not being forced
		// to pass down a reference to this instance everywhere we only allow
		// one.
		// And there is no point having two games running at the same time.
		if (Game.getInstance() != null) {
			throw new Exception("Can only exist one Game");
		}

		GAME_WIDTH = width;
		GAME_HEIGHT = height;

		Game.instance = this;

		JFrame container = new JFrame("Bullet Hell");

		JPanel panel = (JPanel) container.getContentPane();
		panel.setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
		panel.setLayout(null);

		// setup our canvas size and put it into the content of the frame
		setBounds(0, 0, GAME_WIDTH, GAME_HEIGHT);
		panel.add(this);

		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		setIgnoreRepaint(true);

		// finally make the window visible
		container.pack();
		container.setResizable(false);
		container.setVisible(true);

		// add a listener to respond to the user closing the window. If they
		// do we'd like to exit the game
		container.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});

		// Put the game in the middle of the screen.
		container.setLocationRelativeTo(null);

		// add a key input system (defined below) to our canvas
		// so we can respond to key pressed
		addKeyListener(new KeyInputHandler());

		// request the focus so key events come to us
		requestFocus();

		// create the buffering strategy which will allow AWT
		// to manage our accelerated graphics
		createBufferStrategy(2);
		strategy = getBufferStrategy();

		// initialise the entities in our game so there's something
		// to see at startup
		startGame();
	}

	public void showMessage(String msg) {
		String[] add = msg.split("\n");

		for (String s : add)
			msgHandle.addMessage(s);
	}

	private void startGame() {
		entities.clear();
		guiitems.clear();

		startTime = System.currentTimeMillis();

		leftPressed  = false;
		rightPressed = false;
		upPressed    = false;
		downPressed  = false;
		shiftPressed = false;
		zPressed     = false;
		xPressed     = false;

		// Restart values.
		gameTime = 0L;
		shotDelay = 0L;
		nextSprite = 0L;
		rightPressedMs = 0L;
		leftPressedMs = 0L;
		nextHurtSprite = 0L;
		spawnTime = 1;
		
		
		

		// Start the music
		music = new WavPlayer(new File(path + "/src/sounds/1969.wav"));
		music.play();

		// Create the map first, add all entities and finally add the GUI.
		init();

		// Show welcome message
		showMessage("Stage 1 \n\n It's dark here, but everyone seems to be taking it easy");

	}

	public long getTimeSinceStart() {
		return System.currentTimeMillis() - startTime;
	}

	/**
	 * Returns the number of milliseconds since this round started but actually
	 * calculates with slow motion and all other things you should respect. 20
	 * ms here might be 40 real ms if the game is in 50% slow motion for
	 * example.
	 * 
	 * @return
	 */
	public long getGameTime() {
		return gameTime;
	}

	/**
	 * Returns the path to the directory this game is in this is used for the
	 * music files.
	 * 
	 * @return
	 */
	public String getPath() {
		return path;
	}

	public boolean getGodMode() {
		return godmode;
	}

	public long getScore() {
		return score.getScore();
	}

	public void addScore(long score) {
		this.score.addScore(score);
	}

	public boolean getKeyPressed(int key) {

		for (int i = 0; i < 6; i++) {
			if (key == (i + 1))
				return keys[i];
		}

		return false;

	}

	/**
	 * Gets the current visible view. Currently the game is only within the
	 * screen thus we are returning the screen sice.
	 * @return
	 */
	public Rectangle getView() {
		return new Rectangle(GAME_WIDTH, GAME_HEIGHT);
	}

	private void waitForKeyPress(String message) {
		waitForKeyPress = new WaitForKeyPress(message);
		addGui(waitForKeyPress);
	}


	private void waitForKeyPressEnd() {
		guiremove.add(waitForKeyPress);
		waitForKeyPress = null;
	}

	private void init() {
		initGui();
		initEntities();
	}

	private void initGui() {
		msgHandle = new MessageHandle();
		addGui(msgHandle);
		addGui(new LifeGui());
		addGui(new BombGui());
		addGui(new PowerGui());
		addGui(new GrazeGui());
		addGui(new ScoreGui());
		addGui(new GuiItem() {
			@Override
			public void draw(Graphics2D g) {
				g.setColor(Color.ORANGE);
				g.drawString("Time: " + (getGameTime() / 1000),
						GAME_WIDTH - 100, 50);

			}
		});
	}


	private void initEntities() {

		// Create our character and add it to the game.
		ply   = new CharPegasus(new Point(tempWidth, (Game.GAME_HEIGHT - (Game.GAME_HEIGHT / 10))));
		hbox  = new CharHitBox();
		score = new Score();
		ply.setMaximalSpeed((int) moveSpeed);

		// add entities
		addEntity(ply);
		addEntity(hbox);
		addEntity(new FullPower(tempWidth, tempHeight));
		addEntity(new BombUp(tempWidth, tempHeight));
		addEntity(new LifeUp(tempWidth, tempHeight));
		addEntity(new SmallPowerUp(tempWidth, tempHeight));
		addEntity(new BigPowerUp(tempWidth, tempHeight));
	}

	/**
	 * Check whether the game is waiting for a key to be pressed or not.
	 * @return
	 */
	public boolean isWaitingForKeyPress() {
		return waitForKeyPress != null;
	}

	/**
	 * Remove an entity from the game. The entity removed will no longer move or
	 * be drawn.
	 * 
	 * @param entity
	 *            The entity that should be removed
	 */
	public void removeEntity(Entity entity) {
		removeList.add(entity);
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public void removeGui(GuiItem item) {
		guiremove.add(item);
	}

	public void addGui(GuiItem item) {
		guiitems.add(item);
	}

	/**
	 * Notification that the player has died. This will immedetly end the game.
	 * If you just want to damage the character you use a fitting method on the
	 * character instance. game.getCharacter() to get the character.
	 */
	public void notifyDeath() {
		if (godmode)
			return;
		music = new WavPlayer(new File(path + "/src/sounds/EternalDream.wav"));
		music.play();
		waitForKeyPress("GAME OVER\n try again?");
	}

	public void notifyWin() {
		waitForKeyPress("Congratulations! You win! Score: " + score.getScore());
	}

	public void notifyEnemyKilled() {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);

			if (entity instanceof ExtendedEntity) {
				((ExtendedEntity) entity).notifyEnemyKilled(null);
			}
		}
	}

	public void notifyEnemyKilled(Entity killed) {
		removeEntity(killed);

		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);

			if (entity instanceof ExtendedEntity) {
				((ExtendedEntity) entity).notifyEnemyKilled(killed);
			}
		}
	}

	/**
	 * The main game loop. This loop is running during all game play as is
	 * responsible for the following activities:
	 * <p>
	 * - Working out the speed of the game loop to update moves - Moving the
	 * game entities - Drawing the screen contents (entities, text) - Updating
	 * game events - Checking Input
	 * <p>
	 */
	public void gameLoop() {
		// This is used later in the loop. If it was 0 the game would think we
		// had a enormiuos lag spike and thus we set this to the current time.
		long lastLoopTime = System.currentTimeMillis();

		// keep looping round til the game ends
		while (gameRunning) {

			// work out how long its been since the last update, this
			// will be used to calculate how far the entities should
			// move this loop
			long delta = (long) ((System.currentTimeMillis() - lastLoopTime) * getSpeed());
			lastLoopTime = System.currentTimeMillis();

			// Add to the game time unless the game has already ended.
			if (!isWaitingForKeyPress())
				gameTime += delta;

			// test
			if (3000 * spawnTime <= getGameTime()) {
				spawnTime++;
				boolean oneonly = false;
				for (int i = 0; i < spawnTime; i++) {
					addEntity(new Nexxus(true, 1, 1, 3, 1, 360, 300));
					if (oneonly == false) {
						if (ply.x >= GAME_WIDTH / 2) {
							addEntity(new Nexxus(false, 1, 2, 1, 1, 360, 300));
						} else {
							addEntity(new Nexxus(true, 1, 2, 2, 1, 360, 300));
						}
						oneonly = true;
					}
				}
			}

			// -test

			// brute force collisions, compare every entity against
			// every other entity. If any of them collide notify
			// both entities that the collision has occured
			Entity me = null;
			Entity him = null;
			for (int p = 0; p < entities.size(); p++) {
				for (int s = p + 1; s < entities.size(); s++) {
					me = entities.get(p);
					him = entities.get(s);

					// Sometimes the lists change while running despite my
					// effort to stop it. Especially when the game just started.
					// Therefore I added this to prevent crashes.
					if (!(me == null || him == null)) {
						if (me.collidesWith(him)) {
							me.collidedWith(him);
							him.collidedWith(me);
						}
					}
				}
			}

			// Get hold of a graphics context for the accelerated
			// surface and blank it out
			Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
			g.setColor(Color.black);
			g.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);

			// cycle round asking each entity to move itself
			if (!isWaitingForKeyPress()) {
				for (int i = 0; i < entities.size(); i++) {
					Entity entity = entities.get(i);

					// This list is also changed in another thread sometimes.
					// This is a crash protection.
					if (entity == null)
						continue;

					// Move the entity.
					entity.move(delta);
				}
			}
			/**
			 * Controls what the character does depending on which key the
			 * player presses. It also changes sprites for when the character
			 * moves horizontaly No key presses are registrated when the
			 * character have just lost a life
			 */
			getCharacter().setHorizontalMovement(0);
			getCharacter().setVerticalMovement(0);
			if ((leftPressed) && (!rightPressed)) {
				getCharacter().setHorizontalMovement(-ply.getMaximalSpeed());
				if (leftPressedMs == 0) {
					ply.sprite = SpriteStore.get().getSprite(
							"sprites/reimuL.png");
					leftPressedMs = getGameTime() + 80;
					lastSprite = ply.sprite;
				} else if (getGameTime() > leftPressedMs) {
					leftPressedMs = getGameTime() + 80;
					ply.sprite = lastSprite;
					ply.sprite = SpriteStore.get().getSprite(
							"sprites/reimuL.png");
					lastSprite = ply.sprite;

					if (getGameTime() < ply.getInvincibility()
							&& nextHurtSprite < getGameTime()) {
						ply.sprite = SpriteStore.get().getSprite(
								"sprites/reimuempty.png");
						nextHurtSprite = getGameTime() + 160;
					}
				}
				
				if (getGameTime() < ply.getInvincibility()
						&& nextHurtSprite < getGameTime()) {
					ply.sprite = SpriteStore.get().getSprite(
							"sprites/reimuempty.png");
					nextHurtSprite = Game.getInstance().getGameTime() + 160;
				}
			} else if ((rightPressed) && (!leftPressed)) {
				getCharacter().setHorizontalMovement(ply.getMaximalSpeed());
				if (rightPressedMs == 0) {
					ply.sprite = SpriteStore.get().getSprite(
							"sprites/reimuR.png");
					rightPressedMs = getGameTime() + 80;
					lastSprite = ply.sprite;
				} else if (getGameTime() > rightPressedMs) {
					rightPressedMs = getGameTime() + 80;
					ply.sprite = lastSprite;

					ply.sprite = SpriteStore.get().getSprite(
							"sprites/reimuR.png");

					lastSprite = ply.sprite;

					if (getGameTime() < ply.getInvincibility()
							&& nextHurtSprite < getGameTime()) {
						ply.sprite = SpriteStore.get().getSprite(
								"sprites/reimuempty.png");
						nextHurtSprite = getGameTime() + 160;
					}
				}
			}
			if ((leftPressed) && (rightPressed)) {
				getCharacter().setHorizontalMovement(0);
				rightPressedMs = 0;
				leftPressedMs = 0;
				if (getGameTime() > nextSprite) {
					nextSprite = getGameTime() + 80;
					ply.sprite = lastSprite;
					ply.sprite = SpriteStore.get().getSprite(
							"sprites/reimuR.png");
					lastSprite = ply.sprite;

					if (getGameTime() < ply.getInvincibility()
							&& nextHurtSprite < getGameTime()) {
						ply.sprite = SpriteStore.get().getSprite(
								"sprites/reimuempty.png");
						nextHurtSprite = getGameTime() + 160;
					}
				}
			}
			if ((upPressed) && (!downPressed)) {
				ply.setVerticalMovement(-ply.getMaximalSpeed());
			} else if ((downPressed) && (!upPressed)) {
				ply.setVerticalMovement(ply.getMaximalSpeed());
			}
			if (shiftPressed) {
				ply.setVerticalMovement(ply.getVerticalMovement() / 2);
				ply.setHorizontalMovement(ply.getHorizontalMovement() / 2);
			}
			if (zPressed) {
				if (getGameTime() > shotDelay) {
					shotDelay = getGameTime() + 80;
					addEntity(new PegasusBullet(ply.getX()
							+ (ply.getSprite().getWidth() / 2), ply.getY()
							- (ply.getSprite().getWidth() / 4), 0, -800));
					if (ply.getPower() >= 100) {
						addEntity(new PegasusBullet(ply.getX()
								+ (ply.getSprite().getWidth() / 4), ply.getY(),
								0, -800));
						addEntity(new PegasusBullet(ply.getX()
								+ ply.getSprite().getWidth()
								- (ply.getSprite().getWidth() / 4), ply.getY(),
								0, -800));
					}
					if (ply.getPower() >= 200) {
						if (!shiftPressed) {
							addEntity(new PegasusBullet(ply.getX(), ply.getY()
									+ (ply.getSprite().getWidth() / 4), -2,
									-800));
							addEntity(new PegasusBullet(ply.getX()
									+ ply.getSprite().getWidth(), ply.getY()
									+ (ply.getSprite().getWidth() / 4), 2, -800));
						} else {
							addEntity(new PegasusBullet(ply.getX(), ply.getY()
									+ (ply.getSprite().getWidth() / 4), 2, -800));
							addEntity(new PegasusBullet(ply.getX()
									+ ply.getSprite().getWidth(), ply.getY()
									+ (ply.getSprite().getWidth() / 4), -2,
									-800));
						}
					}
					if (ply.getPower() >= 300) {
						if (!shiftPressed) {
							addEntity(new PegasusBullet(
									ply.getX()
											- (ply.getSprite().getWidth() / 3),
									ply.getY()
											+ ((ply.getSprite().getWidth() / 4) * 2),
									-4, -800));
							addEntity(new PegasusBullet(
									ply.getX() + ply.getSprite().getWidth()
											+ (ply.getSprite().getWidth() / 3),
									ply.getY()
											+ ((ply.getSprite().getWidth() / 4) * 2),
									4, -800));
						} else {
							addEntity(new PegasusBullet(
									ply.getX()
											- (ply.getSprite().getWidth() / 3),
									ply.getY()
											+ ((ply.getSprite().getWidth() / 4) * 2),
									4, -800));
							addEntity(new PegasusBullet(
									ply.getX() + ply.getSprite().getWidth()
											+ (ply.getSprite().getWidth() / 3),
									ply.getY()
											+ ((ply.getSprite().getWidth() / 4) * 2),
									-4, -800));
						}
					}
					if (ply.getPower() >= 400) {
						if (!shiftPressed) {
							addEntity(new PegasusBullet(
									ply.getX()
											- ((ply.getSprite().getWidth() / 3) * 2),
									ply.getY()
											+ ((ply.getSprite().getWidth() / 4) * 3),
									-6, -800));
							addEntity(new PegasusBullet(
									ply.getX()
											+ ply.getSprite().getWidth()
											+ ((ply.getSprite().getWidth() / 3) * 2),
									ply.getY()
											+ ((ply.getSprite().getWidth() / 4) * 3),
									6, -800));
						} else {
							addEntity(new PegasusBullet(
									ply.getX()
											- ((ply.getSprite().getWidth() / 3) * 2),
									ply.getY()
											+ ((ply.getSprite().getWidth() / 4) * 3),
									6, -800));
							addEntity(new PegasusBullet(
									ply.getX()
											+ ply.getSprite().getWidth()
											+ ((ply.getSprite().getWidth() / 3) * 2),
									ply.getY()
											+ ((ply.getSprite().getWidth() / 4) * 3),
									-6, -800));
						}
					}
					new AePlayWave(path + "/src/sounds/se_plst00.wav").start();
				}
			}
			if (xPressed) {

			}

			// cycle round drawing all the entities we have in the game
			for (int i = 0; i < entities.size(); i++) {
				Entity entity = entities.get(i);

				if (entity == null)
					continue;

				entity.draw(g);
			}

			for (int i = 0; i < guiitems.size(); i++) {
				// GuiItems are always drawn on the screen directly, the
				// entities might move with the camera in the future.
				guiitems.get(i).draw(g);
			}

			// remove any entity that has been marked for clear up
			entities.removeAll(removeList);
			removeList.clear();

			guiitems.removeAll(guiremove);
			guiremove.clear();

			// finally, we've completed drawing so clear up the graphics
			// and flip the buffer over
			g.dispose();
			strategy.show();

			// finally pause for a bit. Note: this should run us at about
			// 100 fps but on windows this might vary each loop due to
			// a bad implementation of timer
			try {
				Thread.sleep(sleeptime);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * @return the speed
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * Sets the game speed. 1.0 is default. 0.5 is 50%. 2.0 is 200%
	 * 
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * @return the ply
	 */
	public CharPegasus getCharacter() {
		return ply;
	}

	/**
	 * @return the hbox
	 */
	public CharHitBox getHitBox() {
		return hbox;
	}

	private class KeyInputHandler extends KeyAdapter {

		private int pressCount = 1;

		public void keyPressed(KeyEvent e) {

			if (e.getKeyCode() == KeyEvent.VK_F12
					|| e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (consoleInputField != null
						&& consoleInputField.getCmd() != null) {

					handleCommand(consoleInputField.getCmd());
					removeGui(consoleInputField);
					consoleInputField = null;
				} else if (e.getKeyCode() != KeyEvent.VK_ENTER) {
					consoleInputField = new ConsoleGui("");
					addGui(consoleInputField);
				}
			}

			// if we're waiting for an "any key" typed then we don't
			// want to do anything with just a "press"
			if (isWaitingForKeyPress() || consoleInputField != null) {
				return;
			}

			// Movement, but only if the character didn't just lose a life
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (getGameTime() > ply.getInvincibility() - 2000) {
					leftPressed = true;
				} else {
					leftPressed = false;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (getGameTime() > ply.getInvincibility() - 2000) {
					rightPressed = true;
				} else {
					rightPressed = false;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				if (getGameTime() > ply.getInvincibility() - 2000) {
					upPressed = true;
				} else {
					upPressed = false;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				if (getGameTime() > ply.getInvincibility() - 2000) {
					downPressed = true;
				} else {
					downPressed = false;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				if (getGameTime() > ply.getInvincibility() - 2000) {
					shiftPressed = true;
				} else {
					shiftPressed = false;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_Z) {
				if (getGameTime() > ply.getInvincibility() - 2000) {
					zPressed = true;
				} else {
					zPressed = false;
				}
			}
			if (e.getKeyCode() == KeyEvent.VK_X) {
				if (getGameTime() > ply.getInvincibility() - 2000) {
					xPressed = true;
				} else {
					xPressed = false;
				}
			}
		}

		public void keyReleased(KeyEvent e) {
			// if we're waiting for an "any key" typed then we don't
			// want to do anything with just a "released"
			if (isWaitingForKeyPress() || consoleInputField != null) {
				return;
			}

			// Movement.
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				leftPressed = false;
				leftPressedMs = 0;
				nextSprite = 0;
			}
			if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				rightPressed = false;
				rightPressedMs = 0;
				nextSprite = 0;
			}
			if (e.getKeyCode() == KeyEvent.VK_UP) {
				upPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				downPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
				shiftPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_Z) {
				zPressed = false;
			}
			if (e.getKeyCode() == KeyEvent.VK_X) {
				xPressed = false;
			}
		}

		public void keyTyped(KeyEvent e) {
			// If the console is open this isn't a "any key", it is input to the
			// console.
			if (consoleInputField != null) {
				// Allow backspace.
				if (e.getKeyChar() == (char) 8
						&& consoleInputField.getCmd().length() > 0) {
					String nev = consoleInputField.getCmd();
					nev = nev.substring(0, nev.length() - 1);
					consoleInputField.setCmd(nev);
				} else
					consoleInputField.appendCmd("" + e.getKeyChar());
			}

			else if (isWaitingForKeyPress()) {
				if (pressCount == 1) {
					waitForKeyPressEnd();
					startGame();
					pressCount = 0;
				} else {
					pressCount++;
				}
			}

			// escape
			if (e.getKeyChar() == 27) {
				if (consoleInputField != null) {
					removeGui(consoleInputField);
					consoleInputField = null;
				} else
					System.exit(0);
			}
		}

		private void handleCommand(String instr) {
			if (instr.equals(""))
				return;

			// Remove any new lines and split it into words.
			String[] cmd = instr.replaceAll("\n", "").split(" ");

			if (cmd[0].equalsIgnoreCase("reset")) {
				// The wait for key press resets the game after you click
				// something.
				if (!isWaitingForKeyPress())
					waitForKeyPress("Game reset by console");
				else
					showMessage("Game is already reset!");
			} else if (cmd[0].equalsIgnoreCase("lag")) {
				try {
					sleeptime = Integer.parseInt(cmd[1]);
				} catch (Exception ex) {
					showMessage("Formating error in command.");
				}
			} else if (cmd[0].equalsIgnoreCase("speed")) {
				try {
					setSpeed(Float.parseFloat(cmd[1]));
				} catch (Exception ex) {
					showMessage("Formating error in command.");
				}
			} else if (cmd[0].equalsIgnoreCase("setlives")) {
				try {
					ply.setLives(Integer.parseInt(cmd[1]));
				} catch (Exception ex) {
					showMessage("Formating error in command.");
				}
			} else if (cmd[0].equalsIgnoreCase("setbombs")) {
				try {
					ply.setBombs(Integer.parseInt(cmd[1]));
				} catch (Exception ex) {
					showMessage("Formating error in command.");
				}
			} else if (cmd[0].equalsIgnoreCase("setpower")) {
				try {
					ply.setPower(Integer.parseInt(cmd[1]));
				} catch (Exception ex) {
					showMessage("Formatting error in command.");
				}
			} else if (cmd[0].equalsIgnoreCase("loselife")) {
				try {
					ply.loseLife();
				} catch (Exception ex) {
					showMessage("Formatting error in command.");
				}
			} else if (cmd[0].equalsIgnoreCase("god")) {
				godmode = !godmode;
				showMessage("Godmode is now: " + godmode);
			} else if (cmd[0].equalsIgnoreCase("help")
					|| cmd[0].equalsIgnoreCase("list")) {
				showMessage("reset - Resets the game\n"
						+ "lag <int> - Sets the sleep time. Default 10.\n"
						+ "speed <float> - Sets the game speed. 1.0 is default."
						+ "help or list - Shows this help.\n"
						+ "setlives <int> - Sets your life\n"
						+ "setbombs <int> - Sets your bombs\n"
						+ "setpower <int> - Sets your power\n"
						+ "god - Toggles godmode");
			} else {
				showMessage("Unknown command: " + cmd[0]);
			}
		}
	}

	public static void main(String argv[]) {

		try {
			Game g = new Game(WIDTH, HEIGHT);
			g.gameLoop();
		} catch (Exception ex) {
			System.err.println("*** THE CRASH ***");
			ex.printStackTrace();
		}
	}

}