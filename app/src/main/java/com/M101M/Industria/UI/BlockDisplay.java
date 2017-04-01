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
		
		Vec player = Game.player.pos;
		Game.player.pos = new Vec(b.pos);
		float[] oldMat = new float[16];
		System.arraycopy(GLM.mvpMat,0, oldMat,0, 16);
		System.arraycopy(GLM.uiMat,0, GLM.mvpMat,0, 16);
		
		GLM.translate(new Vec(pos.x, pos.y, 0));
		GLM.scale(new Vec(size.x,size.x,1));
		
		Block.startDrawing();
		b.draw();
		UI.startDrawing();
		
		System.arraycopy(oldMat,0, GLM.mvpMat,0, 16);
		Game.player.pos = player;
	}
	@Override
	public boolean handleTouch(TouchEvent e)
	{
		return false;
	}
}
