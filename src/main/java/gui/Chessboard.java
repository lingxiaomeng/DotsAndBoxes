package gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;


class Chessboard extends GridPane {
    private AI_method ai;
    private String A_name = "Player A";
    private String B_name = "Player B";
    private int step = 0;
    private int n;
    private int m;
    private Mode mode;
    private boolean finished = false;
    private Board_state matrix[][];
    private ArrayList<Button> buttons;
    private ArrayList<Label> labels;
    private Label label_win = new Label();
    private Label A_label = new Label();
    private Label B_label = new Label();
    private boolean aifirst = false;
    private Button undo = new Button("上一步");
    private Stack<State> stack = new Stack<>();

    void setAifirst(boolean aifirst) {
        this.aifirst = aifirst;
    }

    void setAi(AI_method ai) {
        this.ai = ai;
    }

    enum Mode {
        PVP, PVC, CVC
    }

    private void setMatrix(Board_state[][] board_state) {
        this.matrix = new Board_state[board_state.length][board_state[0].length];
        for (int i = 0; i < board_state.length; i++) {
            System.arraycopy(board_state[i], 0, this.matrix[i], 0, board_state[0].length);
        }
    }

    Chessboard() {
        this.setAlignment(Pos.CENTER);
        this.setHgap(10);
        this.setVgap(10);
        this.setPadding(new Insets(10, 10, 10, 10));
    }

    void generateMatrix(int m, int n, Mode mode) {
        this.buttons = new ArrayList<>();
        this.labels = new ArrayList<>();
        this.stack = new Stack<>();
        this.step = 0;
        GridPane board = new GridPane();
        this.m = m;
        this.n = n;
        this.mode = mode;
        this.getChildren().clear();
        n = n * 2 + 1;
        m = m * 2 + 1;
        Board_state a[][] = new Board_state[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i % 2 == 0 && j % 2 == 1) {
                    a[i][j] = Board_state.Button;
                    Button button = new Button("");
                    buttons.add(button);
                    button.setMaxSize(15, 50);
                    button.setMinSize(15, 50);
                    button.setId(generateId(i, j));
                    buttonAction(button);
                    board.add(button, i, j);
                } else if (i % 2 == 1 && j % 2 == 0) {
                    a[i][j] = Board_state.Button;
                    Button button = new Button("");
                    buttons.add(button);
                    button.setId(generateId(i, j));
                    button.setMaxSize(50, 15);
                    button.setMinSize(50, 15);
                    buttonAction(button);
                    board.add(button, i, j);
                } else if (i > 0 && i < m - 1 && j > 0 && j < n - 1 && i % 2 == 1) {
                    a[i][j] = Board_state.BigLabel;
                    Label label = new Label("");
                    label.setId(generateId(i, j));
                    label.setMaxSize(50, 50);
                    label.setMinSize(50, 50);
                    labels.add(label);
                    board.add(label, i, j);
                } else {
                    a[i][j] = Board_state.smallLabel;
                    Label label = new Label();
                    label.setId(generateId(i, j));
                    label.setMinSize(15, 15);
                    label.setMaxSize(15, 15);
                    label.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
                    board.add(label, i, j);
                }
            }
        }
        this.add(board, 0, 0, m, n);
        this.add(A_label, m, 0);
        this.add(B_label, m, 2);
        this.add(label_win, m, 4);
        this.add(undo, m, 5);
        this.matrix = a;
        this.undo.setOnAction(event -> {
            if (stack.size() > 1)
                this.stack.pop();
            State state = this.stack.peek();
            setMatrix(state.getBoard_state());
            check();
            this.step = state.getStep();

        });

        stack.push(new State(this.step, this.matrix));
        check();
        if (!(this.mode == Mode.PVP))
            ai.setBoardstates(this.matrix);
        if (this.mode == Mode.CVC)
            board.setOnMouseClicked(event -> {
                Vector<Integer> v = this.getxy();
                if (v.size() > 0) {
                    int x = v.get(0);
                    int y = v.get(1);

                    for (Button b : buttons
                    ) {
                        if (b.getId().equals(this.generateId(x, y))) {
                            if (step % 2 == 0) {
                                matrix[x][y] = Board_state.A_Button;
                            } else {
                                matrix[x][y] = Board_state.B_button;
                            }
                        }
                    }
                    this.check();
                    step++;
                    stack.push(new State(this.step, this.matrix));
                }
            });
    }

    private void buttonAction(Button button) {
        button.setOnAction(event -> {
//            this.printmatrix();
            int i = this.getid(button.getId()).get(0);
            int j = this.getid(button.getId()).get(1);

            if (mode == Mode.PVP) {
                if (step % 2 == 0) {
                    matrix[i][j] = Board_state.A_Button;
                } else matrix[i][j] = Board_state.B_button;
                this.check();
                stack.push(new State(this.step, this.matrix));
                step++;
            } else if (mode == Mode.PVC) {
                if (aifirst) {
                    if (step % 2 == 1) {
                        matrix[i][j] = Board_state.B_button;
                        this.check();
                        step++;
                    }
                    new Thread(() -> {
                        while (step % 2 == 0) {
                            if (AIAction(Board_state.A_Button)) break;
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    stack.push(new State(this.step, this.matrix));

                } else {
                    if (step % 2 == 0) {
                        matrix[i][j] = Board_state.A_Button;
                        this.check();
                        step++;
                    }

                    new Thread(() -> {
                        while (step % 2 == 1) {
                            if (AIAction(Board_state.B_button)) break;
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }).start();
                    stack.push(new State(this.step, this.matrix));
                }
            }
        });
    }

    private boolean AIAction(Board_state board_state) {

        Vector<Integer> v = this.getxy();
        if (v.size() == 0) return true;
        int x = v.get(0);
        int y = v.get(1);
        for (Button b : buttons
        ) {
            if (b.getId().equals(this.generateId(x, y))) {
                matrix[x][y] = board_state;
            }
        }
        Platform.runLater(this::check);
        step++;
        return false;
    }

    private void check() {
        boolean have = false;
        for (Button b : buttons
        ) {
            String id = b.getId();
            int x = getid(id).get(0);
            int y = getid(id).get(1);
            if (matrix[x][y] == Board_state.A_Button) {
                b.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));
                b.setDisable(true);
            } else if (matrix[x][y] == Board_state.B_button) {
                b.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
                b.setDisable(true);
            } else if (matrix[x][y] == Board_state.Button) {
                b.setBackground(new Background(new BackgroundFill(null, null, null)));
                b.setDisable(false);
            }
        }

        for (Label label : labels) {
            String id = label.getId();
            int i = getid(id).get(0);
            int j = getid(id).get(1);
            if (matrix[i][j] == Board_state.BigLabel)
                if (matrix[i][j + 1].equals(Board_state.Pressed) && matrix[i][j - 1].equals(Board_state.Pressed) &&
                        matrix[i + 1][j].equals(Board_state.Pressed) && matrix[i - 1][j].equals(Board_state.Pressed)) {
                    have = true;
                    if (step % 2 == 0)
                        matrix[i][j] = Board_state.A_Label;
                    else matrix[i][j] = Board_state.B_Label;
                }
            if (matrix[i][j] == Board_state.A_Label) {
                label.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));
            } else if (matrix[i][j] == Board_state.B_Label) {
                label.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
            } else if (matrix[i][j] == Board_state.BigLabel) {
                label.setBackground(new Background(new BackgroundFill(null, null, null)));
            }

        }
        if (have) step++;
        checkFinished();
    }

    private void checkFinished() {
        int a = 0;
        int b = 0;
        int no = 0;
        for (Board_state[] aMatrix : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (aMatrix[j] == Board_state.A_Label)
                    a++;
                if (aMatrix[j] == Board_state.B_Label)
                    b++;
                if (aMatrix[j] == Board_state.BigLabel)
                    no++;
            }
        }
        A_label.setText(String.format("%s得分: %d", A_name, a));
        B_label.setText(String.format("%s得分: %d", B_name, b));
        if (no == 0) {
            this.finished = true;
            this.label_win.setText(String.format("%s win", a > b ? A_name : (a == b ? "nobody" : B_name)));
        } else {
            this.finished = false;
            this.label_win.setText("");
        }
    }

    private String generateId(int i, int j) {
        return String.format("%d,%d", i, j);
    }

    private Vector<Integer> getid(String id) {
        int i = Integer.parseInt(id.split(",")[0]);
        int j = Integer.parseInt(id.split(",")[1]);
        Vector<Integer> vector = new Vector<>();
        vector.add(i);
        vector.add(j);
        return vector;
    }

    private Vector<Integer> getxy() {
        ai.setBoardstates(matrix);
        return ai.getxy();
    }

    private void printmatrix() {
        for (Board_state[] aMatrix : matrix) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(aMatrix[j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
