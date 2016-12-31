package com.bornaapp.borna2d.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.bornaapp.borna2d.ai.AStarGraph;
import com.bornaapp.borna2d.ai.AStarPath;
import com.bornaapp.borna2d.ai.PathRenderer;
import com.bornaapp.borna2d.components.PathComponent;
import com.bornaapp.borna2d.game.levels.LevelBase;
import com.bornaapp.borna2d.Debug.log;

/**
 * Created by Mehdi on 08/30/2015.
 * ...
 */
public class PathFindingSystem extends IteratingSystem {

    Sync sync;
    private PathRenderer pathRenderer = null;
    private ComponentMapper<PathComponent> pathMap;

    public PathFindingSystem(LevelBase level) {
        super(Family.all(PathComponent.class).get(), level.getSystemPriority());
        sync = new Sync();
        pathMap = ComponentMapper.getFor(PathComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        PathComponent pathComp = pathMap.get(entity);

        pathComp.SetPosition(sync.getX(entity), sync.getY(entity));

        if (pathComp.isPathValid()) {
            //
            if (pathComp.drawDebug)
                RenderDebug(pathComp.path);
            //
            if (pathComp.reachedDestination()) {
                //
                pathComp.cancelDestination();
                //
            } else {
                //
                if (!pathComp.reachedTargetNode())
                    pathComp.CalculateVelocityToTargetNode(deltaTime);
                else
                    pathComp.gotoNextNode();
            }
        }
        sync.setVelocity(entity, pathComp.getVelocity());
    }

    public void InitDebugRenderer(AStarGraph graph) {
        pathRenderer = new PathRenderer(graph);
    }

    private void RenderDebug(AStarPath path) {
        try {
            pathRenderer.drawPath(path);
        } catch (Exception e) {
            log.error(e.getMessage() + " , PathRenderer is not Initialized");
        }
    }
}
