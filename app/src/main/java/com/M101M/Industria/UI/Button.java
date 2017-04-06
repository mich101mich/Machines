package com.M101M.Industria.UI;

import com.M101M.Industria.*;
import com.M101M.Industria.UI.*;
import com.M101M.Industria.Utils.*;

public class Button extends Rectangle
{
	public UIElement content = null;
	private OnClickListener listener = null;
	public String toolTip = null;
	public Button(Vec2 pos, Vec2 size, UIElement content, String toolTip, int background, OnClickListener l)
	{
		super(pos, size, background);
		this.content = content;
		this.toolTip = toolTip;
		listener = l;
	}
	public Button(Vec2 pos, Vec2 size, UIElement content, String toolTip, int background)
	{ this(pos, size, content, toolTip, background, null); }
	public Button(Vec2 pos, Vec2 size, UIElement content, String toolTip)
	{ this(pos, size, content, toolTip, DEFAULT_COLOR, null); }
	public Button(Vec2 pos, Vec2 size, UIElement content)
	{ this(pos, size, content, null, DEFAULT_COLOR, null); }
	public Button(Vec2 pos, Vec2 size)
	{ this(pos, size, null, null, DEFAULT_COLOR, null); }
	public void setOnClickListener(OnClickListener l)
	{ listener = l; }

	@Override
	public void draw()
	{
		super.draw();
		if (content != null)
		{
			Vec2 p = content.pos;
			content.pos = Vec2.plus(p, pos);
			content.draw();
			content.pos = p;
		}
		if (toolTip != null && isPressed())
		{
			float width = 0.2f / new Text(new Vec2(), 1, toolTip).size.y;
			Game.ui.popup = new Text(
				new Vec2(pos.x < 0 ? 0 : -width, size.y).add(pos),
				width,
				toolTip,
				0x0,
				DEFAULT_COLOR) {
					public void update() {
						if (!isPressed())
							Game.ui.popupDuration = 1;
					}
				};
			Game.ui.popupDuration = -1;
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
	private int touched = 0, touchID = -1;
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
	public boolean isPressed()
	{
		return touchID != -1;
	}
	
	
	public static interface OnClickListener
	{
		public void onClick(Button b);
	}
	
	public static final int DEFAULT_COLOR = 0xaaaaaa;
}
