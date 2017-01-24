package com.M101M.Industria.World;

import android.opengl.*;
import com.M101M.Industria.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Industria.Utils.*;

public class Plane
{
	Vec pos;
	static int program = -1, matHandle, posHandle, vertexHandle, bufferHandle, indexHandle, selectedHandle;
	public Plane (Vec position)
	{
		pos = position;
	}

	public static void globalInit ()
	{
		program = GLM.createProgram(
			GLM.loadShader(GLES20.GL_VERTEX_SHADER,
				"uniform mat4 mvpMat; uniform vec4 position;",
				"attribute vec4 vertex; varying vec4 pos;",
				"void main() {",
				"		gl_Position = mvpMat * (position + vertex);",
				"		pos = vertex + position;",
				"}"
			),
			GLM.loadShader(GLES20.GL_FRAGMENT_SHADER,
				"varying vec4 pos;",
				"void main() {",
				"		vec4 f = fract(pos);",
				"		if (f.x < 0.02 || f.x > 0.98 || f.z < 0.02 || f.z > 0.98)",
				"				gl_FragColor = vec4(0,0,0,1);",
				"		else gl_FragColor = vec4(0.5,0.5,0.5,1);",
				"}"
			)
		);
		gl.glUseProgram(program);
		matHandle = gl.glGetUniformLocation(program, "mvpMat");
		posHandle = gl.glGetUniformLocation(program, "position");
		vertexHandle = gl.glGetAttribLocation(program, "vertex");
		gl.glEnableVertexAttribArray(vertexHandle);

		int[] handle = GLM.genBuffers(2);
		bufferHandle = handle[0];
		indexHandle = handle[1];
		gl.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferHandle);
		gl.glEnableVertexAttribArray(bufferHandle);
		gl.glBufferData(GLES20.GL_ARRAY_BUFFER, 48, Utils.toFloatBuffer(new float[]{-50,0,50, 50,0,50, -50,0,-50, 50,0,-50}), GLES20.GL_STATIC_DRAW);
		gl.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexHandle);
		gl.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, 4, Utils.toByteBuffer(new byte[]{0,1,2,3}), GLES20.GL_STATIC_DRAW);
	}

	public static void startDrawing ()
	{
		gl.glUseProgram(program);
		gl.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexHandle);
		gl.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferHandle);
		gl.glVertexAttribPointer(vertexHandle, 3, GLES20.GL_FLOAT, false, 12, 0);
		gl.glUniformMatrix4fv(matHandle, 1, false, GLM.mvpMat, 0);
		GLM.drawing = GLM.PLANE;
	}
	public void draw ()
	{
		if (GLM.drawing != GLM.PLANE)
			startDrawing();
		gl.glUniform4fv(posHandle, 1, new float[]{Game.player.pos.x + pos.x, pos.y, Game.player.pos.z + pos.z, 0}, 0);
		gl.glDrawElements(GLES20.GL_TRIANGLE_STRIP, 4, GLES20.GL_UNSIGNED_BYTE, 0);
	}
}
