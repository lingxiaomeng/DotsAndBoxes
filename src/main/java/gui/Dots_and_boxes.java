package gui;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Dots_and_boxes extends Application {
    private TextField msize = new TextField();
    private TextField nsize = new TextField();
    private Label symbolx = new Label("X");
    private Button button = new Button("开始游戏");
    private Label label = new Label("棋盘大小:");
    private Label label1 = new Label("选择模式:");

    public static void main(String args[]) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Dots and Boxes");
        GridPane grid = new GridPane();
        msize.setMaxWidth(50);
        nsize.setMaxWidth(50);
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        HBox hBox1 = new HBox();
        HBox hBox2 = new HBox();
        hBox1.setSpacing(10);
        hBox1.getChildren().add(label);
        hBox1.getChildren().add(msize);
        hBox1.getChildren().add(symbolx);
        hBox1.getChildren().add(nsize);

        hBox2.setSpacing(10);
        RadioButton pvp = new RadioButton("双人对战");
        RadioButton pvc = new RadioButton("人机对战");
        RadioButton cvc = new RadioButton("电脑对战");

        hBox2.getChildren().add(label1);
        hBox2.getChildren().add(pvp);
        hBox2.getChildren().add(pvc);
        hBox2.getChildren().add(cvc);

        RadioButton AIfirst = new RadioButton("电脑先手");
        RadioButton Peoplefirst = new RadioButton("玩家先手");
        ToggleGroup group1 = new ToggleGroup();
        AIfirst.setToggleGroup(group1);
        Peoplefirst.setToggleGroup(group1);


        HBox hBox3 = new HBox();
        hBox3.getChildren().add(AIfirst);
        hBox3.getChildren().add(Peoplefirst);
        hBox3.setSpacing(10);

        final ToggleGroup group = new ToggleGroup();
        pvp.setToggleGroup(group);
        pvc.setToggleGroup(group);
        cvc.setToggleGroup(group);
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (pvc.isSelected()) {
                hBox3.setVisible(true);
            } else {
                hBox3.setVisible(false);
            }
        });

        grid.add(hBox1, 0, 0);
        grid.add(hBox2, 0, 1);
        grid.add(hBox3, 0, 2);
        grid.add(button, 0, 3);
        pvp.setSelected(true);

        Chessboard chessboard = new Chessboard();
        grid.add(chessboard, 4, 0, 1, 10);
        Scene scene = new Scene(grid, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
        button.setOnAction(event -> {
            if (pvp.isSelected()) {
                chessboard.generateMatrix(Integer.parseInt(msize.getText()), Integer.parseInt(nsize.getText()), Chessboard.Mode.PVP);
            } else if (pvc.isSelected()) {
                if (AIfirst.isSelected()) chessboard.setAifirst(true);
                else chessboard.setAifirst(false);
                chessboard.setAi(new RandomAI());
                chessboard.generateMatrix(Integer.parseInt(msize.getText()), Integer.parseInt(nsize.getText()), Chessboard.Mode.PVC);
            } else if (cvc.isSelected()) {
                chessboard.generateMatrix(Integer.parseInt(msize.getText()), Integer.parseInt(nsize.getText()), Chessboard.Mode.CVC);
            }
        });
    }

//
//    private void printmatrix() {
//        for (int[] aMatrix : matrix) {
//            for (int j = 0; j < matrix[0].length; j++) {
//                System.out.print(aMatrix[j] + " ");
//            }
//            System.out.println();
//        }
//    }
}

