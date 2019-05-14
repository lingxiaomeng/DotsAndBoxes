package gui;

abstract class AI_method implements AI {

    Board_state[][] board_states;

    AI_method() {

    }

    void setBoard_states(Board_state[][] board_states) {
        this.board_states = board_states;
    }
}
