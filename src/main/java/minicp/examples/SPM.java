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

public class SPM {
    public static void main(String[] args) {
        int n = 8;
        Solver cp = Factory.makeSolver(false);
        IntVar[] q = Factory.makeIntVarArray(cp, 25, 100);

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
//                System.out.println("solution:" + Arrays.toString(q))
                        System.out.println("solution:" + voir(q)+"\n")
        );
        SearchStatistics stats = search.solve(statistics -> statistics.numberOfSolutions() == 1000);

        System.out.format("#Solutions: %s\n", stats.numberOfSolutions());
        System.out.format("Statistics: %s\n", stats);

    }

    public static String voir(IntVar[] tab){
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
