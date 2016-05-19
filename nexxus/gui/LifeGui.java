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
public class LifeGui extends GuiItem
{
	@Override
	public void draw(Graphics2D g)
	{
		g.setColor(Color.RED);

		Font oldfont = g.getFont();
		g.setFont(oldfont.deriveFont((float) oldfont.getSize()));

		char[] chr;
		if (Game.getInstance().getCharacter().getLives() >= 0)
		{
			chr = new char[Game.getInstance().getCharacter().getLives() + 7];

                        chr[0] = 'L';
                        chr[1] = 'i';
                        chr[2] = 'v';
                        chr[3] = 'e';
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
			chr = new char[] {'D', 'E', 'A', 'D', ' ', 'P', 'A', 'R', 'R', 'O', 'T'};
		}

		g.drawString(new String(chr), Game.GAME_WIDTH - Game.GAME_WIDTH + (g.getFontMetrics().getHeight()), Game.GAME_HEIGHT - (g.getFontMetrics().getHeight() * 4));

		g.setFont(oldfont);
	}

}
