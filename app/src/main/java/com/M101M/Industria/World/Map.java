package com.M101M.Industria.World;

import com.M101M.Industria.Utils.*;

public class Map
{
	Arr<Chunk> chunks = new Arr<Chunk>(100);
	public Chunk getChunk(final Veci pos)
	{
		if (pos == null)
			return null;
		return chunks.find(new Arr.Condition<Chunk>() {
				@Override public boolean test(Chunk e)
				{ return e.inChunk(pos); }
			});
	}
	public void set(Block b)
	{
		if (b == null || b.pos.y >= 8 || b.pos.y < 0)
			return;
		Chunk chunk = getChunk(b.pos);
		if (chunk == null)
		{
			chunk = new Chunk(b.pos);
			chunks.add(chunk);
		}
		chunk.set(b);
	}
	public Block getBlock(Veci pos, int dir)
	{ return getBlock(Veci.move(pos, dir)); }
	public Block getBlock(Veci pos)
	{
		Chunk chunk = getChunk(pos);
		return (chunk == null ? null : chunk.getBlock(pos));
	}
	public void draw() throws Exception
	{
		chunks.forEach(new Arr.Action<Chunk>() {
				@Override public void run(Chunk e) throws Exception
				{ e.draw(); }
			});
	}
}
