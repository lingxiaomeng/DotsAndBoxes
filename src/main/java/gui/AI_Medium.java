package gui;

import java.awt.*;
import java.util.Random;
import java.util.Vector;

public class AI_Medium extends AI_method {
    private int x;
    private int y;

    private int li;
    private int lj;

    AI_Medium() {

    }

    @Override
    void setBoard_states(Board_state[][] board_state) {
        super.setBoard_states(board_state);
        System.out.println();
        li = board_state.length;
        lj = board_state[0].length;
    }

    void search() {
        System.out.println("start");
//        super.printmatrix();
        for (int i = 0; i < super.board_states.length; i++) {
            for (int j = 0; j < super.board_states[0].length; j++) {
                if (super.board_states[i][j] == Board_state.BigLabel) {
                    int num = checknumber(i, j);
                    if (num == 3) {
                        System.out.println("yes");
                        if (board_states[i][j + 1] == Board_state.Button) {
                            this.x = i;
                            this.y = j + 1;
                        } else if (board_states[i][j - 1] == Board_state.Button) {
                            this.x = i;
                            this.y = j - 1;
                        } else if (board_states[i - 1][j] == Board_state.Button) {
                            this.x = i - 1;
                            this.y = j;
                        } else if (board_states[i + 1][j] == Board_state.Button) {
                            this.x = i + 1;
                            this.y = j;
                        }
                        return;
                    }
                }
            }
        }
        Random random = new Random();
        int x = 0;
        int y = 0;
        boolean finished = false;
        int i = 10;
        while (!finished) {
            x = random.nextInt(board_states.length);
            y = random.nextInt(board_states[0].length);
            finished = true;
            for (Board_state[] aMatrix : board_states) {
                for (int j = 0; j < board_states[0].length; j++) {
                    if (aMatrix[j] == Board_state.BigLabel) {
                        int num = checknumber(i, j);
                        if (num < 2) {
                            finished = false;
                            break;
                        }
                    }
                }
            }
            if ((board_states[x][y] == Board_state.Button)) {
//                if ()
                this.x = x;
                this.y = y;
                break;
            }
        }
    }

    private boolean check(int i, int j) {
        if (i >= 1 && j >= 2 && (i < li - 1) && board_states[i - 1][j - 1] == Board_state.Pressed && board_states[i + 1][j - 1] == Board_state.Pressed && board_states[i][j - 2] == Board_state.Pressed)
            return true;
        if (i >= 1 && j < lj - 2 && (i < li - 1) && board_states[i - 1][j + 1] == Board_state.Pressed && board_states[i + 1][j + 1] == Board_state.Pressed && board_states[i][j + 2] == Board_state.Pressed)
            return true;
        if (j >= 1 && i >= 2 && (j < lj - 1) && board_states[i - 1][j - 1] == Board_state.Pressed && board_states[i - 1][j + 1] == Board_state.Pressed && board_states[i - 2][j] == Board_state.Pressed)
            return true;
        if (j >= 1 && i < li - 2 && (j < lj - 1) && board_states[i + 1][j - 1] == Board_state.Pressed && board_states[i + 1][j + 1] == Board_state.Pressed && board_states[i + 2][j] == Board_state.Pressed)
            return true;
        return false;
    }

    private int checknumber(int i, int j) {
        int num = 0;
        if (board_states[i][j - 1].equals(Board_state.Pressed)) num++;
        if (board_states[i][j + 1].equals(Board_state.Pressed)) num++;
        if (board_states[i + 1][j].equals(Board_state.Pressed)) num++;
        if (board_states[i - 1][j].equals(Board_state.Pressed)) num++;
        return num;
    }


    @Override
    public String toString() {
        return "中级";
    }

    @Override
    public Vector<Integer> getxy() {
        search();
        Vector<Integer> vector = new Vector<>();
        vector.add(this.x);
        vector.add(this.y);
        System.out.println(vector);
        return vector;
    }


}
