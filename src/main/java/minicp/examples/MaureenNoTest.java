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

public class MaureenNoTest {
    public static void main(String[] args) {
        int n = 8;
        Solver cp = Factory.makeSolver(false);
        IntVar[] q = Factory.makeIntVarArray(cp, 2, 3);

//        q[1].remove(3);
//        q[1].remove(5);
//        q[1].remove(7);
//        q[1].remove(1);


        cp.post(Factory.allDifferent(q));



        DFSearch search = makeDfs(cp,firstFail(q));
        search.onSolution(() ->
                System.out.println("solution:" + Arrays.toString(q))
//                System.out.println("solution:" + voir(q)+"\n")
        );
        SearchStatistics stats = search.solve(statistics -> statistics.numberOfSolutions() == 1000);

        System.out.format("#Solutions: %s\n", stats.numberOfSolutions());
        System.out.format("Statistics: %s\n", stats);

    }

    static String voir(IntVar[] tab){
        String sortie="";
        for(int i=0;i<tab.length;i++) {
//                System.out.print(tab[i] + " ");
            if(i%5==0){
                sortie+="\n";
            }
                sortie+=(tab[i]) + " ";
//                System.out.println(" ");
            }
            return sortie;


    }
}