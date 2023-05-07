package ecs.items.IOnUseBehavior;

import ecs.components.PositionComponent;
import ecs.entities.Entity;
import ecs.entities.Hero;
import ecs.items.IOnUse;
import ecs.items.ItemData;
import level.elements.tile.FloorTile;
import starter.Game;
import tools.Point;

import java.util.ArrayList;

public class SwordOnUse implements IOnUse {

    @Override
    public void onUse(Entity e, ItemData item) {

    }

    private ArrayList<FloorTile> getFloorTilesToHit(){
        ArrayList<FloorTile> ft = (ArrayList<FloorTile>) Game.currentLevel.getFloorTiles();
        ArrayList<Entity> entities = (ArrayList<Entity>) Game.getEntities();
        Hero hero = (Hero) entities.get(0);
        PositionComponent pos = (PositionComponent) hero.getComponent(PositionComponent.class).get();
        Point heroPoint = pos.getPosition();

    return ft;}
}
