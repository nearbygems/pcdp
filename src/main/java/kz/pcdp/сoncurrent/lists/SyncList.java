package kz.pcdp.сoncurrent.lists;

public final class SyncList extends ListSet {

  public SyncList() {
    super();
  }

  @Override
  public synchronized boolean contains(final Integer object) {
    Entry pred = this.head;
    Entry curr = pred.next;

    while (curr.object.compareTo(object) < 0) {
      pred = curr;
      curr = curr.next;
    }
    return object.equals(curr.object);
  }

  @Override
  public synchronized boolean add(final Integer object) {
    Entry pred = this.head;
    Entry curr = pred.next;

    while (curr.object.compareTo(object) < 0) {
      pred = curr;
      curr = curr.next;
    }

    if (object.equals(curr.object)) {
      return false;
    } else {
      final Entry entry = new Entry(object);
      entry.next = curr;
      pred.next = entry;
      return true;
    }
  }

  @Override
  public synchronized boolean remove(final Integer object) {
    Entry pred = this.head;
    Entry curr = pred.next;

    while (curr.object.compareTo(object) < 0) {
      pred = curr;
      curr = curr.next;
    }

    if (object.equals(curr.object)) {
      pred.next = curr.next;
      return true;
    } else {
      return false;
    }
  }
}
