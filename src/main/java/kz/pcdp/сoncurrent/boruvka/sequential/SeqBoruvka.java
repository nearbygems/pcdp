package kz.pcdp.сoncurrent.boruvka.sequential;

import kz.pcdp.сoncurrent.boruvka.AbstractBoruvka;
import kz.pcdp.сoncurrent.boruvka.Edge;
import kz.pcdp.сoncurrent.boruvka.SolutionToBoruvka;

import java.util.Queue;

public final class SeqBoruvka extends AbstractBoruvka<SeqComponent> {

  public SeqBoruvka() {
    super();
  }

  @Override
  public void computeBoruvka(final Queue<SeqComponent> nodesLoaded,
                             final SolutionToBoruvka<SeqComponent> solution) {
    SeqComponent loopNode = null;

    while (!nodesLoaded.isEmpty()) {

      loopNode = nodesLoaded.poll();

      if (loopNode.isDead) {
        continue;
      }

      final Edge<SeqComponent> e = loopNode.getMinEdge();
      if (e == null) {
        break;
      }

      final SeqComponent other = e.getOther(loopNode);
      other.isDead = true;
      loopNode.merge(other, e.weight());
      nodesLoaded.add(loopNode);

    }

    solution.setSolution(loopNode);
  }
}
