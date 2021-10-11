package sample;

import com.sun.javafx.geom.Vec2d;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.awt.*;
import java.util.ArrayList;

public class Main extends Application {
    //GREEN - START
    //RED - END
    //DARK BLUE - CURRENT NODE - ClosedSet
    //LIGHT BLUE - SURROUNDING NODES - OpenSet
    //PINK - OPTIONAL PATH - Path
    ArrayList<Boxes> boxes = new ArrayList<>();
    ArrayList<OpenSet> openSet = new ArrayList<>();
    ArrayList<ClosedSet> closedSet = new ArrayList<>();
    int arrayPos = 0;

    @Override
    public void start(Stage primaryStage) {
        int WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
        int HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
        int RES = 10;
        int xLength = WIDTH / RES;
        int yLength = HEIGHT / RES;
        Group group = new Group();
        Scene scene = new Scene(group, 1920, 1080);

        for (int i = 0; i < xLength; i++) {//creates the rectangles seen in the window
            for (int j = 0; j < yLength; j++) {
                boxes.add(new Boxes(i, j, arrayPos, WIDTH, HEIGHT, RES, xLength, yLength, group, scene, boxes,openSet,closedSet));
                arrayPos++;
            }
        }
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setFullScreen(true);

        scene.setOnKeyPressed(event -> {//debug stuff
            switch (event.getCode()){
                case E:
                    System.out.println("Open set");
                    for (OpenSet set : openSet) {
                        System.out.println(set.getOpenPos() +  " H Cost: " + boxes.get(set.getOpenPos()).gethCost() + " G Cost: " + boxes.get(set.getOpenPos()).getgCost() + " F Cost: " + boxes.get(set.getOpenPos()).getfCost());
                    }
                    break;
                case R:
                    System.out.println("Closed set " + closedSet.size());
                    for (ClosedSet set : closedSet) {
                        System.out.println(set.getClosePos());
                    }
                    break;
                case F:
                    primaryStage.setFullScreen(true);
                    break;
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
