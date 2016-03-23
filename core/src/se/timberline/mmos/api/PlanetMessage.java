package se.timberline.mmos.api;

import java.io.Serializable;

public class PlanetMessage implements Serializable {

    public final int x;
    public final int y;
    public final String name;

    public PlanetMessage(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }
}
