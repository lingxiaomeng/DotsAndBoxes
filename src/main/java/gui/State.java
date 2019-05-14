package gui;

public class State {
    private int step;
    private Board_state board_state[][];

    State(int step, Board_state[][] board_state) {
        this.step = step;
        setBoard_state(board_state);
    }

    private void setBoard_state(Board_state[][] board_state) {
        this.board_state = new Board_state[board_state.length][board_state[0].length];
        for (int i = 0; i < board_state.length; i++) {
            System.arraycopy(board_state[i], 0, this.board_state[i], 0, board_state[0].length);
        }
    }

    int getStep() {
        return step;
    }

    Board_state[][] getBoard_state() {
        return board_state;
    }
}
