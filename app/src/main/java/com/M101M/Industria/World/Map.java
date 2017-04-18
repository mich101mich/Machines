package com.M101M.Industria.World;

import com.M101M.Industria.*;
import com.M101M.Utils.*;
import java.util.*;

public class Map
{
	private Pile<Chunk> chunks = new Pile<Chunk>(500);
	private Hashtable<Veci, Chunk> map = new Hashtable<Veci, Chunk>(500);
	private Pile<Grid> grids = new Pile<Grid>();
	Chunk getChunk(Veci pos)
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
			for (int i = 0; i < 6; i++)
			{
				Block block = getBlock(b.pos, i);
				if (block != null)
					block.neighbours[Veci.opposite(i)] = b;
				b.neighbours[i] = block;
			}
			if (sendUpdates)
			{
				Game.addUpdates(b.pos);
				addGrid(b);
			}
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
		{
			for (int i = 0; i < 6; i++)
				if (b.neighbours[i] != null)
					b.neighbours[i].neighbours[Veci.opposite(i)] = null;
			Game.addUpdates(b.pos);
			if (b.grid != null)
				removeGrid(b);
		}
	}
	public Block remove(Veci pos)
	{
		Chunk chunk = getChunk(pos);
		if (chunk == null)
			return null;
		Block ret = chunk.remove(pos);
		if (ret != null)
		{
			for (int i = 0; i < 6; i++)
				if (ret.neighbours[i] != null)
					ret.neighbours[i].neighbours[Veci.opposite(i)] = null;
			Game.addUpdates(pos);
			if (ret.grid != null)
				removeGrid(ret);
		}
		return ret;
	}
	public Block getBlock(Veci pos, int dir)
	{ return getBlock(Veci.move(pos, dir)); }
	public Block getBlock(Veci pos)
	{
		Chunk chunk = getChunk(pos);
		return (chunk != null ? chunk.getBlock(pos) : null);
	}
	private void addGrid(Block b)
	{
		if (b == null || !b.conducts())
			return;
		Grid g = null;
		for (Block block : b.neighbours)
		{
			if (block == null || block.grid == null)
				continue;
			if (g == null)
				g = block.grid;
			else if (g != block.grid)
			{
				grids.remove(block.grid);
				g.merge(block.grid);
			}
		}
		if (g != null)
		{
			Grid.inflate(g, b);
			return;
		}
		if (b.isSource())
		{
			g = Grid.create(b);
			if (g != null)
				grids.add(g);
		}
	}
	private void removeGrid(Block b)
	{
		Grid g = b.grid;
		if (g.sources.size() == 1 && b.isSource())
		{
			for (Block block : g.blocks)
				if (block != b)
				{
					block.setPower(false);
					block.grid = null;
				}

			grids.remove(g);
			b.grid = null;
			return;
		}

		int count = 0;
		for (Block block : b.neighbours)
			if (block != null && block.grid != null && ++count > 1)
				break;
		if (count == 1)
		{
			g.remove(b);
			return;
		}
		grids.remove(g);
		for (Block block : g.blocks)
		{
			block.grid = null;
			if (!block.isSource())
				block.setPower(false);
		}
		for (Block source : g.sources)
			addGrid(source);
	}
	public void draw()
	{
		for (Chunk c : chunks)
			c.draw();
	}
	public void update()
	{
		for (Chunk c : chunks)
			c.update();
		for (Grid g : grids)
			g.update();
	}
}
