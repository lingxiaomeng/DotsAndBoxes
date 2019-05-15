package gui;

import java.awt.*;
import java.util.Vector;

public class AI_Medium extends AI_method {
    private int x;
    private int y;

    AI_Medium() {
    }

    void search() {
        for (int i = 0; i < board_states.length; i++) {
            for (int j = 0; j < board_states[0].length; j++) {
                if (board_states[i][j] == Board_state.Button) {

                }
            }
        }
    }

    boolean check(int i, int j, int pi, int pj) {
        return false;
    }


    @Override
    public String toString() {
        return "中级";
    }

    @Override
    public Vector<Integer> getxy() {
        Vector<Integer> vector = new Vector<>();
        vector.add(this.x);
        vector.add(this.y);
        return vector;
    }

//    private void setMatrix(Board_state[][] board_state) {
//        Board_state matrix[][] = new Board_state[board_state.length][board_state[0].length];
//        for (int i = 0; i < board_state.length; i++) {
//            System.arraycopy(board_state[i], 0, matrix[i], 0, board_state[0].length);
//        }
//    }
}
