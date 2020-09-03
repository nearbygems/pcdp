package kz.pcdp.сoncurrent.boruvka;

public abstract class Edge<C extends Component> {

  public abstract double weight();

  public abstract Edge<C> replaceComponent(final C from, final C to);

  public abstract C fromComponent();

  public abstract C toComponent();

  public abstract C getOther(final C component);
}
