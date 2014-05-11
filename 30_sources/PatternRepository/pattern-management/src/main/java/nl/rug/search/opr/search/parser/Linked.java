package nl.rug.search.opr.search.parser;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <T>
 * @author groupD
 */
public class Linked<T> {

    private Linked<T> next = null;
    private final T value;

    public Linked(T value) {
        this(value,null);
    }

    public Linked(T value, Linked<T> next) {
        this.value = value;
        this.next = next;
    }

    public void setNext(Linked<T> next) {
        this.next = next;
    }

    public Linked<T> getNext() {
        return this.next;
    }

    public T value() {
        return this.value;
    }

    public List<T> getList() {

        List<T> list = new ArrayList<T>();
        Linked<T> pointer = this;

        do {
            list.add(pointer.value());
            pointer = pointer.getNext();
        } while (pointer != null);

        return list;
    }
}
