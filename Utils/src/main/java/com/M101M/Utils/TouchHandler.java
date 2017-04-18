package com.M101M.Utils;
import android.view.MotionEvent;
import java.util.Iterator;

public class TouchHandler
{
	private MotionEvent event;
	public final Pile<TouchEvent> touchEvents;
	private Vec2 screenSize;
	private float screenRatio;
	
	public TouchHandler()
	{
		touchEvents = new Pile<TouchEvent>(20);
	}
	
	public boolean onTouchEvent(MotionEvent e)
	{
		if (e.getAction() == e.ACTION_CANCEL)
			event = null;
		else
			event = e;
		return true;
	}
	public void processTouchEvents(Vec2 screenSize, float screenRatio)
	{
		if (event == null)
		{
			Iterator<TouchEvent> it = touchEvents.iterator();
			while (it.hasNext())
			{
				if (!it.next().refresh(null, screenSize, screenRatio))
					it.remove();
			}
			return;
		}
		MotionEvent e = event;
		event = null;
		for (TouchEvent t : touchEvents)
			t.handled = true;
		for (int i=0; i < e.getPointerCount(); i++)
		{
			if (e.getAction() == e.ACTION_UP ||
				(e.getActionMasked() == e.ACTION_POINTER_UP
				&& e.getActionIndex() == i))
				continue;
			final int id = e.getPointerId(i);
			if (touchEvents.find(new Pile.Condition<TouchEvent>(){public boolean test(TouchEvent e) {
							return e.id() == id; }})
				== null)
				touchEvents.add(new TouchEvent(e, i, screenSize, screenRatio));
		}
		Iterator<TouchEvent> it = touchEvents.iterator();
		while (it.hasNext())
		{
			TouchEvent t = it.next();
			if (t.handled && !t.refresh(e, screenSize, screenRatio))
				it.remove();
		}
	}
}
