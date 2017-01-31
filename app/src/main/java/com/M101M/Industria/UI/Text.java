package com.M101M.Industria.UI;
import com.M101M.Industria.Utils.*;

public class Text extends UIElement
{
	String text;
	int background, color;
	public Text(Vec2 pos, String text, int textColor, int backColor)
	{
		super(pos, (text.length() + 1) * TEXT_WIDTH, TEXT_HEIGHT);
		color = textColor;
		background = backColor;
	}
	public Text(Vec2 pos, String text, int textColor)
	{
		this(pos, text, textColor, 0xff000000);
	}
	public Text(Vec2 pos, String text)
	{
		this(pos, text, 0x0);
	}
	@Override
	public void draw()
	{
		
		for (int i=0; i < text.length(); i++)
		{
			
		}
	}

	@Override
	public boolean handleTouch(TouchEvent e)
	{
		return false;
	}
	
	public static final int TEXT_HEIGHT = 16, TEXT_WIDTH = 6;
}
