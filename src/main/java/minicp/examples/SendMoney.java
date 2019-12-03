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

public class SendMoney {
    public static void main(String[] args) {
        int n = 8;
        Solver cp = Factory.makeSolver(false);
        IntVar[] q = Factory.makeIntVarArray(cp, n, 0,9);

//        S=0,E=1,N=2,D=3,M=4,O=5,R=6,Y=7
        q[0].remove(0);
        q[4].remove(0);
        cp.post(Factory.allDifferent(q));
//        cp.post(Factory.lessOrEqual(Factory.sum(Factory.mul(Factory.sum(q[0], q[4]), 1000), Factory.mul(Factory.sum(q[1], q[5]), 100), Factory.mul(Factory.sum(q[2], q[6]), 10), Factory.sum(q[3], q[1])),
//                Factory.sum(Factory.mul(q[4], 10000), Factory.mul(q[5], 1000), Factory.mul(q[2], 100), Factory.mul(q[1], 10), q[7])));
//
//        cp.post(Factory.largerOrEqual(Factory.sum(Factory.mul(Factory.sum(q[0], q[4]), 1000), Factory.mul(Factory.sum(q[1], q[5]), 100), Factory.mul(Factory.sum(q[2], q[6]), 10), Factory.sum(q[3], q[1])),
//                Factory.sum(Factory.mul(q[4], 10000), Factory.mul(q[5], 1000), Factory.mul(q[2], 100), Factory.mul(q[1], 10), q[7])));

        cp.post(Factory.Equal(Factory.sum(Factory.mul(Factory.sum(q[0], q[4]), 1000), Factory.mul(Factory.sum(q[1], q[5]), 100), Factory.mul(Factory.sum(q[2], q[6]), 10), Factory.sum(q[3], q[1])),
                Factory.sum(Factory.mul(q[4], 10000), Factory.mul(q[5], 1000), Factory.mul(q[2], 100), Factory.mul(q[1], 10), q[7])));








                DFSearch dfs = makeDfs(cp, firstFail(q));

        dfs.onSolution(() ->
                System.out.println("solution:" + Arrays.toString(q))
        );


        SearchStatistics stats = dfs.solve(statistics -> statistics.numberOfSolutions() == 1000);

        System.out.format("#Solutions: %s\n", stats.numberOfSolutions());
        System.out.format("Statistics: %s\n", stats);

    }
}