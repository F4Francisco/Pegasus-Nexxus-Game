
package gui;

import game.Game;
import game.GuiItem;

import java.awt.Color;
import java.awt.Graphics2D;


public class MessageHandle extends GuiItem
{
	Message msgs[] = new Message[10];

	public MessageHandle()
	{
		for (int i = 0; i < msgs.length; i++)
		{
			msgs[i] = new Message();
		}
	}

	private void remove()
	{
		for (int i = 0; i < msgs.length - 1; i++)
		{
			msgs[i] = msgs[i + 1];
		}
	}

	/**
	 * Adds a message to the bottom of the area.
	 * @param str
	 */
	public void addMessage(String str)
	{
		remove();
		msgs[msgs.length - 1] = new Message(str, 6500);
	}

	@Override
	public void draw(Graphics2D g)
	{
		for (int i = 0; i < msgs.length; i++)
		{
			int alpha = (int) (255.0 * msgs[i].getAlpha());
			if (alpha <= 255 && alpha > 0)
			{
				g.setColor(new Color(0, 0, 0, alpha));
				g.drawString(msgs[i].str,(Game.GAME_WIDTH-g.getFontMetrics().stringWidth(msgs[i].str))/2 - 1,(Game.GAME_HEIGHT / 2 - 25) - (Game.GAME_HEIGHT / 2 - 25 * i) - 1);
				g.setColor(new Color(255, 255, 255, alpha));
				g.drawString(msgs[i].str,(Game.GAME_WIDTH-g.getFontMetrics().stringWidth(msgs[i].str))/2,(Game.GAME_HEIGHT / 2 - 25) - (Game.GAME_HEIGHT / 2 - 25 * i));
			}
		}
	}

}

class Message
{
	String str;
	long expire;

	public Message() {
		this("", 0);
	}

	public Message(String str, long expire) {
		this.str = str;
		this.expire = System.currentTimeMillis() + expire;
	}

	

	public float getAlpha()
	{
		if (System.currentTimeMillis() > expire)
			return -1.0f;
		else if (System.currentTimeMillis() + 1000 <= expire)
			return 1.0f;
		else
			return (float) ((double) (expire - System.currentTimeMillis()) / 1000.0);
	}
}