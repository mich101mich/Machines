package com.M101M.Industria.World;

import com.M101M.Industria.*;
import com.M101M.Industria.Utils.*;
import java.util.*;

public class Map
{
	private Arr<Chunk> chunks = new Arr<Chunk>(500);
	private Hashtable<Veci, Chunk> map = new Hashtable<Veci, Chunk>(500);
	public Chunk getChunk(Veci pos)
	{
		pos = Chunk.toChunkVec(pos);
		if (pos == null)
			return null;
		return map.get(pos);
	}
	public boolean set(Block b)
	{ return set(b, true); }
	public boolean set(Block b, boolean sendUpdates)
	{
		if (b == null || b.pos == null || b.pos.y >= 8 || b.pos.y < 0)
			return false;
		Chunk chunk = getChunk(b.pos);
		if (chunk == null)
		{
			chunk = new Chunk(b.pos);
			chunks.add(chunk);
			map.put(chunk.getPos(), chunk);
		}
		if (chunk.set(b))
		{
			if (sendUpdates)
				Game.addUpdates(b.pos);
			return true;
		}
		return false;
	}
	public void remove(Block b)
	{
		if (b == null)
			return;
		Chunk chunk = getChunk(b.pos);
		if (chunk != null && chunk.remove(b))
			Game.addUpdates(b.pos);
	}
	public Block remove(Veci pos)
	{
		Chunk chunk = getChunk(pos);
		if (chunk == null)
			return null;
		Block ret = chunk.remove(pos);
		if (ret != null)
			Game.addUpdates(pos);
		return ret;
	}
	public Block getBlock(Veci pos, int dir)
	{ return getBlock(Veci.move(pos, dir)); }
	public Block getBlock(Veci pos)
	{
		Chunk chunk = getChunk(pos);
		return (chunk != null ? chunk.getBlock(pos) : null);
	}
	public void draw()
	{
		for (Chunk c : chunks)
			c.draw();
	}
}
