package kz.pcdp.сoncurrent.boruvka;

public abstract class Component<C extends Component> {

  public abstract int nodeId();

  public abstract void addEdge(final Edge<C> e);

  public abstract double totalWeight();

  public abstract long totalEdges();
}
