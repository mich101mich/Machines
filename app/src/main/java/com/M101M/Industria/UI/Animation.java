package com.M101M.Industria.UI;

public abstract class Animation
{
	public Animation(int duration)
	{
		this.duration = duration;
		tick = -1;
	}
	int duration, tick;
	public abstract void start(UIElement obj);
	public abstract void step(UIElement obj);
	public abstract void stop(UIElement obj);
}
