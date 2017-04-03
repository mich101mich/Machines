package com.M101M.Industria.UI;

import android.opengl.*;
import com.M101M.Industria.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Industria.Utils.*;
import com.M101M.Industria.World.*;

public class BlockDisplay extends UIElement
{
	public int type;
	Block b;
	public BlockDisplay(Vec2 pos, float width, int type)
	{
		super(pos, new Vec2(width, width));
		this.type = type;
		b = new Block(0,0,0, type);
	}
	@Override
	public void draw()
	{
		if (!visible)
			return;
		b.type = type;
		
		Player player = Game.player;
		Mat vpMat = GLM.vpMat;
		
		Game.player = new Player();
		Game.player.rot = new Vec();
		Game.player.pos = new Vec(b.pos);
		GLM.vpMat = new Mat(GLM.uiMat)
			.translate(pos.x, pos.y, 0)
			.scale(size.x,size.x,1);
		
		Block.startDrawing();
		b.draw();
		UI.startDrawing();
		
		GLM.vpMat = vpMat;
		Game.player = player;
	}
	@Override
	public boolean handleTouch(TouchEvent e)
	{
		return false;
	}
}
