package se.timberline.mmos;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import se.timberline.mmos.api.Message;
import se.timberline.mmos.api.PlanetMessage;
import se.timberline.mmos.api.PositionMessage;
import se.timberline.mmos.model.Position;
import se.timberline.mmos.server.LocalServer;

import java.util.ArrayList;
import java.util.List;

public class MmosGame extends ApplicationAdapter implements InputProcessor{
    public static final int ROTATION_SPEED = 100;
    public static final int SPEED_DELTA = 100;
    public static final int VIEWPORT_WIDTH = 1900;
    public static final int VIEWPORT_HEIGHT = 1000;

    SpriteBatch batch;
	Texture shipImage;
    private ShapeRenderer shapeRenderer;
    private List<Sprite> planets;
    private LocalServer server;
    OrthographicCamera camera;
    private Sprite sprite;
    private Vector2 direction = new Vector2(0,1);
    private int speed = 0;
    private boolean turningLeft;
    private boolean turningRight;
    private TiledDrawable background;
    private List<Sprite> bg = new ArrayList<>();
    private BitmapFont font;
    private SpriteBatch textBatch;
    private Position currentSector;
    private Texture planetTexture;

    @Override
	public void create () {
        font = new BitmapFont();
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		batch = new SpriteBatch();
        textBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
		shipImage = new Texture("ship.png");
        Texture starsTexture = new Texture("stars.png");
        planetTexture = new Texture("planet.png");
        int cols = (VIEWPORT_WIDTH / 160) + 1;
        int rows = (VIEWPORT_HEIGHT / 160) + 1;
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                Sprite sp = new Sprite(starsTexture);
                sp.setBounds(x*160-VIEWPORT_WIDTH/2,y*160-VIEWPORT_HEIGHT/2,160,160);
                bg.add(sp);
            }
        }
        background = new TiledDrawable(new TextureRegion(starsTexture));
        sprite = new Sprite(shipImage);
        sprite.setBounds(0,0,160,160);
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
            currentSector = new Position(pmsg.x,pmsg.y);
        } else if (msg instanceof PlanetMessage) {
            Sprite planet = new Sprite(planetTexture);
            PlanetMessage planetMessage = (PlanetMessage) msg;
            planet.setBounds(planetMessage.x,planetMessage.y,160,160);
            planets.add(planet);
        }
    }

    @Override
	public void render () {
        update();
        draw();

    }

    private void update() {
        float delta = Gdx.graphics.getDeltaTime();
        Vector2 distance = direction.cpy().scl(speed * delta);
        sprite.setX(sprite.getX() + distance.x);
        sprite.setY(sprite.getY() + distance.y);
        if (turningLeft) {
            sprite.rotate(ROTATION_SPEED *delta);
            direction.rotate(ROTATION_SPEED*delta);
        }
        if (turningRight) {
            sprite.rotate(-ROTATION_SPEED*delta);
            direction.rotate(-ROTATION_SPEED*delta);
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.set(sprite.getX() + sprite.getWidth() /2,sprite.getY() + sprite.getHeight()/2,0);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
//        background.draw(batch,0,0,500,500);
//		batch.draw(shipImage, 0, 0);
        for (Sprite bgs : bg) {
            bgs.draw(batch);
        }
        if (currentSector != null) {
            for (Sprite planet : planets) {
                planet.draw(batch);
            }
        }
        sprite.draw(batch);
        batch.end();
        textBatch.begin();
        font.draw(textBatch, "Position: " + sprite.getX() +", "+ sprite.getY()+ "\n" + "Sector: " + currentSector.x + ", " + currentSector.y, 10, 40);
        textBatch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.LEFT) {
            turningLeft = true;
        }
        if(keycode == Input.Keys.RIGHT) {
            turningRight = true;
        }
        if(keycode == Input.Keys.UP) {
            speed += SPEED_DELTA;
        }
        if(keycode == Input.Keys.DOWN) {
            speed -= SPEED_DELTA;
        }
        if(keycode == Input.Keys.SPACE) {
            speed = 0;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT) {
            turningLeft = false;
        }
        if(keycode == Input.Keys.RIGHT) {
            turningRight = false;
        }
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
