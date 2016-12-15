package com.bornaapp.borna2d.game.maps;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.bornaapp.borna2d.ai.AStarGraph;
import com.bornaapp.borna2d.ai.GraphType;
import com.bornaapp.borna2d.components.BodyComponent;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.log;

public abstract class Map {

    PooledEngine ashleyEngine;

    public Map() {
        margins = new Margin[4];
        margins[0] = new Margin();
        margins[1] = new Margin();
        margins[2] = new Margin();
        margins[3] = new Margin();
    }

    private int width_InTiles;
    private int height_InTiles;
    private int widthOfEachTile_InPixels;
    private int heightOfEachTile_InPixels;

    public TiledMap tiledMap;
    protected TiledMapRenderer tiledMapRenderer;
    public MapParameters params;

    private Array<MapLocation> mapLocations;
    private Array<MapArea> areaSensors;
    private Array<MapArea> obstacles;

    Margin[] margins;

    private class Margin {
        public float width = 0.5f;
        public boolean enabled = true;
    }

    AStarGraph diagonalGraph;
    AStarGraph edgeGraph;

    //region Loading tiled-Map from Disk

    protected abstract void Init(String filePath);

    public void Load(String filePath) {
        //Load graphics
        try {
            ashleyEngine = Engine.getInstance().getCurrentLevel().getAshleyEngine();

            // retrieve previously loaded tile map from asset manager of current level
            Init(filePath);
            //
            LoadMapDefFromFile();
            // calculate dimensions of playground in both tiles and pixels
            MapProperties properties = tiledMap.getProperties();
            width_InTiles = properties.get("width", Integer.class);
            height_InTiles = properties.get("height", Integer.class);
            widthOfEachTile_InPixels = properties.get("tilewidth", Integer.class);
            heightOfEachTile_InPixels = properties.get("tileheight", Integer.class);
            //initialize collections
            obstacles = new Array<MapArea>();
            areaSensors = new Array<MapArea>();
            mapLocations = new Array<MapLocation>();
            //initialize & Generate margins
            GenerateMargins();
            //Load level data from file
            extractObstacles();
            extractAreaSensors();
            extractCheckpoints();
            //calculate A* pathFinding graphs
            TiledMapTileLayer pathLayer = (TiledMapTileLayer) tiledMap.getLayers().get(params.pathLayerName);
            edgeGraph = new AStarGraph(pathLayer);
            edgeGraph.InitAs(GraphType.Edge);
            diagonalGraph = new AStarGraph(pathLayer);
            diagonalGraph.InitAs(GraphType.Diagonal);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void LoadMapDefFromFile() {
        try {
            Json json = new Json();
            FileHandle file = Gdx.files.internal("mapParams.json");
            params = json.fromJson(MapParameters.class, file);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    //endregion

    //region extracting components from loaded tiled-map
    public void GenerateMargins() {

        int widthInPixels = getWidth_inPixels();
        int heightInPixels = getHeight_inPixels();
        float x1, y1, x2, y2;

        //top Margin
        if (margins[0].enabled) {

            x1 = 0f;
            y1 = heightInPixels - widthOfEachTile_InPixels * margins[0].width;
            x2 = x1 + widthInPixels;
            y2 = y1 + 0f;

            BodyComponent bodyComp = ashleyEngine.createComponent(BodyComponent.class);
            bodyComp.Init_Line(BodyDef.BodyType.StaticBody, x1, y1, x2, y2, false, true);
            obstacles.add(new MapArea("TopMargin", bodyComp));
        }
        //Right Margin
        if (margins[1].enabled) {
            x1 = widthInPixels - widthOfEachTile_InPixels * margins[1].width;
            y1 = 0f;
            x2 = x1 + 0f;
            y2 = y1 + heightInPixels;

            BodyComponent bodyComp = ashleyEngine.createComponent(BodyComponent.class);
            bodyComp.Init_Line(BodyDef.BodyType.StaticBody, x1, y1, x2, y2, false, true);
            obstacles.add(new MapArea("RightMargin", bodyComp));
        }
        //bottom Margin
        if (margins[2].enabled) {
            x1 = 0f;
            y1 = widthOfEachTile_InPixels * margins[2].width;
            x2 = x1 + widthInPixels;
            y2 = y1 + 0f;

            BodyComponent bodyComp = ashleyEngine.createComponent(BodyComponent.class);
            bodyComp.Init_Line(BodyDef.BodyType.StaticBody, x1, y1, x2, y2, false, true);
            obstacles.add(new MapArea("BottomMargin", bodyComp));
        }
        //Left Margin
        if (margins[3].enabled) {
            x1 = widthOfEachTile_InPixels * margins[3].width;
            y1 = 0f;
            x2 = x1 + 0f;
            y2 = y1 + heightInPixels;

            BodyComponent bodyComp = ashleyEngine.createComponent(BodyComponent.class);
            bodyComp.Init_Line(BodyDef.BodyType.StaticBody, x1, y1, x2, y2, false, true);
            obstacles.add(new MapArea("LeftMargin", bodyComp));
        }
    }

    public void extractObstacles() {
        try {

            MapLayer obstaclesLayer = tiledMap.getLayers().get(params.collisionLayerName);
            MapObjects mapObjects = obstaclesLayer.getObjects();

            for (MapObject mapObject : mapObjects) {

                if (mapObject instanceof TextureMapObject)
                    continue;

                // Rectangular objects
                //
                if (mapObject instanceof RectangleMapObject) {
                    RectangleMapObject rect = (RectangleMapObject) mapObject;
                    obstacles.add(new MapArea(mapObject.getName(), extractBoxObject(rect, false)));
                }
                // Polygonal objects
                //
                else if (mapObject instanceof PolygonMapObject) {

                    PolygonMapObject polygon = (PolygonMapObject) mapObject;
                    obstacles.add(new MapArea(mapObject.getName(), extractPolygonObject(polygon, false)));
                }
                // Circular objects
                //
                else if (mapObject instanceof CircleMapObject) {
                    CircleMapObject circle = (CircleMapObject) mapObject;
                    obstacles.add(new MapArea(mapObject.getName(), extractCircleObject(circle, false)));

                }
                //other Objects
                else {
                    obstacles.add(new MapArea(mapObject.getName(), extractUnknownObject(mapObject, false)));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void extractAreaSensors() {
        try {
            MapLayer portalLayer = tiledMap.getLayers().get(params.portalLayerName);
            MapObjects mapObjects = portalLayer.getObjects();

            for (MapObject mapObject : mapObjects) {

                if (mapObject instanceof TextureMapObject)
                    continue;

                // Rectangular objects
                //
                if (mapObject instanceof RectangleMapObject) {
                    RectangleMapObject rect = (RectangleMapObject) mapObject;
                    areaSensors.add(new MapArea(mapObject.getName(), extractBoxObject(rect, true)));
                }
                // Polygonal objects
                //
                else if (mapObject instanceof PolygonMapObject) {
                    PolygonMapObject polygon = (PolygonMapObject) mapObject;
                    areaSensors.add(new MapArea(mapObject.getName(), extractPolygonObject(polygon, true)));
                }
                // Circular objects
                //
                else if (mapObject instanceof CircleMapObject) {
                    CircleMapObject circle = (CircleMapObject) mapObject;
                    areaSensors.add(new MapArea(mapObject.getName(), extractCircleObject(circle, true)));
                }
                //other Objects
                else {
                    areaSensors.add(new MapArea(mapObject.getName(), extractUnknownObject(mapObject, true)));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void extractCheckpoints() {
        try {
            MapLayer checkpointLayer = tiledMap.getLayers().get(params.checkpointsLayerName);
            MapObjects mapObjects = checkpointLayer.getObjects();
            for (MapObject mapObject : mapObjects) {
                String name = mapObject.getName();
                float x = mapObject.getProperties().get("x", Float.class);
                float y = mapObject.getProperties().get("y", Float.class);
                //
                mapLocations.add(new MapLocation(name, x, y));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private BodyComponent extractBoxObject(RectangleMapObject rect, boolean isSensor) {
        //extract data from file
        float x = rect.getRectangle().getX();
        float y = rect.getRectangle().getY();
        float w = rect.getRectangle().getWidth();
        float h = rect.getRectangle().getHeight();
        //
        BodyComponent boxComp = ashleyEngine.createComponent(BodyComponent.class);
        boxComp.Init_Box(BodyDef.BodyType.StaticBody, w, h, x, y, isSensor, true);
        return boxComp;
    }

    private BodyComponent extractPolygonObject(PolygonMapObject polygon, boolean isSensor) {
        //extract data from file
        float x = polygon.getPolygon().getX();
        float y = polygon.getPolygon().getY();

        float[] rawVertices = polygon.getPolygon().getVertices();
        int numVertices = rawVertices.length / 2;
        Vector2[] vertices = new Vector2[numVertices];
        for (int i = 0; i < numVertices; i++) {
            vertices[i] = new Vector2(rawVertices[i * 2], rawVertices[i * 2 + 1]);
        }
        //
        BodyComponent polyComp = ashleyEngine.createComponent(BodyComponent.class);
        polyComp.Init_Polygon(BodyDef.BodyType.StaticBody, vertices, x, y, isSensor, true);
        return polyComp;
    }

    private BodyComponent extractCircleObject(CircleMapObject circle, boolean isSensor) {
        //extract data from file
        float x = circle.getCircle().x;
        float y = circle.getCircle().y;
        float r = circle.getCircle().radius;
        //
        BodyComponent circleComp = ashleyEngine.createComponent(BodyComponent.class);
        circleComp.Init_Circle(BodyDef.BodyType.StaticBody, r, x, y, isSensor, true);
        return circleComp;
    }

    private BodyComponent extractUnknownObject(MapObject mapObject, boolean isSensor) {
        //extract data from file
        float x = mapObject.getProperties().get("x", Float.class);
        float y = mapObject.getProperties().get("y", Float.class);
        float w = mapObject.getProperties().get("width", Float.class);
        float h = mapObject.getProperties().get("height", Float.class);
        // in current version of "Tiled v0.12.3" design program, probably due to a bug,
        // the radius of a circle will be considered zero unless manually resized.
        // To solve the issue, we consider a default value in this case
        if (w == 0) w = 10.0f;
        if (h == 0) h = 10.0f;
        float r = Math.max(w, h) / 2;
        //
        BodyComponent bodyComp = ashleyEngine.createComponent(BodyComponent.class);
        bodyComp.Init_Circle(BodyDef.BodyType.StaticBody, r, x, y, isSensor, true);
        return bodyComp;
    }
    //endregion

    //region Rendering
    public void render(OrthographicCamera camera) {
        if (tiledMapRenderer != null && tiledMap != null) {
            tiledMapRenderer.setView(camera);
            tiledMapRenderer.render();
        }
    }
    //endregion

    //region Properties
    public int getWidth_inTiles() {
        return width_InTiles;
    }

    public int getHeight_inTiles() {
        return height_InTiles;
    }

    public int getWidth_inPixels() {
        return width_InTiles * widthOfEachTile_InPixels;
    }

    public int getHeight_inPixels() {
        return height_InTiles * heightOfEachTile_InPixels;
    }

    public int getWidthOfEachTile_inPixels() {
        return widthOfEachTile_InPixels;
    }

    public int getHeightOfEachTile_inPixels() {
        return heightOfEachTile_InPixels;
    }

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public void EnableMargins(boolean top, boolean right, boolean bot, boolean left) {
        margins[0].enabled = top;
        margins[1].enabled = right;
        margins[2].enabled = bot;
        margins[3].enabled = left;
    }

    public void setMarginsWidth_inTiles(float top, float right, float bot, float left) {
        margins[0].width = top;
        margins[1].width = right;
        margins[2].width = bot;
        margins[3].width = left;
    }

    public MapLocation getLocation(String name) {
        for (MapLocation mapLocation : mapLocations) {
            if (mapLocation.name.equals(name))
                return mapLocation;
        }
        return null;
    }

    public MapArea getAreaSensor(String name) {
        for (MapArea areaSensor : areaSensors) {
            try {
                log.info(areaSensor.name);
                if (areaSensor.name.equals(name))
                    return areaSensor;
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

    public MapArea getObstacle(String name) {
        for (MapArea obstacle : obstacles) {
            if (obstacle.name.equals(name))
                return obstacle;
        }
        return null;
    }

    public AStarGraph getDiagonalGraph() {
        return diagonalGraph;
    }

    public AStarGraph getEdgeGraph() {
        return edgeGraph;
    }

    public void setShader(ShaderProgram shader) {
        if (tiledMapRenderer != null && tiledMap != null)
            ((OrthogonalTiledMapRenderer) tiledMapRenderer).getBatch().setShader(shader);
    }
    //endregion
}