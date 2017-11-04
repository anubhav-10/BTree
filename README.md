# BTree
Generic btree with duplicate data allowed

Your task is to implement a generic DuplicateBTree class. As is to be expected,
DuplicateBTree must implement the BTree ADT we studied in class, except that it can contain multiple copies of the
same key (at times with exactly the same value). This implies you will have to change the algorithms for searching,
inserting and deleting suitably. The exact interface will be as follows:
public interface DuplicateBTree<Key, Value> {
public BTree(int b) throws bNotEvenException; /* Initializes an empty b-tree.
Assume b is even. */
public boolean isEmpty(); /* Returns true if the tree is empty. */
public int size(); /* Returns the number of key-value pairs */
public int height(); /* Returns the height of this B-tree */
public Vector<Value> search(Key key) throws IllegalKeyException; /* Returns all
values associated with a given key in a vector */
public void insert(Key key, Value val); /* Inserts the key-value pair */
public void delete(Key key) throws IllegalKeyException; /* Deletes all
occurrences of key */
public String toString(); /* Prints all the tree in the format listed below */
}
