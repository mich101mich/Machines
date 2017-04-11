package com.M101M.Industria.World;

import android.opengl.*;
import com.M101M.Industria.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Industria.Utils.*;

public class Block
{
	public Veci pos;
	public int type;
	public Grid grid = null;
	public boolean hasBlockUpdate = false;
	public float heat = 0.0f;
	public Block[] neighbours = new Block[6];
	private static final int MAT=0, POS=1, SELECTED=2, TICK=3, TYPE=4, HEAT=5, VERTEX=6, MATERIAL=7, BUFFER=8, INDEX=9;
	private static int handle[] = new int[10];
	
	public Block(int x, int y, int z, int type)
	{ this(new Veci(x, y, z), type); }
	public Block(Veci position, int type)
	{
		pos = position;
		this.type = type;
	}

	public static void globalInit()
	{
		Shader.use(Shader.BLOCK);
		final String[] names = { "mvpMat", "position", "selected", "tick", "type", "heat", "vertex", "material" };
		for (int i=0; i < 6; i++)
			handle[i] = Shader.getUniform(names[i]);

		for (int i=VERTEX; i <= MATERIAL; i++)
			handle[i] = Shader.getAttribute(names[i]);

		System.arraycopy(GLM.genBuffers(2), 0, handle, BUFFER, 2);
		GLM.vbo(handle[BUFFER], new float[]{0,0,0, 0,0,1, 0,1,0, 1,0,0, 0,1,1, 1,0,1, 1,1,0, 1,1,1});

		gl.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, handle[INDEX]);
		gl.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, 36, Utils.toByteBuffer(new byte[]{1,5,4,4,5,7, 5,3,7,7,3,6, 3,0,6,6,0,2, 0,1,2,2,1,4, 0,3,1,1,3,5, 4,7,2,2,7,6}), GLES30.GL_STATIC_DRAW);
	}

	public static void startDrawing()
	{
		Shader.use(Shader.BLOCK);
		gl.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, handle[INDEX]);
		GLM.useVBO(handle[BUFFER], handle[VERTEX], 3, 0);
		
		Mat model = Mat.identity()
			.rotate(Vec.negative(Game.player.rot))
			.translate(Vec.negative(Game.player.pos));
		gl.glUniformMatrix4fv(handle[MAT], 1, false, Mat.multiply(GLM.vpMat, model).toArray(), 0);
		gl.glUniform4fv(handle[SELECTED], 1, (Game.player.selected != null ? new Vec(Game.player.selected.pos).toArray() : new float[]{0,-5,0,0}), 0);
		gl.glUniform1i(handle[TICK], Game.time % 2000);
	}

	public void draw()
	{
		float dist = Vec.minus(pos, Game.player.pos).length();
		if (dist > GLM.renderDistance)
			return;
		double delta = (Math.toDegrees(Math.atan2(pos.x + 0.5 - Game.player.pos.x, pos.z + 0.5 - Game.player.pos.z)) - Game.player.rot.y + 520) % 360;
		if ((delta > 180 ? 360 - delta : delta) > 180 - (dist / GLM.renderDistance) * 150)
			return;

		gl.glUniform4fv(handle[POS], 1, new Vec(pos).toArray(), 0);
		gl.glUniform1i(handle[TYPE], type);
		gl.glUniform1f(handle[HEAT], heat);
		GLM.useVBO(Shader.matHandle, handle[MATERIAL], 4, type*Shader.matStride);

		gl.glDrawElements(GLES30.GL_TRIANGLES, 36, GLES30.GL_UNSIGNED_BYTE, 0);
	}
	public void update()
	{
		if (type == Type.genSol || type == Type.genSolPow)
			setPower(hasSkyAccess());
		if (type == Type.drillPow)
			heat += 15.0f;
			
		if (heat >= 5.0f)
		{
			if (heat >= 100.0f)
			{
				Game.map.remove(this);
				return;
			}
			float newHeat = heat, fanFactor = (type == Type.fanPow ? 4.0f : 1.0f);
			for (Block b : neighbours)
			{
				if (b == null)
					newHeat -= heat * 0.04f * fanFactor;
				else
				{
					float delta = heat * 0.01f * fanFactor * (b.type == Type.fanPow ? 4.0f : 1.0f);
					b.heat += delta;
					newHeat -= delta;
				}
			}
			heat = Math.max(newHeat, 0.0f);
		}
		
		if (hasBlockUpdate)
		{
			
			hasBlockUpdate = false;
		}
	}
	void setPower(boolean pow)
	{
		if (type == Type.cable && pow)
			type = Type.cablePow;
		else if (type == Type.cablePow && !pow)
			type = Type.cable;
		else if (type == Type.fan && pow)
			type = Type.fanPow;
		else if (type == Type.fanPow && !pow)
			type = Type.fan;
		else if (type == Type.drill && pow && pos.y == 0)
			type = Type.drillPow;
		else if (type == Type.drillPow && !pow)
			type = Type.drill;
		else if (type == Type.genSol && pow)
			type = Type.genSolPow;
		else if (type == Type.genSolPow && !pow)
			type = Type.genSol;
		else
			return;
		Game.addUpdates(pos);
	}
	public boolean hasPower()
	{
		return Type.hasPower[type];
	}
	public boolean isSource()
	{
		return Type.category[type] == Type.SOURCE;
	}
	public boolean isMachine()
	{
		return Type.category[type] == Type.MACHINE;
	}
	public boolean conducts()
	{
		return Type.conducts[type];
	}
	public boolean exists()
	{
		return Game.map.getBlock(pos) == this;
	}
	public static boolean hasPower(Block b)
	{
		return b != null && b.hasPower() && b.exists();
	}
	public boolean hasSkyAccess()
	{
		Chunk c = Game.map.getChunk(pos);
		int x = pos.x - c.pos.x, z = pos.z - c.pos.z;
		for (int y = pos.y + 1; y < 8; y++)
			if (c.chunk[x][y][z] != null)
				return false;
		return true;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null || !(obj instanceof Block))
			return false;
		return ((Block)obj).type == type && Veci.equals(((Block)obj).pos, pos);
	}
}
