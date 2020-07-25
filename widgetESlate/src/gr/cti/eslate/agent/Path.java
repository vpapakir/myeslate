package gr.cti.eslate.agent;

import java.io.IOException;
import java.io.NotActiveException;
import java.io.ObjectInputStream;

/**
 *
 * A collection of PathSegments which implements the Path
 * of an Agent.
 * <p>
 *
 * @author      Dennis M. Sosnoski
 * @author      Kriton Kyrimis
 * @author      Giorgos Vasiliou
 * @version     1.0.0, 26-Apr-2000
 * @see         gr.cti.eslate.agent.PathSegment
 */

public class Path extends gr.cti.typeArray.ArrayBase {
    protected PathSegment[] baseArray;
    transient private PathListener pathListeners;
    private static final long serialVersionUID=-59659841137449490L;
    /**
     * The index of the active segment, i.e. the segment that points are added to.
     */
    private int active=-1;

    public Path(int size,int growth) {
        super(size,growth,PathSegment.class);
    }

    public Path(int size) {
        super(size,PathSegment.class);
    }

    public Path() {
        super(PathSegment.class);
    }
    /**
     * Implementation of callout to get the underlying array.
     */
    protected Object getArray() {
        return baseArray;
    }
    /**
     * Implementation of callout to set the underlying array.
     */
    protected void setArray(Object array) {
        baseArray = (PathSegment[]) array;
    }
    /**
     * Implementation of callout to initialize a portion of the array.
     */
    protected void discardValues(int from, int to) {
        for (int i=from;i<to;i++)
            baseArray[i]=null;
    }
    /**
     * Append a value to the collection.
     */
    public int add(PathSegment value) {
        int index=getAddIndex();
        baseArray[index]=value;
        return index;
    }
    /**
     * Insert a value into the collection.
     */
    public void add(int index,PathSegment value) {
        makeInsertSpace(index);
        baseArray[index]=value;
    }
    /**
     * Get value from the collection.
     */
    public PathSegment get(int index) {
        if (index<countPresent) {
            return baseArray[index];
        } else {
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
        }
    }
    /**
     * Set the value at a position in the collection.
     */
    public void set(int index,PathSegment value) {
        if (index < countPresent) {
            baseArray[index] = value;
        } else {
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
        }
    }
    /**
     * Convert to an array.
     */
    public PathSegment[] toArray() {
        return (PathSegment[]) buildArray();
    }
    /**
     * Return the first element in the collection.
     */
    public PathSegment front() {
        return get(0);
    }
    /**
     * Return the last PathSegment in the collection.
     * @return	The requested PathSegment.
     */
    public PathSegment back() {
        return get(countPresent-1);
    }
    /**
     * Remove and return the first PathSegment in the collection.
     * @return	The removed PathSegment.
     */
    public PathSegment popFront() {
        PathSegment first=get(0);
        remove(0);
        return first;
    }

    /**
     * Insert a PathSegment in front of the first element in the collection.
     * @param	value	The PathSegment to insert.
     */
    public void pushFront(PathSegment value) {
        add(0, value);
    }

    /**
     * Remove at most a given number of elements within a range of indices
     * that match a particular PathSegment.
     * @param	first	The index of the first element to consider.
     * @param	last	The index of the last element to consider.
     * @param	element	The PathSegment to match.
     * @param	count	The maximum number of elements to remove.
     * @return	The number of PathSegments that were removed.
     */
    public int removeElements(int first,int last,PathSegment element,int count) {
        if (first >= 0 && first < countPresent && last >= 0 && last < countPresent) {
        	if (first > last) {
        	    int tmp=first;
	            first=last;
	            last=tmp;
	        }
        	int deleted = 0;
	        if (element == null) {
        	    for (int i=first; i<=last && count>0; i++)
	                if (baseArray[i] == null) {
	                    remove(i);
	                    deleted++;
	                    count--;
	                    i--; last--; // i-th element was deleted, so retry
	                }
	        } else {
        	    for (int i=first; i<=last && count>0; i++)
	                if (element.equals(baseArray[i])) {
                        remove(i);
                        deleted++;
                        count--;
                        i--; last--; // i-th element was deleted, so retry
	                }
            }
	        return deleted;
        } else
	        throw new ArrayIndexOutOfBoundsException("Invalid index value");
    }
    /**
     * Remove all elements that match a particular PathSegment.
     * @param	element	The PathSegment to match.
     * @return	The number of PathSegments that were removed.
     */
    public int removeElements(PathSegment element) {
        return removeElements(0,countPresent-1,element,countPresent);
    }
    /**
     * Remove at most a given number of elements that match a particular PathSegment.
     * @param	element	The PathSegment to match.
     * @param	count	The maximum number of elements to remove.
     * @return	The number of PathSegments that were removed.
     */
    public int removeElements(PathSegment element, int count) {
        return removeElements(0,countPresent-1,element,count);
    }
    /**
     * Remove all elements within a range of indices that match a particular
     * PathSegment.
     * @param	first	The index of the first element to consider.
     * @param	last	The index of the last element to consider.
     * @param	element	The PathSegment to match.
     * @return	The number of PathSegment that were removed.
     */
    public int removeElements(int first,int last,PathSegment element) {
        return removeElements(first,last,element,countPresent);
    }

    /**
     * Replace all elements within a range of indices that match a particular
     * PathSegment with a new value.
     * @param	first		The index of the first element to consider.
     * @param	last		The index of the last element to consider.
     * @param	oldValue	The PathSegment to match.
     * @param	newValue	The new value.
     * @return	The number of PathSegments that were replaced.
     */
    public int replace(int first, int last, PathSegment oldValue, PathSegment newValue) {
        if (first >= 0 && first < countPresent && last >= 0 && last < countPresent) {
            int replaced = 0;
	        if (oldValue == null) {
	            for (int i=first; i<=last; i++)
	                if (baseArray[i] == null) {
	                    baseArray[i] = newValue;
	                    replaced++;
	                }
	        } else {
	            for (int i=first; i<=last; i++)
	                if (oldValue.equals(baseArray[i])) {
	                    baseArray[i] = newValue;
	                    replaced++;
	                }
            }
	        return replaced;
        } else
            throw new ArrayIndexOutOfBoundsException("Invalid index value");
    }
    /**
     * Replace all elements that match a particular PathSegment with a new value.
     * @param	oldValue	The PathSegment to match.
     * @param	newValue	The new value.
     * @return	The number of PathSegment that were replaced.
     */
    public int replace(PathSegment oldValue,PathSegment newValue) {
        return replace(0,countPresent-1,oldValue,newValue);
    }
    /**
     * Count all elements within a range of indices that match a particular
     * PathSegment.
     * @param	first	The index of the first element to consider.
     * @param	last	The index of the last element to consider.
     * @param	value	The PathSegment to match.
     * @return	The number of PathSegment matched.
     */
    public int count(int first,int last,PathSegment value) {
        if (first >= 0 && first < countPresent && last >= 0 && last < countPresent) {
            int found = 0;
            if (value == null) {
                for (int i=first; i<=last; i++)
                    if (baseArray[i] == null)
                        found++;
            } else {
                for (int i=first; i<=last; i++)
                    if (value.equals(baseArray[i]))
                        found++;
            }
            return found;
        } else
	        throw new ArrayIndexOutOfBoundsException("Invalid index value");
    }
    /**
     * Count all elements that match a particular PathSegment.
     * @param	value	The PathSegment to match.
     * @return	The number of PathSegments matched.
     */
    public int count(PathSegment value) {
        return count(0,countPresent-1,value);
    }
    /**
     * Return the index of the first object within a range of indices that
     * matches a particular PathSegment.
     * @param	first	The index of the first element to consider.
     * @param	last	The index of the last element to consider.
     * @param	value	The PathSegment to match.
     * @return	The requested index or -1 if the PathSegment is not found.
     */
    public int indexOf(int first,int last,PathSegment value) {
        if (first >= 0 && first < countPresent && last >= 0 && last < countPresent) {
	        if (value == null) {
	            for (int i=first; i<=last; i++)
	                if (baseArray[i] == null)
	                    return i;
	        } else {
    	        for (int i=first; i<=last; i++)
	                if (value.equals(baseArray[i]))
	                    return i;
            }
        	return -1;
        } else
	        throw new ArrayIndexOutOfBoundsException("Invalid index value");
    }
    /**
     * Return the index of the first object that matches a particular PathSegment.
     * @param	value	The PathSegment to match.
     * @return	The requested index or -1 if the PathSegment is not found.
     */
    public int indexOf(PathSegment value) {
      return indexOf(0, countPresent-1, value);
    }
    /**
     * Check if the collection contains a particular PathSegment.
     * @param	value	The PathSegment to match.
     * @return	True if the PathSegment is contained in the collection, false
     *		otherwise.
     */
    public boolean contains(PathSegment value) {
        return (indexOf(value)!=-1);
    }
    /**
     * Adds a segment to the path and sets it the active segment.
     * @return  The segment added.
     */
    public PathSegment addSegment() {
        PathSegment ns;
        //If the segment is the first one, create it with the default properties
        if (size()==0)
            ns=new PathSegment();
        //Otherwise create the segment with its previous one's properties
        else {
            PathSegment pr=get(size()-1);
            if (pr.getPaintAs()==PathSegment.PAINT_AS_GRADIENT_COLOR)
                ns=new PathSegment(pr.getWidth(),pr.getGradientStart(),pr.getGradientEnd(),pr.getStrokeAs());
            else
                ns=new PathSegment(pr.getWidth(),pr.getSolidColor(),pr.getStrokeAs());
        }
        active=add(ns);
        ns.setPath(this);
        return ns;
    }
    /**
     * Adds a point to the active segment.
     */
    public void addPoint(double x,double y) {
        if (active!=-1)
            get(active).addPoint(x,y);
        else {
            addSegment();
            addPoint(x,y);
        }
    }
    /**
     * @return  The active segment.
     */
    public PathSegment getActiveSegment() {
        if (active==-1)
            return null;
        return get(active);
    }
    /**
     * Informs the listeners that one path segment has changed properties.
     */
    public void segmentPropertiesChanged(PathSegment changed) {
        if (pathListeners!=null)
            pathListeners.segmentPropertiesChanged(new PathEvent(this,PathEvent.PATH_PROPERTIES_CHANGED,indexOf(changed)));
    }
    /**
     * Adds a path listener. The path events inform about changes in the properties and
     * not on every addition of a point to improve performance.
     */
    public void addPathListener(PathListener pl) {
        pathListeners=PathEventMulticaster.add(pathListeners,pl);
    }
    /**
     * Removes a path listener. The path events inform about changes in the properties and
     * not on every addition of a point to improve performance.
     */
    public void removePathListener(PathListener pl) {
        pathListeners=PathEventMulticaster.remove(pathListeners,pl);
    }
    /**
     * Override to set properties.
     */
    private void readObject(ObjectInputStream in) throws ClassNotFoundException, IOException {
        try {
            in.defaultReadObject();
        } catch(NotActiveException e) {
            System.err.println("AGENT#200006011949: Cannot read default serialized data.");
        }
        for (int i=0;i<size();i++)
            get(i).setPath(this);
    }
}
