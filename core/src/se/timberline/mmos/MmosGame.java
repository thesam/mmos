package se.timberline.mmos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import se.timberline.mmos.api.PlanetMessage;
import se.timberline.mmos.api.PositionMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class MmosGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
    private ShapeRenderer shapeRenderer;
    private List<PlanetMessage> planets;

    @Override
	public void create () {
		batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
		img = new Texture("badlogic.jpg");
        planets = new ArrayList<PlanetMessage>();
        Socket server = null;
        try {
            server = new Socket("localhost", 50000);
            final ObjectInputStream serverIn = new ObjectInputStream(server.getInputStream());
//            final ObjectOutputStream serverOut = new ObjectOutputStream(server.getOutputStream());
            System.out.println("Server connected");
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("In thread");
                            try {
                                PositionMessage msg = (PositionMessage) serverIn.readObject();
                                System.out.println(msg.x);
                                System.out.println(msg.y);
                                planets.add((PlanetMessage) serverIn.readObject());
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

	@Override
	public void render () {
//		Gdx.gl.glClearColor(1, 0, 0, 1);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
//        camera.update();
//        shapeRenderer.setProjectionMatrix(camera.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (PlanetMessage planet : planets) {
            shapeRenderer.setColor(1, 1, 0, 1);
            shapeRenderer.circle(planet.x, planet.y, 5);
        }
        shapeRenderer.end();

    }
}
