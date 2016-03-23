package se.timberline.mmos;

import se.timberline.mmos.api.PlanetMessage;
import se.timberline.mmos.api.PositionMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class Server {

    public static final void main(String[] arg) throws IOException {
        ServerSocket socket = new ServerSocket(50000);
        while (true) {
            Socket client = socket.accept();
//            ObjectInputStream clientIn = new ObjectInputStream(client.getInputStream());
            ObjectOutputStream clientOut = new ObjectOutputStream(client.getOutputStream());
            System.out.println("Client connected");
            clientOut.writeObject(new PositionMessage(0,0));
            clientOut.writeObject(new PlanetMessage(10,10, "Centrus"));
            clientOut.flush();
            System.out.println("flushed");
        }
    }
}
