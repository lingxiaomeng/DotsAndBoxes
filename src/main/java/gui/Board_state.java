package gui;

public enum Board_state {
    Button, smallLabel, BigLabel, A_Label, B_Label, Pressed, A_Button, B_button;

    boolean equals(Board_state boardstate) {
        if (this == A_Button || this == B_button) {
            return boardstate == Pressed;
        }
        return false;
    }
}
