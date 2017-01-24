package com.M101M.Industria.Utils;

import android.view.*;

public class TouchEvent
{
	public Type type;
	public int id;
	public Vec2 pos;
	public boolean handled;
	public TouchEvent(MotionEvent e, int index)
	{
		id = e.getPointerId(index);
		refresh(e);
		type = Type.DOWN;
	}
	public boolean refresh(MotionEvent e)
	{
		int i = e.findPointerIndex(id);
		if (i == e.INVALID_POINTER_ID
			|| (e.getAction() == e.ACTION_POINTER_UP
				&& e.getActionIndex() == i))
		{
			return false;
		}
		pos = new Vec2(e.getX(i), e.getY(i));
		type = Type.HOLD;
		handled = false;
		return true;
	}
	public enum Type {
		DOWN,
		HOLD
	}
}
