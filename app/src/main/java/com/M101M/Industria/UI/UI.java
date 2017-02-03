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
		
		final Text werbung = new Text(new Vec2(-GLM.ratio, 0.7f), GLM.ratio, "Hier könnte\nIhre Werbung stehen!", 0xff0000, 0xffffff);
		final Animation annoy = new Animation(-1){
				Vec2 pos;
				public void start(UIElement obj)
				{ pos = new Vec2(obj.pos); }
				public void step(UIElement obj)
				{
					Text t = ((Text)obj);
					t.color = 0xff << ((int)(Math.random()*3) * 8);
					t.textColor = 0xff << ((int)(Math.random()*3) * 8);
					t.pos.x = pos.x + ((float)Math.random()* 0.06f - 0.03f);
					t.pos.y = pos.y + ((float)Math.random()* 0.06f - 0.03f);
				}
				public void stop(UIElement obj)
				{
					Text t = ((Text)obj);
					t.color = 0xffffff;
					t.textColor = 0x0;
					t.pos = pos;
				}
			};
		werbung.setAnimation(annoy);
		elements.add(werbung);
		
		elements.add(new Button(new Vec2(-GLM.ratio + 0.1f, -0.9f), GLM.ratio * 2 - 0.2f, "Hello World!", new Button.OnClickListener(){
			boolean active = true;
			public void onClick(Button b) {
				if (active)
					werbung.setAnimation(null);
				else
					werbung.setAnimation(annoy);
				active = !active;
			}}));
		
		//elements.add(new BlockDisplay(new Vec2(0,0), 0.5f, 3));
	}
	public static void startDrawing()
	{
		gl.glDisable(GLES20.GL_DEPTH_TEST);
		gl.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	public void add(UIElement e)
	{
		elements.add(e);
	}
	public void update()
	{
		for (UIElement e : elements)
			e.update();
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
			if (e.handleTouch(event))
				return true;
		return false;
	}
}
