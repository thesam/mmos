package se.timberline.mmos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
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
    private Sprite sprite;
    private Vector2 direction = new Vector2(0,100);

    @Override
	public void create () {
        camera = new OrthographicCamera(1900,1000);
		batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
		img = new Texture("badlogic.jpg");
        sprite = new Sprite(img);
        sprite.setOrigin(sprite.getWidth()/2,sprite.getHeight()/2);
        sprite.setPosition(10 - sprite.getWidth()/2,10-sprite.getHeight()/2);
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
        camera.position.set(sprite.getX(),sprite.getY(),0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
		batch.begin();
//		batch.draw(img, 0, 0);
        sprite.draw(batch);
		batch.end();
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1);
//        shapeRenderer.triangle(30,30,30,40,50,50);
        if (currentPosition != null) {
            for (PlanetMessage planet : planets) {
                shapeRenderer.circle(planet.x, planet.y, 5);
            }
        }
        shapeRenderer.end();

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT) {
            sprite.rotate(1);
            direction.rotate(1);
        }
        if(keycode == Input.Keys.RIGHT) {
            sprite.rotate(-1);
            direction.rotate(-1);
        }
        if(keycode == Input.Keys.UP) {
            float x = sprite.getX();
            float y = sprite.getY();
            sprite.setX(x + direction.x);
            sprite.setY(y + direction.y);
        }
//        if(keycode == Input.Keys.DOWN)
//            camera.translate(0,32);
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
