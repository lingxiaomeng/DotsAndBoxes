package gui;

import java.util.Random;
import java.util.Vector;

public class RandomAI extends AI_method implements AI {

    RandomAI() {

    }


    @Override
    public Vector<Integer> getxy() {
        Vector<Integer> vector = new Vector<>();
        Random random = new Random();
        int x = 0;
        int y = 0;
        boolean finished = false;
        while (!finished) {
            x = random.nextInt(boardstates.length);
            y = random.nextInt(boardstates.length);
//            System.out.println(boardstates[x][y]+" "+x+" "+y);
            finished = true;
            for (Board_state[] aMatrix : boardstates) {
                for (int j = 0; j < boardstates[0].length; j++) {
                    if (aMatrix[j] == Board_state.Button)
                        finished = false;
                }
            }
            if ((boardstates[x][y] == Board_state.Button)) {
                vector.add(x);
                vector.add(y);
                break;
            }

        }
//        System.out.println(vector);

        return vector;
    }

    private void printmatrix() {
        for (Board_state[] aMatrix : boardstates) {
            for (int j = 0; j < boardstates[0].length; j++) {
                System.out.print(aMatrix[j] + " ");
            }
            System.out.println();
        }
    }
}
