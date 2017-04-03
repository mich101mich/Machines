package com.M101M.Industria.UI;
import android.opengl.*;
import com.M101M.Industria.GLHelp.*;
import com.M101M.Industria.Utils.*;

public class Text extends Rectangle
{
	private String text;
	public int textColor;
	private float scale;
	private static int matHandle, vertexHandle, colHandle, textHandle, uvHandle;
	public Text(Vec2 pos, float width, String text, int textColor, int backColor)
	{
		super(pos, new Vec2(width, 0), backColor);
		this.textColor = textColor;
		setText(text);
	}
	public Text(Vec2 pos, float width, String text, int textColor)
	{
		this(pos, width, text, textColor, 0xff000000);
	}
	public Text(Vec2 pos, float width, String text)
	{
		this(pos, width, text, 0x0);
	}
	public static void globalInit()
	{
		Shader.use(Shader.TEXT);
		matHandle = Shader.getUniform("mvpMat");
		colHandle = Shader.getUniform("color");
		textHandle = Shader.getUniform("text");
		vertexHandle = Shader.getAttribute("vertex");
		uvHandle = Shader.getAttribute("uv");
	}
	public String getText()
	{ return text; }
	public void setText(String t)
	{
		text = t;
		int maxWidth = 0;
		String[] lines = text.split("\n");
		for (String s : lines)
			if (s.length() > maxWidth)
				maxWidth = s.length();
		scale = size.x / (maxWidth * TEXT_WIDTH + 1);
		size.y = scale * (lines.length * TEXT_HEIGHT + 1);
	}
	@Override
	public void draw()
	{
		if (!visible)
			return;
		super.draw();
		Shader.use(Shader.TEXT);
		gl.glUniformMatrix4fv(matHandle, 1, false, GLM.uiMat.toArray(), 0);
		gl.glUniform4fv(colHandle, 1, Utils.hexToArray(textColor),0);
		gl.glVertexAttribPointer(uvHandle, 2, GLES20.GL_FLOAT, false, 0, Utils.toFloatBuffer(new float[]{0,0, 0,TEXT_HEIGHT, TEXT_WIDTH, 0, TEXT_WIDTH, TEXT_HEIGHT}));
		float lw = TEXT_WIDTH * scale, lh = TEXT_HEIGHT * scale;
		int x = -1, y = 0;
		for (int i=0; i < text.length(); i++)
		{
			x++;
			int index = text.charAt(i);
			if (index == 'ä') index = 95;
			else if (index == 'ö') index = 96;
			else if (index == 'ü') index = 97;
			else if (index == 'Ä') index = 98;
			else if (index == 'Ö') index = 99;
			else if (index == 'Ü') index = 100;
			else if (index == '\n')
			{
				x = -1; y++;
				continue;
			}
			else if (index > 31 && index < 127)
				index -= 32;
			else
				continue;
			
			GLES20.glUniform1iv(textHandle, 6, Shader.letters, index * 6);
			gl.glVertexAttribPointer(vertexHandle, 2, GLES20.GL_FLOAT, false, 8, Utils.toFloatBuffer(new float[]{
						pos.x + lw*x, pos.y + lh*y + scale,
						pos.x + lw*x, pos.y + lh*(y+1) + scale,
						pos.x + lw*(x+1), pos.y + lh*y + scale,
						pos.x + lw*(x+1),  pos.y + lh*(y+1) + scale }));

			gl.glDrawElements(GLES20.GL_TRIANGLE_STRIP, 4, GLES20.GL_UNSIGNED_BYTE, Utils.toByteBuffer(new byte[]{ 0, 1, 2, 3 }));
		}
	}

	@Override
	public boolean handleTouch(TouchEvent e)
	{
		return false;
	}
	
	public static final int TEXT_HEIGHT = 8, TEXT_WIDTH = 6;
}
