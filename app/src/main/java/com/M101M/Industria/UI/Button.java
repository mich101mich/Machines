package com.M101M.Industria.UI;

import com.M101M.Industria.Utils.*;

public class Button extends Text
{
	public Button(Vec2 pos, String text)
	{
		super(pos, text);
	}

	@Override
	public void draw()
	{
		
	}
	@Override
	public boolean handleTouch(TouchEvent e)
	{
		return false;
	}
}
