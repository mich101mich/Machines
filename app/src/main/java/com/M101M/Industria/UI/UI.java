package com.M101M.Industria.UI;
import com.M101M.Industria.Utils.*;

public class UI
{
	Arr<UIElement> elements;
	public UI()
	{
		elements = new Arr<UIElement>();
	}
	public void draw()
	{
		for (UIElement e : elements)
			e.draw();
	}
	public boolean onTouch(TouchEvent e)
	{
		
		return false;
	}
}
