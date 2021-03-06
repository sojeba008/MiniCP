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
import org.chocosolver.solver.constraints.nary.nValue.amnv.mis.F;

import java.util.Arrays;

import static minicp.cp.BranchingScheme.*;

/**
 * The N-Queens problem.
 * <a href="http://csplib.org/Problems/prob054/">CSPLib</a>.
 */

public class LeZebreDeLewis {
    public static void main(String[] args) {
        int n = 5;
        int T=1000;
        int P=250;
        Solver cp = Factory.makeSolver(false);
        IntVar [] N = new IntVar[n];  //{Espagnol Ukrainien Japonais Norvegien Anglais}
        IntVar [] C = new IntVar[n];  //{Rouge Verte Jaune Bleu Blanche}
        IntVar [] A = new IntVar[n];  //{Chien Renard Escargot Cheval Zebre}
        IntVar [] B = new IntVar[n];  //{Lait Café Thé Vin Eau}
        IntVar [] S = new IntVar[n];  //{Kools Cravens Gitanes Chesterfields OldGolds}

        IntVar [] Const = new IntVar[3];
        Const[0] = Factory.makeIntVar(cp,0,0);
        Const[1] = Factory.makeIntVar(cp,1,1);
        Const[2] = Factory.makeIntVar(cp,2,2);
        System.out.println(Const[2]);
        for(int i=0;i<n;i++){
            N[i] = Factory.makeIntVar(cp, 0,4);
            C[i] = Factory.makeIntVar(cp, 0,4);
            B[i] = Factory.makeIntVar(cp, 0,4);
            S[i] = Factory.makeIntVar(cp, 0,4);
            A[i] = Factory.makeIntVar(cp, 0,4);
        }


        for(int i=0;i<n;i++){
            cp.post(Factory.allDifferent(N));
            cp.post(Factory.allDifferent(C));
            cp.post(Factory.allDifferent(B));
            cp.post(Factory.allDifferent(S));
            cp.post(Factory.allDifferent(A));
        }


            //N3=0
        cp.post(Factory.lessOrEqual(N[3],Const[0]));
        cp.post(Factory.largerOrEqual(N[3],Const[0]));
            //C3=1
        cp.post(Factory.lessOrEqual(C[3],Const[1]));
        cp.post(Factory.largerOrEqual(C[3],Const[1]));
            //B0=2
        cp.post(Factory.lessOrEqual(B[0],Const[2]));
        cp.post(Factory.largerOrEqual(B[0],Const[2]));

            //N4=C0
        cp.post(Factory.Equal(N[4],C[0]));
            //C1=B1
        cp.post(Factory.Equal(C[1],B[1]));
            //C2=S0
        cp.post(Factory.Equal(C[2],S[0]));
            //C4=C1+1
        cp.post(Factory.Equal(C[4],Factory.plus(C[1], 1)));
            //N0=A0
        cp.post(Factory.Equal(N[0],A[0]));
            //N1=B2
        cp.post(Factory.Equal(N[1],B[2]));
            //N2=S1
        cp.post(Factory.Equal(N[2],S[1]));
            //S4=A2
        cp.post(Factory.Equal(S[4],A[2]));
            //S3+/-1=A[1]
        cp.post(Factory.Equal(Factory.minus(S[3],1),B[1]));
            //S0+/-1=A1
        cp.post(Factory.Equal(Factory.plus(S[0],1),A[3]));




        IntVar [] q = new IntVar[n*n];

//        Factory.abs()

        System.arraycopy(N,0,q,0,N.length);
        System.arraycopy(C,0,q,5,C.length);
        System.arraycopy(A,0,q,10,A.length);
        System.arraycopy(B,0,q,15,B.length);
        System.arraycopy(S,0,q,20,S.length);




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