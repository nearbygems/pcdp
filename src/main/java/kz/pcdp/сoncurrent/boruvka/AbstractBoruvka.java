package kz.pcdp.сoncurrent.boruvka;

import java.util.Queue;

public abstract class AbstractBoruvka<C extends Component> {

  public abstract void computeBoruvka(final Queue<C> nodesLoaded,
                                      final SolutionToBoruvka<C> solution);
}
