/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import game.GuiItem;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author patrik
 */
public class ConsoleGui extends GuiItem
{
	private String cmd;

	public ConsoleGui(String cmd)
	{
		this.cmd = cmd;
	}

	@Override
	public void draw(Graphics2D g)
	{
			String str = getCmd();
			if (System.currentTimeMillis() % 800 > 400)
				str += "|";

			g.setColor(Color.BLACK);
			g.drawString(str, 20, 220);
			g.setColor(Color.WHITE);
			g.drawString(str, 19, 219);
	}

	/**
	 * @return the cmd
	 */
	public String getCmd() {
		return cmd;
	}

	/**
	 * @param cmd the cmd to set
	 */
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	/**
	 * Appends the cmd. Same as <code>setCmd(getCmd() + add);</code>
	 * @param add
	 */
	public void appendCmd(String add)
	{
		this.cmd += add;
	}

}
