package com.M101M.Industria.UI;
import android.opengl.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Industria.Utils.*;

public class Rectangle extends UIElement
{
	protected static int matHandle, colHandle, vertexHandle, bufferHandle;
	int color;
	public Rectangle(Vec2 pos, Vec2 size, int color)
	{
		super(pos, size);
		this.color = color;
	}
	public static void globalInit()
	{
		Shader.use(Shader.RECTANGLE);
		matHandle = Shader.getUniform("mvpMat");
		colHandle = Shader.getUniform("color");
		vertexHandle = Shader.getAttribute("vertex");
		bufferHandle = GLM.genBuffers(1)[0];
		
		float[] corners = {0,1, 1,1, 0,0, 1,0};
		gl.glBindBuffer(GLES30.GL_ARRAY_BUFFER, bufferHandle);
		gl.glBufferData(GLES30.GL_ARRAY_BUFFER, corners.length * 4, Utils.toFloatBuffer(corners), GLES30.GL_STATIC_DRAW);
		gl.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
	}
	@Override
	public void draw()
	{
		if (!visible || Utils.hexToArray(color)[3] < 0.01)
			return;
		
		Shader.use(Shader.RECTANGLE);
		
		Mat model = Mat.identity()
			.translate(pos.x, pos.y,0)
			.scale(size.x, size.y,0);
		
		gl.glUniformMatrix4fv(matHandle, 1, false, Mat.multiply(GLM.uiMat, model).toArray(), 0);
		gl.glUniform4fv(colHandle, 1, Utils.hexToArray(color),0);
		gl.glBindBuffer(GLES30.GL_ARRAY_BUFFER, bufferHandle);
		gl.glVertexAttribPointer(vertexHandle, 2, GLES30.GL_FLOAT, false, 0, 0);
		gl.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
		
		gl.glDrawElements(GLES20.GL_TRIANGLE_STRIP, 4, GLES20.GL_UNSIGNED_BYTE, Utils.toByteBuffer(new byte[]{ 0, 1, 2, 3 }));
	}
	@Override
	public boolean handleTouch(TouchEvent e)
	{
		return false;
	}
}
