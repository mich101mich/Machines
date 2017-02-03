package com.M101M.Industria.UI;

import com.M101M.Industria.Utils.*;

public class Button extends Text
{
	private OnClickListener listener;
	public Button(Vec2 pos, float width, String text, int textColor, int background, OnClickListener l)
	{
		super(pos, width, text, textColor, background);
		listener = l;
	}
	public Button(Vec2 pos, float width, String text, int textColor, int background)
	{
		this(pos, width, text, textColor, background, null);
	}
	public Button(Vec2 pos, float width, String text)
	{
		this(pos, width, text, 0x0, 0xaaaaaa);
	}
	public Button(Vec2 pos, float width, String text, OnClickListener l)
	{
		this(pos, width, text, 0x0, 0xaaaaaa, l);
	}
	public void setOnClickListener(OnClickListener l)
	{ listener = l; }
	@Override
	public void update()
	{
		super.update();
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
			if (e.type() == e.UP && listener != null && bounds().contains(e.x(), e.y()))
				listener.onClick(this);
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
}
