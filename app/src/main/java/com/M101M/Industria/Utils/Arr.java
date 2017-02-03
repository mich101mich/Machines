package com.M101M.Industria.Utils;
import java.util.*;

public class Arr<E extends Object> implements Iterable
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
	public void clear()
	{
		if (count == 0)
			return;
		content = (E[])new Object[max];
		count = slot = 0;
	}
	public void reserve(int amount)
	{
		int newMax = amount + count;
		if (newMax <= max)
			return;
		E[] next = (E[])new Object[newMax];
		System.arraycopy(content, 0, next, 0, max);
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
		for (int i=slot; i < max; i++)
		{
			if (content[i] != null)
				continue;
			content[i] = e; count++; slot = i + 1;
			return;
		}
		throw new RuntimeException("something happened");
	}
	public E find(Condition<E> p)
	{
		for (E e : this)
			if (p.test(e))
				return e;
		return null;
	}
	public boolean remove(E e)
	{
		if (e == null || count == 0)
			return false;
		Iterator it = iterator();
		while (it.hasNext())
		{
			if (it.next() == e)
			{
				it.remove();
				return true;
			}
		}
		return false;
	}
	public E remove(Condition<E> p)
	{
		Iterator<E> it = iterator();
		while (it.hasNext())
		{
			E e = it.next();
			if (p.test(e))
			{
				it.remove();
				return e;
			}
		}
		return null;
	}
	public void removeAll(Condition<E> p)
	{
		Iterator<E> it = iterator();
		while (it.hasNext())
		{
			E e = it.next();
			if (p.test(e))
				it.remove();
		}
	}
	private void removeAt(int i)
	{
		content[i] = null;
		count--;
		if (i < slot)
			slot = i;
	}
	@Override
	public Iterator<E> iterator()
	{
		return new Iterator<E>() {
			int index = -1, n = 0;
			public boolean hasNext()
			{
				return n < count;
			}
			public E next()
			{
				while (content[++index] == null);
				n++;
				return content[index];
			}
			public void remove()
			{
				removeAt(index);
				n--;
			}
		};
	}
	public static interface Condition<E extends Object>
	{public boolean test(E e);}
}
