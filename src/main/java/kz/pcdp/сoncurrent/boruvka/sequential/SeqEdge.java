package kz.pcdp.сoncurrent.boruvka.sequential;

import kz.pcdp.сoncurrent.boruvka.Edge;

public final class SeqEdge extends Edge<SeqComponent> implements Comparable<Edge> {

  protected SeqComponent fromComponent;

  protected SeqComponent toComponent;

  public double weight;

  protected SeqEdge(final SeqComponent from, final SeqComponent to, final double w) {
    fromComponent = from;
    toComponent = to;
    weight = w;
  }

  @Override
  public SeqComponent fromComponent() {
    return fromComponent;
  }

  @Override
  public SeqComponent toComponent() {
    return toComponent;
  }

  @Override
  public double weight() {
    return weight;
  }

  public SeqComponent getOther(final SeqComponent from) {
    if (fromComponent == from) {
      assert (toComponent != from);
      return toComponent;
    }

    if (toComponent == from) {
      assert (fromComponent != from);
      return fromComponent;
    }
    assert (false);
    return null;

  }

  @Override
  public int compareTo(final Edge e) {
    if (e.weight() == weight) {
      return 0;
    } else if (weight < e.weight()) {
      return -1;
    } else {
      return 1;
    }
  }

  public SeqEdge replaceComponent(final SeqComponent from,
                                  final SeqComponent to) {
    if (fromComponent == from) {
      fromComponent = to;
    }
    if (toComponent == from) {
      toComponent = to;
    }
    return this;
  }
}
