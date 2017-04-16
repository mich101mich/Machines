package com.M101M.Industria.Utils;
import java.util.*;

public class Pile<E extends Object> extends AbstractList<E>
{
	private E[] content;
	private int lastElement;
	
	public Pile()
	{
		this(100);
	}
	public Pile(int size)
	{
		if (size <= 0)
			throw new IllegalArgumentException("Size cannot be less than or equal to 0");
		content = (E[])new Object[size];
		lastElement = -1;
	}
	public Pile(Collection<E> src)
	{
		this(src.size());
		addAll(src);
	}
	
	public void reserve(int count)
	{
		if (size() + count <= content.length)
			return;
		
		int newSize = (int)((size() + count) * 1.2f);
		E[] old = content;
		content = (E[])new Object[newSize];
		System.arraycopy(old,0, content,0, size());
	}

	@Override
	public int size()
	{
		return lastElement + 1;
	}

	@Override
	public E get(int index)
	{
		return content[index];
	}

	@Override
	public boolean add(E e)
	{
		reserve(1);
		content[++lastElement] = e;
		return true;
	}

	@Override
	public void add(int index, E element)
	{
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException("length: " + size() + "; index: " + index);
		add(content[index]);
		content[index] = element;
	}

	@Override
	public E remove(int index)
	{
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException("length: " + size() + "; index: " + index);
		
		E ret = content[index];
		content[index] = content[lastElement];
		content[lastElement--] = null;
		return ret;
	}

	@Override
	public E set(int index, E element)
	{
		if (index >= size())
			throw new IndexOutOfBoundsException("length: " + size() + "; index: " + index);
		
		E ret = content[index];
		content[index] = element;
		return ret;
	}

	@Override
	public void clear()
	{
		content = (E[])new Object[content.length];
		lastElement = -1;
		modCount++;
	}
	
	public void trim()
	{
		trim(size());
	}
	
	public void trim(int size)
	{
		E[] next = (E[])new Object[size];
		System.arraycopy(content,0, next,0, Math.min(size(), size));
		content = next;
		lastElement = Math.min(lastElement, size - 1);
		modCount++;
	}
	
	public E find(Condition<E> condition)
	{
		for (E e : this)
			if (condition.test(e))
				return e;
		return null;
	}
	
	public Pile<E> filter(Condition<E> condition)
	{
		Pile<E> ret = new Pile<E>(size());
		for (E e : this)
			if (condition.test(e))
				ret.add(e);
		return ret;
	}
	
	public static abstract class Condition<E extends Object>
	{
		public abstract boolean test(E e);
	}
}
