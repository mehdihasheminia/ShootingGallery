package com.bornaapp.borna2d.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.bornaapp.borna2d.ai.AStarGraph;
import com.bornaapp.borna2d.ai.PathRenderer;
import com.bornaapp.borna2d.components.PathComponent;
import com.bornaapp.borna2d.game.levels.Engine;
import com.bornaapp.borna2d.game.levels.LevelBase;
import com.bornaapp.borna2d.log;

/**
 * Created by Mehdi on 08/30/2015.
 * ...
 */
public class PathFindingSystem extends IteratingSystem {

    Sync sync;
    private ComponentMapper<PathComponent> pathMap;

    private PathRenderer pathRenderer = new PathRenderer(Engine.getInstance().getCurrentLevel().getMap().getEdgeGraph());

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
                pathRenderer.drawPath(pathComp.path);
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
}
