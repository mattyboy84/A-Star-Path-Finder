package sample;

import javafx.scene.paint.Color;

import java.util.ArrayList;

public class OpenSet {
    int openPos;
    boolean repeat = false;

    public OpenSet(int input, ArrayList<OpenSet> openSet, ArrayList<Boxes> boxes, ArrayList<ClosedSet> closedSet) {
        try {
            //System.out.println("input sent: " + input + " size of " + openSet.size());
            for (OpenSet set : openSet) {
                if ((input == set.getOpenPos())) {//stops a repeat
                    repeat = true;
                    break;
                }
            }
            for (ClosedSet set : closedSet) {//stops a closed node being entered as an open node
                if (input == set.getClosePos()) {
                    repeat = true;
                    break;
                }
            }


            if (!(boxes.get(input).wall) && !repeat) {//stops walls from being entered as open nodes
                this.openPos = input;
                repeat = false;
            } else {
                openSet.remove(input);
                /*
                this.openPos = 696969;
                for (int j = 0; j < openSet.size(); j++) {
                    if (openSet.get(j).getOpenPos() == 696969) {
                        openSet.remove(openSet.get(j));

                    }
                }
                */
            }
        }catch (Exception e){//is there is an array index out of bounds, it will be removed from the best

            this.openPos = 696969;
            for (int j = 0; j < openSet.size(); j++) {
                if (openSet.get(j).getOpenPos() == 696969) {
                    openSet.remove(openSet.get(j));
                }
            }
        }
    }

    public int getOpenPos() {
        return openPos;
    }

    public void setOpenPos(int openPos) {
        this.openPos = openPos;
    }
}
