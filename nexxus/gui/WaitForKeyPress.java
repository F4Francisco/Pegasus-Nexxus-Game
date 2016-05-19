/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import game.Game;
import game.GuiItem;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author patrik
 */
public class WaitForKeyPress extends GuiItem
{
	private final String msg;

	private final static String PRESS_ANY_KEY = "Press any key";

	public WaitForKeyPress(String msg)
	{
		if (msg == null)
			throw new IllegalArgumentException("Message is null!");
		this.msg = msg;
	}

	@Override
	public void draw(Graphics2D g)
	{
		g.setColor(Color.BLACK);
		g.drawString(msg,(Game.GAME_WIDTH-g.getFontMetrics().stringWidth(msg))/2 + 1,Game.GAME_HEIGHT / 2 - 25 + 1);
		g.drawString(PRESS_ANY_KEY,(Game.GAME_WIDTH-g.getFontMetrics().stringWidth(PRESS_ANY_KEY))/2 + 1,Game.GAME_HEIGHT / 2 + 25 + 1);
		g.setColor(Color.WHITE);
		g.drawString(msg,(Game.GAME_WIDTH-g.getFontMetrics().stringWidth(msg))/2,Game.GAME_HEIGHT / 2 - 25);
		g.drawString(PRESS_ANY_KEY,(Game.GAME_WIDTH-g.getFontMetrics().stringWidth(PRESS_ANY_KEY))/2,Game.GAME_HEIGHT / 2 + 25);
	}
}
