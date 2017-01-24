package com.M101M.Industria.GLHelp;

import android.opengl.GLES31;
import java.nio.Buffer;
import android.opengl.GLU;

public class gl
{
	public static void glDeleteBuffers(int count, int[] buffers, int offset)
	{
		GLES31.glDeleteBuffers(count, buffers, offset);
		debug("glDeleteBuffers", count, buffers, offset);
	}
	public static void glGenBuffers(int count, int[] output, int offset)
	{
		GLES31.glGenBuffers(count, output, offset);
		debug("glGenBuffers", count, output, offset);
	}
	public static void glClear(int bits)
	{
		GLES31.glClear(bits);
		debug("glClear", bits);
	}
	public static void glViewport(int x, int y, int width, int height)
	{
		GLES31.glViewport(x,y,width,height);
		debug("glViewport", x,y,width,height);
	}
	public static void glBlendFunc(int base, int action)
	{
		GLES31.glBlendFunc(base, action);
		debug("glBlendFunc", base, action);
	}
	public static void glCullFace(int face)
	{
		GLES31.glCullFace(face);
		debug("glCullFace", face);
	}
	public static void glEnable(int flag)
	{
		GLES31.glEnable(flag);
		debug("glEnable", flag);
	}
	public static void glClearColor(float r, float g, float b, float a)
	{
		GLES31.glClearColor(r,g,b,a);
		debug("glClearColor", r,g,b,a);
	}
	public static void glLinkProgram(int program)
	{
		GLES31.glLinkProgram(program);
		debug("glLinkProgram", program);
	}
	public static void glAttachShader(int program, int shader)
	{
		GLES31.glAttachShader(program, shader);
		debug("glAttachShader", program, shader);
	}
	public static int glCreateProgram()
	{
		int ret = GLES31.glCreateProgram();
		debug("glCreateProgram");
		return ret;
	}
	public static void glCompileShader(int shader)
	{
		GLES31.glCompileShader(shader);
		String err = GLES31.glGetShaderInfoLog(shader);
		if (!err.isEmpty())
			throw new GLESException(err);
		debug("glCompileShader", shader);
	}
	public static void glShaderSource(int shader, String code)
	{
		GLES31.glShaderSource(shader, code);
		debug("glShaderSource", shader, code);
	}
	public static void glUniform1i(int handle, int data)
	{
		GLES31.glUniform1i(handle, data);
		debug("glUniform1i", handle, data);
	}
	public static void glDrawElements(int type, int count, int indexType, int offset)
	{
		GLES31.glDrawElements(type, count, indexType, offset);
		debug("glDrawElements", type, count, indexType, offset);
	}
	public static void glVertexAttribPointer(int handle, int size, int dataType, boolean normalized, int stride, int offset)
	{
		GLES31.glVertexAttribPointer(handle, size, dataType, normalized, stride, offset);
		debug("glVertexAttribPointer", handle, size, dataType, normalized, stride, offset);
	}
	public static void glBindBuffer(int type, int handle)
	{
		GLES31.glBindBuffer(type, handle);
		debug("glBindBuffer", type, handle);
	}
	public static void glBufferData(int buffer, int byteCount, Buffer data, int drawType)
	{
		GLES31.glBufferData(buffer, byteCount, data, drawType);
		debug("glBufferData", buffer, byteCount, data, drawType);
	}
	public static int glCreateShader(int type)
	{
		int ret = GLES31.glCreateShader(type);
		debug("glCreateShader", type);
		return ret;
	}
	public static void glUniform4fv(int handle, int p1, float[] data, int offset)
	{
		GLES31.glUniform4fv(handle, p1, data, offset);
		debug("glUniform4fv", handle, p1, data, offset);
	}
	public static void glUniformMatrix4fv(int handle, int p1, boolean p2, float[] data, int offset)
	{
		GLES31.glUniformMatrix4fv(handle, p1, p2, data, offset);
		debug("glUniformMatrix4fv", handle, p1, p2, data, offset);
	}
	public static int glGetAttribLocation(int program, String name)
	{
		int ret = GLES31.glGetAttribLocation(program, name);
		debug("glGetAttribLocation", program, name);
		return ret;
	}
	public static int glGetUniformLocation(int program, String name)
	{
		int ret = GLES31.glGetUniformLocation(program, name);
		debug("glGetUniformLocation", program, name);
		return ret;
	}
	public static void glUseProgram(int program)
	{
		GLES31.glUseProgram(program);
		debug("glUseProgram", program);
	}
	public static void glEnableVertexAttribArray(int handle)
	{
		GLES31.glEnableVertexAttribArray(handle);
		debug("glEnableVertexAttribArray", handle);
	}
	static void debug(String method, Object... params)
	{
		int err = GLES31.glGetError();
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