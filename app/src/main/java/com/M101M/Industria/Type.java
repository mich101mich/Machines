package com.M101M.Industria;

public class Type
{
	public static final int grass=0, stone=1, mud=2, cable=3, cablePow=4, fan=5, fanPow=6, drill=7, drillPow=8, genSol=9, genSolPow=10;
	public static final String[] names;
	public static final boolean[] placeable, conducts, hasPower;
	public static final int category[], OTHER = 0, SOURCE = 1, MACHINE = 2;
	public static final int count;
	
	static {
		count = 11;
		names = new String[count];
		placeable = new boolean[count];
		conducts = new boolean[count];
		hasPower = new boolean[count];
		category = new int[count];
		
		describe(grass); name("Grass");
		placeable();
		
		describe(stone); name("Stone");
		placeable();
		
		describe(mud); name("Mud");
		placeable();
		
		describe(cable); name("Cable");
		placeable();
		conducts();
		
		describe(cablePow); name("Powered Cable");
		hasPower();
		
		describe(fan); name("Fan");
		placeable();
		machine();
		
		describe(fanPow); name("Fan");
		machine();
		hasPower();
		
		describe(drill); name("Drill");
		placeable();
		machine();
		
		describe(drillPow); name("Drill");
		machine();
		hasPower();
		
		describe(genSol); name("Solar Panel");
		placeable();
		source();
		
		describe(genSolPow); name("Solar Panel");
		source();
		hasPower();
	}
	
	private static int describing;
	private static void describe(int type)
	{
		describing = type;
	}
	private static void name(String name)
	{
		names[describing] = name;
	}
	private static void placeable()
	{
		placeable[describing] = true;
	}
	private static void conducts()
	{
		conducts[describing] = true;
	}
	private static void source()
	{
		category[describing] = SOURCE;
		conducts();
	}
	private static void machine()
	{
		category[describing] = MACHINE;
		conducts();
	}
	private static void hasPower()
	{
		hasPower[describing] = true;
		conducts();
	}
}
