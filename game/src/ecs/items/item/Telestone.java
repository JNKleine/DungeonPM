package ecs.items.item;

import ecs.components.stats.DamageModifier;
import ecs.items.*;
import ecs.items.IOnCollectBehavior.DeletingItemInWorld;
import ecs.items.IOnDropBehavior.DeleteOnDropAndSpawnInWorld;
import ecs.items.IOnUseBehavior.TeleportPlayerOnUse;

/** Item from type Telestone * */
public class Telestone extends Item {

    /** Create a Telestone with specified properties * */
    public Telestone() {
        super(
                ItemType.Active,
                "items/telestone/telestoneInventoryAnimation",
                "items/telestone/telestoneWorldAnimation",
                "Telestone",
                "Magic stone, that can teleport you",
                new DeletingItemInWorld(),
                new DeleteOnDropAndSpawnInWorld(),
                new TeleportPlayerOnUse(),
                new DamageModifier(),
                50);
    }
}
