package kz.pcdp.сoncurrent.boruvka.parallel;

import kz.pcdp.сoncurrent.boruvka.BoruvkaFactory;
import kz.pcdp.сoncurrent.boruvka.ParBoruvka.ParComponent;
import kz.pcdp.сoncurrent.boruvka.ParBoruvka.ParEdge;

public final class ParBoruvkaFactory implements BoruvkaFactory<ParComponent, ParEdge> {

  @Override
  public ParComponent newComponent(final int nodeId) {
    return new ParComponent(nodeId);
  }

  @Override
  public ParEdge newEdge(final ParComponent from, final ParComponent to, final double weight) {
    return new ParEdge(from, to, weight);
  }
}
