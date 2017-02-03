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
}
