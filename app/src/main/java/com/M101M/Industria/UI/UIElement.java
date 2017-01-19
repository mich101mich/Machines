package com.M101M.Industria.UI;

import com.M101M.Industria.Utils.*;

public abstract class UIElement
{
	UI ui;
	public Vec2 pos;
	public UIElement(Vec2 position)
	{
		pos = position;
	}
	public abstract void draw();
}
