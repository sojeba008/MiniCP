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
import minicp.search.Objective;
import minicp.search.SearchStatistics;

import java.util.Arrays;

import static minicp.cp.BranchingScheme.*;
import static minicp.cp.Factory.*;

/**
 * The N-Queens problem.
 * <a href="http://csplib.org/Problems/prob054/">CSPLib</a>.
 */

public class Acierie {
    public static void main(String[] args) {
        int n = 4;
        Solver cp = Factory.makeSolver(false);
        IntVar [] q = new IntVar[n];
        q[0] = Factory.makeIntVar(cp, 0, 6000); //b
        q[1] = Factory.makeIntVar(cp, 0, 4000); //r
        q[2] = Factory.makeIntVar(cp, 0, 40);    //hb
        q[3] = Factory.makeIntVar(cp, 0, 40);  //hr


        cp.post(Factory.Equal(q[0],mul(q[2],200)));
        cp.post(Factory.Equal(q[1],mul(q[3],140)));






        IntVar fonction;
        fonction = sum(mul(q[0],25),mul(q[1],30));
        Objective obj = cp.maximize(fonction);


        DFSearch dfs = makeDfs(cp, firstFail(q));

        dfs.onSolution(() ->
                System.out.println("solution:" + Arrays.toString(q)+" with objective:" + fonction.min())
        );

        SearchStatistics stats = dfs.optimize(obj);
//        dfs.onSolution(() -> System.out.println("objective:" + obj));


        System.out.format("#Solutions: %s\n", stats.numberOfSolutions());
        System.out.format("Statistics: %s\n", stats);
    }
}