package com.M101M.Industria.UI;

import android.graphics.*;
import com.M101M.Industria.Utils.*;

public abstract class UIElement
{
	UI ui;
	public Vec2 pos;
	public RectF bounds;
	public UIElement(Vec2 position)
	{
		pos = position;
	}
	public abstract void draw();
	public abstract boolean handleTouch(TouchEvent e);
}
