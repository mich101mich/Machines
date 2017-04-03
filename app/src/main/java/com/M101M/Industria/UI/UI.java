package com.M101M.Industria.UI;
import android.opengl.*;
import com.M101M.Industria.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Industria.Utils.*;

public class UI
{
	private Arr<UIElement> elements;
	public UI()
	{
		elements = new Arr<UIElement>();
		
		final float buttonX = GLM.ratio - 0.4f;
		
		final Container blocks = new Container(
			new Vec2(-buttonX, -0.8f),
			new Vec2(2 * buttonX, 0.3f),
			Container.Align.RIGHT);
		
		final Button expand = new Button(
			new Vec2(buttonX, -0.8f),
			new Vec2(0.3f,0.3f),
			new Text(
				new Vec2(0, -0.3f/7),
				0.3f,
				"<"));
			
		final Button place  = new Button(
			new Vec2(buttonX, -0.2f),
			new Vec2(0.3f,0.3f),
			new BlockDisplay(
				new Vec2(0.01f, 0.01f),
				0.28f,
				Game.player.placeType));
			
		final Button delete = new Button(
			new Vec2(buttonX, 0.4f),
			new Vec2(0.3f,0.3f),
			new Text(
				new Vec2(0, -0.3f/7 *2),
				0.3f,
				"x",
				0xff0000));
				
		blocks.visible = false;
		for (int type : Type.placeable)
		{
			Button b = new Button(
				new Vec2(0,0),
				new Vec2(0.3f,0.3f),
				new BlockDisplay(new Vec2(), 0.3f, type));
			b.setOnClickListener(new Button.OnClickListener(){
					public void onClick(Button b) {
						((BlockDisplay)place.content).type
							= Game.player.placeType
							= ((BlockDisplay)b.content).type;
						blocks.visible = false;
					}});
			blocks.add(b);
		}
		
		delete.setOnClickListener(new Button.OnClickListener(){
				public void onClick(Button b) {
					switch (Game.player.action)
					{
					case Player.DELETE:
						Game.player.action = Player.DELETE_DRAG;
						delete.pos.x = buttonX - 0.1f;
						break;
					case Player.DELETE_DRAG:
						Game.player.action = Player.SELECT;
						delete.pos.x = buttonX;
						break;
					default:
						Game.player.action = Player.DELETE;
						place.pos.x = buttonX;
						delete.pos.x = buttonX - 0.05f;
					}
				}});
		place.setOnClickListener(new Button.OnClickListener(){
				public void onClick(Button b) {
					switch (Game.player.action)
					{
					case Player.PLACE:
						Game.player.action = Player.PLACE_DRAG;
						place.pos.x = buttonX - 0.1f;
						break;
					case Player.PLACE_DRAG:
						Game.player.action = Player.SELECT;
						place.pos.x = buttonX;
						break;
					default:
						Game.player.action = Player.PLACE;
						delete.pos.x = buttonX;
						place.pos.x = buttonX - 0.05f;
					}
					
					
					
					
					blocks.add(new Button(
						new Vec2(0,0),
						new Vec2(0.3f,0.3f),
						new BlockDisplay(new Vec2(), 0.3f, 0)));
					
					
					
					
					
				}});
		expand.setOnClickListener(new Button.OnClickListener(){
				public void onClick(Button b) {
					if (blocks.visible)
					{
						blocks.visible = false;
					}
					else
					{
						blocks.visible = true;
					}
				}});
		
		elements.add(blocks);
		elements.add(expand);
		elements.add(place);
		elements.add(delete);
		
		elements.add(new Circle(new Vec2(-GLM.ratio + 0.2f, 0.8f), new Vec2(0.2f,0.2f), 0xaaaaaa));
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
