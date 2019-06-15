import java.util.*;

/**
 * Represents specialized HashMap implementation for Natural Language Processing.
 * The key is filename containing the word.
 * The value is the List containing the word positions in that file.
 * @see Word_Map
 * @author Ahmed Semih Özmekik
 */
public class File_Map implements Map
{


    private ArrayList<String> fnames; // key list.
    private ArrayList<List<Integer>> occurances; // value list.

    /**
     * Creates a FileMap.
     */
    public File_Map(){
        fnames = new ArrayList<>();
        occurances = new ArrayList<>();
    }

    @Override
    public int size() {
        return fnames.size();
    }

    @Override
    public boolean isEmpty() {
        return fnames.isEmpty();
    }

    /**
     * Pre-Condition: key item must be valid type.
     * @param key is the item.
     * @return true if the map contains the key, false otherwise.
     */
    @Override
    public boolean containsKey(Object key) {
        String theKey = (String) key;
        return fnames.contains(theKey);
    }


    /**
     * Pre-Condition: value item must be valid type.
     * @param value is the item.
     * @return true if the map contains the key, false otherwise.
     */
    @Override
    public boolean containsValue(Object value) {
        Integer theValue = (Integer) value;
        for (List list: occurances){
            if (list.contains(theValue))
                return true;
        }
        return false;
    }

    /**
     * Returns the FileMap for given key.
     * Pre-Condition: given key item must be string which is the filename.
     * @param key is the filename.
     * @return List of positions of the word in that filename.
     */
    @Override
    public Object get(Object key) {
        String theKey = (String) key;
        int index = fnames.indexOf(theKey);
        if (index>=0) // the key is in the map.
            return occurances.get(index);
        else
            throw new NoSuchElementException("Key does not exist");

    }

    /**
     * Inserts key, value pair to the map.
     * Pre-Condition: given pair items must be valid type(String(filename), Integer).
     * Post-Condition: If the key already exist, just appends the given position
     *                 integer to the list of positions.
     * @param key is String which is the filename.
     * @param value is the position of integer.
     * @return the old value of key in the map if the key already
     *         is in the map, otherwise return null.
     */
    @Override
    public Object put(Object key, Object value) {
        String theKey = (String) key;
        Integer theValue = (Integer) value;

        if (!containsKey(theKey)){ // insert the new pairs, if key does not exist.
            fnames.add(theKey);
            occurances.add(new ArrayList<>());
        }

        int index = fnames.indexOf(theKey);
        if (!occurances.get(index).contains(theValue))  // key exists, and avoid duplicates of the same value.
            occurances.get(index).add(theValue);


        return null;
    }

    /**
     * Removes the filename and positions pair from the map.
     * @param key is filename String.
     * @return the removed list of positions.
     */
    @Override
    public Object remove(Object key) {
        String theKey = (String) key;
        int index = fnames.indexOf(theKey);

        fnames.remove(index);
        return occurances.remove(index);
    }

    /**
     * Puts all pair items in the given map collection to this map.
     * Pre-Condition: Given map's items must be valid type.
     * @param m is the map to insert.
     */
    @Override
    @SuppressWarnings("unchecked cast")
    public void putAll(Map m) {
        Map<String, Integer> map = (Map<String, Integer>) m;
        for (Map.Entry<String, Integer> entry: map.entrySet())
            put(entry.getKey(), entry.getValue());
    }

    @Override
    public void clear() {
        fnames.clear();
        occurances.clear();
    }

    /**
     * Returns the Set of filename Strings.
     * @return the Set of filename Strings.
     */
    @Override
    public Set keySet() {
        return new HashSet<>(fnames);
    }

    /**
     * Returns the Set of Positions.
     * @return the Set of Positions.
     */
    @Override
    public Collection values() {
        return new HashSet<>(occurances);
    }

    /**
     * Returns the set of Entries containing filename Strings and positions.
     * @return the set of key, value pairs.
     */
    @Override
    public Set<Entry> entrySet() {
        Set<Entry> set = new HashSet<>();

        for(int idx = 0; idx<fnames.size(); ++idx){
            set.add(new AbstractMap.
                    SimpleEntry<>(fnames.get(idx), occurances.get(idx)));
        }

        return set;
    }

    /**
     * Compares this File_Map with given File_Map.
     * @param obj is another File_Map to compare with.
     * @return true if they have the same pairs.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof File_Map){
            File_Map other = (File_Map) obj;
            if (fnames.equals(other.fnames) && occurances.equals(other.occurances))
                return true;
        }
        return false;

    }

    /**
     * Prints the file map: filename -> position list.
     */
    public void printFileMap(){
        for(int i=0;i<size();++i){
            System.out.println("\t\t\t" + fnames.get(i) + "->"+ occurances.get(i));
        }
    }
}
