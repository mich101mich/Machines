package com.M101M.Industria.World;

import android.opengl.*;
import com.M101M.Industria.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Industria.Utils.*;

public class Plane
{
	Vec pos;
	private static final int MAT=0, POS=1, VERTEX=2, BUFFER=3, INDEX=4;
	private static final int[] handles = new int[5];
	
	public Plane (Vec position)
	{
		pos = position;
	}

	public static void globalInit ()
	{
		Shader.use(Shader.PLANE);
		handles[MAT] = Shader.getUniform("mvpMat");
		handles[POS] = Shader.getUniform("position");
		handles[VERTEX] = Shader.getAttribute("vertex");

		System.arraycopy(GLM.genBuffers(2),0, handles,BUFFER, 2);
		gl.glBindBuffer(GLES20.GL_ARRAY_BUFFER, handles[BUFFER]);
		gl.glEnableVertexAttribArray(handles[BUFFER]);
		gl.glBufferData(GLES20.GL_ARRAY_BUFFER, 48, Utils.toFloatBuffer(new float[]{-50,0,50, 50,0,50, -50,0,-50, 50,0,-50}), GLES20.GL_STATIC_DRAW);
		gl.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		
		gl.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, handles[INDEX]);
		gl.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, 4, Utils.toByteBuffer(new byte[]{0,1,2,3}), GLES20.GL_STATIC_DRAW);
	}

	public static void startDrawing ()
	{
		Shader.use(Shader.PLANE);
		gl.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, handles[INDEX]);
		gl.glBindBuffer(GLES20.GL_ARRAY_BUFFER, handles[BUFFER]);
		gl.glVertexAttribPointer(handles[VERTEX], 3, GLES20.GL_FLOAT, false, 0, 0);
		gl.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		
		Mat model = Mat.identity()
			.rotate(Vec.negative(Game.player.rot))
			.translate(Vec.negative(Game.player.pos));
		gl.glUniformMatrix4fv(handles[MAT], 1, false, Mat.multiply(GLM.vpMat, model).toArray(), 0);
	}
	public void draw ()
	{
		gl.glUniform4fv(handles[POS], 1, new float[]{Game.player.pos.x + pos.x, pos.y, Game.player.pos.z + pos.z, 0}, 0);
		gl.glDrawElements(GLES20.GL_TRIANGLE_STRIP, 4, GLES20.GL_UNSIGNED_BYTE, 0);
	}
}
