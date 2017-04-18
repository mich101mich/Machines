package com.M101M.Industria.UI;

import com.M101M.Utils.*;
import java.util.*;

public class Container extends Rectangle
{
	private ArrayList<UIElement> children;
	private Vec2 anchor;
	private final Align align;
	private float scrollOffset, totalSize;
	public float maxOffset;
	public Container(Vec2 pos, Vec2 size, Align align)
	{
		super(pos, size, 0xaaaaaa);
		this.align = align;
		children = new ArrayList<UIElement>(50);
		anchor = new Vec2(pos);

		if (isHorizontal())
		{
			maxOffset = size.x;
			this.size.x = 0;
		}
		else
		{
			maxOffset = size.y;
			this.size.y = 0;
		}

		if (align == Align.RIGHT)
			anchor.x += maxOffset;
		if (align == Align.BOTTOM)
			anchor.y += maxOffset;
	}
	public void add(UIElement e)
	{
		if (isHorizontal())
		{
			e.pos = new Vec2(totalSize, 0);
			totalSize += e.size.x + 0.02f;
			size.x = Math.min(totalSize, maxOffset);
			if (align == Align.RIGHT)
				pos.x = anchor.x - size.x;
		}
		else
		{
			e.pos = new Vec2(0, totalSize);
			totalSize += e.size.y + 0.02f;
			size.y = Math.min(totalSize, maxOffset);
			if (align == Align.BOTTOM)
				pos.y = anchor.y - size.y;
		}
		children.add(e);
	}
	private Vec2 getChildPos(UIElement child)
	{
		Vec2 p = Vec2.plus(anchor, (isHorizontal() ? scrollOffset : 0), (isHorizontal() ? 0 : scrollOffset));
		switch (align)
		{
		case RIGHT:
			return Vec2.minus(p, child.pos.x + child.size.x, 0);
		case BOTTOM:
			return Vec2.minus(p, 0, child.pos.y + child.size.y);
		default:
			return Vec2.plus(p, child.pos);
		}
	}
	@Override
	public void draw()
	{
		if (!visible)
			return;
		super.draw();
		for (UIElement e : children)
		{
			Vec2 p = e.pos;
			e.pos = getChildPos(e);
			if (e.pos.x >= pos.x && e.pos.y >= pos.y && e.pos.x + e.size.x <= pos.x + size.x && e.pos.y + e.size.y <= pos.y + size.y)
				e.draw();
			e.pos = p;
		}
	}
	
	private float scrollDistance = 0;
	@Override
	public void update()
	{
		super.update();
		if (Math.abs(scrollDistance) > 0)
		{
			scrollOffset += scrollDistance / 10.0f;
			scrollDistance *= 0.9f;
			scrollOffset = Math.max(0, Math.min(scrollOffset, totalSize - (isHorizontal() ? size.x : size.y)));
		}
		for (UIElement e : children)
			e.update();
	}
	
	private int touchID = -1;
	private float touchPos, touchDistance, touchDelta;
	@Override
	public boolean handleTouch(TouchEvent t)
	{
		if (!visible)
			return false;
			
		if (t.id() == touchID)
		{
			if (t.type() == t.UP)
			{
				touchID = -1;
				scrollDistance += 20*touchDelta;
				if (Math.abs(touchDistance) > 0.1f)
					return t.handled = true;
			}
			float tp = (isHorizontal() ? t.x() : t.y());
			touchDelta = tp - touchPos;
			scrollDistance += touchDelta;
			touchDistance += touchDelta;
			touchPos = tp;
		}
		
		if (t.type() == t.DOWN && bounds().contains(t.x(), t.y()))
		{
			touchID = t.id();
			touchPos = (isHorizontal() ? t.x() : t.y());
			touchDistance = 0;
		}
			
		for (UIElement e : children)
		{
			Vec2 p = e.pos;
			e.pos = getChildPos(e);
			if (e.pos.x >= pos.x && e.pos.y >= pos.y && e.pos.x + e.size.x <= pos.x + size.x && e.pos.y + e.size.y <= pos.y + size.y
				&& e.handleTouch(t))
			{
				e.pos = p;
				return true;
			}
			e.pos = p;
		}
		return t.handled = t.id() == touchID;
	}
	private boolean isHorizontal()
	{ return align == Align.LEFT || align == Align.RIGHT; }
	public enum Align
	{
		LEFT, RIGHT, TOP, BOTTOM;
	}
}
