package gui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Vector;


class Chessboard extends GridPane {
    private AI_method ai;
    private String A_name;
    private String B_name;
    private Step step = Step.A;
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
    private Alert alert = new Alert(Alert.AlertType.INFORMATION);


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
        this.step = Step.A;
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
        if (this.mode == Mode.PVC) {
            ai.setBoard_states(this.matrix);
            if (aifirst) {
                buttons.get(0).fire();
                A_name = "Computer";
                B_name = "Player";
            } else {
                A_name = "Player";
                B_name = "Computer";
            }
        } else if (this.mode == Mode.CVC) {
            A_name = "Computer A";
            B_name = "Computer B";
            ai.setBoard_states(this.matrix);
            board.setOnMouseClicked(event -> {
                Vector<Integer> v = this.getxy();
                if (v.size() > 0) {
                    int x = v.get(0);
                    int y = v.get(1);

                    for (Button b : buttons
                    ) {
                        if (b.getId().equals(this.generateId(x, y))) {
                            if (step == Step.A) {
                                matrix[x][y] = Board_state.A_Button;
                            } else if (step == Step.B) {
                                matrix[x][y] = Board_state.B_button;
                            }
                        }
                    }
                    if (!this.check()) this.step = this.step.reverse();
                    stack.push(new State(this.step, this.matrix));
                }
            });
        } else {
            A_name = "Player A";
            B_name = "Player B";
        }


        stack.push(new State(this.step, this.matrix));
        check();
    }

    private void buttonAction(Button button) {
        button.setOnMouseEntered(event -> {
            if (step == Step.A) {
                button.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));
            } else button.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
        });
        button.setOnMouseExited(event -> {
            int i = this.getId(button.getId()).get(0);
            int j = this.getId(button.getId()).get(1);
            if (matrix[i][j] == Board_state.Button)
                button.setBackground(new Background(new BackgroundFill(null, null, null)));
        });
        button.setOnAction(event -> {
//            this.printmatrix();
            int i = this.getId(button.getId()).get(0);
            int j = this.getId(button.getId()).get(1);

            if (mode == Mode.PVP) {
                if (step == Step.A) {
                    matrix[i][j] = Board_state.A_Button;
                } else if (step == Step.B) matrix[i][j] = Board_state.B_button;
                if (!this.check()) this.step = this.step.reverse();
                stack.push(new State(this.step, this.matrix));
            } else if (mode == Mode.PVC) {
                if (aifirst) {

                    if (step == Step.B) {
                        matrix[i][j] = Board_state.B_button;
                        if (!this.check()) this.step = this.step.reverse();
                    }
                    new Thread(() -> {
                        while (step == Step.A) {
                            if (AIAction(Board_state.A_Button)) break;
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        stack.push(new State(this.step, this.matrix));
                    }).start();

                } else {
                    if (step == Step.A) {
                        matrix[i][j] = Board_state.A_Button;
                        if (!this.check()) this.step = this.step.reverse();
                    }
                    new Thread(() -> {
                        while (step == Step.B) {
                            if (AIAction(Board_state.B_button)) break;
                            try {
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        stack.push(new State(this.step, this.matrix));
                    }).start();

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
        Platform.runLater(() -> {
            if (!Chessboard.this.check()) Chessboard.this.step = Chessboard.this.step.reverse();
        });
        return false;
    }

    private boolean check() {
        boolean have = false;
        for (Button b : buttons
        ) {
            String id = b.getId();
            int x = getId(id).get(0);
            int y = getId(id).get(1);
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
            int i = getId(id).get(0);
            int j = getId(id).get(1);
            if (matrix[i][j] == Board_state.BigLabel)
                if (matrix[i][j + 1].equals(Board_state.Pressed) && matrix[i][j - 1].equals(Board_state.Pressed) &&
                        matrix[i + 1][j].equals(Board_state.Pressed) && matrix[i - 1][j].equals(Board_state.Pressed)) {
                    have = true;
                    if (step == Step.A)
                        matrix[i][j] = Board_state.A_Label;
                    else if (step == Step.B) matrix[i][j] = Board_state.B_Label;
                }
            if (matrix[i][j] == Board_state.A_Label) {
                label.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));
            } else if (matrix[i][j] == Board_state.B_Label) {
                label.setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
            } else if (matrix[i][j] == Board_state.BigLabel) {
                label.setBackground(new Background(new BackgroundFill(null, null, null)));
            }

        }
        checkFinished();
        return have;
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
            alert.setTitle("游戏结束");
            alert.setHeaderText(null);
            alert.setContentText(String.format("%s win", a > b ? A_name : (a == b ? "nobody" : B_name)));
            alert.show();
        } else {
            this.finished = false;
            this.label_win.setText("");
        }
    }

    private String generateId(int i, int j) {
        return String.format("%d,%d", i, j);
    }

    private Vector<Integer> getId(String id) {
        int i = Integer.parseInt(id.split(",")[0]);
        int j = Integer.parseInt(id.split(",")[1]);
        Vector<Integer> vector = new Vector<>();
        vector.add(i);
        vector.add(j);
        return vector;
    }

    private Vector<Integer> getxy() {
        ai.setBoard_states(matrix);
        return ai.getxy();
    }
//
//    private void printmatrix() {
//        for (Board_state[] aMatrix : matrix) {
//            for (int j = 0; j < matrix[0].length; j++) {
//                System.out.print(aMatrix[j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println();
//    }
}
