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

public class MaureenNoSquare {
    public static void main(String[] args) {
        int n = 8;
        Solver cp = Factory.makeSolver(false);
        IntVar[] q = Factory.makeIntVarArray(cp, 25, 100);

                System.out.println(Arrays.toString(q));
                System.out.println(Factory.sum(q[0],q[1]));
//
        for(int i=0;i<25;i++) {
            q[i].remove(0);
        }
        int i=0;
        cp.post(Factory.isEqual(Factory.sum(q[i],q[1+i],q[2+i],q[3+i],q[4+i]),50));
        i=4;
        cp.post(Factory.isEqual(Factory.sum(q[i],q[1+i],q[2+i],q[3+i],q[4+i]),50));
        i=9;
        cp.post(Factory.isEqual(Factory.sum(q[i],q[1+i],q[2+i],q[3+i],q[4+i]),50));
        i=14;
        cp.post(Factory.isEqual(Factory.sum(q[i],q[1+i],q[2+i],q[3+i],q[4+i]),50));
        i=19;
        cp.post(Factory.isEqual(Factory.sum(q[i],q[1+i],q[2+i],q[3+i],q[4+i],q[5+i]),50));
//        i=40;
//        cp.post(Factory.isEqual(Factory.sum(q[0+i],q[1+i],q[2+i],q[3+i],q[4+i],q[5+i],q[6+i],q[7+i]),26));
//        i=48;
//        cp.post(Factory.isEqual(Factory.sum(q[0+i],q[1+i],q[2+i],q[3+i],q[4+i],q[5+i],q[6+i],q[7+i]),14));
//        i=56;
//        cp.post(Factory.isEqual(Factory.sum(q[0+i],q[1+i],q[2+i],q[3+i],q[4+i],q[5+i],q[6+i],q[7+i]),27));

//        i=7;
        i=4;
        int[] vert=new int[8];
//        vert= new int[]{20, 28, 33, 26, 27, 23, 31, 20};
        vert= new int[]{50, 50, 50, 50, 50, 50, 50, 50};
        for(int j=0;j<4;j++)
        {
            cp.post(Factory.isEqual(Factory.sum(q[j], q[i + 1 + j], q[2 * i + 2 + j], q[3 * i + 3 + j], q[4 * i + 4 + j]), vert[j]));
        }
        cp.post(Factory.allDifferent(q));
//        j=1;
//        cp.post(Factory.isEqual(Factory.sum(q[j],q[i+1+j],q[2*i+2+j],q[3*i+3+j],q[4*i+4+j],q[6*i+5+j],q[7*i+6+j],q[8*i+7+j]),86));




//        cp.post(Factory.notEqual(q[i], q[j], j - i));

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