package com.M101M.Industria.Utils;

public class Veci
{
	public int x,y,z;
	public Veci()
	{ x=0;y=0;z=0; }
	public Veci(int... xyz)
	{ x=xyz[0];y=xyz[1];z=xyz[2]; }
	public Veci(Veci v)
	{ x=v.x;y=v.y;z=v.z; }
	public Veci(Vec v)
	{ x=(int)Math.floor(v.x);y=(int)Math.floor(v.y);z=(int)Math.floor(v.z); }
	public Veci(Vec2 v)
	{ x=(int)Math.floor(v.x);y=(int)Math.floor(v.y);z=0; }
	public int[] toArray()
	{ return new int[]{x,y,z,0};}
	public Veci add(Veci o)
	{ return this.add(o.x, o.y, o.z); }
	public Veci add(int... xyz)
	{ x+=xyz[0];y+=xyz[1];z+=xyz[2]; return this; }
	public Veci scale(float by)
	{ x*=by; y*=by; z*=by; return this; }
	public Veci move(int dir)
	{
		switch(dir)
		{
		case 0: return this.add(0,0,1);
		case 1: return this.add(1,0,0);
		case 2: return this.add(0,0,-1);
		case 3: return this.add(-1,0,0);
		case 4: return this.add(0,-1,0);
		case 5: return this.add(0,1,0);
		default: return this;
		}
	}
	public static int opposite(int dir)
	{
		switch(dir)
		{
			case 0: return 2;
			case 1: return 3;
			case 2: return 0;
			case 3: return 1;
			case 4: return 5;
			case 5: return 6;
			default: return dir;
		}
	}
	public float length()
	{ return (float)Math.sqrt(x*x + y*y + z*z); }
	public boolean inRangeTo(Veci o, float distance)
	{
		Veci d = minus(this,o);
		return d.x*d.x + d.y*d.y + d.z*d.z < distance*distance;
	}
	public Veci modulo(int m)
	{ x = (x + m) % m; y = (y + m) % m; z = (z + m) % m; return this;}
	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (obj instanceof Vec)
			return x == ((Vec)obj).x && y == ((Vec)obj).y && z == ((Vec)obj).z;
		if (obj instanceof Veci)
			return x == ((Veci)obj).x && y == ((Veci)obj).y && z == ((Veci)obj).z;
		if (obj instanceof Vec2)
			return x == ((Vec2)obj).x && y == ((Vec2)obj).y;
		return false;
	}
	@Override
	public int hashCode()
	{
		return x*y*z + x + y + z + x*y + y*z + z*x;
	}
	
	public static Veci minus(Veci l,Veci r)
	{ return new Veci(l.x-r.x, l.y-r.y, l.z-r.z); }
	public static Veci plus(Veci l,Veci r)
	{ return new Veci(l.x+r.x, l.y+r.y, l.z+r.z); }
	public static Veci negative(Veci v)
	{ return new Veci(-v.x,-v.y,-v.z); }
	public static Veci inverted(Veci v)
	{ return new Veci(v.z,v.y,v.x); }
	public static boolean equals(Veci l,Veci r)
	{ return l.x==r.x && l.y==r.y && l.z==r.z; }
	public static Veci move(Veci v, int dir)
	{ return new Veci(v).move(dir); }
}
