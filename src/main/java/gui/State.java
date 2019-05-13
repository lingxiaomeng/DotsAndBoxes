package gui;

public class State {
    private int step;
    private Board_state board_state[][];

    public State(int step, Board_state[][] board_state) {
        this.step = step;
        this.board_state = board_state;
    }

    public int getStep() {
        return step;
    }

    public Board_state[][] getBoard_state() {
        return board_state;
    }
}
