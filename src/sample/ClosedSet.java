package sample;

import java.util.ArrayList;

public class ClosedSet {
    int closePos;

    public ClosedSet(int input, ArrayList<OpenSet> openSet, ArrayList<Boxes> boxes, ArrayList<ClosedSet> closedSet){
        this.closePos = input;

    }


    public int getClosePos() {
        return closePos;
    }

    public void setClosePos(int closePos) {
        this.closePos = closePos;
    }
}
