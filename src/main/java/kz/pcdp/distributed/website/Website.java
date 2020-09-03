package kz.pcdp.distributed.website;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public final class Website implements Serializable {

  private int id;

  private LinkedList<Integer> edges;

  public Website(final int setId) {
    this.id = setId;
    this.edges = new LinkedList<Integer>();
  }

  public Website() {
    this(-1);
  }

  void addEdge(final int target) {
    edges.add(target);
  }

  int getId() { return id; }

  int getNEdges() {
    return edges.size();
  }

  Iterator<Integer> edgeIterator() {
    return edges.iterator();
  }

  @Override
  public boolean equals(final Object o) {
    if (o instanceof Website) {
      Website other = (Website) o;
      return other.id == id;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return id;
  }
}
