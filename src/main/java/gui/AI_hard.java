package gui;

import java.util.Vector;

public class AI_hard extends AI_method {
    private int x;
    private int y;

    private Board_state state[][];
    private int[][] chessArray;


    //private int playerId ;

    AI_hard() {
    }

    @Override
    public Vector<Integer> getxy() {
        Vector<Integer> vector = new Vector<>();
        vector.add(this.x);
        vector.add(this.y);
        return vector;
    }
}
