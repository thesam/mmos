package se.timberline.mmos.api;

import java.io.Serializable;

public class PositionMessage implements Serializable {

    public final int x;
    public final int y;

    public PositionMessage(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
