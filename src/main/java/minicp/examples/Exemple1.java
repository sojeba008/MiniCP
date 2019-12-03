/*
 * mini-cp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License  v3
 * as published by the Free Software Foundation.
 *
 * mini-cp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY.
 * See the GNU Lesser General Public License  for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mini-cp. If not, see http://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * Copyright (c)  2018. by Laurent Michel, Pierre Schaus, Pascal Van Hentenryck
 */

package minicp.examples;

import minicp.cp.Factory;
import minicp.engine.core.IntVar;
import minicp.engine.core.Solver;
import minicp.search.DFSearch;
import minicp.search.SearchStatistics;

import java.util.Arrays;

import static minicp.cp.BranchingScheme.*;
import static minicp.cp.Factory.makeDfs;

/**
 * The N-Queens problem.
 * <a href="http://csplib.org/Problems/prob054/">CSPLib</a>.
 */

public class Exemple1 {
    public static void main(String[] args) {
        int n = 3;
        Solver cp = Factory.makeSolver(false);
        IntVar[] q = Factory.makeIntVarArray(cp, n, 1,3);


        cp.post(Factory.lessOrEqual(Factory.sum(q[0],q[1]),q[2]));  //NSW != V
        cp.post(Factory.largerOrEqual(Factory.sum(q[0],q[1]),q[2]));  //NSW != V

//        DFSearch search = makeDfs(cp,firstFail(q));
        DFSearch search = Factory.makeDfs(cp, () -> {
            IntVar qs = selectMin(q,
                    qi -> qi.size() > 1,
                    qi -> qi.size());
            if (qs == null) return EMPTY;
            else {
                int v = qs.min();
                return branch(() -> Factory.equal(qs, v),
                        () -> Factory.notEqual(qs, v));
            }
        });
        search.onSolution(() ->
                System.out.println("solution:" + Arrays.toString(q))
        );

        SearchStatistics stats = search.solve(statistics -> statistics.numberOfSolutions() == 1000);

        System.out.format("#Solutions: %s\n", stats.numberOfSolutions());
        System.out.format("Statistics: %s\n", stats);
    }
}