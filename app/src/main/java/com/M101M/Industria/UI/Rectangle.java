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
		colHandle = Shader.getUniform("color");
		vertexHandle = Shader.getAttribute("vertex");
	}
	@Override
	public void draw()
	{
		if (Utils.hexToArray(color)[3] < 0.01)
			return;
		
		Shader.use(Shader.RECTANGLE);
		
		gl.glUniformMatrix4fv(matHandle, 1, false, GLM.uiMat, 0);
		gl.glUniform4fv(colHandle, 1, Utils.hexToArray(color),0);
		gl.glVertexAttribPointer(vertexHandle, 2, GLES20.GL_FLOAT, false, 8, Utils.toFloatBuffer(new float[]{
					pos.x, pos.y,
					pos.x, pos.y + size.y,
					pos.x + size.x, pos.y,
					pos.x + size.x,  pos.y + size.y }));
		
		gl.glDrawElements(GLES20.GL_TRIANGLE_STRIP, 4, GLES20.GL_UNSIGNED_BYTE, Utils.toByteBuffer(new byte[]{ 0, 1, 2, 3 }));
	}
	@Override
	public boolean handleTouch(TouchEvent e)
	{
		return false;
	}
}
