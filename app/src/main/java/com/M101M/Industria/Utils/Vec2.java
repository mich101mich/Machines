package com.M101M.Industria.Utils;

public class Vec2
{
	public float x, y;
	public Vec2(float X, float Y)
	{
		x = X;
		y = Y;
	}
	public Vec2()
	{ this(0, 0); }
	public Vec2(Vec2 src)
	{ this(src.x, src.y); }

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (obj instanceof Vec2)
			return x == ((Vec2)obj).x && y == ((Vec2)obj).y;
		if (obj instanceof Vec)
			return x == ((Vec)obj).x && y == ((Vec)obj).y;
		if (obj instanceof Veci)
			return x == ((Veci)obj).x && y == ((Veci)obj).y;
		return false;
	}
	
	public Vec2 add(Vec2 r)
	{ x += r.x; y += r.y; return this;}
	public Vec2 add(float x, float y)
	{ return add(new Vec2(x,y)); }
	public Vec2 sub(Vec2 r)
	{ return add(negative(r)); }
	public Vec2 sub(float x, float y)
	{ return sub(new Vec2(x,y)); }
	public Vec2 scale(float factor)
	{ x *= factor; y *= factor; return this; }
	public float length()
	{ return (float)Math.sqrt(x*x + y*y); }
	public Vec2 unit()
	{ return scale(1.0f / length()); }
	
	public static Vec2 plus(Vec2 l, Vec2 r)
	{ return new Vec2(l).add(r); }
	public static Vec2 minus(Vec2 l, Vec2 r)
	{ return new Vec2(l).sub(r); }
	public static Vec2 plus(Vec2 l, float xr, float yr)
	{ return new Vec2(l).add(xr,yr); }
	public static Vec2 minus(Vec2 l, float xr, float yr)
	{ return new Vec2(l).sub(xr,yr); }
	public static Vec2 negative(Vec2 v)
	{ return new Vec2(-v.x, -v.y); }
}
