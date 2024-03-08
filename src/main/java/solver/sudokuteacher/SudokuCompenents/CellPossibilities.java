package solver.sudokuteacher.SudokuCompenents;

public class CellPossibilities {


    int[] possibilities;
    int count;

    public CellPossibilities() {
        this.possibilities = new int[10];
        for (int i = 1; i <= 9; i++) {
            this.possibilities[i] = i;
        }
        this.count = 9;
    }

    public boolean contains(int i){

        if(i < 1 || i > 9){
            return false;
        }
        return possibilities[i] == i;
    }

    public boolean remove(int possible){
        if(possible < 1 || possible > 9){
            return false;
        }
        if(possibilities[possible] == possible){
            possibilities[possible] = 0;
            this.count--;
            return true;
        }else{
            return false;
        }
    }
    public int size(){
        return count;
    }
    public void clear(){
        for (Integer possible: possibilities) {
            possibilities[possible] = 0;
        }
        count = 0;
    }

    public int get(int number){
        number++;
        if(number < 1 || number > 9){
            return 0;
        }else{
            return possibilities[number];
        }
    }

    public int getLastPossible(){
        if(size() == 1){
            for (Integer possible: possibilities) {
                if(possible != 0){
                    return possible;
                }
            }
        }
        return 0;
    }

    public boolean containsAll(int[] cellPossibilities){
        for (int i = 1; i <= 9; i++) {
            if(possibilities[i] == cellPossibilities[i]){

            }
        }
        return false;
    }

    public int[] getPossibles() {
        return possibilities;
    }

}

