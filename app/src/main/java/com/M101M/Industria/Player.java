package com.M101M.Industria;
import com.M101M.Industria.Utils.*;
import com.M101M.Industria.World.*;

public class Player
{
	public Vec pos = new Vec(0,8,3), rot = new Vec(-40,0,0);
	public Block selected;
	public int placeType = 0;
	public int action;
	public static final int SELECT=0, PLACE=1, PLACE_DRAG=2, DELETE=3, DELETE_DRAG=4;
}
