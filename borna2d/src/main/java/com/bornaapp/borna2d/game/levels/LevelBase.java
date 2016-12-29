package com.bornaapp.borna2d.game.levels;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bornaapp.borna2d.OnScreenDisplay;
import com.bornaapp.borna2d.asset.Assets;
import com.bornaapp.borna2d.game.maps.Map;
import com.bornaapp.borna2d.game.maps.OrthogonalMap;
import com.bornaapp.borna2d.graphics.Background;
import com.bornaapp.borna2d.graphics.GrayscaleShader;
import com.bornaapp.borna2d.graphics.ParallaxBackground;
import com.bornaapp.borna2d.log;
import com.bornaapp.borna2d.physics.CollisionListener;
import com.bornaapp.borna2d.physics.DebugRenderer2D;

import java.util.ArrayList;

import box2dLight.RayHandler;

/**
 *
 */
public abstract class LevelBase implements GestureListener {

    protected Engine engine = Engine.getInstance();

    private String assetManifestPath;
    public Assets assets = new Assets();
    public boolean loadProgressively = true;

    private ArrayList<Delegate> delegates = new ArrayList<Delegate>();

    private boolean created = false;
    private boolean paused = false;

    public Color backColor = Color.DARK_GRAY;
    final private SpriteBatch batch = new SpriteBatch();
    final private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public Background background;
    public ParallaxBackground parallax;

    private Map map = new OrthogonalMap();

    public Stage uiStage = new Stage();
    public boolean debugUI = false;

    private OrthographicCamera camera;
    private Viewport viewport;

    private World world;
    private ArrayList<Body> killList = new ArrayList<Body>();
    public boolean debugPhysics = false;
    public DebugRenderer2D debugRenderer = new DebugRenderer2D();

    private boolean enableLights = false;
    private RayHandler rayHandler;

    InputMultiplexer multiplexer = new InputMultiplexer();

    private PooledEngine ashleyEngine = new PooledEngine();
    int systemPriority;
    int defaultZ = 0;

    //region Constructor

    protected LevelBase(String _assetManifestpath) {
        assetManifestPath = _assetManifestpath;
    }

    //endregion

    //region Assets

    private boolean LoadAssets(boolean progressiveLoading) {
        try {
            if (!progressiveLoading) {

                assets.LoadAll(assetManifestPath);
                return false;

            } else {
                return assets.LoadByStep(assetManifestPath);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    //endregion

    //region Delegates
    public void AddDelegate(Delegate _delegate) {
        delegates.add(_delegate);
    }

    public void ExecuteDelegate(String _name) {
        for (Delegate delegate : delegates) {
            if (delegate.name.equals(_name))
                delegate.Execute();
        }
    }
    //endregion

    //region Level Pause/Run states
    public boolean isPaused() {
        return paused;
    }

    public void Pause() {
        paused = true;
    }

    public void Unpause() {
        paused = false;
    }
    //endregion

    //region Graphics and Rendering

    public SpriteBatch getBatch() {
        return batch;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    private void ClearScreen() {
        Gdx.gl.glClearColor(backColor.r, backColor.g, backColor.b, backColor.a);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void DrawProgressCircle(float progress) {
        shapeRenderer.setProjectionMatrix(camera.combined);
        //Circle position and size
        float x = engine.ScreenWidth() / 2;
        float y = engine.ScreenHeight() / 2;
        float r = 30;
        //Draw backGround circle
        shapeRenderer.setColor(Color.GRAY);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(x, y, r);
        shapeRenderer.end();
        //Draw foreGround arc
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.arc(x, y, r, 0, progress * 360);
        shapeRenderer.end();
    }

    //endregion

    //region Physics

    public World getWorld() {
        return world;
    }

    private void SetupPhysicsWorld() {
        world = new World(engine.getConfig().gravity, false);//Engine.getCurrentLevel().getMap().params.gravity
        world.setContactListener(new CollisionListener());
    }

    public void AddToKillList(Body body) {
        killList.add(body);
    }

    //endregion

    //region Lights

    public void setLightingEnabled(boolean _enableLights) {
        this.enableLights = _enableLights;
    }

    public boolean getLightingEnabled() {
        return enableLights;
    }

    public RayHandler getRayHandler() {
        return rayHandler;
    }

    private void SetupLights() {
        rayHandler = new RayHandler(world);
    }
    //endregion

    //region Tiled-map

    public com.bornaapp.borna2d.game.maps.Map getMap() {
        return this.map;
    }

    //endregion

    //region Ashley
    //todo...."ashley event system" can be used to dispose() resources that are not part of assets like PointLight

    public PooledEngine getAshleyEngine() {
        return ashleyEngine;
    }

    public int getSystemPriority() {
        return systemPriority++;
    }

    public int getDefaultZ() {
        return defaultZ++;
    }
    //endregion

    //region Input management

    private void SetupInputs() {
        multiplexer.addProcessor(uiStage);
        multiplexer.addProcessor(new GestureDetector(this));
        Gdx.input.setInputProcessor(multiplexer);
    }
    //endregion

    //region Camera and Viewport

    private void SetupCamera() {
        // a camera is responsible for mapping any point to camera space(View)
        // and then projecting them to screen space(projection) which projection
        // can be orthographic or perspective
        camera = new OrthographicCamera();
    }

    private void SetupViewport() {
        // a viewport manages the method the camera uses to map any point in world space to camera space.
        // As we resize the window, viewport width and height remains constant and scales the rendering content
        // to match the new window Width and Height.
        viewport = new ExtendViewport(engine.ScreenWidth(), engine.ScreenHeight(), camera);
        viewport.apply(true);
        viewport.update(engine.ScreenWidth(), engine.ScreenHeight());
    }

    private void SetupUIStage() {

        uiStage = new Stage(viewport);
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    //endregion

    //region Debug on-Screen display
    public boolean osdVisible = true;
    public OnScreenDisplay osd = new OnScreenDisplay();
    //endregion

    //region child-Level methods
    protected abstract void onCreate();

    protected void onDispose() {
    }

    protected abstract void onUpdate();

    protected abstract void onPause();

    protected void onResize(int width, int height) {
    }

    /**
     * When a Level is paused, Rendering continues
     * but other systems are stopped
     */
    protected void onSystemPause() {
    }

    /**
     * Restore Level's state to what
     * it was before pausing
     */
    protected void onSystemResume() {
    }

    public abstract void NextLevel();

    public abstract void RestartLevel();

    //endregion

    //region Handling Engine requests

    void Create() {
        if (created)
            return;

        SetupCamera();

        SetupViewport();

        SetupUIStage();

        //Loading common resources
        assets.LoadAll("assetManifest_common.json");

        //Loading level-specific resources
        boolean isLoadingInProgress = LoadAssets(loadProgressively);
        if (isLoadingInProgress)
            return;

        log.info("");

        SetupPhysicsWorld();

        SetupLights();

        SetupInputs();

        osd.Init();

        try {
            onCreate();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        created = true;
    }

    /**
     * Package private:
     * must only get called by engine in response to applicationListener needs
     */
    void Dispose() {

        log.info("");
        onDispose();

        try {
            delegates.clear();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            ashleyEngine.removeAllEntities();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            batch.dispose();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            shapeRenderer.dispose();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            osd.dispose();

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            debugRenderer.dispose();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            rayHandler.dispose();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            world.dispose();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        try {
            assets.dispose();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        System.gc();
        created = false;
    }

    /**
     * Package private:
     * must only get called by engine in response to applicationListener needs
     */
    void Render(float delta) {

        ClearScreen();

        //continue loading assets if any
        if (!created) {
            DrawProgressCircle(assets.getProgress());
            Create();
            return;
        }

        if (!paused) {
            //update box2d physics world
            world.step(0.016f, 6, 2); //uses 60fps instead of deltaTime

            //removing unused bodies from the world
            if (!killList.isEmpty()) {
                for (int i = 0; i < killList.size(); i++) {
                    world.destroyBody(killList.get(i));
                    killList.remove(i);
                }
            }

            // run-time user updates while game is running
            onUpdate();

            //Use default shader
            batch.setShader(null);

        } else {
            // run-time user updates while game is paused
            onPause();

            //Use black & white shader
            batch.setShader(GrayscaleShader.shader);
        }

        //Limit camera to map borders
        if (camera.position.x > map.getWidth_inPixels() - camera.viewportWidth / 2)
            camera.position.x = map.getWidth_inPixels() - camera.viewportWidth / 2;
        if (camera.position.x < camera.viewportWidth / 2)
            camera.position.x = camera.viewportWidth / 2;
        if (camera.position.y > map.getHeight_inPixels() - camera.viewportHeight / 2)
            camera.position.y = map.getHeight_inPixels() - camera.viewportHeight / 2;
        if (camera.position.y < camera.viewportHeight / 2)
            camera.position.y = camera.viewportHeight / 2;

        // Update camera matrix
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        //start batch rendering
        if (batch.isDrawing())
            batch.end();
        batch.begin();

        //render background Layer
        if (background != null)
            background.render();

        if (parallax != null)
            parallax.render(delta);

        batch.end();

        map.render(camera);

        //update ashley systems
        ashleyEngine.update(deltaTime());

        //render Box2D lights
        if (enableLights) {
            rayHandler.setCombinedMatrix(camera.combined.cpy().scale(engine.getConfig().ppm, engine.getConfig().ppm, 1f));
            rayHandler.updateAndRender();
        }

        //render Box2D physics debug info
        if (debugPhysics)
            debugRenderer.render(world, camera);

        uiStage.setDebugAll(debugUI);

        //render & Update UI
        uiStage.draw();

        //draw on-screen debug texts
        if (osdVisible)
            osd.render();
    }

    /**
     * Package private
     * must only get called by engine in response to applicationListener needs
     */
    void Resize(int width, int height) {
        log.info("");
        viewport.update(width, height, true);
        onResize(width, height);
    }

    /**
     * Package private
     * must only get called by engine in response to applicationListener needs
     */
    void SystemPause() {
        if (!paused) {
            log.info("");
            paused = true;
            onSystemPause();
        }
    }

    /**
     * Package private
     * must only get called by engine in response to applicationListener needs
     */
    void SystemResume() {
        if (paused) {
            log.info("");
            Gdx.input.setInputProcessor(multiplexer);
            if (batch.isDrawing())
                batch.end();
            paused = false;
            onSystemResume();
        }
    }
    //endregion

    //region Gesture Overrided methods
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return true;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
    //endregion

    //region Utilities

    public float deltaTime() {
        return (paused ? 0f : Gdx.graphics.getDeltaTime());
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    //endregion
}