package com.bornaapp.borna2d.ai;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.bornaapp.borna2d.game.levels.Engine;

import java.util.Iterator;

public class PathRenderer {

    AStarGraph graph;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public PathRenderer(AStarGraph _graph) {
        graph = _graph;
    }

    public void drawPath(AStarPath path) {

        Iterator<Node> pathIterator = path.iterator();
        Node node1, node2 = null;

        while (pathIterator.hasNext()) {

            node1 = pathIterator.next();
            Vector2 p1 = graph.getNodeXY(node1);

            shapeRenderer.setProjectionMatrix(Engine.getInstance().getCurrentLevel().getCamera().combined);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.circle(p1.x, p1.y, 5);
            shapeRenderer.end();

            if (node2 != null) {
                Vector2 p2 = graph.getNodeXY(node2);
                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
                shapeRenderer.line(p1.x, p1.y, p2.x, p2.y);
                shapeRenderer.end();
            }
            node2 = node1;
        }
    }
}
