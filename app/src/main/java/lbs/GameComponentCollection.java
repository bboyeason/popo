/* copyright(c) 2010/11/22 by ��ߪ����j�Ǹ�u�|�B�f��
 * 
 * 
 * 
 */

package lbs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class GameComponentCollection implements Collection<DrawableGameComponent>{
	
	protected List<DrawableGameComponent> componse = new ArrayList<DrawableGameComponent>();
	
	
	public boolean add(DrawableGameComponent arg0) {
		arg0.Initialize();
		arg0.LoadContent();
		
		return componse.add(arg0);
	}
	
	public DrawableGameComponent get(int arg0) {
		return componse.get(arg0);
	}
	
	public void add(int arg0,DrawableGameComponent arg1)
	{
		componse.add(arg0, arg1);
	}
	
	public boolean addAll(Collection<? extends DrawableGameComponent> arg0) {
		// TODO Auto-generated method stub
		return componse.addAll(arg0);
	}

	public void clear() {
		componse.clear();
	}

	public boolean contains(Object arg0) {
		// TODO Auto-generated method stub
		return componse.contains(arg0);
	}

	public boolean containsAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return componse.containsAll(arg0);
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return componse.isEmpty();
	}

	public Iterator<DrawableGameComponent> iterator() {
		// TODO Auto-generated method stub
		return componse.iterator();
	}

	public boolean remove(Object arg0) {
		return componse.remove(arg0);
	}

	public boolean removeAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return componse.removeAll(arg0);
	}

	public boolean retainAll(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return componse.retainAll(arg0);
	}

	public int size() {
		// TODO Auto-generated method stub
		return componse.size();
	}

	public Object[] toArray() {
		// TODO Auto-generated method stub
		return componse.toArray();
	}

	public <T> T[] toArray(T[] arg0) {
		// TODO Auto-generated method stub
		return componse.toArray(arg0);
	}
}
