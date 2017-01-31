package com.M101M.Industria.UI;

import android.graphics.*;
import com.M101M.Industria.Utils.*;

public abstract class UIElement
{
	public Vec2 pos, size;
	public UIElement(Vec2 pos, Vec2 size)
	{
		this.pos = pos;
		this.size = size;
	}
	public UIElement(Vec2 pos, float width, float height)
	{ this(pos, new Vec2(width, height)); }
	public UIElement(float x, float y, float width, float height)
	{ this(new Vec2(x,y), width, height); }
	public UIElement(Vec2 pos)
	{ this(pos, 0, 0); }
	public UIElement(float x, float y)
	{ this(new Vec2(x, y)); }
	public UIElement()
	{ this(0, 0); }
	
	public RectF bounds()
	{
		return new RectF(pos.x, pos.y, pos.x + size.x, pos.y + size.y);
	}
	
	public abstract void draw();
	public abstract boolean handleTouch(TouchEvent e);
}
