package com.bornaapp.borna2d.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.log;
import com.bornaapp.borna2d.physics.CollisionEvent;

/**
 * Created by Mehdi on 08/29/2015.
 * ...
 */
public class BodyComponent extends Component {

    public Body body;

    //region Methods

    //private constructor, as components must be created
    //using Ashley Engine and initialize afterwards.
    private BodyComponent() {
    }

    public void Init_Circle(BodyDef.BodyType bodyType, float r, float x, float y, boolean isSensor, boolean fixedRotation, CollisionEvent event) {

        if (r <= 0.0f) r = 1.0f;

        r = PixeltoMeters(r);
        x = PixeltoMeters(x);
        y = PixeltoMeters(y);

        //body properties
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.fixedRotation = fixedRotation;
        // Box2D position is relative to center of body.
        // However, to be compatible with other Libs,
        // we offset this to be relative to bot-left.
        bodyDef.position.set(x + r, y + r);

        FixtureDef fixDef = new FixtureDef();
        Shape shape = new CircleShape();
        shape.setRadius(r);
        fixDef.shape = shape;
        fixDef.isSensor = isSensor;
        fixDef.density = 1.0f;
        fixDef.restitution = 0.05f; //value must be between 0.0f & 1.0f
        fixDef.friction = 1.0f;     //value must be between 0.0f & 1.0f

        // applying properties to body
        body = Engine.getInstance().getCurrentLevel().getWorld().createBody(bodyDef);
        body.createFixture(fixDef).setUserData(event);

        //deallocate unnecessary  elements
        shape.dispose();
    }

    public void Init_Circle(BodyDef.BodyType bodyType, float r, float x, float y, boolean isSensor, boolean fixedRotation) {
        Init_Circle(bodyType, r, x, y, isSensor, fixedRotation, null);
    }

    public void Init_Box(BodyDef.BodyType bodyType, float width, float height, float x, float y, boolean isSensor, boolean fixedRotation, CollisionEvent event) {

        if (width <= 0.0f) width = 1.0f;
        if (height <= 0.0f) height = 1.0f;

        width = PixeltoMeters(width);
        height = PixeltoMeters(height);
        x = PixeltoMeters(x);
        y = PixeltoMeters(y);

        //body properties
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.fixedRotation = fixedRotation;
        // Box2D position is relative to center of body.
        // However, to be compatible with other Libs,
        // we offset this to be relative to bot-left.
        bodyDef.position.set(x + width / 2, y + height / 2);

        FixtureDef fixDef = new FixtureDef();
        Shape shape = new PolygonShape();
        ((PolygonShape) shape).setAsBox(width / 2, height / 2); //TODO why w/2 ?
        fixDef.shape = shape;
        fixDef.isSensor = isSensor;
        fixDef.density = 1.0f;
        fixDef.restitution = 0.05f;
        fixDef.friction = 1.0f;

        // applying properties to body
        body = Engine.getInstance().getCurrentLevel().getWorld().createBody(bodyDef);
        body.createFixture(fixDef).setUserData(event);

        //deallocate unnecessary  elements
        shape.dispose();
    }

    public void Init_Box(BodyDef.BodyType bodyType, float width, float height, float x, float y, boolean isSensor, boolean fixedRotation) {
        Init_Box(bodyType, width, height, x, y, isSensor, fixedRotation, null);
    }

    public void Init_Polygon(BodyDef.BodyType bodyType, Vector2[] vertices, float x, float y, boolean isSensor, boolean fixedRotation, CollisionEvent event) {
        //Box2D only supports convex polygons of 8 vertices max

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vector2(PixeltoMeters(vertices[i].x), PixeltoMeters(vertices[i].y));
        }

        x = PixeltoMeters(x);
        y = PixeltoMeters(y);

        //body properties
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.fixedRotation = fixedRotation;
        bodyDef.position.set(x, y);

        FixtureDef fixDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        fixDef.shape = shape;
        fixDef.isSensor = isSensor;
        fixDef.density = 1.0f;
        fixDef.restitution = 0.05f;
        fixDef.friction = 1.0f;

        // applying properties to body
        body = Engine.getInstance().getCurrentLevel().getWorld().createBody(bodyDef);
        body.createFixture(fixDef).setUserData(event);

        //deallocate unnecessary  elements
        shape.dispose();
    }

    public void Init_Polygon(BodyDef.BodyType bodyType, Vector2[] vertices, float x, float y, boolean isSensor, boolean fixedRotation) {
        Init_Polygon(bodyType, vertices, x, y, isSensor, fixedRotation, null);
    }

    public void Init_Line(BodyDef.BodyType bodyType, float x1, float y1, float x2, float y2, boolean isSensor, boolean fixedRotation, CollisionEvent event) {

        x1 = PixeltoMeters(x1);
        y1 = PixeltoMeters(y1);
        x2 = PixeltoMeters(x2);
        y2 = PixeltoMeters(y2);

        //body properties
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.fixedRotation = fixedRotation;

        FixtureDef fixDef = new FixtureDef();
        Shape shape = new EdgeShape();
        ((EdgeShape) shape).set(new Vector2(x1, y1), new Vector2(x2, y2));
        fixDef.shape = shape;
        fixDef.isSensor = isSensor;
        fixDef.density = 1.0f;
        fixDef.restitution = 0.05f;
        fixDef.friction = 1.0f;

        // applying properties to body
        body = Engine.getInstance().getCurrentLevel().getWorld().createBody(bodyDef);
        body.createFixture(fixDef).setUserData(event);

        //deallocate unnecessary  elements
        shape.dispose();
    }

    public void Init_Line(BodyDef.BodyType bodyType, float x1, float y1, float x2, float y2, boolean isSensor, boolean fixedRotation) {
        Init_Line(bodyType, x1, y1, x2, y2, isSensor, fixedRotation, null);
    }

    public void Init_Capsule(BodyDef.BodyType bodyType, float r, float h, float x, float y, boolean isSensor, boolean fixedRotation, CollisionEvent event) {

        r = PixeltoMeters(r);
        h = PixeltoMeters(h);
        x = PixeltoMeters(x);
        y = PixeltoMeters(y);

        if (r <= 0.0f) r = 1.0f;
        if (h <= 0.0f) h = 1.0f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.fixedRotation = fixedRotation;
        // Box2D position is relative to center of body.
        // However, to be compatible with other Libs,
        // we offset this to be relative to bot-left.
        bodyDef.position.set(x + r, y + r + h / 2);
        body = Engine.getInstance().getCurrentLevel().getWorld().createBody(bodyDef);

        //central rectangle fixture
        FixtureDef boxFixDef = new FixtureDef();
        Shape boxShape = new PolygonShape();
        ((PolygonShape) boxShape).setAsBox(r * 0.95f, h / 2, new Vector2(0, 0), 0.0f);
        boxFixDef.shape = boxShape;
        boxFixDef.isSensor = isSensor;
        boxFixDef.density = 1.0f;
        boxFixDef.restitution = 0.05f;
        boxFixDef.friction = 1.0f;
        body.createFixture(boxFixDef);
        boxShape.dispose();

        //top circle fixture
        FixtureDef headFixDef = new FixtureDef();
        CircleShape headShape = new CircleShape();
        headShape.setRadius(r);
        headShape.setPosition(new Vector2(0, h / 2));
        headFixDef.shape = headShape;
        headFixDef.isSensor = isSensor;
        headFixDef.density = 1.0f;
        headFixDef.restitution = 0.05f;
        headFixDef.friction = 1.0f;
        body.createFixture(headFixDef);
        headShape.dispose();

        //bottom circle fixture
        FixtureDef footFixDef = new FixtureDef();
        CircleShape footShape = new CircleShape();
        footShape.setRadius(r);
        footShape.setPosition(new Vector2(0, -h / 2));
        footFixDef.shape = footShape;
        footFixDef.isSensor = isSensor;
        footFixDef.density = 1.0f;
        footFixDef.restitution = 0.05f;
        footFixDef.friction = 1.0f;
        body.createFixture(footFixDef).setUserData(event);
        footShape.dispose();
    }

    public void Init_Capsule(BodyDef.BodyType bodyType, float r, float h, float x, float y, boolean isSensor, boolean fixedRotation) {
        Init_Capsule(bodyType, r, h, x, y, isSensor, fixedRotation, null);
    }

    //Box2D units are different from LibGdx rendering units
    //
    private float PixeltoMeters(float distanceInPixels) {
        int ppm = Engine.getInstance().getConfig().ppm;
        return distanceInPixels / ppm;
    }

    private float MetertoPixels(float distanceInMeters) {
        int ppm = Engine.getInstance().getConfig().ppm;
        return distanceInMeters * ppm;
    }
    //endregion

    //region properties

    public Vector2 getPositionOfCenter_inPixels() { //<----gives the pos of center,but others use bot-left
        Vector2 bodyPos = body.getPosition();
        return new Vector2(MetertoPixels(bodyPos.x), MetertoPixels(bodyPos.y));
    }

    public void setMaterial(float density, float restitution, float friction) {
        // restitution value must be between 0.0f & 1.0f
        if (restitution > 1.0f) restitution = 1.0f;
        if (restitution < 0.0f) restitution = 0.0f;
        // friction value must be between 0.0f & 1.0f
        if (friction > 1.0f) friction = 1.0f;
        if (friction < 0.0f) friction = 0.0f;

        //set one material to all fixtures
        for (Fixture fixture : body.getFixtureList()) {
            fixture.setDensity(density);
            fixture.setRestitution(restitution);
            fixture.setFriction(friction);
        }
    }
    //endregion
}