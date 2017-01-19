package com.M101M.Industria;

import android.content.*;
import android.opengl.*;
import android.view.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Industria.Utils.*;
import com.M101M.Industria.World.*;
import java.util.*;

public class OpenGLView extends GLSurfaceView
{
	MyRenderer rend;
	
	public OpenGLView (Context context)
	{
		super(context);
		setEGLContextClientVersion(3);
		rend = new MyRenderer();
		setRenderer(rend);

	}

	boolean dialogOpen = false;
	void display (final String message)
	{
		if (dialogOpen)
			return;
		dialogOpen = true;
		final MainActivity a = (MainActivity)getContext();
		a.runOnUiThread(new Runnable() {
				@Override public void run ()
				{
					new android.app.AlertDialog.Builder(a)
						.setTitle("debug")
						.setMessage(message)
						.setNegativeButton("ok", new android.content.DialogInterface.OnClickListener(){
							@Override public void onClick (android.content.DialogInterface p1, int p2)
							{ dialogOpen = false; }
						})
						.create().show();
				}
			});
	}
	void showError (Exception e, String method)
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

	Vec touchPoints[];
	@Override public boolean onTouchEvent (MotionEvent e)
	{
		int count = e.getPointerCount() - (e.getAction() == e.ACTION_UP ? 1 : 0);
		if (count == 0)
		{
			touchPoints = null;
			return true;
		}
		touchPoints = new Vec[count];
		for (int i=0; i < count; i++)
			touchPoints[i] = new Vec(
				e.getX(i) / (float)getWidth() * 2 - 1,
				e.getY(i) / (float)getHeight() * (-2) + 1,
				0);
		return true;
	}

	final float walkSpeed = 0.3f, lookSpeed = 2.5f;
	long lastUpdate = System.currentTimeMillis(), delta = 0;
	void PhysicUpdate ()
	{
		Vec[] touch = touchPoints;
		long dt = System.currentTimeMillis() - lastUpdate;
		lastUpdate += dt;
		delta += dt;
		int updates = 0;
		while (delta >= 1000 / Game.tps && ++updates < 10)
		{
			Game.update();
			delta -= 1000 / Game.tps;
		}

		Vec dp = new Vec(), dr = new Vec();
		if (touch == null)
			return;
		boolean chg = false;
		for (Vec t : touch)
		{
			if (Math.abs(t.x) > 0.7f) // right
			{
				if (Math.abs(t.y) < 0.3) dr.y += -Math.signum(t.x);
				else										 dp.x += Math.signum(t.x);
				chg = true;
			}
			if (Math.abs(t.y) > 0.7f) // front
			{
				if (Math.abs(t.x) < 0.3) dr.x += Math.signum(t.y);
				else										 dp.z += -Math.signum(t.y);
				chg = true;
			}
		}
		if (chg)
		{
			Game.player.pos.move(dp.scale(walkSpeed), new Vec(0, -Game.player.rot.y, 0)).add(new Vec(200, 0, 200)).modulo(400).add(new Vec(-200, 0, -200));
			Game.player.rot.add(dr.scale(lookSpeed));
			Game.player.rot.x = Math.min(70, Math.max(-70, Game.player.rot.x));
			Game.player.rot.y = (Game.player.rot.y + 360) % 360;
			return;
		}

		float[] tpos = new float[]{touch[0].x * GLM.w2,touch[0].y * GLM.h2,-3,0}, out = new float[4];
		Matrix.setIdentityM(GLM.transMat, 0);
		Matrix.rotateM(GLM.transMat, 0, Game.player.rot.y, 0, 1, 0);
		Matrix.rotateM(GLM.transMat, 0, Game.player.rot.x, 1, 0, 0);
		Matrix.multiplyMV(out, 0, GLM.transMat, 0, tpos, 0);
		Vec dir = new Vec(out);
		Veci target = Utils.rayHit(Game.player.pos, dir);
		if (target == null)
			Game.player.selected = null;
		else if (target.y == -1)
		{
			Game.map.set(new Block(target.add(0, 1, 0), Type.cable));
			Game.updates.add(target);
			//Game.player.selected = new Block(target, Type.mud);
		}
		else
			Game.player.selected = Game.map.getBlock(target);
	}

	public class MyRenderer implements Renderer
	{
		@Override
		public void onSurfaceCreated (javax.microedition.khronos.opengles.GL10 unused, javax.microedition.khronos.egl.EGLConfig config)
		{
			try
			{ GLM.setup(); }
			catch (Exception e)
			{ showError(e, "onSurfaceCreated"); }
		}

		@Override
		public void onDrawFrame (javax.microedition.khronos.opengles.GL10 unused)
		{
			try
			{
				Game.time = (Game.time + 1) % 10000;

				PhysicUpdate();

				GLM.startDrawing();

				Game.ui.draw();

				GLM.rotate(Vec.negative(Game.player.rot));
				GLM.translate(Vec.negative(Game.player.pos));

				Game.map.draw();
				Game.ground.draw();

			}
			catch (Exception e)
			{ showError(e, "onDrawFrame"); }
		}

		@Override
		public void onSurfaceChanged (javax.microedition.khronos.opengles.GL10 unused, int width, int height)
		{
			try
			{ GLM.changeSurface(width, height); }
			catch (Exception e)
			{ showError(e, "onSurfaceChanged"); }
		}
	}
}
