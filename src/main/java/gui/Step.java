package gui;

public enum Step {
    A, B;

    Step reverse() {
        if (this == A) return B;
        else return A;
    }
}
