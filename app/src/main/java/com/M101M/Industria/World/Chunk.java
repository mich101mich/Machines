package com.M101M.Industria.World;

import com.M101M.Industria.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Industria.Utils.*;

public class Chunk
{
	Veci pos;
	Arr<Block> blocks = new Arr<Block>(30);
	Block[][][] chunk = new Block[8][8][8];
	int needsUpdate = 0;
	Chunk(Veci position)
	{
		int x = (position.x >= 0 ? position.x/8 : (position.x+1)/8-1);
		int z = (position.z >= 0 ? position.z/8 : (position.z+1)/8-1);
		pos = new Veci(x*8,0,z*8);
	}
	boolean inChunk(Veci p)
	{ return p != null && p.x >= pos.x && p.x < pos.x+8 && p.z >= pos.z && p.z < pos.z+8 && p.y >= 0 && p.y < 8; }
	void set(Block b)
	{
		if (b == null || !inChunk(b.pos))
			return;
		remove(b.pos);
		if (b.needsUpdate())
			needsUpdate++;
		blocks.add(b);
		set(b.pos, b);
	}
	private void set(Veci p, Block val)
	{ chunk[p.x-pos.x][p.y][p.z-pos.z] = val; }
	void remove(Block b)
	{
		if (b == null || getBlock(b.pos) == null)
			return;
		if (b.needsUpdate())
			needsUpdate--;
		blocks.remove(b);
		set(b.pos, null);
	}
	Block remove(Veci p)
	{
		Block ret = getBlock(p);
		remove(ret);
		return ret;
	}
	Block getBlock(Veci p)
	{
		if (!inChunk(p))
			return null;
		return chunk[p.x-pos.x][p.y][p.z-pos.z];
	}
	void draw() throws Exception
	{
		if (new Vec(Game.player.pos.x - pos.x - 4, 0,Game.player.pos.z - pos.z - 4).length() > GLM.renderDistance + 6)
			return;

		blocks.forEach(new Arr.Action<Block>() {
				@Override public void run(Block e) throws Exception
				{ e.draw(); }
			});
	}
}
