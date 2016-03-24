package se.timberline.mmos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import se.timberline.mmos.api.Message;
import se.timberline.mmos.api.PlanetMessage;
import se.timberline.mmos.api.PositionMessage;
import se.timberline.mmos.model.Position;
import se.timberline.mmos.server.LocalServer;

import java.util.ArrayList;
import java.util.List;

public class MmosGame extends ApplicationAdapter implements InputProcessor{
	SpriteBatch batch;
	Texture img;
    private ShapeRenderer shapeRenderer;
    private List<PlanetMessage> planets;
    private Position currentPosition;
    private LocalServer server;
    OrthographicCamera camera;

    @Override
	public void create () {
        camera = new OrthographicCamera(1900,1000);
		batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
		img = new Texture("badlogic.jpg");
        planets = new ArrayList<>();
        server = new LocalServer();
        server.connect(msg -> parseMessage(msg));
        Gdx.input.setInputProcessor(this);
    }

    private void parseMessage(Message msg) {
        if (msg instanceof PositionMessage) {
            PositionMessage pmsg = (PositionMessage) msg;
            currentPosition = new Position(pmsg.x,pmsg.y);
        } else if (msg instanceof PlanetMessage) {
            planets.add((PlanetMessage) msg);
        }
    }

    @Override
	public void render () {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		batch.begin();
//		batch.draw(img, 0, 0);
//		batch.end();
        camera.update();
//        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1);
        shapeRenderer.triangle(30,30,30,40,50,50);
        if (currentPosition != null) {
            for (PlanetMessage planet : planets) {
                shapeRenderer.circle(planet.x, planet.y, 5);
            }
            shapeRenderer.end();
        }

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT)
            camera.translate(-32,0);
        if(keycode == Input.Keys.RIGHT)
            camera.translate(32,0);
        if(keycode == Input.Keys.UP)
            camera.translate(0,-32);
        if(keycode == Input.Keys.DOWN)
            camera.translate(0,32);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
