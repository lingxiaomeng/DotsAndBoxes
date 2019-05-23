package gui;

abstract class AI_method implements AI {

    Board_state[][] board_states;

    AI_method() {

    }

    void setBoard_states(Board_state[][] board_state) {
        Board_state matrix[][] = new Board_state[board_state.length][board_state[0].length];
        for (int i = 0; i < board_state.length; i++) {
            System.arraycopy(board_state[i], 0, matrix[i], 0, board_state[0].length);
        }
        board_states = matrix;
    }

    protected void printmatrix() {
        for (Board_state[] aMatrix : board_states) {
            for (int j = 0; j < board_states[0].length; j++) {
                System.out.print(aMatrix[j] + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }
}
