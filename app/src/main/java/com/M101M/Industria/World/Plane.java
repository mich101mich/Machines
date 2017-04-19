package com.M101M.Industria.World;

import android.opengl.*;
import com.M101M.Industria.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Utils.*;

public class Plane
{
	Vec pos;
	private static int matHandle, posHandle, vertexHandle, bufferHandle;
	
	public Plane (Vec position)
	{
		pos = position;
	}

	public static void globalInit ()
	{
		Shader.use(Shader.PLANE);
		matHandle = Shader.getUniform("mvpMat");
		posHandle = Shader.getUniform("position");
		vertexHandle = Shader.getAttribute("vertex");

		bufferHandle = GLM.genBuffers(1)[0];
		GLM.vbo(bufferHandle, new float[]{-50,0,50, 50,0,50, -50,0,-50, 50,0,-50});
	}

	public static void startDrawing ()
	{
		Shader.use(Shader.PLANE);
		GLM.useVBO(bufferHandle, vertexHandle, 3, 0);
		
		Mat model = Mat.identity()
			.rotate(Vec.negative(Game.player.rot))
			.translate(Vec.negative(Game.player.pos));
		gl.glUniformMatrix4fv(matHandle, 1, false, Mat.multiply(GLM.vpMat, model).toArray(), 0);
	}
	public void draw ()
	{
		gl.glUniform4fv(posHandle, 1, new float[]{Game.player.pos.x + pos.x, pos.y, Game.player.pos.z + pos.z, 0}, 0);
		GLM.draw(GLES20.GL_TRIANGLE_STRIP, 4);
	}
}
