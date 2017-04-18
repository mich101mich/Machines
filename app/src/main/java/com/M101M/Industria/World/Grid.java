package com.M101M.Industria.World;

import com.M101M.Industria.*;
import com.M101M.Utils.*;
import java.util.*;

public class Grid
{
	ArrayList<Block> blocks, sources, machines;
	boolean hasPower = false;
	
	public Grid()
	{
		blocks = new ArrayList<Block>(50);
		machines = new ArrayList<Block>(30);
		sources = new ArrayList<Block>(30);
	}
	
	public void update()
	{
		int power = 0;
		for (Block b : sources)
			if (b.hasPower())
				power++;
		boolean pow = (power > 0 && power >= machines.size());
		if (hasPower != pow)
		{
			hasPower = pow;
			for (Block b : blocks)
				if (!b.isSource())
					b.setPower(hasPower);
		}
	}
	
	boolean add(Block b)
	{
		if (b == null || !b.conducts())
			return false;
		for (Block block : blocks)
			if (block == b)
				return false;
		blocks.add(b);
		if (b.isSource())
			sources.add(b);
		else if (b.isMachine())
			machines.add(b);
		b.grid = this;
		if (!b.isSource())
			b.setPower(hasPower);
		return true;
	}
	void remove(Block b)
	{
		if (b == null)
			return;
		blocks.remove(b);
		if (b.isSource())
			sources.remove(b);
		else if (b.isMachine())
			machines.remove(b);
		b.grid = null;
	}
	void merge(Grid other)
	{
		for (Block b : other.blocks)
			add(b);
	}
	
	public static Grid create(Block origin)
	{
		return inflate(new Grid(), origin);
	}
	public static Grid inflate(Grid g, Block origin)
	{
		Grid ret = g;
		
		Stack<Veci> check = new Stack<Veci>();
		check.push(origin.pos);
		while (!check.isEmpty())
		{
			Veci p = check.pop();
			Chunk c = Game.map.getChunk(p);
			if (c == null)
				continue;
			Block b = c.getBlock(p);
			if (b == null || !b.conducts() || b.grid == g)
				continue;
			if (b.grid != null)
			{
				for (Block block : g.blocks)
					b.grid.add(block);
				g = b.grid;
				ret = null;
				continue;
			}
			if (g.add(b))
				for (int i = 0; i < 6; i++)
					check.add(Veci.move(p, i));
		}
		g.update();
		return ret;
	}
	
}
