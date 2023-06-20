package ecs.items.IOnDropBehavior;

import ecs.components.InventoryComponent;
import ecs.components.ai.AITools;
import ecs.entities.Entity;
import ecs.items.IOnDrop;
import ecs.items.ItemData;
import ecs.items.ItemDataGenerator;
import ecs.items.WorldItemBuilder;
import level.elements.tile.Tile;
import tools.Point;

import java.util.List;
import java.util.Random;

/**
 * Handles what happend when an item is dropped out of an inventory and needs to get spawned in the world
 */
public class DeleteOnDropAndSpawnInWorld implements IOnDrop {

    @Override
    public void onDrop(Entity user, ItemData which, Point position) {
        Random rd = new Random();
        InventoryComponent ic = (InventoryComponent)user.getComponent(InventoryComponent.class).get();
        ic.removeItem(which);
        ic.setCurMainItem(null);
        List<Tile> accessibleTiles = AITools.getAccessibleTilesInRange(position, 1f);
        Tile tileToDropOn = accessibleTiles.get(rd.nextInt(accessibleTiles.size()));
         new WorldItemBuilder().buildWorldItemAtPosition(which,tileToDropOn.getCoordinateAsPoint());
    }
}
