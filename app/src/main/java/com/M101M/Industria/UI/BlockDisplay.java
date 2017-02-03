package com.M101M.Industria.UI;

import com.M101M.Industria.GLHelp.*;
import com.M101M.Industria.Utils.*;
import com.M101M.Industria.World.*;

public class BlockDisplay extends Rectangle
{
	public int type;
	Block b;
	public BlockDisplay(Vec2 pos, float width, int type)
	{
		super(pos, new Vec2(width, width), 0x44888888);
		this.type = type;
		b = new Block(0,0,0, type);
	}
	@Override
	public void draw()
	{
		super.draw();
		b.type = type;
		Block.startDrawing();
		b.draw();
		UI.startDrawing();
	}
}
