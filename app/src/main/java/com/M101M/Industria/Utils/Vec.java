package com.M101M.Industria.Utils;

import android.opengl.Matrix;

public class Vec
{
	public float x=0,y=0,z=0;
	public Vec(){}
	public Vec(float... xyz)
	{ x=xyz[0];y=xyz[1];z=xyz[2]; }
	public Vec(Vec v)
	{ x=v.x;y=v.y;z=v.z; }
	public Vec(Veci v)
	{ x=v.x;y=v.y;z=v.z; }
	public Vec(Vec2 v)
	{ x=v.x;y=v.y;z=0; }
	public float[] toArray()
	{ return new float[]{x,y,z,0};}
	public Vec add(Vec o)
	{ return add(o.x,o.y,o.z); }
	public Vec add(float... xyz)
	{ x+=xyz[0]; y+=xyz[1]; z+=xyz[2]; return this; }
	public Vec scale(float by)
	{ x*=by; y*=by; z*=by; return this; }
	public Vec move(Vec by,Vec dir)
	{
		float[] mat = new float[16];
		float[] c = new float[4];
		Utils.setRotation(mat,0,Vec.negative(dir));
		Matrix.multiplyMV(c,0, mat,0, by.toArray(),0);
		add(new Vec(c));
		return this;
	}
	public float length()
	{ return (float)Math.sqrt(x*x + y*y + z*z); }
	public boolean inRangeTo(Vec o, float distance)
	{
		Vec d = minus(this,o);
		return d.x*d.x + d.y*d.y + d.z*d.z < distance*distance;
	}
	public boolean inRangeTo(Veci o, float distance)
	{ return inRangeTo(new Vec(o), distance); }
	public Vec modulo(float m)
	{ x=(x+m)%m; y=(y+m)%m; z=(z+m)%m; return this;}
	public Vec unit()
	{ return this.scale(1 / this.length()); }
	public static Vec minus(Vec l,Vec r)
	{ return negative(r).add(l); }
	public static Vec plus(Vec l,Vec r)
	{ return new Vec(l).add(r); }
	public static Vec negative(Vec v)
	{ return new Vec(-v.x,-v.y,-v.z); }
	public static Vec inverted(Vec v)
	{ return new Vec(v.z,v.y,v.x); }
	public static boolean equals(Vec l,Vec r)
	{ return l.x==r.x && l.y==r.y && l.z==r.z; }
	public static Vec unit(Vec v)
	{ return new Vec(v).unit(); }

	public static Vec minus(Veci l,Vec r)
	{ return minus(new Vec(l), r); }
	public static Vec minus(Vec l,Veci r)
	{ return minus(l, new Vec(r)); }
	public static Vec plus(Veci l,Vec r)
	{ return plus(new Vec(l), r); }
	public static Vec plus(Vec l,Veci r)
	{ return plus(l, new Vec(r)); }
	public static boolean equals(Veci l,Vec r)
	{ return equals(new Vec(l), r); }
	public static boolean equals(Vec l,Veci r)
	{ return equals(l, new Vec(r)); }
	@Override public String toString()
	{ return "("+x+", "+y+", "+z+")"; }
}
