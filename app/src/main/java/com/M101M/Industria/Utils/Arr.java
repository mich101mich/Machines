package com.M101M.Industria.Utils;
import java.util.*;
import com.android.internal.util.Predicate;

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
	}
	public E find(Predicate<E> p)
	{
		for (E e : this)
			if (p.apply(e))
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
	public E remove(Predicate<E> p)
	{
		Iterator<E> it = iterator();
		while (it.hasNext())
		{
			E e = it.next();
			if (p.apply(e))
			{
				it.remove();
				return e;
			}
		}
		return null;
	}
	public void removeAll(Predicate<E> p)
	{
		Iterator<E> it = iterator();
		while (it.hasNext())
		{
			E e = it.next();
			if (p.apply(e))
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
}
