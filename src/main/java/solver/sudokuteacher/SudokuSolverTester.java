package solver.sudokuteacher;
import solver.sudokuteacher.SudokuCompenents.Sudoku;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SudokuSolverTester {
    public static void main(String[] args) throws IOException {
        double totalStartTime = System.nanoTime();

        boolean solveSingle = false;
        boolean writeToFile = false;

        ArrayList<String> sudokusUnsolved = new ArrayList<>();

        if (solveSingle) {
            String sudokuString = "901500046425090081860010020502000000019000460600000002196040253200060817000001694";
            Sudoku sudokuLinear = new Sudoku(sudokuString);

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

                    double startTime = System.nanoTime();

                    Sudoku sudokuLinear = new Sudoku(data);
                    try {
                        if (sudokuLinear.solve()) {
                            double endTime = System.nanoTime();
                            double duration = (endTime - startTime) / 1000;
                            averageSolveTime += duration;
                            countSolved++;
                            //sudokusUnsolved.add(data);
                        } else {
                            countUnsolved++;
                            if (sudokuLinear.containsInvalidCell()) {
                                countContainingInvalidSquare++;
                                //sudokusUnsolved.add(data);
                            }
                            sudokusUnsolved.add(data);
                        }
                    }catch (StackOverflowError e){
                        System.out.println(data);
                    }
                }

            } while (myReader.hasNextLine());


            if(writeToFile) {
                FileWriter writer = new FileWriter("unsolved");
                for (String unsolvedSudoku : sudokusUnsolved) {
                    writer.write(unsolvedSudoku + System.lineSeparator());
                }
                writer.close();
            }

            double totalEndTime = System.nanoTime();
            double totalDuration = (totalEndTime - totalStartTime) / 1000000000;

            System.out.println("Number of Sudokus solved: " + countSolved);
            System.out.println("Number of Sudokus unsolved: " + countUnsolved);
            System.out.println("Number of Sudokus incorrect: " + countIncorrect);
            System.out.println("Number of Sudokus with invalid Squares: " + countContainingInvalidSquare);
            System.out.println("Average Solve Time: " + (averageSolveTime / countSolved));
            System.out.println("Total Program Time: " + totalDuration);

        }
    }
}
