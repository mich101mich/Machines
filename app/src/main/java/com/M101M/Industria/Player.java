package com.M101M.Industria;
import com.M101M.Industria.Utils.*;
import com.M101M.Industria.World.*;

public class Player
{
	public Vec pos = new Vec(0,8,3), rot = new Vec(-40,0,0);
	public Block selected;
	public int placeType = 0;
	public int action;
	public static final float walkSpeed = 0.35f, lookSpeed = 1.2f;
	public static final int SELECT=0, PLACE=1, DELETE=2;
}
