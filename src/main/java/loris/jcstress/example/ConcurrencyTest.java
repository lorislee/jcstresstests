/*
 * Copyright (c) 2017, Red Hat Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted
 * provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this list of conditions
 * and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this list of
 * conditions and the following disclaimer in the documentation and/or other materials provided with
 * the distribution.
 *
 * * Neither the name of Oracle nor the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package loris.jcstress.example;

import org.openjdk.jcstress.annotations.Actor;
import org.openjdk.jcstress.annotations.Expect;
import org.openjdk.jcstress.annotations.JCStressTest;
import org.openjdk.jcstress.annotations.Outcome;
import org.openjdk.jcstress.annotations.State;
import org.openjdk.jcstress.infra.results.II_Result;

// See jcstress-samples or existing tests for API introduction and testing guidelines

/*
 * This is our first concurrency test. It is deliberately simplistic to show testing approaches,
 * introduce JCStress APIs, etc.
 * 
 * Suppose we want to see if the field increment is atomic. We can make test with two actors, both
 * actors incrementing the field and recording what value they observed into the result object. As
 * JCStress runs, it will invoke these methods on the objects holding the field once per each actor
 * and instance, and record what results are coming from there.
 * 
 * Done enough times, we will get the history of observed results, and that would tell us something
 * about the concurrent behavior. For example, running this test would yield:
 * 
 * [OK] ConcurrencyTest (JVM args: [-server]) Observed state Occurrences Expectation Interpretation
 * 1, 1 54,734,140 ACCEPTABLE Both threads came up with the same value: atomicity failure. 
 * 1, 2 47,037,891 ACCEPTABLE actor1 incremented, then actor2. 
 * 2, 1 53,204,629 ACCEPTABLE actor2 incremented, then actor1.
 * 
 * How to run this test: $ java -jar jcstresstests/target/jcstress.jar -t ConcurrencyTest
 */

// Mark the class as JCStress test.
@JCStressTest
// Outline the outcomes here. The default outcome is provided, you need to remove it:
@Outcome(id = "1, 1", expect = Expect.ACCEPTABLE_INTERESTING,
        desc = "Both actors came up with the same value: atomicity failure.")
@Outcome(id = "1, 2", expect = Expect.ACCEPTABLE, desc = "actor1 incremented, then actor2.")
@Outcome(id = "2, 1", expect = Expect.ACCEPTABLE, desc = "actor2 incremented, then actor1.")
@State
public class ConcurrencyTest {

    int v;

    @Actor
    public void actor1(II_Result r) {
        // Put the code for first thread here
        r.r1 = ++v; // record result from actor1 to field r1
    }

    @Actor
    public void actor2(II_Result r) {
        // Put the code for second thread here
        r.r2 = ++v; // record result from actor2 to field r2
    }

}
