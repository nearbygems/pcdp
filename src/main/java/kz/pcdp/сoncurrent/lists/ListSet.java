package kz.pcdp.сoncurrent.lists;

public abstract class ListSet {

  protected final Entry head;

  public ListSet() {
    this.head = new Entry(Integer.MIN_VALUE);
    this.head.next = new Entry(Integer.MAX_VALUE);
  }

  public Entry getHead() {
    return head;
  }

  abstract boolean add(Integer o);

  abstract boolean remove(Integer o);

  abstract boolean contains(Integer o);
}
