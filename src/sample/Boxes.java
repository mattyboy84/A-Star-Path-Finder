package sample;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jdk.nashorn.internal.runtime.Undefined;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Boxes {
    int interation = 0;
    Rectangle rectangle;
    Random rand = new Random();
    Boolean wall;
    int currentNode;
    int arrayPos;
    int height;
    int width;
    int startPos;
    int endPos;
    int hCost;
    int gCost;
    int fCost;
    int previous;
    Boolean calculated;
    //|||||||||||||||||||||||||||||||||
    Scanner scanner = new Scanner(System.in);
    int lowestFVal = 1000000000;
    int lowestFPos;
    ArrayList<Path> path = new ArrayList<>();


    public Boxes(int i, int j, int arrayPos, int WIDTH, int HEIGHT, int RES, int xLength, int yLength, Group group, Scene scene, ArrayList<Boxes> boxes, ArrayList<OpenSet> openSet, ArrayList<ClosedSet> closedSet) {
        this.rectangle = new Rectangle((WIDTH - (WIDTH - (RES * i))), (HEIGHT - (HEIGHT - (RES * j))), RES, RES);
        this.calculated = false;
        this.arrayPos = arrayPos;
        this.height = RES;
        this.width = RES;
        switch (rand.nextInt(2)) {
            case 0:
                this.rectangle.setFill(Color.WHITE);
                this.wall = false;
                break;
            case 1:
                this.rectangle.setFill(Color.BLACK);
                this.wall = true;
                break;
        }
        this.rectangle.setOnMouseClicked(event -> {
            switch (event.getButton()) {
                case PRIMARY://sets start point
                    this.rectangle.setFill(Color.GREEN);
                    break;
                case SECONDARY://sets end point
                    this.rectangle.setFill(Color.RED);
                    codeStuff(i, j, arrayPos, WIDTH, HEIGHT, RES, xLength, yLength, group, scene, boxes, openSet, closedSet);
                    break;
                case MIDDLE:
                    if (this.rectangle.getFill() == Color.WHITE) {//for making own path
                        this.rectangle.setFill(Color.BLACK);
                        this.wall = true;
                    } else if (this.rectangle.getFill() == Color.BLACK) {//for making own path
                        this.rectangle.setFill(Color.WHITE);
                        this.wall = false;
                    }
                    if (this.rectangle.getFill() == Color.BLUEVIOLET || this.rectangle.getFill() == Color.BLUE) {//debug
                        System.out.println("Looking at Node: " + this.arrayPos + " It came from " + this.previous);
                    }
                    break;
            }
        });

        group.getChildren().add(this.rectangle);
    }

    private void codeStuff(int i, int j, int arrayPos, int WIDTH, int HEIGHT, int RES, int xLength, int yLength, Group group, Scene scene, ArrayList<Boxes> boxes, ArrayList<OpenSet> openSet, ArrayList<ClosedSet> closedSet) {
        for (int k = 0; k < boxes.size(); k++) {
            if (boxes.get(k).getRectangle().getFill() == Color.GREEN) {
                startPos = k;
            } else if (boxes.get(k).getRectangle().getFill() == Color.RED) {
                endPos = k;
            }
        }
        System.out.println("start at " + startPos + " End at " + endPos);

        currentNode = startPos;
        codeStuffSub(i, j, arrayPos, WIDTH, HEIGHT, RES, xLength, yLength, group, scene, boxes, openSet, closedSet);


    }

    private void codeStuffSub(int i, int j, int arrayPos, int WIDTH, int HEIGHT, int RES, int xLength, int yLength, Group group, Scene scene, ArrayList<Boxes> boxes, ArrayList<OpenSet> openSet, ArrayList<ClosedSet> closedSet) {
        if (currentNode != endPos) {
            if (!(boxes.get(currentNode).getRectangle().getFill() == Color.GREEN || boxes.get(currentNode).getRectangle().getFill() == Color.RED)) {
                boxes.get(currentNode).getRectangle().setFill(Color.BLUE);
            }
            //System.out.println("currentNode changed to: " + currentNode);
            interation++;
            //System.out.println("--------------------------------------------------------------------------------------------");
            //System.out.println("Current node: " + currentNode);
            openSet.add(new OpenSet(currentNode + 1, openSet, boxes, closedSet));
            openSet.add(new OpenSet(currentNode - 1, openSet, boxes, closedSet));
            openSet.add(new OpenSet(currentNode + yLength, openSet, boxes, closedSet));
            openSet.add(new OpenSet(currentNode - yLength, openSet, boxes, closedSet));//adds neighbour nodes to the open set
            openSet.add(new OpenSet(currentNode + yLength + 1, openSet, boxes, closedSet));
            openSet.add(new OpenSet(currentNode + yLength - 1, openSet, boxes, closedSet));
            openSet.add(new OpenSet(currentNode - yLength + 1, openSet, boxes, closedSet));
            openSet.add(new OpenSet(currentNode - yLength - 1, openSet, boxes, closedSet));
            closedSet.add(new ClosedSet(currentNode, openSet, boxes, closedSet));
            for (int k = 0; k < openSet.size(); k++) {

                if (openSet.get(k).getOpenPos() == 696969) {
                    openSet.remove(openSet.get(k));
                    // System.out.println("Bad: " + openSet.get(k).getOpenPos() + " At pos: " + k);
                }

                try {
                    if (!(boxes.get(openSet.get(k).getOpenPos()).getRectangle().getFill() == Color.RED || boxes.get(openSet.get(k).getOpenPos()).getRectangle().getFill() == Color.GREEN)) {
                        boxes.get(openSet.get(k).getOpenPos()).getRectangle().setFill(Color.DEEPSKYBLUE);//Looking around the node
                    }
                } catch (Exception e) {

                }
            }
            //G cost - how far away from start node
            //H cost - how far away from end node
            //F cost - G+H
            for (int k = 0; k < openSet.size(); k++) {
                if (!boxes.get(openSet.get(k).getOpenPos()).calculated) {
                    boxes.get(openSet.get(k).getOpenPos()).sethCost(openSet.get(k).getOpenPos(), startPos, endPos, currentNode, boxes, openSet, closedSet);//sends an open position for its h cost to be calculated
                    boxes.get(openSet.get(k).getOpenPos()).setgCost(openSet.get(k).getOpenPos(), startPos, endPos, currentNode, boxes, openSet, closedSet);//sends an open position for its g cost to be calculated
                    boxes.get(openSet.get(k).getOpenPos()).setfCost(boxes, openSet.get(k).getOpenPos(), boxes.get(openSet.get(k).getOpenPos()).getgCost(), boxes.get(openSet.get(k).getOpenPos()).gethCost(), currentNode);//sends an open position for its g cost to be calculated
                }
            }
            for (OpenSet set : openSet) {
                //System.out.println("Position of " + set.getOpenPos() + " F cost of: " + boxes.get(set.getOpenPos()).getfCost());
                if (boxes.get(set.getOpenPos()).getfCost() < lowestFVal) {
                    lowestFVal = boxes.get(set.getOpenPos()).getfCost();
                    lowestFPos = set.getOpenPos();
                }
            }
            //System.out.println("lowest F is: " + lowestFVal + " at position: " + lowestFPos);
            currentNode = lowestFPos;

            for (int k = 0; k < openSet.size(); k++) {
                if (openSet.get(k).getOpenPos() == currentNode) {
                    openSet.remove(openSet.get(k));
                }
            }

            lowestFVal = 1000000000;


            codeStuffSub(i, j, arrayPos, WIDTH, HEIGHT, RES, xLength, yLength, group, scene, boxes, openSet, closedSet);

        } else {
            System.out.println("found it :O");
            System.out.println("Found after " + interation + " Interations");
            System.out.println("Open set contains " + openSet.size() + " positions");
            System.out.println("Closed set contains " + closedSet.size() + " positions");

            //Go find the path
            int temp = currentNode;
            while (boxes.get(temp).previous > 0) {
                path.add(new Path(boxes.get(temp).previous));
                temp = boxes.get(temp).previous;

            }
            for (Path value : path) {
                if (boxes.get(value.getPath()).getRectangle().getFill() != Color.GREEN && boxes.get(value.getPath()).getRectangle().getFill() != Color.RED)
                    boxes.get(value.getPath()).getRectangle().setFill(Color.PURPLE);
            }
        }
    }

    public int gethCost() {
        return hCost;
    }

    public void sethCost(int lookAtNode, int startPos, int endPos, int currentNode, ArrayList<Boxes> boxes, ArrayList<OpenSet> openSet, ArrayList<ClosedSet> closedSet) {
        //H cost - how far away from end node
        /*
        System.out.println("-------------");
        System.out.println("current node is " + currentNode);
        System.out.println("start is " + startPos + " end is: " + endPos);
        System.out.println("looking at node " + lookAtNode);
        System.out.println("-------------");
        */
        //System.out.println((boxes.get(currentNode).getRectangle().getBoundsInParent()));
        //System.out.println((boxes.get(endPos).getRectangle().getBoundsInParent()));
        int dy = (int) Math.abs((boxes.get(lookAtNode).getRectangle().getBoundsInParent().getMinY()) - (boxes.get(endPos).getRectangle().getBoundsInParent().getMinY()));
        int dx = (int) Math.abs((boxes.get(lookAtNode).getRectangle().getBoundsInParent().getMinX()) - (boxes.get(endPos).getRectangle().getBoundsInParent().getMinX()));
        //System.out.println("dx: " + dx + " dy: " + dy);
        int distance = (int) Math.sqrt((dx * dx) + (dy * dy));
        //System.out.println("(H Cost) Distance from end node: " + distance);
        // System.out.println("------------------------------------");

        //this.hCost = distance;
        this.hCost = distance;
    }

    public int getgCost() {
        return gCost;
    }

    public void setgCost(int lookAtNode, int startPos, int endPos, int currentNode, ArrayList<Boxes> boxes, ArrayList<OpenSet> openSet, ArrayList<ClosedSet> closedSet) {
        //G cost - how far away from start node
        /*
        System.out.println("-------------");
        System.out.println("current node is " + currentNode);
        System.out.println("start is " + startPos + " end is: " + endPos);
        System.out.println("looking at node " + lookAtNode);
        System.out.println("-------------");
        */
        int dy = (int) Math.abs((boxes.get(lookAtNode).getRectangle().getBoundsInParent().getMinY()) - (boxes.get(startPos).getRectangle().getBoundsInParent().getMinY()));
        int dx = (int) Math.abs((boxes.get(lookAtNode).getRectangle().getBoundsInParent().getMinX()) - (boxes.get(startPos).getRectangle().getBoundsInParent().getMinX()));
        //System.out.println("dx: " + dx + " dy: " + dy);
        int distance = (int) Math.sqrt((dx * dx) + (dy * dy));
        //System.out.println("(G Cost) Distance from start node: " + distance);
        //System.out.println("------------------------------------");

        this.gCost = distance;
    }

    public int getfCost() {
        return fCost;
    }

    public void setfCost(ArrayList<Boxes> boxes, int node, int gCost, int hCost, int currentNode) {
        boxes.get(node).calculated = true;
        this.fCost = gCost + hCost;
        this.previous = currentNode;
        //System.out.println(this.fCost);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
