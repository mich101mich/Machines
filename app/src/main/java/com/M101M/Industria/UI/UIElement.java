package com.M101M.Industria.UI;

import android.graphics.*;
import com.M101M.Utils.*;

public abstract class UIElement
{
	public Vec2 pos, size;
	public boolean visible = true;
	private Animation animation;
	public UIElement parent = null;
	public UIElement(Vec2 pos, Vec2 size)
	{
		this.pos = pos;
		this.size = size;
	}
	
	public RectF bounds()
	{
		return new RectF(pos.x, pos.y, pos.x + size.x, pos.y + size.y);
	}
	public void setAnimation(Animation anim)
	{
		if (animation != null)
			animation.stop(this);
		animation = anim;
		if (animation != null)
			anim.start(this);
	}
	public void update()
	{
		if (animation != null)
		{
			animation.tick++;
			animation.step(this);
			if (animation.duration > 0 && animation.tick >= animation.duration)
			{
				animation.stop(this);
				animation = null;
			}
		}
	}
	
	public abstract void draw();
	public abstract boolean handleTouch(TouchEvent e);
}
