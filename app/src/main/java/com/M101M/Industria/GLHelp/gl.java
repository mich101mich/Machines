package com.M101M.Industria.GLHelp;

import android.opengl.GLES20;
import java.nio.Buffer;
import android.opengl.GLU;
import java.nio.*;

public class gl
{
	public static void glDisable(int flag)
	{
		GLES20.glDisable(flag);
		debug("glDisable", flag);
	}

	public static void glVertexAttribPointer(int handle, int size, int dataType, boolean normalized, int stride, Buffer buffer)
	{
		GLES20.glVertexAttribPointer(handle, size, dataType, normalized, stride, buffer);
		debug("glVertexAttribPointer", handle, size, dataType, normalized, stride, buffer);
	}
	public static void glDeleteBuffers(int count, int[] buffers, int offset)
	{
		GLES20.glDeleteBuffers(count, buffers, offset);
		debug("glDeleteBuffers", count, buffers, offset);
	}
	public static void glGenBuffers(int count, int[] output, int offset)
	{
		GLES20.glGenBuffers(count, output, offset);
		debug("glGenBuffers", count, output, offset);
	}
	public static void glClear(int bits)
	{
		GLES20.glClear(bits);
		debug("glClear", bits);
	}
	public static void glViewport(int x, int y, int width, int height)
	{
		GLES20.glViewport(x,y,width,height);
		debug("glViewport", x,y,width,height);
	}
	public static void glBlendFunc(int base, int action)
	{
		GLES20.glBlendFunc(base, action);
		debug("glBlendFunc", base, action);
	}
	public static void glCullFace(int face)
	{
		GLES20.glCullFace(face);
		debug("glCullFace", face);
	}
	public static void glEnable(int flag)
	{
		GLES20.glEnable(flag);
		debug("glEnable", flag);
	}
	public static void glClearColor(float r, float g, float b, float a)
	{
		GLES20.glClearColor(r,g,b,a);
		debug("glClearColor", r,g,b,a);
	}
	public static void glLinkProgram(int program)
	{
		GLES20.glLinkProgram(program);
		debug("glLinkProgram", program);
	}
	public static void glAttachShader(int program, int shader)
	{
		GLES20.glAttachShader(program, shader);
		debug("glAttachShader", program, shader);
	}
	public static int glCreateProgram()
	{
		int ret = GLES20.glCreateProgram();
		debug("glCreateProgram");
		return ret;
	}
	public static void glCompileShader(int shader)
	{
		GLES20.glCompileShader(shader);
		String err = GLES20.glGetShaderInfoLog(shader);
		if (!err.isEmpty())
			throw new GLESException(err);
		debug("glCompileShader", shader);
	}
	public static void glShaderSource(int shader, String code)
	{
		GLES20.glShaderSource(shader, code);
		debug("glShaderSource", shader, code);
	}
	public static void glUniform1i(int handle, int data)
	{
		GLES20.glUniform1i(handle, data);
		debug("glUniform1i", handle, data);
	}
	public static void glDrawElements(int type, int count, int indexType, int offset)
	{
		GLES20.glDrawElements(type, count, indexType, offset);
		debug("glDrawElements", type, count, indexType, offset);
	}
	public static void glDrawElements(int type, int count, int indexType, Buffer buffer)
	{
		GLES20.glDrawElements(type, count, indexType, buffer);
		debug("glDrawElements", type, count, indexType, buffer);
	}
	public static void glVertexAttribPointer(int handle, int size, int dataType, boolean normalized, int stride, int offset)
	{
		GLES20.glVertexAttribPointer(handle, size, dataType, normalized, stride, offset);
		debug("glVertexAttribPointer", handle, size, dataType, normalized, stride, offset);
	}
	public static void glBindBuffer(int type, int handle)
	{
		GLES20.glBindBuffer(type, handle);
		debug("glBindBuffer", type, handle);
	}
	public static void glBufferData(int buffer, int byteCount, Buffer data, int drawType)
	{
		GLES20.glBufferData(buffer, byteCount, data, drawType);
		debug("glBufferData", buffer, byteCount, data, drawType);
	}
	public static int glCreateShader(int type)
	{
		int ret = GLES20.glCreateShader(type);
		debug("glCreateShader", type);
		return ret;
	}
	public static void glUniform4fv(int handle, int count, float[] data, int offset)
	{
		GLES20.glUniform4fv(handle, count, data, offset);
		debug("glUniform4fv", handle, count, data, offset);
	}
	public static void glUniformMatrix4fv(int handle, int count, boolean normalized, float[] data, int offset)
	{
		GLES20.glUniformMatrix4fv(handle, count, normalized, data, offset);
		debug("glUniformMatrix4fv", handle, count, normalized, data, offset);
	}
	public static int glGetAttribLocation(int program, String name)
	{
		int ret = GLES20.glGetAttribLocation(program, name);
		debug("glGetAttribLocation", program, name);
		return ret;
	}
	public static int glGetUniformLocation(int program, String name)
	{
		int ret = GLES20.glGetUniformLocation(program, name);
		debug("glGetUniformLocation", program, name);
		return ret;
	}
	public static void glUseProgram(int program)
	{
		GLES20.glUseProgram(program);
		debug("glUseProgram", program);
	}
	public static void glEnableVertexAttribArray(int handle)
	{
		GLES20.glEnableVertexAttribArray(handle);
		debug("glEnableVertexAttribArray", handle);
	}
	static void debug(String method, Object... params)
	{
		int err = GLES20.glGetError();
		if (err == 0)
			return;
		String message = GLU.gluErrorString(err) + "\nin " + method;
		for (Object o : params)
			message += "\n" + o;
		throw new GLESException(message);
	}
	static class GLESException extends RuntimeException
	{
		GLESException(String msg)
		{ super(msg); }
	}
}
