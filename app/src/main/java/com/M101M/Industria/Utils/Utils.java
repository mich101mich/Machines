package com.M101M.Industria.Utils;

import android.opengl.*;
import com.M101M.Industria.*;
import com.M101M.Industria.World.*;
import java.nio.*;

public class Utils
{
	public static ByteBuffer toByteBuffer(byte[] arr)
	{
		ByteBuffer ret = ByteBuffer.allocateDirect(arr.length);
		ret.order(ByteOrder.nativeOrder());
		ret.put(arr);
		ret.position(0);
		return ret;
	}
	public static FloatBuffer toFloatBuffer(float[] arr)
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
		bb.order(ByteOrder.nativeOrder());
		FloatBuffer ret = bb.asFloatBuffer();
		ret.put(arr);
		ret.position(0);
		return ret;
	}
	public static IntBuffer toIntBuffer(int[] arr)
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
		bb.order(ByteOrder.nativeOrder());
		IntBuffer ret = bb.asIntBuffer();
		ret.put(arr);
		ret.position(0);
		return ret;
	}
	public static ShortBuffer toShortBuffer(short[] arr)
	{
		ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 2);
		bb.order(ByteOrder.nativeOrder());
		ShortBuffer ret = bb.asShortBuffer();
		ret.put(arr);
		ret.position(0);
		return ret;
	}
	public static float[] subArray(float[] src, int... indices)
	{
		float[] ret = new float[indices.length];
		for (int i=0; i<ret.length; i++)
			ret[i] = src[indices[i]];
		return ret;
	}
	public static Object[] subArray(Object[] src, int... indices)
	{
		Object[] ret = new Object[indices.length];
		for (int i=0; i<ret.length; i++)
			ret[i] = src[indices[i]];
		return ret;
	}
	public static float[] hexToArray(int color)
	{
		return new float[]{ ((color >> 16)&255)/255.0f, ((color >> 8)&255)/255.0f, (color&255)/255.0f, 1.0f - ((color>>24)&255)/255.0f};
	}
	public static int ArrayToHex(float[] color)
	{
		return ((int)((1-color[3])*255) << 24) + ((int)(color[0]*255) << 16) + ((int)(color[1]*255) << 8) + (int)(color[2]*255);
	}
	public static float[] rotate(float x,float y, float angle)
	{
		angle = (float)Math.toRadians(angle);
		return new float[] {
			(float)(x*Math.cos(angle) + y*Math.sin(angle)),
			(float)(y*Math.cos(angle) - x*Math.sin(angle))
		};
	}
	public static void multiply(float[] left,int leftOffset, float[] right,int rightOffset)
	{
		float[] temp = new float[16];
		Matrix.multiplyMM(temp,0, left,leftOffset, right,rightOffset);
		System.arraycopy(temp,0, left,leftOffset, 16);
	}
	public static void setRotation(float[] m,int mOff, Vec angle)
	{
		Matrix.setIdentityM(m,mOff);
		Matrix.rotateM(m,mOff, angle.x, 1,0,0);
		Matrix.rotateM(m,mOff, angle.y, 0,1,0);
		Matrix.rotateM(m,mOff, angle.z, 0,0,1);
	}
	public static Veci rayHit(Vec start, Vec dir)
	{
		Vec p = new Vec(start);
		dir = Vec.unit(dir).scale(0.1f);
		Veci block = new Veci(p);
		for (int count = 0; count < 300; count++)
		{
			p.add(dir);
			if (!Veci.equals(new Veci(p), block))
			{
				block = new Veci(p);
				if (block.y < 0 || Game.map.getBlock(block) != null)
					return block;
			}
		}
		return null;
	}
	public static boolean find(Veci start, int[] targets, int[] walkable)
	{
		Veci[] path = new Veci[200], all = new Veci[200];
		int pathi = 0, alli = 0;
		Veci pos = new Veci(start);
		loop: while (pathi < 200 && alli < 200)
		{
			for(int i=0; i<6; i++)
			{
				Veci p = Veci.move(pos, i);
				Block b = Game.map.getBlock(p);
				int type = (b != null ? b.type : -1);
				if (contains(targets, type))
					return true;
				if (b == null ||  p.y < 0 || !contains(walkable, type) || contains(all, p))
					continue;
				path[pathi++] = pos; all[alli++] = pos;
				pos = p;
				continue loop;
			}
			if (pathi <= 0)
				return false;
			pos = path[--pathi];
		}
		return false;
	}
	public static boolean contains(Veci[] array, Veci x)
	{
		for (Veci o : array)
		{
			if (o == null)
				return false;
			if (Veci.equals(o, x))
				return true;
		}
		return false;
	}
	public static boolean contains(int[] array, int x)
	{
		for (int o : array)
			if (o == x)
				return true;
		return false;
	}
	public static String join(String[] arr, String ch)
	{
		String ret = arr[0];
		for (int i=1; i < arr.length; i++)
			ret += ch + arr[i];
		return ret;
	}
}
