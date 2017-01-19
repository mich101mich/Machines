package com.M101M.Industria.World;

import android.opengl.*;
import com.M101M.Industria.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Industria.Utils.*;

public class Block
{
	public Veci pos;
	public int type;
	public Block source;
	private static final int MAT=0, POS=1, SELECTED=2, TICK=3, TYPE=4, VERTEX=5, MATERIAL=6, BUFFER=7, INDEX=8;
	private static int program = -1, handle[] = new int[9];
	private boolean initialised = false;
	private int lastUpdate = -1;

	public Block(int x, int y, int z, int type)
	{ this(new Veci(x, y, z), type); }
	public Block(Veci position, int type)
	{
		pos = position;
		this.type = type;
	}

	public static void globalInit() throws Exception
	{
		program = GLM.createProgram(
			GLM.loadShader(GLES20.GL_VERTEX_SHADER,
				"uniform mat4 mvpMat;",
				"uniform vec4 position, selected;",
				"uniform int type;",
				"attribute vec4 vertex;",
				"attribute vec4 material;",
				"varying vec4 pos;",
				"varying float sel;",
				"varying vec4 color;",
				"void main() {",
				"		gl_Position = mvpMat * (position + vertex);",
				"		color = material; pos = vertex; sel = (selected.x == position.x && selected.y == position.y && selected.z == position.z ? 1.0 : 0.0);",
				"}"
			),
			GLM.loadShader(GLES30.GL_FRAGMENT_SHADER,
				"uniform int tick,type;",
				"varying vec4 pos;",
				"varying float sel;",
				"varying vec4 color;",
				"void set(float f, vec4 col) {",
				"		gl_FragColor = f*col + (1.0-f)*vec4(vec3(1,1,1)-col.rgb,1);",
				"}",
				"void main() {",
				"		float f = 1.0;",
				"		if (sel == 1.0)",
				"				f = sin(6.2832*float(tick-tick/" + (1300 / Game.tps) + "*" + (1300 / Game.tps) + ")/" + (1300 / Game.tps) + ".0)/2.0+0.5;",
				"		set(f, color);",
				"		if (type == " + Type.cable + " || type == " + Type.cablePow + ") {",
				"       float d = 0.15, s = -1.0, l = 0.0;",
				"				if (abs(pos.x-0.5) < d) { s = pos.x; l = (fract(pos.y) == 0.0 ? pos.z : pos.y); }",
				"				if (abs(pos.y-0.5) < d) { s = pos.y; l = (fract(pos.z) == 0.0 ? pos.x : pos.z); }",
				"				if (abs(pos.z-0.5) < d) { s = pos.z; l = (fract(pos.x) == 0.0 ? pos.y : pos.x); }",
				"				if (s == -1.0 || (abs(s-0.5) < 0.027 && abs(l-0.5) >= d)) {",
				"						set(f, vec4(0,0,0,1));",
				"						return;",
				"				}",
				"				if (type != " + Type.cablePow + " || abs(l-0.5) < d)",
				"						return;",
				"				s -= 0.5-d;",
				"				float c = 0.28*sin(6.28319*((s<d ? l : 1.0-l) + float(tick)/" + (600 / Game.tps) + ".0));",
				"				set(f, color + vec4(c,c,c,0));",
				"		}",
				"}"
			)
		);
		gl.glUseProgram(program);
		final String[] names = { "mvpMat", "position", "selected", "tick", "type", "vertex", "material" };
		for (int i=0; i < 5; i++)
			handle[i] = gl.glGetUniformLocation(program, names[i]);

		for (int i=VERTEX; i <= MATERIAL; i++)
		{
			handle[i] = gl.glGetAttribLocation(program, names[i]);
			gl.glEnableVertexAttribArray(handle[i]);
		}

		System.arraycopy(GLM.genBuffers(2), 0, handle, BUFFER, 2);
		gl.glBindBuffer(GLES30.GL_ARRAY_BUFFER, handle[BUFFER]);
		gl.glEnableVertexAttribArray(handle[BUFFER]);
		gl.glBufferData(GLES30.GL_ARRAY_BUFFER, 96, Utils.toFloatBuffer(new float[]{0,0,0, 0,0,1, 0,1,0, 1,0,0, 0,1,1, 1,0,1, 1,1,0, 1,1,1}), GLES30.GL_STATIC_DRAW);
		gl.glVertexAttribPointer(handle[VERTEX], 3, GLES30.GL_FLOAT, false, 0, 0);
		gl.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

		gl.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, handle[INDEX]);
		gl.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, 36, Utils.toByteBuffer(new byte[]{1,5,4,4,5,7, 5,3,7,7,3,6, 3,0,6,6,0,2, 0,1,2,2,1,4, 0,3,1,1,3,5, 4,7,2,2,7,6}), GLES30.GL_STATIC_DRAW);
	}
	void init()
	{

		initialised = true;
	}

	public static void startDrawing() throws Exception
	{
		gl.glUseProgram(program);
		gl.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, handle[INDEX]);

		gl.glBindBuffer(GLES30.GL_ARRAY_BUFFER, handle[BUFFER]);
		gl.glVertexAttribPointer(handle[VERTEX], 3, GLES30.GL_FLOAT, false, 12, 0);

		gl.glUniformMatrix4fv(handle[MAT], 1, false, GLM.mvpMat, 0);
		gl.glUniform4fv(handle[SELECTED], 1, (Game.player.selected != null ? new Vec(Game.player.selected.pos).toArray() : new float[]{0,-5,0,0}), 0);
		gl.glUniform1i(handle[TICK], Game.time);

		GLM.drawing = GLM.BLOCK;
	}

	public void draw() throws Exception
	{
		float dist = Vec.minus(pos, Game.player.pos).length();
		if (dist > GLM.renderDistance)
			return;
		double delta = (Math.toDegrees(Math.atan2(pos.x + 0.5 - Game.player.pos.x, pos.z + 0.5 - Game.player.pos.z)) - Game.player.rot.y + 520) % 360;
		if ((delta > 180 ? 360 - delta : delta) > 180 - (dist / GLM.renderDistance) * 150)
			return;

		if (GLM.drawing != GLM.BLOCK)
			startDrawing();

		if (!initialised)
			init();

		gl.glUniform4fv(handle[POS], 1, new Vec(pos).toArray(), 0);
		gl.glUniform1i(handle[TYPE], type);
		gl.glBindBuffer(GLES30.GL_ARRAY_BUFFER, Shader.matHandle);
		gl.glVertexAttribPointer(handle[MATERIAL], 4, GLES30.GL_FLOAT, false, 0, type*Shader.matStride);

		gl.glDrawElements(GLES30.GL_TRIANGLES, 36, GLES30.GL_UNSIGNED_BYTE, 0);
	}
	public void update()
	{
		if (!needsUpdate())
			return;
		lastUpdate = Game.time;
		if (conducts() && !isSource())
		{
			if (!hasPower(source))
				source = findSource();
			setPower(hasPower(source));
		}
	}
	Block findSource()
	{
		Block b;
		for (int i=0; i < 6; i++)
		{
			b = Game.map.getBlock(pos, i);
			if (b == null || !b.hasPower())
				continue;
			while (hasPower(b.source))
				b = b.source;
			if (b.isSource())
				return b;
		}
		return null;
	}
	void setPower(boolean pow)
	{
		if (type == Type.cable && pow)
			type = Type.cablePow;
		else if (type == Type.cablePow && !pow)
			type = Type.cable;
		else
			return;
		for (int i=0; i < 6; i++)
		{
			Block b = Game.map.getBlock(Veci.move(pos, i));
			if (b != null && b.conducts() && !b.isSource() && (b.hasPower() != pow))
				Game.updates.add(b.pos);
		}
	}
	final static int[] conductors = { Type.cable, Type.cablePow, Type.stone };
	final static int[] powerSources = { Type.stone };
	boolean needsUpdate()
	{
		return (type == Type.cable || type == Type.cablePow)
			&& lastUpdate != Game.time;
	}
	boolean hasPower()
	{
		return type == Type.cablePow
			|| Utils.contains(powerSources, type);
	}
	boolean isSource()
	{
		return Utils.contains(powerSources, type);
	}
	boolean conducts()
	{
		return Utils.contains(conductors, type);
	}
	boolean exists()
	{
		return Game.map.getBlock(pos) == this;
	}
	static boolean hasPower(Block b)
	{
		return b != null && b.hasPower() && b.exists();
	}
}
