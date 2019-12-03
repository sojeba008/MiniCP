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

import static minicp.cp.BranchingScheme.*;
import static minicp.cp.Factory.makeDfs;

/**
 * The N-Queens problem.
 * <a href="http://csplib.org/Problems/prob054/">CSPLib</a>.
 */

public class Autralia {
    public static void main(String[] args) {
        int n = 7;
        Solver cp = Factory.makeSolver(false);
        IntVar[] q = Factory.makeIntVarArray(cp, n, 0,2);
//      Domain:{Rouge=2,vert=1,bleu=2}

//      WA=0,NT=1,SA=2,Q=3,NSW=4,V=5,T=6
        cp.post(Factory.notEqual(q[0],q[1]));  //WA  != NT
        cp.post(Factory.notEqual(q[0],q[2]));  //WA  != SA
        cp.post(Factory.notEqual(q[1],q[2]));  //NT  != SA
        cp.post(Factory.notEqual(q[1],q[3]));  //NT  != Q
        cp.post(Factory.notEqual(q[2],q[3]));  //SA  != Q
        cp.post(Factory.notEqual(q[2],q[4]));  //SA  != NSW
        cp.post(Factory.notEqual(q[2],q[5]));  //SA  != V
        cp.post(Factory.notEqual(q[4],q[3]));  //NSW != Q
        cp.post(Factory.notEqual(q[4],q[5]));  //NSW != V


    DFSearch search = makeDfs(cp,firstFail(q));
        search.onSolution(() ->
                System.out.println("solution:" + afficherVilleCouleur(q))
        );
        SearchStatistics stats = search.solve(statistics -> statistics.numberOfSolutions() == 1000);
        System.out.format("#Solutions: %s\n", stats.numberOfSolutions());
        System.out.format("Statistics: %s\n", stats);
    }
     static String afficherVilleCouleur(IntVar[] tab){
        String[] couleur= new String[]{"Red", "Green", "Blue"};
        String[] ville= new String[]{"WA", "NT", "SA","Q","NSW","V","T"};
        String sortie="";
        for (int i=0;i<tab.length;i++)
        {
            String temp="";
            temp=tab[i].toString().replace("{","");
            temp=temp.replace("}","");
            sortie+=(" "+ville[i]+":"+couleur[Integer.parseInt(temp)]);
        }
        return sortie;
    }
}