package kz.pcdp.сoncurrent.boruvka.sequential;

import kz.pcdp.сoncurrent.boruvka.Component;
import kz.pcdp.сoncurrent.boruvka.Edge;

import java.util.ArrayList;
import java.util.List;

public final class SeqComponent extends Component<SeqComponent> {

  public final int nodeId;

  public List<Edge<SeqComponent>> edges = new ArrayList<>();

  public double totalWeight = 0;

  public long totalEdges = 0;

  public boolean isDead = false;

  protected SeqComponent(final int setNodeId) {
    super();
    this.nodeId = setNodeId;
  }

  @Override
  public int nodeId() {
    return nodeId;
  }

  @Override
  public double totalWeight() {
    return totalWeight;
  }

  @Override
  public long totalEdges() {
    return totalEdges;
  }

  public void addEdge(final Edge<SeqComponent> e) {
    int i = 0;
    while (i < edges.size()) {
      if (e.weight() < edges.get(i).weight()) {
        break;
      }
      i++;
    }
    edges.add(i, e);
  }

  public Edge<SeqComponent> getMinEdge() {
    if (edges.size() == 0) {
      return null;
    }
    return edges.get(0);
  }

  public void merge(final SeqComponent other, final double edgeWeight) {
    totalWeight += other.totalWeight + edgeWeight;
    totalEdges += other.totalEdges + 1;
    final List<Edge<SeqComponent>> newEdges = new ArrayList<>();
    int i = 0;
    int j = 0;
    while (i + j < edges.size() + other.edges.size()) {
      while (i < edges.size()) {
        final Edge e = edges.get(i);
        if ((e.fromComponent() != this && e.fromComponent() != other)
          || (e.toComponent() != this && e.toComponent() != other)
        ) {
          break;
        }
        i++;
      }
      while (j < other.edges.size()) {
        final Edge e = other.edges.get(j);
        if ((e.fromComponent() != this && e.fromComponent() != other)
          || (e.toComponent() != this && e.toComponent() != other)
        ) {
          break;
        }
        j++;
      }
      if (j < other.edges.size() && (i >= edges.size()
        || edges.get(i).weight() > other.edges.get(j).weight())
      ) {
        newEdges.add(other.edges.get(j++).replaceComponent(other,
          this));
      } else if (i < edges.size()) {
        newEdges.add(edges.get(i++).replaceComponent(other, this));
      }
    }
    other.edges.clear();
    edges.clear();
    edges = newEdges;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Component)) {
      return false;
    }

    final Component component = (Component) o;

    if (nodeId != component.nodeId()) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return nodeId;
  }
}
