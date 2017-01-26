package com.bornaapp.shootingGallery.Characters;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Timer;
import com.bornaapp.borna2d.Debug.log;
import com.bornaapp.borna2d.PlayStatus;
import com.bornaapp.borna2d.components.AnimationComponent;
import com.bornaapp.borna2d.components.BodyComponent;
import com.bornaapp.borna2d.components.TextureAtlasComponent;
import com.bornaapp.borna2d.components.ZComponent;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.physics.CircleDef;
import com.bornaapp.borna2d.physics.CollisionEvent;
import com.bornaapp.borna2d.physics.PolygonDef;

/**
 * Created by Hashemi on 12/21/2016.
 */
public abstract class Duck {

    Entity entity;

    //values
    public boolean flipped = false;
    protected String textPath;
    protected float scale;
    protected float texOffsetX;
    protected float texOffsetY;
    protected float targetR;
    protected Vector2[] wingVertices;
    protected float headR;
    protected float headOffsetX;
    protected float headOffsetY;

    protected Vector2 startPosition;
    protected float linearVelocity;
    protected float oscillationRadius;
    protected float oscillationSpeed;

    protected float missX;
    //
    protected float LineY;
    private float elapsedTime = 0.0f;
    private boolean isFalling = false;

    //
    public boolean centerTargeted = false;
    public boolean marginTargeted = false;

    public TextureAtlasComponent texComp;

    public AnimationComponent animComp;
    public Animation anim_spin;

    public BodyComponent bodyComp;

    public ZComponent zComp;

    public abstract void Init();

    protected Duck(float _lineY, boolean _flipped) {

        flipped = _flipped;
        LineY = _lineY;

        PooledEngine ashleyEngine = Engine.getInstance().getCurrentLevel().getAshleyEngine();
        //
        entity = ashleyEngine.createEntity();
        ashleyEngine.addEntity(entity);
        //
        // Initialize parameters
        //
        Init();
        //
        // Texture
        //
        texComp = ashleyEngine.createComponent(TextureAtlasComponent.class);
        texComp.Init(textPath, scale);
        texComp.flipX = flipped;
        texComp.offsetX = texOffsetX;
        texComp.offsetY = texOffsetY;
        entity.add(texComp);
        //
        // Animation Sets
        //
        TextureRegion[] frames_spin = new TextureRegion[12];
        frames_spin[0] = (texComp.textureAtlas.findRegion("spin_0"));
        frames_spin[1] = (texComp.textureAtlas.findRegion("spin_1"));
        frames_spin[2] = (texComp.textureAtlas.findRegion("spin_2"));
        frames_spin[3] = (texComp.textureAtlas.findRegion("spin_3"));
        frames_spin[4] = (texComp.textureAtlas.findRegion("spin_4"));
        frames_spin[5] = (texComp.textureAtlas.findRegion("spin_5"));
        frames_spin[6] = (texComp.textureAtlas.findRegion("spin_6"));
        frames_spin[7] = (texComp.textureAtlas.findRegion("spin_7"));
        frames_spin[8] = (texComp.textureAtlas.findRegion("spin_8"));
        frames_spin[9] = (texComp.textureAtlas.findRegion("spin_9"));
        frames_spin[10] = (texComp.textureAtlas.findRegion("spin_10"));
        frames_spin[11] = (texComp.textureAtlas.findRegion("spin_11"));
        anim_spin = new Animation(1 / 24f, frames_spin);
        anim_spin.setPlayMode(Animation.PlayMode.LOOP);

        animComp = ashleyEngine.createComponent(AnimationComponent.class);
        animComp.Init(anim_spin);
        animComp.setPlayStatus(PlayStatus.Stopped);
        entity.add(animComp);
        //
        // Z-Order
        //
        zComp = ashleyEngine.createComponent(ZComponent.class);
        zComp.Init();
        entity.add(zComp);
        //
        // Physical body component
        //
        bodyComp = ashleyEngine.createComponent(BodyComponent.class);
        bodyComp.CreateBody(BodyDef.BodyType.DynamicBody, startPosition.x, startPosition.y, true);
        //target fixture
        bodyComp.AddFixture(new CircleDef(targetR), 0f, 0f, true, new CollisionEvent(this) {
            @Override
            public void onBeginContact(Object collidedObject, Body collidedBody, Fixture collidedFixture) {
                if (collidedObject instanceof Crosshair)
                    centerTargeted = true;
            }

            @Override
            public void onEndContact(Object collidedObject, Body collidedBody, Fixture collidedFixture) {
                if (collidedObject instanceof Crosshair)
                    centerTargeted = false;
            }
        });
        //wing fixture
        bodyComp.AddFixture(new PolygonDef(wingVertices), 0, 0, true, new CollisionEvent(this) {
            @Override
            public void onBeginContact(Object collidedObject, Body collidedBody, Fixture collidedFixture) {
                if (collidedObject instanceof Crosshair)
                    marginTargeted = true;
            }

            @Override
            public void onEndContact(Object collidedObject, Body collidedBody, Fixture collidedFixture) {
                if (collidedObject instanceof Crosshair)
                    marginTargeted = false;
                //
                //check contact with margins
                //

            }
        });
        //head fixture
        bodyComp.AddFixture(new CircleDef(headR), headOffsetX, headOffsetY, true, new CollisionEvent(this) {
            @Override
            public void onBeginContact(Object collidedObject, Body collidedBody, Fixture collidedFixture) {
                if (collidedObject instanceof Crosshair)
                    marginTargeted = true;
            }

            @Override
            public void onEndContact(Object collidedObject, Body collidedBody, Fixture collidedFixture) {
                if (collidedObject instanceof Crosshair)
                    marginTargeted = false;
            }
        });
        bodyComp.body.setGravityScale(0);
        entity.add(bodyComp);
    }

    public void update(boolean isBulletTravelling) {
        if (!isFalling) {
            //Hit test
            if (isBulletTravelling) {
                if (centerTargeted)
                    fall();
                else if (marginTargeted)
                    Spin();
            }
            //Movement
            float dt = Engine.getInstance().getCurrentLevel().deltaTime();
            elapsedTime += dt;
            Vector2 pos = bodyComp.getPositionOfCenter_inPixels();
            pos.x += linearVelocity * dt;
            pos.y = LineY + MathUtils.sin(oscillationSpeed * elapsedTime * MathUtils.PI) * oscillationRadius;
            bodyComp.setPositionOfCenter_inPixels(pos.x, pos.y);
        }
    }

    public void Spin() {
        animComp.setPlayStatus(PlayStatus.Playing);

        final Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                animComp.setPlayStatus(PlayStatus.Stopped);
            }
        }, 1);
    }

    public void fall() {
        isFalling = true;
        bodyComp.body.setGravityScale(1.0f);
        Engine.getInstance().getCurrentLevel().ExecuteDelegate("Fall");
    }

    public void Missed() {
        Engine.getInstance().getCurrentLevel().ExecuteDelegate("Missed");
        bodyComp.Destroy();
        Engine.getInstance().getCurrentLevel().getAshleyEngine().removeEntity(entity);
    }
}