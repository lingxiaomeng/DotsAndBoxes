package gui;

abstract class AI_method implements AI {

    Board_state[][] boardstates;

    AI_method() {

    }

    void setBoardstates(Board_state[][] boardstates) {
        this.boardstates = boardstates;
    }
}
