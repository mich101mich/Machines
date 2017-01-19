package com.M101M.Industria.UI;
import com.M101M.Industria.Utils.*;

public class UI
{
	Arr<UIElement> elements;
	public UI()
	{
		elements = new Arr<UIElement>();
	}
	public void draw() throws Exception
	{
		
		elements.forEach(new Arr.Action<UIElement>(){
				@Override public void run(UIElement e) throws Exception
				{
					e.draw();
				}});
	}
}
