package ecs.components.ai.idle;
import ecs.components.HealthComponent;
import ecs.components.PositionComponent;
import ecs.components.ai.AITools;
import ecs.components.ai.idle.IIdleAI;
import ecs.entities.Entity;
import ecs.entities.Faction;
import ecs.entities.PillowOfBadDreams;
import ecs.entities.SlimeGuard;
import ecs.entities.Trap.ExplosionTrap;
import ecs.entities.Trap.SpikeTrap;
import level.elements.tile.FloorTile;
import level.elements.tile.Tile;
import starter.Game;
import tools.Point;

import java.util.ArrayList;
import java.util.Random;

public class TelepeterOutOfRangeWalk implements IIdleAI {

    private int curFrames = 0;
    private final int waitFramesTillSpawn;

    private final int waitFramesTillTeleport;
    private final int maxEntitiesPerSpawn;

    private final int maxTrapsPerSpawn;

    public TelepeterOutOfRangeWalk(int waitFramesTillSpawn, int waitFramesTillTeleport, int maxTrapsPerSpawn, int maxEntitiesPerSpawn) {
        this.waitFramesTillSpawn = waitFramesTillSpawn;
        this.waitFramesTillTeleport = waitFramesTillTeleport;
        this.maxEntitiesPerSpawn = maxEntitiesPerSpawn;
        this.maxTrapsPerSpawn = maxEntitiesPerSpawn;

    }

    @Override
    public void idle(Entity entity) {
        HealthComponent hc = (HealthComponent)entity.getComponent(HealthComponent.class).get();
        if(curFrames%waitFramesTillSpawn == 0 && Game.getEntities().size() <= 20) {
            if(hc.getCurrentHealthpoints()*2 <= hc.getMaximalHealthpoints()) {
                spawnMonster();
            }
            else {
                spawnTraps();
            }
        }
        if(curFrames%waitFramesTillTeleport == 0) {
            if(hc.getCurrentHealthpoints()*2 <= hc.getMaximalHealthpoints()) {
                teleportNextToPlayer(entity);
            }
            else {
                teleportRandom(entity);
            }
        }
            curFrames = curFrames < Integer.MAX_VALUE? ++curFrames: 0;
    }

    private void spawnMonster() {
        Random rnd = new Random();
        int anzMonster = rnd.nextInt(1,maxEntitiesPerSpawn+1);
        int monsterKind = rnd.nextInt(2);
        for (int i = 0; i < anzMonster; i++) {
            if(monsterKind == 0) {
                new SlimeGuard();
            }
            else
                new PillowOfBadDreams();
        }
    }

    private void spawnTraps() {
        for(Entity e: Game.getEntities()) {
            if (e.getFaction().equals(Faction.TRAP)) {
                HealthComponent hc = (HealthComponent) e.getComponent(HealthComponent.class).get();
                hc.setCurrentHealthpoints(0);
            }
        }
            Random rnd = new Random();
            int anzTrap = rnd.nextInt(1,maxTrapsPerSpawn+1);
            int trapKind = rnd.nextInt(2);
            for (int i = 0; i < anzTrap; i++) {
                if(trapKind == 0) {
                    new SpikeTrap();
                }
                else
                    new ExplosionTrap();
            }

    }

    private void teleportRandom(Entity entity) {
        boolean fLoop = true;
        PositionComponent pc = (PositionComponent) entity.getComponent(PositionComponent.class).get();
        PositionComponent pcH = (PositionComponent)Game.getHero().get().getComponent(PositionComponent.class).get();
        while(fLoop) {
            Point newPos = Game.currentLevel.getRandomFloorTile().getCoordinateAsPoint();
            if(!newPos.equals(pcH.getPosition()) || Game.currentLevel.getFloorTiles().size()<5) {
                pc.setPosition(newPos);
                fLoop = false;
            }
        }
    }

    private void teleportNextToPlayer(Entity entity) {
        PositionComponent pc = (PositionComponent) entity.getComponent(PositionComponent.class).get();
        PositionComponent pcH = (PositionComponent)Game.getHero().get().getComponent(PositionComponent.class).get();
        ArrayList<Tile> nextToPlayer = (ArrayList) AITools.getAccessibleTilesInRange(pcH.getPosition(),3f);
        Random rn = new Random();
        pc.setPosition(nextToPlayer.get(rn.nextInt(0,nextToPlayer.size()))
            .getCoordinateAsPoint());

    }
}
