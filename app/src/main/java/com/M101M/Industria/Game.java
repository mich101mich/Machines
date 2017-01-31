package com.M101M.Industria;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Industria.UI.*;
import com.M101M.Industria.Utils.*;
import com.M101M.Industria.World.*;

public class Game
{
	public static int time, tps=10;
	public static Map map;
	public static Player player;
	public static Plane ground;
	public static UI ui;
	public static Arr<Veci> updates;

	public static void init()
	{
		time = 0;
		Shader.init();
		map = new Map();
		player = new Player();
		updates = new Arr<Veci>();
		ui = new UI();
		Block.globalInit();
		Plane.globalInit();
		Rectangle.globalInit();

		ground = new Plane(new Vec(0,-0.01f,0));

		for (int i=1; i<10; i++)
			map.set(new Block(i,0,0, Type.mud));
		for (int i=1; i<8; i++)
			map.set(new Block(0,i,0, Type.grass));
		for (int i=1; i<10; i++)
			map.set(new Block(0,0,i, Type.stone));
		for (int i=0; i<5; i++)
			for (int j=0; j<10; j++)
				map.set(new Block(-i,0,-j, i));
				
		ui.add(new Rectangle(0,0,10,10,0xffffff));
	}
	public static void stop()
	{
		GLM.stop();
		map = null;
		player = null;
		ground = null;
		updates = null;
	}
	public static void update()
	{
		Arr<Veci> old = updates;
		updates = new Arr<Veci>(old.max);
		for (Veci v : old)
		{
			Block b = map.getBlock(v);
			if (b != null)
				b.update();
		}
	}
}
