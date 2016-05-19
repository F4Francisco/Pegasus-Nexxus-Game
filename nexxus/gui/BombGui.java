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
public class BombGui extends GuiItem
{
	@Override
	public void draw(Graphics2D g)
	{
		g.setColor(Color.GREEN);

		Font oldfont = g.getFont();
		g.setFont(oldfont.deriveFont((float) oldfont.getSize()));

		char[] chr;
		if (Game.getInstance().getCharacter().getBombs() >= 0)
		{
			chr = new char[Game.getInstance().getCharacter().getBombs() + 7];

                        chr[0] = 'B';
                        chr[1] = 'o';
                        chr[2] = 'm';
                        chr[3] = 'b';
                        chr[4] = 's';
                        chr[5] = ':';
                        chr[6] = ' ';
			char symbol = '�';

			for (int i = 7; i < chr.length; i++)
			{
				chr[i] = symbol;
			}
		}
		else
		{
			chr = new char[] {'N', 'O', 'B', 'O', 'M', 'B', 'S'};
		}

		g.drawString(new String(chr), Game.GAME_WIDTH - Game.GAME_WIDTH + (g.getFontMetrics().getHeight()), Game.GAME_HEIGHT - (g.getFontMetrics().getHeight() * 3));

		g.setFont(oldfont);
	}

}
