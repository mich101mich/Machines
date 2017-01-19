package com.M101M.Industria.Utils;

public class Arr<E extends Object>
{
	public int count = 0, max;
	private int slot = 0;
	private E[] content;
	public Arr()
	{ this(100); }
	public Arr(int expectedCapacity)
	{
		max = expectedCapacity;
		content = (E[])new Object[max];
	}
	public void reserve(int amount)
	{
		int newMax = amount + count;
		if (newMax <= max)
			return;
		E[] next = (E[])new Object[newMax];
		System.arraycopy(content,0, next,0, max);
		content = next;
		max = newMax;
	}
	public void add(E e)
	{
		if (e == null)
			return;
		if (count == max)
		{
			reserve(max);
			slot = count;
		}
		for (int i=slot; i<max; i++)
		{
			if (content[i] != null)
				continue;
			content[i] = e; count++; slot = i + 1;
			return;
		}
	}
	public E find(Condition<E> c)
	{
		if (count == 0)
			return null;
		int tCount = count;
		for (int i=0; i<max; i++)
		{
			if (content[i] == null)
				continue;
			if (c.test(content[i]))
				return content[i];
			if (--tCount <= 0)
				break;
		}
		return null;
	}
	public void forEach(Action<E> a) throws Exception 
	{ forEach(a, false); }
	public void forEach(Action<E> a, boolean pop) throws Exception
	{
		if (count == 0)
			return;
		int tCount = count;
		for (int i=0; i<max; i++)
		{
			if (content[i] == null)
				continue;
			a.run(content[i]);
			if (pop)
				remove(i);
			if (--tCount <= 0)
				return;
		}
		return;
	}
	public E remove(Condition<E> c)
	{
		if (count == 0)
			return null;
		int tCount = count;
		for (int i=0; i<max; i++)
		{
			if (content[i] == null)
				continue;
			if (c.test(content[i]))
			{
				E ret = content[i];
				remove(i);
				if (i < slot)
					slot = i;
				return ret;
			}
			if (--tCount <= 0)
				break;
		}
		return null;
	}
	public void remove(E e)
	{
		if (e == null || count == 0)
			return;
		int tCount = count;
		for (int i=0; i<max; i++)
		{
			if (content[i] == null)
				continue;
			if (content[i] == e)
			{
				remove(i);
				if (i < slot)
					slot = i;
				return;
			}
			if (--tCount <= 0)
				break;
		}
	}
	private void remove(int i)
	{
		content[i] = null;
		count--;
	}
	public static interface Condition<E extends Object>
	{ boolean test(E e) }
	public static interface Action<E extends Object>
	{ void run(E e) throws Exception }
}
