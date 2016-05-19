package gui;

import game.Game;
import game.GuiItem;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 *
 * @author patrik & Daniel
 */
public class GrazeGui extends GuiItem
{
	@Override
	public void draw(Graphics2D g)
	{
		g.setColor(Color.WHITE);

		Font oldfont = g.getFont();
		g.setFont(oldfont.deriveFont((float) oldfont.getSize()));

                g.drawString("Graze: " + Integer.toString(Game.getInstance().getCharacter().getGraze()), Game.GAME_WIDTH - Game.GAME_WIDTH + (g.getFontMetrics().getHeight()), Game.GAME_HEIGHT - g.getFontMetrics().getHeight());
		g.setFont(oldfont);
	}

}