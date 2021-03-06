package com.M101M.Industria.GLHelp;

import android.opengl.*;
import com.M101M.Industria.*;
import com.M101M.Utils.*;

public class GLM
{
	public static Mat vpMat, uiMat;
	public static final int renderDistance = 30;
	public static Vec2 screen;
	public static float ratio;
	private static int buffers[] = new int[64], bufferCount = 0, indexBufferHandle;
	
	public static int loadShader(int type, String... lines)
	{
		String code = Utils.join(lines, "\n");
		int shader = gl.glCreateShader(type);
		gl.glShaderSource(shader, "precision highp float;"+ code);
		gl.glCompileShader(shader);
		return shader;
	}
	public static int createProgram(int... shaders)
	{
		int program = gl.glCreateProgram();
		for (int shader : shaders)
			gl.glAttachShader(program, shader);
		gl.glLinkProgram(program);
		return program;
	}
	public static void vbo(int handle, byte[] values)
	{
		gl.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, handle);
		gl.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, values.length, Utils.toByteBuffer(values), GLES30.GL_STATIC_DRAW);
		gl.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	public static void vbo(int handle, float[] values)
	{
		gl.glBindBuffer(GLES30.GL_ARRAY_BUFFER, handle);
		gl.glBufferData(GLES30.GL_ARRAY_BUFFER, values.length * 4, Utils.toFloatBuffer(values), GLES30.GL_STATIC_DRAW);
		gl.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
	}
	public static void useVBO(int bufferHandle, int vertexHandle, int floatsPerVertex, int offset)
	{
		gl.glBindBuffer(GLES30.GL_ARRAY_BUFFER, bufferHandle);
		gl.glVertexAttribPointer(vertexHandle, floatsPerVertex, GLES30.GL_FLOAT, false, 0, offset);
		gl.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
	}
	public static void draw(int type, int count, int bufferHandle)
	{
		gl.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, bufferHandle);
		gl.glDrawElements(type, count, GLES30.GL_UNSIGNED_BYTE, 0);
		gl.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	public static void draw(int type, int count)
	{
		draw(type, count, indexBufferHandle);
	}
	public static void setup()
	{
		buffers = new int[64]; bufferCount = 0;
		gl.glClearColor(0.6f, 0.6f, 1.0f, 1.0f);
		gl.glEnable(GLES20.GL_DEPTH_TEST);
		gl.glEnable(GLES20.GL_CULL_FACE);
		gl.glCullFace(GLES20.GL_BACK);
		gl.glEnable(GLES20.GL_BLEND);
		gl.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		indexBufferHandle = genBuffers(1)[0];
		byte[] indices = new byte[255];
		for (int i = 0; i < 255; i++)
			indices[i] = (byte)i;
		vbo(indexBufferHandle, indices);
	}
	public static void changeSurface(int w, int h)
	{
		screen = new Vec2(w, h); ratio = ((float)w/h);
		gl.glViewport(0,0,w,h);
		
		Mat projection, view;
		
		projection = Mat.frustum(-ratio,ratio, -1,1, 3,renderDistance);
		view = Mat.lookAt(new Vec(0,0,0), new Vec(0,0,-1), new Vec(0,1,0));
		vpMat = Mat.multiply(projection, view);
		
		projection = Mat.frustum(-ratio,ratio, 1,-1, 3,7);
		view = Mat.lookAt(new Vec(0,0,3), new Vec(0,0,-1), new Vec(0,1,0));
		uiMat = Mat.multiply(projection, view);
		
		Game.init();
	}
	public static void startDrawing()
	{
		gl.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GLES20.GL_DEPTH_TEST);
	}
	public static int[] genBuffers(int count)
	{
		int[] ret = new int[count];
		gl.glGenBuffers(count, ret,0);
		System.arraycopy(ret,0, buffers,bufferCount, count);
		bufferCount += count;
		return ret;
	}
	public static void stop()
	{
		if (bufferCount > 0)
			gl.glDeleteBuffers(bufferCount, buffers,0);
	}
}
