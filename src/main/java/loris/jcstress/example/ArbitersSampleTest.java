/**
 * 
 */
package loris.jcstress.example;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Arbiter;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.I_Result;

/*
 * Another flavor of the same test as ArbitersSampleTest is using arbiters. Arbiters run after both
 * actors, and therefore can observe the final result.
 * 
 * This allows to directly observe the atomicity failure:
 * 
 * [OK] ArbitersSampleTest 
 * (JVM args: [-server]) 
 * Observed state     Occurrences    Expectation                    Interpretation 
 * 1                  940,359        ACCEPTABLE_INTERESTING         One update lost: atomicity failure. 
 * 2                  168,950,601    ACCEPTABLE                     Actors updated independently.
 * 
 * How to run this test: $ java -jar jcstress-samples/target/jcstress.jar -t ArbitersSampleTest
 */
@JCStressTest

// These are the test outcomes.
@Outcome(id = "1", expect = Expect.ACCEPTABLE_INTERESTING,
        desc = "One update lost: atomicity failure.")
@Outcome(id = "2", expect = Expect.ACCEPTABLE, desc = "Actors updated independently.")
@State
public class ArbitersSampleTest {
    int v;

    @Actor
    public void actor1() {
        v++;
    }

    @Actor
    public void actor2() {
        v++;
    }

    @Arbiter
    public void arbiter(I_Result r) {
        r.r1 = v;
    }
}
