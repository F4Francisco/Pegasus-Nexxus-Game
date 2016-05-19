package gui;

import game.Game;
import game.GuiItem;
import highscore.Score;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 *
 * @author patrik & Daniel
 */
public class ScoreGui extends GuiItem
{
	@Override
	public void draw(Graphics2D g)
	{
		g.setColor(Color.WHITE);

		Font oldfont = g.getFont();
		g.setFont(oldfont.deriveFont((float) oldfont.getSize()));

                g.drawString("Score: " + Long.toString(Game.getInstance().getScore()), Game.GAME_WIDTH - Game.GAME_WIDTH + (g.getFontMetrics().getHeight()), g.getFontMetrics().getHeight());
		g.setFont(oldfont);
	}

}
