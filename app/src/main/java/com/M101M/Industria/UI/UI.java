package com.M101M.Industria.UI;
import android.opengl.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Industria.Utils.*;

public class UI
{
	private Arr<UIElement> elements;
	public UI()
	{
		elements = new Arr<UIElement>();
		
	}
	public void add(UIElement e)
	{
		elements.add(e);
	}
	public void draw()
	{
		// start drawing?
		
		for (UIElement e : elements)
			e.draw();
	}
	public boolean onTouch(TouchEvent event)
	{
		for (UIElement e : elements)
			if (e.bounds().contains(event.pos.x, event.pos.y)
					&& e.handleTouch(event))
				return true;
		return false;
	}
}
