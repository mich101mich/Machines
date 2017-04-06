package com.M101M.Industria;

import android.content.*;
import android.opengl.*;
import android.view.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Industria.UI.*;
import com.M101M.Industria.Utils.*;
import com.M101M.Industria.World.*;
import java.util.*;

public class OpenGLView extends GLSurfaceView
{
	MyRenderer rend;

	public OpenGLView(Context context)
	{
		super(context);
		setEGLContextClientVersion(3);
		rend = new MyRenderer();
		setRenderer(rend);
	}

	boolean dialogOpen = false;
	void display(final String message)
	{
		if (dialogOpen)
			return;
		dialogOpen = true;
		final MainActivity a = (MainActivity)getContext();
		a.runOnUiThread(new Runnable() {
				@Override public void run()
				{
					new android.app.AlertDialog.Builder(a)
						.setTitle("debug")
						.setMessage(message)
						.setNegativeButton("ok", new android.content.DialogInterface.OnClickListener(){
							@Override public void onClick(android.content.DialogInterface p1, int p2)
							{ dialogOpen = false; }
						})
						.create().show();
				}
			});
	}
	void showError(Exception e, String method)
	{
		String[] className = e.getClass().getName().split("[$.]");
		String message = className[className.length - 1] + "\n" + e.getMessage() + "\n";
		for (StackTraceElement s : e.getStackTrace())
		{
			String[] c = s.getClassName().split("[.$]");
			message += "\nat l." + s.getLineNumber() + " in " + c[c.length - 1] + "." + s.getMethodName();
			if (s.getMethodName().contentEquals(method))
				break;
		}
		display(message);
	}

	MotionEvent event;
	Arr<TouchEvent> touch = new Arr<TouchEvent>(20);
	@Override public boolean onTouchEvent(MotionEvent e)
	{
		if (e.getAction() == e.ACTION_CANCEL)
			event = null;
		else
			event = e;
		return true;
	}
	void handleTouch()
	{
		if (event == null)
		{
			Iterator<TouchEvent> it = touch.iterator();
			while (it.hasNext())
			{
				if (!it.next().refresh(null, GLM.screen, GLM.ratio))
					it.remove();
			}
			return;
		}
		MotionEvent e = event;
		event = null;
		for (TouchEvent t : touch)
			t.handled = true;
		for (int i=0; i < e.getPointerCount(); i++)
		{
			if (e.getAction() == e.ACTION_UP ||
				(e.getActionMasked() == e.ACTION_POINTER_UP
				&& e.getActionIndex() == i))
				continue;
			final int id = e.getPointerId(i);
			if (touch.find(new Arr.Condition<TouchEvent>(){public boolean test(TouchEvent e)
						{
							return e.id() == id; }})
				== null)
				touch.add(new TouchEvent(e, i, GLM.screen, GLM.ratio));
		}
		Iterator<TouchEvent> it = touch.iterator();
		while (it.hasNext())
		{
			TouchEvent t = it.next();
			if (t.handled && !t.refresh(e, GLM.screen, GLM.ratio))
				it.remove();
		}
	}

	long lastUpdate = System.currentTimeMillis(), delta = 0;
	void PhysicUpdate()
	{
		handleTouch();

		for (TouchEvent t : touch)
			Game.ui.onTouch(t);

		long dt = System.currentTimeMillis() - lastUpdate;
		lastUpdate += dt;
		delta += dt;
		int updates = 0;
		while (delta >= 1000 / Game.tps && ++updates < 10)
		{
			Game.update();
			delta -= 1000 / Game.tps;
		}

		TouchEvent t = touch.find(new Arr.Condition<TouchEvent>() {public
				boolean test(TouchEvent e) {
					return !e.handled;
				}});
		if (t == null)
			return;
		
		Vec tpos = new Vec(t.x(),-t.y(),-3);
		Vec dir = Mat.identity()
			.rotate(0, Game.player.rot.y, 0)
			.rotate(Game.player.rot.x, 0, 0)
			.multiply(tpos);
		
		Veci target = Utils.rayHit(Game.player.pos, dir);
		if (target == null)
			Game.player.selected = null;
		else
		{
			switch (Game.player.action)
			{
			case Player.SELECT:
				Game.player.selected = Game.map.getBlock(target);
				break;
			case Player.PLACE:
				if (t.type() != t.DOWN)
					break;
				target = Utils.rayHitEnd(Game.player.pos, dir);
				Game.map.set(new Block(target, Game.player.placeType));
				break;
			case Player.DELETE:
				if (t.type() != t.DOWN)
					break;
				Game.map.remove(target);
				break;
			}
		}
	}

	public class MyRenderer implements Renderer
	{
		@Override
		public void onSurfaceCreated(javax.microedition.khronos.opengles.GL10 unused, javax.microedition.khronos.egl.EGLConfig config)
		{
			try
			{ GLM.setup(); }
			catch (Exception e)
			{ showError(e, "onSurfaceCreated"); }
		}

		@Override
		public void onDrawFrame(javax.microedition.khronos.opengles.GL10 unused)
		{
			try
			{
				Game.time = (Game.time + 1) % 10000;

				Game.ui.update();
				PhysicUpdate();

				GLM.startDrawing();

				Block.startDrawing();
				Game.map.draw();

				Plane.startDrawing();
				Game.ground.draw();

				UI.startDrawing();
				Game.ui.draw();
			}
			catch (Exception e)
			{ showError(e, "onDrawFrame"); }
		}

		@Override
		public void onSurfaceChanged(javax.microedition.khronos.opengles.GL10 unused, int width, int height)
		{
			try
			{ GLM.changeSurface(width, height); }
			catch (Exception e)
			{ showError(e, "onSurfaceChanged"); }
		}
	}
}
