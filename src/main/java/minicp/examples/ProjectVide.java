package minicp.examples;

import minicp.cp.Factory;
import minicp.engine.core.IntVar;
import minicp.engine.core.Solver;
import minicp.search.DFSearch;
import minicp.search.SearchStatistics;

import java.util.Arrays;

import static minicp.cp.BranchingScheme.firstFail;
import static minicp.cp.Factory.makeDfs;
import static minicp.cp.Factory.makeIntVarArray;


public class ProjectVide {
    public static void main(String[] args) {
        // 1- Solver
        //----Exemple----------------------------------------------
        Solver cp = Factory.makeSolver(false);
        //---------------------------------------------------------

        //  2- Variables impliquées dans la modélisation du problème
        //---Exemple de deux variable A et B de domaines [1:7]------
        IntVar A = Factory.makeIntVar(cp, 1,7);
        IntVar B = Factory.makeIntVar(cp, 1,7);
        //----------------------------------------------------------

        //   3- Les contraintes
        //--------Exemple: A>2 et B<=4------------------------------
        cp.post(Factory.isLarger(A,2));
        cp.post(Factory.isLessOrEqual(B,4));
        //----------------------------------------------------------


        IntVar[] q = new IntVar[2];
        q[0]=A;
        q[1]=B;


        //  4- Resolution par la recherche
        DFSearch search = makeDfs(cp,firstFail(q));
        search.onSolution(() ->
                System.out.println("solution:" + Arrays.toString(q))

        );
        SearchStatistics stats = search.solve(statistics -> statistics.numberOfSolutions() == 1000);

        System.out.format("#Solutions: %s\n", stats.numberOfSolutions());
        System.out.format("Statistics: %s\n", stats);

    }

}