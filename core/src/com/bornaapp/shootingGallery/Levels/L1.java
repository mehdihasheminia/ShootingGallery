package com.bornaapp.shootingGallery.Levels;import com.badlogic.ashley.core.PooledEngine;import com.badlogic.gdx.Gdx;import com.badlogic.gdx.Input;import com.badlogic.gdx.graphics.Color;import com.badlogic.gdx.math.Vector2;import com.badlogic.gdx.physics.box2d.Body;import com.badlogic.gdx.physics.box2d.Fixture;import com.badlogic.gdx.utils.TimeUtils;import com.bornaapp.borna2d.game.levels.Delegate;import com.bornaapp.borna2d.game.levels.Engine;import com.bornaapp.borna2d.game.levels.LevelBase;import com.bornaapp.borna2d.game.maps.Map;import com.bornaapp.borna2d.log;import com.bornaapp.borna2d.physics.BoxDef;import com.bornaapp.borna2d.physics.CollisionEvent;import com.bornaapp.borna2d.systems.SoundSystem;import com.bornaapp.borna2d.systems.PathFindingSystem;import com.bornaapp.borna2d.systems.RenderingSystem;import com.bornaapp.shootingGallery.Characters.Bee;import com.bornaapp.shootingGallery.Characters.Crosshair;import com.bornaapp.shootingGallery.Characters.DuckWhite;import com.bornaapp.shootingGallery.Characters.Rifle;import com.bornaapp.shootingGallery.Controls.CoinCounter;import com.bornaapp.shootingGallery.Controls.LifeCounter;import com.bornaapp.shootingGallery.Controls.PauseMenu;public class L1 extends LevelBase {    public L1() {        super("assetManifest_L1.json");        loadProgressively = true;    }    Engine engine = Engine.getInstance();    Rifle rifle;    Crosshair crosshair;    DuckWhite duck1;    DuckWhite duck2;    Bee bee;    int score;    float life = 100f;    // UI    CoinCounter coinCounter;    CoinCounter bulletCounter;    LifeCounter lifeCounter;    PauseMenu pauseMenu;    boolean riffleDragged = false;    @Override    protected void onCreate() {        AddDelegate(new Delegate("Shoot") {            @Override            public void Execute() {                //                bulletCounter.setCoinCount(crosshair.remainingBullets);            }        });        AddDelegate(new Delegate("Fall") {            @Override            public void Execute() {                score += 10;                coinCounter.setCoinCount(score);            }        });        AddDelegate(new Delegate("Missed") {            @Override            public void Execute() {                life -= 25;                lifeCounter.setLifeCount(life);            }        });        long t = TimeUtils.millis();        log.info("Time stamp is: " + Long.toString(t));        //Map        //        Map map = this.getMap();        map.EnableMargins(true, true, true, true);        map.Load("M1.tmx");        map.getObstacle("RightMargin").SetCollisionEvent(new CollisionEvent(map) {            @Override            public void onBeginContact(Object collidedObject, Body collidedBody, Fixture collidedFixture) {                if (collidedObject instanceof DuckWhite) {                    DuckWhite duckWhite = (DuckWhite) collidedObject;                    if (!duckWhite.flipped)                        duckWhite.Missed();                }            }        });        map.getObstacle("LeftMargin").SetCollisionEvent(new CollisionEvent(map) {            @Override            public void onBeginContact(Object collidedObject, Body collidedBody, Fixture collidedFixture) {                if (collidedObject instanceof DuckWhite) {                    DuckWhite duckWhite = (DuckWhite) collidedObject;                    if (duckWhite.flipped)                        duckWhite.Missed();                }            }        });        //Objects & Characters        //        rifle = new Rifle();        Vector2 riflePos = rifle.bodyComp.getPositionOfCenter_inPixels();        crosshair = new Crosshair(riflePos.x, riflePos.y + 100);        duck1 = new DuckWhite(150f, false);        duck2 = new DuckWhite(250f, true);        bee = new Bee(new Vector2(200f, 200f), map.getDiagonalGraph());        bee.addCheckPoint(new Vector2(200f, 300f));        bee.addCheckPoint(new Vector2(600f, 30f));        bee.addCheckPoint(new Vector2(700f, 300f));        bee.addCheckPoint(new Vector2(50f, 500f));        coinCounter = new CoinCounter();        bulletCounter = new CoinCounter();        lifeCounter = new LifeCounter();        pauseMenu = new PauseMenu();        //Setting up Z-order        map.EnableLayeredRendering();        int z = 0;        crosshair.zComp.z = z++;        rifle.zComp.z = z++;        map.InitLayer("water2", z++);        duck1.zComp.z = z++;        map.InitLayer("water1", z++);        duck2.zComp.z = z++;        bee.zComp.z = z++;        map.InitLayer("grass2", z++);        map.InitLayer("decorations", z++);        map.InitLayer("bg", z++);        //Ashley        //        PooledEngine ashleyEngine = getAshleyEngine();        PathFindingSystem pathFindingSystem = new PathFindingSystem(this);        pathFindingSystem.InitDebugRenderer(map.getDiagonalGraph());        ashleyEngine.addSystem(pathFindingSystem);        ashleyEngine.addSystem(new RenderingSystem(this));        ashleyEngine.addSystem(new SoundSystem(this));        //other initializations        //        this.backColor = Color.BLACK;        this.setLightingEnabled(false);        Engine.getInstance().setMasterVolume(1.0f);        //Controls & Menus        //        coinCounter.Init();        bulletCounter.margin = 200;        bulletCounter.Init();        bulletCounter.setCoinCount(crosshair.maxBullets);        lifeCounter.Init();        lifeCounter.setLifeCount(life);        pauseMenu.Init();        log.info("Loading time is: " + Long.toString(TimeUtils.timeSinceMillis(t)));    }    @Override    protected void onUpdate() {        bee.update();        //queryInput        //        pauseMenu.setVisible(false);        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {            this.paused = true;        }        //-------------------------------- Rifle control --------------------------------------        //check if user is dragging rifle        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {            if (rifle.bodyComp.ContainsPointOfScreenCoord(Gdx.input.getX(), Gdx.input.getY()))                riffleDragged = true;        } else            riffleDragged = false;        //How much rifle is dragged?        float deltaX = 0;        float deltaY = 0;        if (riffleDragged) {            deltaX = Gdx.input.getDeltaX();            deltaY = Gdx.input.getDeltaY();        }        //move rifle        if (riffleDragged) {            BoxDef rifleBox = (BoxDef) rifle.bodyComp.shapeDef;            Vector2 riflePos = rifle.bodyComp.getPositionOfCenter_inPixels();            //limit rifle movement            float centerX = riflePos.x + deltaX;            if (centerX > getCamera().viewportWidth - (rifleBox.width / 2f))                centerX = getCamera().viewportWidth - (rifleBox.width / 2f);            if (centerX < rifleBox.width / 2f)                centerX = rifleBox.width / 2f;            float centerY = rifleBox.height / 2f;            rifle.bodyComp.setPositionOfCenter_inPixels(centerX, centerY);        }        rifle.update();        if(crosshair.isReloading){            rifle.Reload();        }        //------------------------------ crosshair Control -------------------------------------        //Move crosshair        if (riffleDragged) {            Vector2 crosshairPos = crosshair.bodyComp.getPositionOfCenter_inPixels();            //limit crosshair movement            float centerX = crosshairPos.x + deltaX;            if (centerX > getCamera().viewportWidth)                centerX = getCamera().viewportWidth;            if (centerX < 0)                centerX = 0;            float centerY = crosshairPos.y - deltaY;            if (centerY > getCamera().viewportHeight)                centerY = getCamera().viewportHeight;            if (centerY < 0)                centerY = 0;            crosshair.bodyComp.setPositionOfCenter_inPixels(centerX, centerY);        }        crosshair.update();        //-------------------------------------- Duck ----------------------------------------------        duck1.update(crosshair.bulletIsTravelling);        duck2.update(crosshair.bulletIsTravelling);        // on-screen-debug texts & Lighting states        //        if (Gdx.input.isKeyJustPressed(Input.Keys.F1))            osd.SetF1(!osd.getF1());        if (Gdx.input.isKeyJustPressed(Input.Keys.F2))            osdVisible = !osdVisible;        osd.log("FPS", Integer.toString(Engine.getInstance().frameRate()));        //render debug info        //        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3))            debugUI = !debugUI;        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) //error        {            debugPhysics = !debugPhysics;            bee.setDrawDebug(!bee.isDrawDebug());        }        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) //error            setLightingEnabled(!getLightingEnabled());    }    @Override    protected void whilePause() {        this.paused = true;        pauseMenu.setVisible(true);        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {            this.paused = false;            pauseMenu.setVisible(false);        }    }    @Override    protected void onDispose() {    }    @Override    protected void onResize(int width, int height) {    }    @Override    protected void onPause() {    }    @Override    protected void onResume() {    }    @Override    public void NextLevel() {        Engine.getInstance().setLevel(new L1());    }    @Override    public void RestartLevel() {        Engine.getInstance().setLevel(new L1());    }    //region Gesture Overrided methods    @Override    public boolean touchDown(float x, float y, int pointer, int button) {        return false;    }    @Override    public boolean tap(float x, float y, int count, int button) {        crosshair.Shoot();        return true;    }    @Override    public boolean longPress(float x, float y) {        return false;    }    @Override    public boolean fling(float velocityX, float velocityY, int button) {        return false;    }    @Override    public boolean pan(float x, float y, float deltaX, float deltaY) {        return true;    }    @Override    public boolean panStop(float x, float y, int pointer, int button) {        return false;    }    @Override    public boolean zoom(float initialDistance, float distance) {        return false;    }    @Override    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {        return false;    }    //endregion}