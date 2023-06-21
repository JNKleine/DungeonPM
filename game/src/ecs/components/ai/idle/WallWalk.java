package ecs.components.ai.idle;

import ecs.components.PositionComponent;
import ecs.components.ai.AITools;
import ecs.entities.Entity;
import java.util.ArrayList;
import level.elements.tile.FloorTile;
import level.elements.tile.WallTile;
import level.tools.Coordinate;
import starter.Game;

/**
 * Entities with this AI will run most of the time on the FloorTiles, which are attached to a
 * WallTile
 */
public class WallWalk implements IIdleAI {

    private boolean firstRun = true;
    private ArrayList<FloorTile> currentPathFloor = new ArrayList<>();
    private ArrayList<FloorTile> floorAtAWall = new ArrayList<FloorTile>();
    private FloorTile targetTile = null;

    /** WallWalk's Constructor scans the level for all FloorTiles, connected to the wall */
    public WallWalk() {
        ArrayList<WallTile> wall = (ArrayList) Game.currentLevel.getWallTiles();
        for (WallTile wt : wall) {
            ArrayList<FloorTile> floor = (ArrayList) Game.currentLevel.getFloorTiles();
            for (FloorTile ft : floor) {
                if (!floorAtAWall.contains(ft)) {
                    if (wt.getCoordinate().x == ft.getCoordinate().x + 1
                            && wt.getCoordinate().y == ft.getCoordinate().y) {
                        floorAtAWall.add(ft);
                    } else if (wt.getCoordinate().x == ft.getCoordinate().x - 1
                            && wt.getCoordinate().y == ft.getCoordinate().y) {
                        floorAtAWall.add(ft);
                    } else if (wt.getCoordinate().x == ft.getCoordinate().x
                            && wt.getCoordinate().y == ft.getCoordinate().y + 1) {
                        floorAtAWall.add(ft);
                    } else if (wt.getCoordinate().x == ft.getCoordinate().x
                            && wt.getCoordinate().y == ft.getCoordinate().y - 1) {
                        floorAtAWall.add(ft);
                    }
                }
            }
        }
    }

    public void idle(Entity entity) {
        if (firstRun) {
            deepCopyList();
            firstRun = false;
        }

        PositionComponent pc =
                (PositionComponent) entity.getComponent(PositionComponent.class).get();
        Coordinate currentPosEntity = pc.getPosition().toCoordinate();
        if (targetTile == null
                || (currentPosEntity.x == targetTile.getCoordinate().x
                        && currentPosEntity.y == targetTile.getCoordinate().y)) {
            if (currentPathFloor.size() == 0) {
                deepCopyList();
            }
            targetTile = getNearestTile(currentPosEntity);
        }
        AITools.move(entity, AITools.calculatePath(currentPosEntity, targetTile.getCoordinate()));
    }

    private void deepCopyList() {
        currentPathFloor.clear();
        for (FloorTile ft : floorAtAWall) {
            currentPathFloor.add(ft);
        }
    }

    private FloorTile getNearestTile(Coordinate curPosEntity) {
        int distance = -1;
        FloorTile nearestTile = null;
        for (FloorTile ft : currentPathFloor) {
            int curDistance =
                    Math.abs(
                            (curPosEntity.x + curPosEntity.y)
                                    - (ft.getCoordinate().x + ft.getCoordinate().y));
            if (distance == -1 || curDistance < distance) {
                nearestTile = ft;
                distance = curDistance;
            }
        }
        currentPathFloor.remove(nearestTile);
        return nearestTile;
    }
}
