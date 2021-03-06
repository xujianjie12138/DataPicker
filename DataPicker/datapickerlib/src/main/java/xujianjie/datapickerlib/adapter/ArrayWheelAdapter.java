package xujianjie.datapickerlib.adapter;

import java.util.ArrayList;

public class ArrayWheelAdapter<T> implements WheelAdapter
{
    public static final int DEFAULT_LENGTH = 4;
    private ArrayList<T> items;
    private int length;

    public ArrayWheelAdapter(ArrayList<T> items, int length)
    {
        this.items = items;
        this.length = length;
    }

    public ArrayWheelAdapter(ArrayList<T> items)
    {
        this(items, DEFAULT_LENGTH);
    }

    @Override
    public Object getItem(int index)
    {
        if (index >= 0 && index < items.size())
        {
            return items.get(index);
        }
        return "";
    }

    @Override
    public int getItemsCount()
    {
        return items.size();
    }

    @Override
    public int indexOf(Object o)
    {
        return items.indexOf(o);
    }
}

