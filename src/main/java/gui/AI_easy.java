package gui;

import java.util.Random;
import java.util.Vector;

public class AI_easy extends AI_method implements AI {

    AI_easy() {

    }

    @Override
    public String toString() {
        return "简单";
    }

    @Override
    public Vector<Integer> getxy() {
        Vector<Integer> vector = new Vector<>();
        Random random = new Random();
        int x = 0;
        int y = 0;
        boolean finished = false;
        while (!finished) {
            x = random.nextInt(board_states.length);
            y = random.nextInt(board_states.length);
//            System.out.println(board_states[x][y]+" "+x+" "+y);
            finished = true;
            for (Board_state[] aMatrix : board_states) {
                for (int j = 0; j < board_states[0].length; j++) {
                    if (aMatrix[j] == Board_state.Button)
                        finished = false;
                }
            }
            if ((board_states[x][y] == Board_state.Button)) {
                vector.add(x);
                vector.add(y);
                break;
            }

        }
//        System.out.println(vector);

        return vector;
    }

    private void printmatrix() {
        for (Board_state[] aMatrix : board_states) {
            for (int j = 0; j < board_states[0].length; j++) {
                System.out.print(aMatrix[j] + " ");
            }
            System.out.println();
        }
    }
}
