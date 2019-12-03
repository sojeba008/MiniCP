package minicp.examples;

import minicp.cp.Factory;
import minicp.engine.core.IntVar;
import minicp.engine.core.Solver;
import minicp.search.DFSearch;
import minicp.search.SearchStatistics;

import java.util.Arrays;

import static minicp.cp.BranchingScheme.firstFail;
import static minicp.cp.Factory.makeDfs;

public class Sudoku {

    public static void main (String [] args){
        Solver cp = Factory.makeSolver();
        IntVar [][] q = new IntVar[9][9];

        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                q[i][j] = Factory.makeIntVar(cp, 1, 9);
            }
        }

        //Line Constraint
        for (int i = 0; i < 9; i++){
            cp.post(Factory.allDifferent(q[i]));
        }

        //Column Constraint
        for (int i = 0; i < 9; i++){
            IntVar [] column = new IntVar[q.length];
            for (int j = 0; j < 9; j++){
                column[j] = q[j][i];
            }
            cp.post(Factory.allDifferent(column));
        }

        //Cell Constraint
        for (int i = 0; i < 9; i+=3){
            for (int j = 0; j < 9; j+=3){
                IntVar [] cell = new IntVar[9];
                int step = 0;
                for (int k = 0; k < 3; k++){
                    for (int l = 0; l < 3; l++){
                        cell[step] = q[i+k][j+l];
                        step++;
                    }
                }
                cp.post(Factory.allDifferent(cell));
            }
        }

        //All Value
        IntVar [] all_values = new IntVar[81];
        for(int i = 0; i < q.length; i++){
            System.arraycopy(q[i], 0, all_values, i*q.length, q.length);
        }

        //Search
        DFSearch dfs = makeDfs(cp, firstFail(all_values));

        dfs.onSolution(() -> {
                    for (int i = 0; i < 9; i++) {
                        System.out.println(Arrays.toString(q[i]));
                    }
                    System.out.println("");
                }
        );

        SearchStatistics stats = dfs.solve(stat -> stat.numberOfSolutions() >= 100); // stop on first solution

        System.out.println(stats);

    }
}


