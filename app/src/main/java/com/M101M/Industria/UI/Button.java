package com.M101M.Industria.UI;

import com.M101M.Industria.Utils.*;

public class Button extends Rectangle
{
	public UIElement content = null;
	private OnClickListener listener = null;
	public Button(Vec2 pos, Vec2 size, UIElement content, int background, OnClickListener l)
	{
		super(pos, size, background);
		this.content = content;
		listener = l;
	}
	public void setOnClickListener(OnClickListener l)
	{ listener = l; }

	@Override
	public void draw()
	{
		super.draw();
		if (content != null)
		{
			Vec2 p = content.pos;
			content.pos = Vec2.add(p, pos);
			content.draw();
			content.pos = p;
		}
	}
	
	@Override
	public void update()
	{
		super.update();
		if (content != null)
			content.update();
		if (touched == 1)
			touched = 2;
		else if (touched == 2)
		{
			setAnimation(new Animation(7){
					float[] startColor;
					public void start(UIElement obj)
					{ startColor = Utils.hexToArray(((Button)obj).color); }
					public void step(UIElement obj)
					{
						float factor = (1 + 0.25f * tick / duration);
						float[] col = { startColor[0] * factor, startColor[1] * factor, startColor[2] * factor, startColor[3] };
						((Button)obj).color = Utils.ArrayToHex(col);
					}
					public void stop(UIElement obj)
					{
						tick = duration;
						step(obj);
					}
				});
			touched = 0;
			touchID = -1;
		}
	}
	int touched = 0, touchID = -1;
	@Override
	public boolean handleTouch(TouchEvent e)
	{
		if (e.type() == e.DOWN && touched == 0 && bounds().contains(e.x(), e.y()))
		{
			touchID = e.id();
			setAnimation(new Animation(7){
					float[] startColor;
					public void start(UIElement obj)
					{ startColor = Utils.hexToArray(((Button)obj).color); }
					public void step(UIElement obj)
					{
						float factor = (1 - 0.2f * tick / duration);
						float[] col = { startColor[0] * factor, startColor[1] * factor, startColor[2] * factor, startColor[3] };
						((Button)obj).color = Utils.ArrayToHex(col);
					}
					public void stop(UIElement obj)
					{
						tick = duration;
						step(obj);
					}
				});
			touched = 1;
		}
		if (e.id() == touchID && touched != 0)
		{
			if (e.type() == e.UP && bounds().contains(e.x(), e.y()))
			{
				if (listener != null)
					listener.onClick(this);
			}
			else
				touched = 1;
			return e.handled = true;
		}
		return false;
	}
	public static interface OnClickListener
	{
		public void onClick(Button b);
	}
	
	public Button(Vec2 pos, Vec2 size, UIElement content, int background)
	{ this(pos, size, content, background, null); }
	public Button(Vec2 pos, Vec2 size, UIElement content, OnClickListener l)
	{ this(pos, size, content, 0xaaaaaa, l); }
	public Button(Vec2 pos, Vec2 size, int background, OnClickListener l)
	{ this(pos, size, null, background, l); }
	public Button(Vec2 pos, UIElement content, int background, OnClickListener l)
	{ this(pos, content.size, content, background, l); }
	
	public Button(Vec2 pos, Vec2 size, UIElement content)
	{ this(pos, size, content, null); }
	
	public Button(Vec2 pos, Vec2 size, String text, int textColor, int background, OnClickListener l)
	{ this(pos, size, new Text(pos, size.x, text, textColor), background, l); }
	public Button(Vec2 pos, float width, String text, int textColor)
	{ this(pos, new Text(pos, width, text).size, new Text(pos, width, text, textColor)); }
	
}
