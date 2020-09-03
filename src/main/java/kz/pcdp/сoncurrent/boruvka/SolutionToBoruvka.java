package kz.pcdp.сoncurrent.boruvka;

public final class SolutionToBoruvka<C extends Component> {

  private C solution = null;

  public void setSolution(final C setSolution) {
    assert (solution == null);
    assert (setSolution != null);
    this.solution = setSolution;
  }

  public C getSolution() {
    return solution;
  }
}
