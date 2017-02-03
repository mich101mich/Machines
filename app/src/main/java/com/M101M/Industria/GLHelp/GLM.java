package com.M101M.Industria.GLHelp;

import android.opengl.*;
import com.M101M.Industria.*;
import com.M101M.Industria.Utils.*;

public class GLM
{
	public static final float[] mvpMat = new float[16], transMat = new float[16], viewMat = new float[16], uiMat = new float[16];
	public static int buffers[] = new int[64], bufferCount = 0, renderDistance = 30;
	public static Vec2 screen;
	public static float ratio;
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
	public static void setup()
	{
		buffers = new int[64]; bufferCount = 0;
		gl.glClearColor(0.6f, 0.6f, 1.0f, 1.0f);
		gl.glEnable(GLES20.GL_DEPTH_TEST);
		gl.glEnable(GLES20.GL_CULL_FACE);
		gl.glCullFace(GLES20.GL_BACK);
		gl.glEnable(GLES20.GL_BLEND);
		gl.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	}
	public static void changeSurface(int w, int h)
	{
		screen = new Vec2(w, h); ratio = ((float)w/h);
		gl.glViewport(0,0,w,h);
		float[] lookMat = new float[16], projMat = new float[16];
		Matrix.frustumM(projMat,0, -ratio,ratio, -1,1, 3,renderDistance);
		Matrix.setLookAtM(lookMat,0, 0,0,0, 0,0,-1, 0,1,0);
		Matrix.multiplyMM(viewMat,0, projMat,0, lookMat,0);
		
		lookMat = new float[16]; projMat = new float[16];
		Matrix.frustumM(projMat,0, -ratio,ratio, 1,-1, 3,7);
		Matrix.setLookAtM(lookMat,0, 0,0,3, 0,0,0, 0,1,0);
		Matrix.multiplyMM(uiMat,0, projMat,0, lookMat,0);
		
		Game.init();
	}
	public static void startDrawing()
	{
		gl.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		gl.glEnable(GLES20.GL_DEPTH_TEST);
		System.arraycopy(viewMat,0, mvpMat,0, 16);
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
