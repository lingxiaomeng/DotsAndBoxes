package gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Stack;

public class Dots_and_boxes extends Application {
    private TextField msize = new TextField();
    private TextField nsize = new TextField();
    private Label symbolx = new Label("X");
    private Button button = new Button("开始游戏");
    private Label label = new Label("棋盘大小:");
    private Label label1 = new Label("选择模式:");
    private Label message = new Label();

    private ArrayList<AI_method> ai_methods = new ArrayList<>();

    public static void main(String args[]) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
        ai_methods.add(new AI_easy());
        ai_methods.add(new AI_Medium());
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
        RadioButton Playerfirst = new RadioButton("玩家先手");
        ToggleGroup group1 = new ToggleGroup();
        Playerfirst.setSelected(true);
        AIfirst.setToggleGroup(group1);
        Playerfirst.setToggleGroup(group1);


        HBox hBox3 = new HBox();
        hBox3.getChildren().add(Playerfirst);
        hBox3.getChildren().add(AIfirst);
        hBox3.setSpacing(10);
        ObservableList<AI_method> options = FXCollections.observableArrayList(
                ai_methods);

        final ComboBox<AI_method> comboBox = new ComboBox<>(options);

        comboBox.getSelectionModel().selectFirst();
        HBox hBox4 = new HBox();
        hBox4.getChildren().add(new Label("选择难度:"));
        hBox4.getChildren().add(comboBox);
        hBox4.setSpacing(10);

        final ToggleGroup group = new ToggleGroup();
        pvp.setToggleGroup(group);
        pvc.setToggleGroup(group);
        cvc.setToggleGroup(group);
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (pvc.isSelected()) {
                grid.getChildren().remove(hBox3);
                grid.getChildren().remove(hBox4);
                grid.add(hBox3, 0, 2, 1, 1);
                grid.add(hBox4, 1, 2);
            } else if (cvc.isSelected()) {
                grid.getChildren().remove(hBox3);
                grid.getChildren().remove(hBox4);
                grid.add(hBox4, 1, 2);
            } else {
                grid.getChildren().remove(hBox3);
                grid.getChildren().remove(hBox4);
            }
        });
        grid.add(hBox1, 0, 0, 2, 1);
        grid.add(hBox2, 0, 1, 2, 1);
        grid.add(button, 0, 3, 2, 1);
        grid.add(message, 0, 4, 2, 1);
        message.setVisible(false);
        pvp.setSelected(true);

        Chessboard chessboard = new Chessboard();
        grid.add(chessboard, 4, 0, 1, 10);
        Scene scene = new Scene(grid, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
        button.setOnAction(event -> {
            try {
                int m = Integer.parseInt(msize.getText());
                int n = Integer.parseInt(nsize.getText());
                if (pvp.isSelected()) {
                    chessboard.generateMatrix(m, n, Chessboard.Mode.PVP);
                } else if (pvc.isSelected()) {
                    if (AIfirst.isSelected()) chessboard.setAifirst(true);
                    else chessboard.setAifirst(false);
                    if (comboBox.getValue() != null) {
                        chessboard.setAi(comboBox.getValue());
                        chessboard.generateMatrix(m, n, Chessboard.Mode.PVC);
                    }
                } else if (cvc.isSelected()) {
                    if (comboBox.getValue() != null) {
                        chessboard.setAi(comboBox.getValue());
                        chessboard.generateMatrix(m, n, Chessboard.Mode.CVC);
                    }
                }
            } catch (NumberFormatException exception) {

                Timeline error = new Timeline(new KeyFrame(Duration.seconds(0), e -> {
                    message.setTextFill(Color.RED);
                    message.setText("请输入正确的数字");
                    message.setVisible(true);
                }), new KeyFrame(Duration.seconds(1), e -> {
                    message.setVisible(false);
//                message.setText("请选择文件");
                }));
                error.setCycleCount(1);
                error.play();

            }
        });
    }
}

