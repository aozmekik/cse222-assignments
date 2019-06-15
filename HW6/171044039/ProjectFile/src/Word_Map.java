import java.security.InvalidParameterException;
import java.util.*;

/**
 * Represents specialized HashMap implementation for Natural Language Processing.
 * The key is words.
 * The value is another HashMap (FileMap).
 * @see File_Map
 * @author Ahmed Semih Özmekik
 */
public class Word_Map implements Map, Iterable
{

    private final static int INITCAP = 10;  //initial capacity
    private int CURRCAP = INITCAP;   //current capacity
    private final static float LOADFACT = 0.75f;
    private final static float THRESHOLD = LOADFACT;
    private float loadFact = 0f;

    private Node[] table;
    private int size = 0; // number of key, value pairs in table.

    private Node head;
    private Node tail; // to protect const time complexity in put() method.

    /* Represents a single Entry in map. */
    private static class Node {
        private String key; // the word.
        private File_Map value; // the file map.
        private Node next = null;

        /* Creates a Node with given key, value pairs. */
        private Node(String key, File_Map value)
        {
            this.key = key;
            this.value = value;
        }
    }


    /**
     * Initializes the Word_Map.
     */
    public Word_Map() {
        this.table = new Node[INITCAP];
    }

    @Override
    public Iterator iterator() {
        return new MapIterator(head);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }


    /**
     * Checks if the Word_Map contains the word.
     * Pre-Condition: given key item must be String which is the word itself.
     * Post-Condition: Returns true if the word is in map, false otherwise.
     * @param key is the word.
     * @return True if the map contains the word, false otherwise.
     */
    @Override
    public boolean containsKey(Object key) {

        int idx = findIndex((String) key);
        if (table[idx] == null)
            return false;

        return true;
    }

    /**
     * Checks if Word_Map contains the given File_Map for any word.
     * Pre-Condition: given value item must be a File_Map.
     * Post-Condition: Returns true if the key item is in map, false otherwise.
     * @param value is the File_Map.
     * @return True if the map contains the given File_Map, false otherwise.
     */
    @Override
    public boolean containsValue(Object value) {
        for (Object theKey: this){
            File_Map theValue = (File_Map) get(theKey);
            if (theValue.equals(value))
                return true;
        }
        return false;
    }

    /**
     * Returns the value for given word.
     * Pre-Condition: given key item must be the word which is String.
     * @param key is the word.
     * @return File_Map for given word.
     */
    @Override
    public Object get(Object key) {
        String theKey = castToString(key);

        int idx = findIndex(theKey);
        if (table[idx] == null)
            throw new NoSuchElementException("Key does not exist!");
        return table[idx].value;
    }

    /**
     * Inserts key, value pair to the map.
     * Pre-Condition: given pair items must be valid type(String, FileMap).
     * @param key is String which is the word.
     * @param value is the File_Map.
     * @return the old value of key in the map if the key already
     *         is in the map, otherwise return null.
     */
    @Override
    public Object put(Object key, Object value) {

        String theKey = castToString(key);
        File_Map theValue = castToFileMap(value);

        // findIndex() uses linear probing.
        int idx = findIndex(theKey);

        if (table[idx] == null) { // Key does not exist, so insert the entry.
            table[idx] = new Node(theKey, theValue);
            ++size;
            updateLoadFactor();

            if(head == null){
                head = table[idx];
                tail = head;
            }
            else{
                tail.next = table[idx];
                tail = tail.next;
            }

            if(rehashNeeded())
                rehash();

            return null;
        }

        // Key exist, change the value.
        File_Map oldMap = table[idx].value;
        table[idx].value = theValue;
        return oldMap;
    }

    /**
     * Puts all pair items in the given map collection to this map.
     * Pre-Condition: Given map's items must be valid type.
     * @param m is the map to insert.
     */
    @Override
    @SuppressWarnings("unchecked cast")
    public void putAll(Map m) {
        Map<String, File_Map> map = (Map<String, File_Map>) m;
        for (Map.Entry<String, File_Map> entry: map.entrySet())
            put(entry.getKey(), entry.getValue());

    }

    @Override
    /*You do not need to implement remove function * */
    public Object remove(Object key) { return null; }

    /**
     * Clears the map.
     */
    @Override
    public void clear() {
        size = 0;
        table = new Node[CURRCAP];
        updateLoadFactor();
    }

    /**
     * Gets the set of keys in the map.
     * @return set of keys.
     */
    @Override
    public Set keySet() {
        Set<String> set = new HashSet<>();

        for (Object key:this)
            set.add((String) key);

        return set;
    }

    /**
     * Gets the Collection of values in the map.
     * @return collection of values.
     */
    @Override
    @SuppressWarnings("unchecked call")
    public Collection values() {
        Collection set = new HashSet();
        for (Object key: this)
            set.add(get(key));
        return set;
    }

    @Override
    /*You do not need to implement entrySet function*/
    public Set<Entry> entrySet() {
        Set<Entry> set = new HashSet<>();


        for (Object object:this){
            String key = (String) object;
            File_Map value = (File_Map) get(key);
            set.add(new AbstractMap.SimpleEntry<>(key, value));
        }
        return set;
    }

    /* Represents the custom iterator for Word_Map */
    private static class MapIterator implements Iterator<String>{
        private Node nextItem;

        private MapIterator(Node head){
            nextItem = head;
        }

        /**
         * Checks if the iteration has next item.
         * @return true if there is next item, false otherwise.
         */
        @Override
        public boolean hasNext() {
            return nextItem != null;
        }

        /**
         * Returns the next value in the iteration and move
         * forward.
         * @return next value in the iteration.
         */
        @Override
        public String next() {
            String item = nextItem.key;
            nextItem = nextItem.next;
            return item;
        }

    }


    private boolean rehashNeeded(){
        return loadFact>THRESHOLD;
    }

    private void updateLoadFactor()
    {
        loadFact = (float) size /table.length;
    }


    private int computeIndex(String key)
    {
        int index = key.hashCode() % CURRCAP;
        if (index < 0)
            index += table.length;
        return index;
    }

    /*
     * Returns index of the key if the key exists in table,
     * Otherwise returns the index of first empty place.
     * Uses linear probing.
     */
    private int findIndex(String key){
        int idx = computeIndex(key);

        while(table[idx] != null && !key.equals(table[idx].key)){
            ++idx;
            if (idx >= table.length) // back to top.
                idx = 0;
        }

        return idx;
    }

    /*
     * Rehashes the table efficiently by using the pointers on entries.
     */
    private void rehash(){

        CURRCAP = 2*CURRCAP + 1; // less collusion probability in odd numbers.
        table = new Node[CURRCAP];

        Node temp = head;
        head = null;
        tail = null;
        size = 0;
        while(temp != null){
            put(temp.key, temp.value);
            temp = temp.next;
        }

        updateLoadFactor();
    }

    private static String castToString(Object object)
    {
        if (!(object instanceof String))
            throw new InvalidParameterException("Invalid key type!");
        return (String) object;
    }

    private static File_Map castToFileMap(Object object)
    {
        if (!(object instanceof File_Map))
            throw new InvalidParameterException("Invalid value type!");
        return (File_Map) object;
    }


}
