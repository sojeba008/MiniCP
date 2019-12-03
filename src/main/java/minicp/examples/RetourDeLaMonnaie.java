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

/**
 * The N-Queens problem.
 * <a href="http://csplib.org/Problems/prob054/">CSPLib</a>.
 */

public class RetourDeLaMonnaie {
    public static void main(String[] args) {
        int n = 5;
        int T=1000;
        int P=250;
        Solver cp = Factory.makeSolver(false);
        IntVar [] q = new IntVar[n];
        int borne=(int) ((T-P)/200);

        q[0] = Factory.makeIntVar(cp, 0,borne );
         borne=(int) ((T-P)/100);
//         System.out.println((borne));
        q[1] = Factory.makeIntVar(cp, 0, borne);
         borne=(int) ((T-P)/50);
        q[2] = Factory.makeIntVar(cp, 0, borne);
         borne=(int) ((T-P)/25);
        q[3] = Factory.makeIntVar(cp, 0, borne);
        q[4] = Factory.makeIntVar(cp, T-P, T-P);
//        System.out.println(q[4]);



        cp.post(Factory.lessOrEqual(Factory.sum(Factory.mul(q[0],200),Factory.mul(q[1],100),Factory.mul(q[2],50),Factory.mul(q[3],25)),q[4]));
        cp.post(Factory.largerOrEqual(Factory.sum(Factory.mul(q[0],200),Factory.mul(q[1],100),Factory.mul(q[2],50),Factory.mul(q[3],25)),q[4]));

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