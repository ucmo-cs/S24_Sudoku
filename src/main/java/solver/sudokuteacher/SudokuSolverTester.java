package solver.sudokuteacher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SudokuSolverTester {
    public static void main(String[] args) throws IOException {
        int[][] testBoard = new int[9][9];

        boolean solveSingle = false;
        boolean writeToFile = false;

        ArrayList<String> sudokusUnsolved = new ArrayList<>();

        if (solveSingle) {
            String sudokuString = "850603914600501382103840657030087460006135708780406030000358106318764000560010803";
            int index = 0;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (sudokuString.charAt(index) == '.') {
                        testBoard[i][j] = 0;
                    }else {
                        testBoard[i][j] = Integer.parseInt(String.valueOf(sudokuString.charAt(index)));
                    }
                    index++;
                }
            }
            Sudoku sudokuLinear = new Sudoku(testBoard);

            if (sudokuLinear.solve()) {sudokuLinear.display();
            } else {
                System.out.println("Sudoku cannot be solved");
                if(sudokuLinear.containsInvalidCell()){
                    System.out.println("Sudoku has invalid cell");
                }
                sudokuLinear.display();
            }

        } else {
            File unsolvedSudokus = new File("src/main/resources/solver/sudokuteacher/puzzles2_17_clue");
            Scanner myReader = new Scanner(unsolvedSudokus);
            int countSolved = 0;
            int countUnsolved = 0;
            int countIncorrect = 0;
            int countContainingInvalidSquare = 0;
            double averageSolveTime = 0;

            do {
                String data = myReader.nextLine();
                if (data.startsWith("#") || data.startsWith(" ") || data.startsWith("\n")) {
                } else {

                    int index = 0;
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            if (data.charAt(index) == '.') {
                                testBoard[i][j] = 0;
                            } else {

                                testBoard[i][j] = data.charAt(index) - '0';
                            }
                            index++;
                        }
                    }

                    double startTime = System.nanoTime();

                    Sudoku sudokuLinear = new Sudoku(testBoard);
                    if (sudokuLinear.solve()) {
                        double endTime = System.nanoTime();
                        double duration = (endTime - startTime) / 1000;
                        averageSolveTime += duration;
                        countSolved++;
                        //sudokusUnsolved.add(data);
                    } else {
                        countUnsolved++;
                        if(sudokuLinear.containsInvalidCell()){
                          countContainingInvalidSquare++;
                          // sudokusUnsolved.add(data);
                        }
                        sudokusUnsolved.add(data);
                    }
                }
            } while (myReader.hasNextLine());

            if(writeToFile) {
                FileWriter writer = new FileWriter("solved");
                for (String unsolvedSudoku : sudokusUnsolved) {
                    writer.write(unsolvedSudoku + System.lineSeparator());
                }
                writer.close();
            }

            System.out.println("Number of Sudokus solved: " + countSolved);
            System.out.println("Number of Sudokus unsolved: " + countUnsolved);
            System.out.println("Number of Sudokus incorrect: " + countIncorrect);
            System.out.println("Number of Sudokus with invalid Squares: " + countContainingInvalidSquare);
            System.out.println("Average Solve Time: " + (averageSolveTime / countSolved));

        }
        /*
        double startTime = System.nanoTime();

        Sudoku sudokuLinear = new Sudoku(testBoard);


        new SolveSudokuLinear(sudokuLinear);
        double endTime = System.nanoTime();

        double duration = (endTime - startTime);
        System.out.println("Linear Solver solved in: " + duration / 1000000);
        sudokuLinear.display();

         startTime = System.nanoTime();

        Sudoku sudokuMultiThread = new Sudoku(testBoard);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute( new SolveSudokuTask(sudokuMultiThread));
        executor.shutdown();
        while(!executor.isTerminated());

         endTime = System.nanoTime();

         duration = (endTime - startTime);
        System.out.println("MultiThread Solver solved in: " + duration / 1000000);
        sudokuMultiThread.display();

*/
    }
}
