package lbs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import objects.FishObj;
import objects.GameObj;

/**
 * Created by eason on 2015/10/22.
 */
public class FishCollection implements Collection<FishObj> {
    protected List<FishObj> componse = new ArrayList<FishObj>();


    public FishObj get(int arg0) {
        return componse.get(arg0);
    }
    @Override
    public boolean add(FishObj object) {
        //        object.initialize();

        return componse.add(object);
    }

    @Override
    public boolean addAll(Collection<? extends FishObj> collection) {
        return componse.addAll(collection);
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean contains(Object object) {
        return componse.contains(object);
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        return componse.containsAll(collection);
    }

    @Override
    public boolean isEmpty() {
        return componse.isEmpty();
    }

    @Override
    public Iterator<FishObj> iterator() {
        return null;
    }

    @Override
    public boolean remove(Object object) {
        return componse.remove(object);
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return componse.removeAll(collection);
    }

    @Override
    public boolean retainAll(Collection<?> collection) {
        return componse.removeAll(collection);
    }

    @Override
    public int size() {
        return componse.size();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] array) {
        return null;
    }
}
