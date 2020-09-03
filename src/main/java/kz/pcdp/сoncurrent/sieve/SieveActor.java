package kz.pcdp.сoncurrent.sieve;

import edu.rice.pcdp.Actor;

import java.util.HashSet;
import java.util.Set;

import static edu.rice.pcdp.PCDP.finish;

public final class SieveActor extends Sieve {

  @Override
  public int countPrimes(final int limit) {
    SieveActorActor sieveActor = new SieveActorActor();

    finish(() -> {
      for (int i = 2; i <= limit; i++) {
        sieveActor.send(i);
      }
      sieveActor.send(0);
    });

    int numPrimes = 0;
    SieveActorActor loopActor = sieveActor;
    while (loopActor != null) {
      numPrimes += loopActor.numLocalPrimes();
      loopActor = loopActor.nextActor;
    }

    return numPrimes;
  }

  public static final class SieveActorActor
    extends Actor {

    private static final int MAX_LOCAL_PRIMES = 1000;

    private Set<Integer> localPrimes = new HashSet<>(MAX_LOCAL_PRIMES);
    private SieveActorActor nextActor;

    @Override
    public void process(final Object msg) {
      Integer candidate = (Integer) msg;

      if (candidate <= 0) {
        if (this.nextActor != null) {
          this.nextActor.send(msg);
        }
      } else {
        boolean locallyPrime = isLocalPrime(candidate);

        if (locallyPrime) {
          if (this.localPrimes.size() < MAX_LOCAL_PRIMES) {
            this.localPrimes.add(candidate);
          } else {
            if (nextActor == null) {
              nextActor = new SieveActorActor();
            }

            nextActor.send(msg);
          }
        }
      }
    }

    boolean isLocalPrime(int candidate) {
      for (Integer prime : localPrimes) {
        if (candidate % prime == 0) {
          return false;
        }
      }
      return true;
    }

    public int numLocalPrimes() {
      return this.localPrimes.size();
    }
  }
}
