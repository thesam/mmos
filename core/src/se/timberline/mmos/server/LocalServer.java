package se.timberline.mmos.server;

import org.apache.commons.math3.random.MersenneTwister;
import se.timberline.mmos.api.Message;
import se.timberline.mmos.api.PlanetMessage;
import se.timberline.mmos.api.PositionMessage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

public class LocalServer {

    List<Consumer<Message>> messageConsumers = new ArrayList<>();

    public void connect(Consumer<Message> messageConsumer) {
        messageConsumers.add(messageConsumer);
        messageConsumer.accept(new PositionMessage(0,0));
        int[] sectorSeed = {0, 0};
        MersenneTwister twister = new MersenneTwister(sectorSeed);
        System.out.println(twister.nextInt());
        System.out.println(twister.nextInt());
        messageConsumer.accept(new PlanetMessage(10,10,"Foo"));
    }

}
