package com.M101M.Industria.UI;
import android.opengl.*;
import com.M101M.Industria.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Utils.*;

public class Circle extends UIElement
{
	private static int matHandle, colHandle, vertexHandle, bufferHandle;
	public int color;
	public Circle(Vec2 pos, Vec2 size, int color)
	{
		super(pos, size);
		this.color = color;
	}
	public Circle(Vec2 pos, float radius, int color)
	{
		this(pos, new Vec2(2 * radius, 2 * radius), color);
	}
	public static void globalInit()
	{
		Shader.use(Shader.CIRCLE);
		matHandle = Shader.getUniform("mvpMat");
		colHandle = Shader.getUniform("color");
		vertexHandle = Shader.getAttribute("vertex");
		bufferHandle = GLM.genBuffers(1)[0];

		
		GLM.vbo(bufferHandle, new float[]{ -1,-1, -1,1, 1,-1, 1,1 });
	}
	@Override
	public void draw()
	{
		if (!visible || Utils.hexToArray(color)[3] < 0.01)
			return;

		Shader.use(Shader.CIRCLE);

		Mat model = Mat.identity()
			.translate(pos.x, pos.y,0)
			.scale(size.x/2.0f, size.y/2.0f,0);

		gl.glUniformMatrix4fv(matHandle, 1, false, Mat.multiply(GLM.uiMat, model).toArray(), 0);
		gl.glUniform4fv(colHandle, 1, Utils.hexToArray(color),0);
		GLM.useVBO(bufferHandle, vertexHandle, 2, 0);
		
		GLM.draw(GLES20.GL_TRIANGLE_STRIP, 4);
	}
	@Override
	public boolean handleTouch(TouchEvent e)
	{
		return false;
	}
	@Override
	public android.graphics.RectF bounds()
	{
		return new android.graphics.RectF(pos.x - size.x / 2, pos.y - size.y / 2, pos.x + size.x / 2, pos.y + size.y / 2);
	}
	
}
