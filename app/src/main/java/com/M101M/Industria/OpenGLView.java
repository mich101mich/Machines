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

	MotionEvent event;
	Arr<TouchEvent> touch = new Arr<TouchEvent>(20);
	@Override public boolean onTouchEvent (MotionEvent e)
	{
		if (e.getAction() == event.ACTION_UP || e.getAction() == e.ACTION_CANCEL)
			event = null;
		else
			event = e;
		return true;
	}
	void handleTouch()
	{
		if (event == null)
		{
			touch.clear();
			return;
		}
		MotionEvent e = event;
		for (TouchEvent t : touch)
			t.handled = true;
		for (int i=0; i < e.getPointerCount(); i++)
		{
			if (e.getAction() == e.ACTION_POINTER_UP
				&& e.getActionIndex() == i)
				continue;
			final int id = e.getPointerId(i);
			if (touch.find(new Arr.Condition<TouchEvent>(){public boolean test(TouchEvent e){
					return e.id == id; }})
					== null)
				touch.add(new TouchEvent(e, i));
		}
		Iterator<TouchEvent> it = touch.iterator();
		while (it.hasNext())
		{
			TouchEvent t = it.next();
			if (t.handled && !t.refresh(e))
				it.remove();
			else
				t.pos = new Vec2(t.pos.x / (GLM.width/2.0f) - 1, 1 - t.pos.y / (GLM.height/2.0f));
		}
		
		for (TouchEvent t : touch)
			Game.ui.onTouch(t);
	}

	final float walkSpeed = 0.3f, lookSpeed = 2.5f;
	long lastUpdate = System.currentTimeMillis(), delta = 0;
	void PhysicUpdate ()
	{
		handleTouch();
		
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
		boolean chg = false;
		for (TouchEvent t : touch)
		{
			if (t.handled)
				continue;
			if (Math.abs(t.pos.x) > 0.7f) // right
			{
				if (Math.abs(t.pos.y) < 0.3) dr.y += -Math.signum(t.pos.x);
				else										 dp.x += Math.signum(t.pos.x);
				t.handled = chg = true;
			}
			if (Math.abs(t.pos.y) > 0.7f) // front
			{
				if (Math.abs(t.pos.x) < 0.3) dr.x += Math.signum(t.pos.y);
				else										 dp.z += -Math.signum(t.pos.y);
				t.handled = chg = true;
			}
		}
		if (chg)
		{
			Game.player.pos.move(dp.scale(walkSpeed), new Vec(0, -Game.player.rot.y, 0)).add(new Vec(200, 0, 200)).modulo(400).add(new Vec(-200, 0, -200));
			Game.player.rot.add(dr.scale(lookSpeed));
			Game.player.rot.x = Math.min(70, Math.max(-70, Game.player.rot.x));
			Game.player.rot.y = (Game.player.rot.y + 360) % 360;
		}

		TouchEvent t = touch.find(new Arr.Condition<TouchEvent>() {public boolean test(TouchEvent e) {
				return !e.handled;
			}});
		if (t == null)
			return;
		float[] tpos = new float[]{t.pos.x * GLM.w2,t.pos.y * GLM.h2,-3,0}, out = new float[4];
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

				GLM.rotate(Vec.negative(Game.player.rot));
				GLM.translate(Vec.negative(Game.player.pos));

				gl.glEnable(GLES20.GL_DEPTH_TEST);
				
				Block.startDrawing();
				Game.map.draw();
				
				Plane.startDrawing();
				Game.ground.draw();
				
				gl.glDisable(GLES20.GL_DEPTH_TEST);
				gl.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
				gl.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
				
				Game.ui.draw();
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
