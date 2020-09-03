package kz.pcdp.сoncurrent.lists;

import java.util.concurrent.locks.ReentrantLock;

public final class CoarseLists {

  public static final class CoarseList extends ListSet {

    private final ReentrantLock lock = new ReentrantLock();

    public CoarseList() {
      super();
    }

    @Override
    boolean add(final Integer object) {
      try {
        lock.lock();

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
      } finally {
        lock.unlock();
      }
    }

    @Override
    boolean remove(final Integer object) {
      try {
        lock.lock();

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
      } finally {
        lock.unlock();
      }
    }

    @Override
    boolean contains(final Integer object) {
      try {
        lock.lock();

        Entry pred = this.head;
        Entry curr = pred.next;

        while (curr.object.compareTo(object) < 0) {
          pred = curr;
          curr = curr.next;
        }
        return object.equals(curr.object);
      } finally {
        lock.unlock();
      }
    }
  }

  public static final class RWCoarseList extends ListSet {

    public RWCoarseList() {
      super();
    }

    @Override
    boolean add(final Integer object) {
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
    boolean remove(final Integer object) {
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

    @Override
    boolean contains(final Integer object) {
      Entry pred = this.head;
      Entry curr = pred.next;

      while (curr.object.compareTo(object) < 0) {
        pred = curr;
        curr = curr.next;
      }
      return object.equals(curr.object);
    }
  }
}
