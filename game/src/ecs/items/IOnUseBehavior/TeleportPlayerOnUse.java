package ecs.items.IOnUseBehavior;

import ecs.components.InventoryComponent;
import ecs.components.PositionComponent;
import ecs.entities.Entity;
import ecs.items.IOnUse;
import ecs.items.ItemData;
import level.elements.tile.FloorTile;
import starter.Game;
import tools.Point;

import java.util.ArrayList;
import java.util.Random;

public class TeleportPlayerOnUse implements IOnUse {
    @Override
    public void onUse(Entity e, ItemData item) {
        InventoryComponent ic = (InventoryComponent) e.getComponent(InventoryComponent.class).get();
        ic.removeItem(item);
        PositionComponent pc = (PositionComponent)e.getComponent(PositionComponent.class).get();
        pc.setPosition(getRandomAccesiblePoint());
    }

    private Point getRandomAccesiblePoint() {
        ArrayList<FloorTile> ft = (ArrayList)Game.currentLevel.getFloorTiles();
        Random rd = new Random();
        return ft.get(rd.nextInt(0,ft.size())).getCoordinateAsPoint();
    }
}
