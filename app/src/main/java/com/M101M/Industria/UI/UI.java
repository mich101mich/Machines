package com.M101M.Industria.UI;
import android.opengl.*;
import com.M101M.Industria.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Utils.*;

public class UI
{
	private java.util.ArrayList<UIElement> elements;
	public UIElement popup = null;
	public int popupDuration = 0;
	public UI()
	{
		elements = new java.util.ArrayList<UIElement>(50);
		
		final float buttonX = GLM.ratio - 0.4f;
		
		final Container blocks = new Container(
			new Vec2(-buttonX, -0.8f),
			new Vec2(2 * buttonX, 0.3f),
			Container.Align.RIGHT);
			
		final BlockDisplay place = new BlockDisplay(
			new Vec2(0.01f, 0.01f),
			0.28f,
			Game.player.placeType);
			
		final Text delete = new Text(
			new Vec2(0, -0.3f/7 *2),
			0.3f,
			"x",
			0xff0000);
		
		final Button expand = new Button(
			new Vec2(buttonX, -0.8f),
			new Vec2(0.3f,0.3f),
			new Text(
				new Vec2(0, -0.3f/7),
				0.3f,
				"<"));
			
		final Button action = new Button(
			new Vec2(buttonX, -0.2f),
			new Vec2(0.3f,0.3f),
			null,
			"select Blocks");
				
		blocks.visible = false;
		for (int type = 0; type < Type.count; type++)
		{
			if (!Type.placeable[type])
				continue;
			Button b = new Button(
				new Vec2(0,0),
				new Vec2(0.3f,0.3f),
				new BlockDisplay(new Vec2(), 0.3f, type),
				Type.names[type],
				Button.DEFAULT_COLOR,
				new Button.OnClickListener(){
					public void onClick(Button b) {
						place.type
							= Game.player.placeType
							= ((BlockDisplay)b.content).type;
						blocks.visible = false;
						((Text)expand.content).setText("<");
					}});
			blocks.add(b);
		}
		
		action.setOnClickListener(new Button.OnClickListener(){
				public void onClick(Button b) {
					switch (Game.player.action)
					{
					case Player.PLACE:
						Game.player.action = Player.DELETE;
						b.content = delete;
						b.toolTip = "break Blocks";
						break;
					case Player.DELETE:
						Game.player.action = Player.SELECT;
						b.content = null;
						b.toolTip = "select Blocks";
						break;
					default:
						Game.player.action = Player.PLACE;
						b.content = place;
						b.toolTip = "place Blocks";
					}
				}});
		expand.setOnClickListener(new Button.OnClickListener(){
				public void onClick(Button b) {
					((Text)b.content).setText(blocks.visible ? "<" : ">");
					blocks.visible = !blocks.visible;
				}});
				
		elements.add(blocks);
		elements.add(expand);
		elements.add(action);
		
		// fps counter
		elements.add(new Text(new Vec2(-GLM.ratio, -1), 0.5f, ""){
			long lastUpdate = System.currentTimeMillis();
			@Override
			public void update()
			{
				if (Game.time % 20 != 0)
					return;
				long current = System.currentTimeMillis();
				float fps = (float) Math.floor(20000.0f / (current - lastUpdate) * 10) / 10.0f;
				lastUpdate = current;
				setText(Float.toString(fps));
			}
		});
		
		final int rotColor = 0x11aaaaff;
		final float radius = 0.3f;
		
		final Button rotButton = new Button(
			new Vec2(GLM.ratio - 1.8f * radius, 1.0f - 1.8f * radius),
			new Vec2(radius, radius),
			new Circle(new Vec2(radius / 2.0f, radius / 2.0f), radius / 3.0f, rotColor),
			null,
			0xff000000);
		elements.add(rotButton);
		
		final Circle joyStick = new Circle(new Vec2(-GLM.ratio + 1.3f*radius, 1.0f - 1.3f*radius), radius, 0x11aaaaaa){
			int touchID = -1;
			Circle knob = new Circle(new Vec2(-GLM.ratio + 1.3f*radius, 1.0f - 1.3f*radius), radius/3.0f, 0x55ffffff);
			public void draw()
			{
				color = (rotButton.isPressed() ? rotColor : 0x11aaaaaa);
				knob.color = 0x44555555 + (rotButton.isPressed() ? rotColor : 0x11aaaaaa);
				super.draw();
				knob.draw();
			}
			public boolean handleTouch(TouchEvent e)
			{
				if (e.type() == e.DOWN && bounds().contains(e.x(),e.y()))
					touchID = e.id();
				
				if (touchID == e.id())
				{
					if (e.type() == e.UP)
					{
						knob.pos = pos;
						touchID = -1;
					}
					else
					{
						Vec2 delta = new Vec2(e.x(), e.y()).sub(pos);
						if (delta.length() > radius)
							delta.unit().scale(radius);
						knob.pos = Vec2.plus(pos, delta);
						
						if (!rotButton.isPressed())
							Game.player.pos.move(new Vec(delta.x / radius, 0, delta.y / radius).scale(Player.walkSpeed), new Vec(0, -Game.player.rot.y, 0));
						else
						{
							Game.player.rot.add(new Vec(-delta.y / radius, -delta.x / radius, 0).scale(Player.lookSpeed));
							Game.player.rot.x = Math.min(70, Math.max(-70, Game.player.rot.x));
							Game.player.rot.y = (Game.player.rot.y + 360) % 360;
						}
					}
					return e.handled = true;
				}
				return false;
			}
		};
		elements.add(joyStick);
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
	public void remove(UIElement e)
	{
		elements.remove(e);
	}
	public void update()
	{
		for (UIElement e : elements)
			e.update();
		
		if (popup != null)
		{
			popup.update();
			if (--popupDuration == 0)
				popup = null;
		}
	}
	
	public void draw()
	{
		// start drawing?

		for (UIElement e : elements)
			e.draw();
		
		if (popup != null)
			popup.draw();
	}
	public boolean onTouch(TouchEvent event)
	{
		if (popup != null && popup.handleTouch(event))
			return true;
		
		for (UIElement e : elements)
			if (e.handleTouch(event))
				return true;
		
		return false;
	}
}
