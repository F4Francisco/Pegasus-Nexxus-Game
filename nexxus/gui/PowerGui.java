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
public class PowerGui extends GuiItem
{
	@Override
	public void draw(Graphics2D g)
	{
		g.setColor(Color.WHITE);

		Font oldfont = g.getFont();
		g.setFont(oldfont.deriveFont((float) oldfont.getSize()));

		char[] chr;
		if (Game.getInstance().getCharacter().getPower() < 400)
		{
                        String first = Integer.toString(Game.getInstance().getCharacter().getPower() / 100);
                        String last = Integer.toString(Game.getInstance().getCharacter().getPower() - ((Game.getInstance().getCharacter().getPower() / 10) * 10));
                        String second = Integer.toString(Game.getInstance().getCharacter().getPower() - (Integer.parseInt(first) * 100) - Integer.parseInt(last));
			chr = new char[] {'P', 'o', 'w', 'e', 'r', ':', ' ', new Character(first.charAt(0)), '.', new Character(second.charAt(0)), new Character(last.charAt(0))};
		}
		else
		{
			chr = new char[] {'P', 'o', 'w', 'e', 'r', ':', ' ', 'M', 'A', 'X'};
		}

		g.drawString(new String(chr), Game.GAME_WIDTH - Game.GAME_WIDTH + (g.getFontMetrics().getHeight()), Game.GAME_HEIGHT - (g.getFontMetrics().getHeight() * 2));

		g.setFont(oldfont);
	}

}
