package minicp.examples;

import minicp.cp.Factory;
import minicp.engine.core.IntVar;
import minicp.engine.core.Solver;
import minicp.search.DFSearch;
import minicp.search.SearchStatistics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static minicp.cp.BranchingScheme.firstFail;
import static minicp.cp.Factory.makeDfs;


public class SudokuFromFile {

    FileReader fileReader;
    int [][] grid;

    public SudokuFromFile(String fileName){
        fileReader = new FileReader(fileName);
        grid = fileReader.getSudoku_grid();
    }

    public void solve(){

        Solver cp = Factory.makeSolver();
        IntVar [][] q = new IntVar[9][9];

        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){

                if(grid[i][j] == 0){
                    q[i][j] = Factory.makeIntVar(cp, 1, 9);
                }
                else {
                    q[i][j] = Factory.makeIntVar(cp, grid[i][j], grid[i][j]);
                }
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


        DFSearch dfs = makeDfs(cp, firstFail(all_values));

        dfs.onSolution(() -> {
                    for (int i = 0; i < 9; i++) {
                        System.out.println(Arrays.toString(q[i]));
                    }
                    System.out.println("");
                }
        );

        SearchStatistics stats = dfs.solve(stat -> stat.numberOfSolutions() >= 1); // stop on first solution

        System.out.println(stats);


    }

    public static void main (String [] args){

        System.out.println(Paths.get("").toAbsolutePath().toString());
        final String fileName = Paths.get("").toAbsolutePath().toString()+"\\src\\main\\java\\minicp\\examples\\test.txt";
        SudokuFromFile sudoku = new SudokuFromFile(fileName);
        sudoku.solve();
    }





}


class FileReader  {

    String fileName;
    int [][] sudoku_grid;

    FileReader(String fileName){
        this.fileName = fileName;
        this.readSudokuFromFile();

    }

    public int[][] getSudoku_grid() {
        return sudoku_grid;
    }

    private void readSudokuFromFile(){
            String [][] grid = new String[9][9];
        try {
            List<String> allLines = Files.readAllLines(Paths.get(this.fileName));
            grid = new String[allLines.size()][allLines.size()];
            int cursor = 0;
            for (String line : allLines) {
                grid[cursor] = line.split(" ");
                cursor++;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int [][] sudoku_grid = new int[9][9];

        for(int i = 0; i < 9; i++){

            for(int j = 0; j <9; j++){

                if (grid[i][j].equalsIgnoreCase(".")){
                    sudoku_grid[i][j] = 0;
                }
                else {
                    sudoku_grid[i][j] = Integer.parseInt(grid[i][j]);
                }

            }
            this.sudoku_grid = sudoku_grid;
        }

    }


}





