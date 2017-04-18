package com.M101M.Utils;

import android.view.MotionEvent;

public class TouchEvent
{
	private int type;
	private final int id;
	private Vec2 pos;
	public boolean handled;
	TouchEvent(MotionEvent e, int index, Vec2 screenSize, float screenRatio)
	{
		id = e.getPointerId(index);
		refresh(e, screenSize, screenRatio);
	}
	boolean refresh(MotionEvent e, Vec2 screenSize, float screenRatio)
	{
		handled = false;
		if (e == null)
		{
			if (type == UP)
				return false;
			type = HOLD;
			return true;
		}
		int i = e.findPointerIndex(id);
		if (i == e.INVALID_POINTER_ID
			|| (e.getActionMasked() == e.ACTION_POINTER_UP && e.getActionIndex() == i)
			|| e.getAction() == e.ACTION_UP)
		{
			if (type == UP)
				return false;
			type = UP;
			return true;
		}
		Vec2 next = new Vec2((e.getX(i)/screenSize.x *2 -1) * screenRatio, e.getY(i)/screenSize.y *2 -1);
		if (pos == null)
			type = DOWN;
		else if (!pos.equals(next))
			type = MOVE;
		else
			type = HOLD;
		pos = next;
		return true;
	}
	public int id()
	{ return id; }
	public int type()
	{ return type; }
	public float x()
	{ return pos.x; }
	public float y()
	{ return pos.y; }
	public static final int
		DOWN = 0,
		HOLD = 1,
		MOVE = 2,
		UP = 3;
}
