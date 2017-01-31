package com.M101M.Industria.UI;
import android.opengl.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Industria.Utils.*;

public class Rectangle extends UIElement
{
	static int matHandle, colHandle, vertexHandle;
	int color;
	public Rectangle(Vec2 pos, Vec2 size, int color)
	{
		super(pos, size);
		this.color = color;
	}
	public Rectangle(float x,float y, float width,float height, int color)
	{
		this(new Vec2(x,y), new Vec2(width,height), color);
	}
	public static void globalInit()
	{
		Shader.use(Shader.RECTANGLE);
		matHandle = Shader.getUniform("mvpMat");
		//colHandle = Shader.getUniform("color");
		vertexHandle = Shader.getAttribute("vertex");
	}
	@Override
	public void draw()
	{
		Shader.use(Shader.RECTANGLE);
		
		gl.glUniformMatrix4fv(matHandle, 1, false, GLM.uiMat, 0);
		//gl.glUniform4fv(colHandle, 1, Utils.hexToArray(color),0);
		gl.glVertexAttribPointer(vertexHandle, 3, GLES20.GL_FLOAT, false, 12, Utils.toFloatBuffer(new float[]{
					-0.5f,  0.5f, 0.0f,   // top left
					-0.5f, -0.5f, 0.0f,   // bottom left
					0.5f, -0.5f, 0.0f,   // bottom right
					0.5f,  0.5f, 0.0f }));
		
		gl.glDrawElements(GLES20.GL_TRIANGLES, 6, GLES20.GL_UNSIGNED_BYTE, Utils.toByteBuffer(new byte[]{ 0, 1, 2, 0, 2, 3 }));
	}
	@Override
	public boolean handleTouch(TouchEvent e)
	{
		return false;
	}
}
