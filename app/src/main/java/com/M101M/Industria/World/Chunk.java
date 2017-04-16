package com.M101M.Industria.World;

import com.M101M.Industria.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Industria.Utils.*;

public class Chunk
{
	Veci pos;
	Pile<Block> blocks = new Pile<Block>(30);
	Block[][][] chunk = new Block[8][8][8];
	public Chunk(Veci position)
	{
		pos = toChunkVec(position);
	}
	public boolean inChunk(Veci p)
	{
		return pos.equals(toChunkVec(p));
	}
	public Veci getPos()
	{
		return new Veci(pos);
	}
	public boolean set(Block b)
	{
		if (b == null || !inChunk(b.pos))
			return false;
		remove(b.pos);
		blocks.add(b);
		set(b.pos, b);
		return true;
	}
	private void set(Veci p, Block val)
	{ chunk[p.x-pos.x][p.y][p.z-pos.z] = val; }
	public boolean remove(Block b)
	{
		if (b == null || getBlock(b.pos) != b)
			return false;
		blocks.remove(b);
		set(b.pos, null);
		return true;
	}
	public Block remove(Veci p)
	{
		Block ret = getBlock(p);
		remove(ret);
		return ret;
	}
	public Block getBlock(Veci p)
	{
		if (!inChunk(p))
			return null;
		return chunk[p.x-pos.x][p.y][p.z-pos.z];
	}
	public void draw()
	{
		if (new Vec(Game.player.pos.x - pos.x - 4, 0,Game.player.pos.z - pos.z - 4).length() > GLM.renderDistance + 6)
			return;
		for (Block b : blocks)
			b.draw();
	}
	public void update()
	{
		for (Block b : blocks)
			b.update();
	}
	public static Veci toChunkVec(Veci p)
	{
		if (p == null || p.y < 0 || p.y >= 8)
			return null;
		return new Veci(
			(p.x >= 0 ? p.x/8 : (p.x+1)/8-1) * 8,
			0,
			(p.z >= 0 ? p.z/8 : (p.z+1)/8-1) * 8
		);
	}
}
