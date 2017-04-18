package com.M101M.Utils;
import android.opengl.Matrix;

public class Mat
{
	final float[] content = new float[16];
	
	public Mat()
	{}
	public Mat(float[] content, int offset)
	{
		System.arraycopy(content,offset, this.content,0, 16);
	}
	public Mat(Mat m)
	{
		this(m.toArray(),0);
	}

	public float[] toArray()
	{
		return content;
	}
	
	public Mat multiply(Mat m)
	{
		float[] temp = new float[16];
		Matrix.multiplyMM(temp,0, toArray(),0, m.toArray(),0);
		System.arraycopy(temp,0, content,0, 16);
		return this;
	}
	public Vec multiply(Vec v)
	{
		float[] ret = new float[4];
		Matrix.multiplyMV(ret,0, toArray(),0, v.toArray(),0);
		return new Vec(ret);
	}
	public Mat rotate(Vec by)
	{
		return rotate(by.x, by.y, by.z);
	}
	public Mat rotate(float x, float y, float z)
	{
		Matrix.rotateM(content,0, x, 1,0,0);
		Matrix.rotateM(content,0, y, 0,1,0);
		Matrix.rotateM(content,0, z, 0,0,1);
		return this;
	}
	public Mat scale(Vec v)
	{
		return scale(v.x,v.y,v.z);
	}
	public Mat scale(float x, float y, float z)
	{
		Matrix.scaleM(content,0, x,y,z);
		return this;
	}
	public Mat translate(Vec v)
	{
		return translate(v.x,v.y,v.z);
	}
	public Mat translate(float x, float y, float z)
	{
		Matrix.translateM(content,0, x,y,z);
		return this;
	}
	
	public static Mat multiply(Mat left, Mat right)
	{
		return new Mat(left).multiply(right);
	}
	
	public static Mat identity()
	{
		Mat ret = new Mat();
		Matrix.setIdentityM(ret.toArray(),0);
		return ret;
	}
	public static Mat lookAt(Vec eye, Vec center, Vec up)
	{
		Mat ret = new Mat();
		Matrix.setLookAtM(ret.toArray(),0, eye.x,eye.y,eye.z, center.x,center.y,center.z, up.x,up.y,up.z);
		return ret;
	}
	public static Mat frustum(float left, float right, float bottom, float top, float near, float far)
	{
		Mat ret = new Mat();
		Matrix.frustumM(ret.toArray(),0, left, right, bottom, top, near, far);
		return ret;
	}
}
