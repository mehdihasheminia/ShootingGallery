package com.bornaapp.borna2d.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.physics.BoxDef;
import com.bornaapp.borna2d.physics.CapsuleDef;
import com.bornaapp.borna2d.physics.CircleDef;
import com.bornaapp.borna2d.physics.CollisionEvent;
import com.bornaapp.borna2d.physics.LineDef;
import com.bornaapp.borna2d.physics.PolygonDef;
import com.bornaapp.borna2d.physics.ShapeDef;

/**
 * Created by Mehdi on 08/29/2015.
 * ...
 */
public class BodyComponent extends Component {

    public Body body;
    public ShapeDef shapeDef; //<---doesnt work for bodies composed of several fixture

    //protected constructor, as components must only
    //be created using Ashley Engine or child classes.
    protected BodyComponent() {
    }

    //region Core Body & Fixture initialization methods
    public void CreateBody(BodyDef.BodyType bodyType, float x, float y, boolean fixedRotation) {
        //body properties
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.fixedRotation = fixedRotation;
        // Box2D position is relative to center of body.
        bodyDef.position.set(PixeltoMeters(x), PixeltoMeters(y));
        body = Engine.getInstance().getCurrentLevel().getWorld().createBody(bodyDef);
    }

    public void AddFixture(CircleDef circleDef, float offsetX, float offsetY, boolean isSensor, CollisionEvent event) {

        float r = circleDef.r;

        if (r <= 0.0f) r = 1.0f;

        r = PixeltoMeters(r);
        offsetX = PixeltoMeters(offsetX);
        offsetY = PixeltoMeters(offsetY);

        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(r);
        shape.setPosition(new Vector2(offsetX, offsetY));
        fixtureDef.shape = shape;
        fixtureDef.isSensor = isSensor;
        fixtureDef.density = 1.0f;
        fixtureDef.restitution = 0.05f;
        fixtureDef.friction = 1.0f;
        //apply to body
        body.createFixture(fixtureDef).setUserData(event);
        //deallocate unnecessary  elements
        shape.dispose();
    }

    public void AddFixture(BoxDef boxDef, float offsetX, float offsetY, boolean isSensor, CollisionEvent event) {

        float width = boxDef.width;
        float height = boxDef.height;

        if (width <= 0.0f) width = 1.0f;
        if (height <= 0.0f) height = 1.0f;

        width = PixeltoMeters(width);
        height = PixeltoMeters(height);
        offsetX = PixeltoMeters(offsetX);
        offsetY = PixeltoMeters(offsetY);

        FixtureDef fixDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2, new Vector2(offsetX, offsetY), 0);
        fixDef.shape = shape;
        fixDef.isSensor = isSensor;
        fixDef.density = 1.0f;
        fixDef.restitution = 0.05f;
        fixDef.friction = 1.0f;
        // applying properties to body
        body.createFixture(fixDef).setUserData(event);
        //deallocate unnecessary  elements
        shape.dispose();
    }

    public void AddFixture(PolygonDef polygonDef, float offsetX, float offsetY, boolean isSensor, CollisionEvent event) {

        //Box2D only supports convex polygons of 8 vertices max
        Vector2[] vertices = polygonDef.vertices;

        offsetX = PixeltoMeters(offsetX);
        offsetY = PixeltoMeters(offsetY);

        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vector2(PixeltoMeters(vertices[i].x) + offsetX, PixeltoMeters(vertices[i].y) + offsetY);
        }

        FixtureDef fixDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.set(vertices);
        fixDef.shape = shape;
        fixDef.isSensor = isSensor;
        fixDef.density = 1.0f;
        fixDef.restitution = 0.05f;
        fixDef.friction = 1.0f;
        // applying properties to body
        body.createFixture(fixDef).setUserData(event);
        //deallocate unnecessary  elements
        shape.dispose();
    }

    public void AddFixture(LineDef lineDef, float offsetX, float offsetY, boolean isSensor, CollisionEvent event) {

        Vector2 p1 = lineDef.point1.cpy();
        Vector2 p2 = lineDef.point2.cpy();

        offsetX = PixeltoMeters(offsetX);
        offsetY = PixeltoMeters(offsetY);

        p1.x = PixeltoMeters(p1.x + offsetX);
        p1.y = PixeltoMeters(p1.y + offsetY);
        p2.x = PixeltoMeters(p2.x + offsetX);
        p2.y = PixeltoMeters(p2.y + offsetY);

        FixtureDef fixDef = new FixtureDef();
        Shape shape = new EdgeShape();
        ((EdgeShape) shape).set(p1, p2);
        fixDef.shape = shape;
        fixDef.isSensor = isSensor;
        fixDef.density = 1.0f;
        fixDef.restitution = 0.05f;
        fixDef.friction = 1.0f;
        // applying properties to body
        body.createFixture(fixDef).setUserData(event);
        //deallocate unnecessary  elements
        shape.dispose();
    }

    public void AddFixture(CapsuleDef capsuleDef, float offsetX, float offsetY, boolean isSensor, CollisionEvent headEvent, CollisionEvent trunkEvent, CollisionEvent footEvent) {

        float r = capsuleDef.r;
        float h = capsuleDef.h;

        //central rectangle(trunk) fixture
        AddFixture(new BoxDef(2 * r, h), offsetX, offsetY, isSensor, trunkEvent);

        //top circle fixture
        AddFixture(new CircleDef(r), offsetX, offsetY + h / 2, isSensor, headEvent);

        //bottom circle fixture
        AddFixture(new CircleDef(r), offsetX, offsetY - h / 2, isSensor, footEvent);
    }
    //endregion

    //region body easy-initialization methods
    public void Init(BodyDef.BodyType bodyType, CircleDef circleDef, float x, float y, boolean isSensor, boolean fixedRotation, CollisionEvent event) {

        shapeDef = circleDef;

        CreateBody(bodyType, x, y, fixedRotation);

        AddFixture(circleDef, 0f, 0f, isSensor, event);
    }

    public void Init(BodyDef.BodyType bodyType, CircleDef circleDef, float x, float y, boolean isSensor, boolean fixedRotation) {
        Init(bodyType, circleDef, x, y, isSensor, fixedRotation, null);
    }

    public void Init(BodyDef.BodyType bodyType, BoxDef boxDef, float x, float y, boolean isSensor, boolean fixedRotation, CollisionEvent event) {

        shapeDef = boxDef;

        CreateBody(bodyType, x, y, fixedRotation);

        AddFixture(boxDef, 0f, 0f, isSensor, event);
    }

    public void Init(BodyDef.BodyType bodyType, BoxDef boxDef, float x, float y, boolean isSensor, boolean fixedRotation) {
        Init(bodyType, boxDef, x, y, isSensor, fixedRotation, null);
    }

    public void Init(BodyDef.BodyType bodyType, PolygonDef polygonDef, float x, float y, boolean isSensor, boolean fixedRotation, CollisionEvent event) {

        shapeDef = polygonDef;

        CreateBody(bodyType, x, y, fixedRotation);

        AddFixture(polygonDef, 0f, 0f, isSensor, event);
    }

    public void Init(BodyDef.BodyType bodyType, PolygonDef polygonDef, float x, float y, boolean isSensor, boolean fixedRotation) {
        Init(bodyType, polygonDef, x, y, isSensor, fixedRotation, null);
    }

    public void Init(BodyDef.BodyType bodyType, LineDef lineDef, boolean isSensor, boolean fixedRotation, CollisionEvent event) {

        shapeDef = lineDef;

        CreateBody(bodyType, 0, 0, fixedRotation);

        AddFixture(lineDef, 0f, 0f, isSensor, event);
    }

    public void Init(BodyDef.BodyType bodyType, LineDef lineDef, boolean isSensor, boolean fixedRotation) {
        Init(bodyType, lineDef, isSensor, fixedRotation, null);
    }

    public void Init(BodyDef.BodyType bodyType, CapsuleDef capsuleDef, float x, float y, boolean isSensor, boolean fixedRotation, CollisionEvent headEvent, CollisionEvent trunkEvent, CollisionEvent footEvent) {

        shapeDef = capsuleDef;

        CreateBody(bodyType, x, y, fixedRotation);

        AddFixture(capsuleDef, 0f, 0f, isSensor, headEvent, trunkEvent, footEvent);
    }

    public void Init(BodyDef.BodyType bodyType, CapsuleDef capsuleDef, float x, float y, boolean isSensor, boolean fixedRotation) {
        Init(bodyType, capsuleDef, x, y, isSensor, fixedRotation, null, null, null);
    }
    //endregion

    //region properties

    public Vector2 getPositionOfCenter_inPixels() { //<----gives the pos of center,but others use bot-left
        Vector2 bodyPos = body.getPosition();
        return new Vector2(MetertoPixels(bodyPos.x), MetertoPixels(bodyPos.y));
    }

    public void setPositionOfCenter_inPixels(float x, float y) {
        body.setTransform(PixeltoMeters(x), PixeltoMeters(y), 0f);
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

    //region Utilities
    public boolean ContainsPointOfScreenCoord(float screenX, float screenY) {

        //convert mouse pointer coordinates from screen-space to world-space
        Vector3 worldCoord = Engine.getInstance().getCurrentLevel().getCamera().unproject(new Vector3(screenX, screenY, 0));

        //convert world-space unit from pixels(rendering) to metres(Box2D)
        worldCoord.x = PixeltoMeters(worldCoord.x);
        worldCoord.y = PixeltoMeters(worldCoord.y);

        //check if this point is in contact with any fixtures
        for (Fixture fixture : body.getFixtureList()) {
            if (fixture.testPoint(worldCoord.x, worldCoord.y))
                return true;
        }
        return false;
    }

    public boolean ContainsPoint(float x, float y) {

        //convert world-space unit from pixels(rendering) to metres(Box2D)
        x = PixeltoMeters(x);
        y = PixeltoMeters(y);

        //check if this point is in contact with any fixtures
        for (Fixture fixture : body.getFixtureList()) {
            if (fixture.testPoint(x, y))
                return true;
        }
        return false;
    }

    //endregion

    //region Unit-Converters

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

    public void Destroy(){
        Engine.getInstance().getCurrentLevel().AddToKillList(body);
    }
}