package com.M101M.Industria.GLHelp;

import android.opengl.*;
import com.M101M.Industria.*;
import com.M101M.Industria.Utils.*;

public class GLM
{
	public static int drawing;
	public static final int BLOCK = 1, PLANE = 2, BUTTON = 3, TEXT = 4;
	public static final float[] mvpMat = new float[16], transMat = new float[16], viewMat = new float[16];
	public static int buffers[] = new int[64], bufferCount = 0, renderDistance = 30, width, height;
	public static float w2, h2 = 1.0f;
	public static void rotate(Vec angle)
	{
		Utils.setRotation(transMat,0, angle);
		Utils.multiply(mvpMat,0, transMat,0);
	}
	public static void translate(Vec delta)
	{
		Matrix.setIdentityM(transMat,0);
		Matrix.translateM(transMat,0,delta.x,delta.y,delta.z);
		Utils.multiply(mvpMat,0, transMat,0);
	}
	public static int loadShader(int type, String... lines) throws Exception
	{
		String code = Utils.join(lines, "\n");
		int shader = gl.glCreateShader(type);
		gl.glShaderSource(shader, "precision highp float;"+ code);
		gl.glCompileShader(shader);
		return shader;
	}
	public static int createProgram(int... shaders) throws Exception
	{
		int program = gl.glCreateProgram();
		for (int shader : shaders)
			gl.glAttachShader(program, shader);
		gl.glLinkProgram(program);
		return program;
	}
	public static void setup() throws Exception
	{
		buffers = new int[64]; bufferCount = 0;
		gl.glClearColor(0.6f, 0.6f, 1.0f, 1.0f);
		gl.glEnable(GLES20.GL_DEPTH_TEST);
		gl.glEnable(GLES20.GL_CULL_FACE);
		gl.glCullFace(GLES20.GL_BACK);
		gl.glEnable(GLES20.GL_BLEND);
		gl.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		Game.init();
	}
	public static void changeSurface(int w, int h) throws Exception
	{
		width = w; height = h; w2 = (float)w/h;
		gl.glViewport(0,0,width,height);
		float[] lookMat = new float[16], projMat = new float[16];
		Matrix.frustumM(projMat,0, -w2,w2,-h2,h2,3,renderDistance);
		Matrix.setLookAtM(lookMat,0, 0,0,0, 0,0,-1, 0,1,0);
		Matrix.multiplyMM(viewMat,0, projMat,0, lookMat,0);
	}
	public static void startDrawing() throws Exception
	{
		gl.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		System.arraycopy(viewMat,0, mvpMat,0, 16);
		drawing = -1;
	}
	public static int[] genBuffers(int count) throws Exception
	{
		int[] ret = new int[count];
		gl.glGenBuffers(count, ret,0);
		System.arraycopy(ret,0, buffers,bufferCount, count);
		bufferCount += count;
		return ret;
	}
	public static void stop() throws Exception
	{
		if (bufferCount > 0)
			gl.glDeleteBuffers(bufferCount, buffers,0);
	}
}
