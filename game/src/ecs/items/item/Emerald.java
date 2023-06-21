package ecs.items.item;

import ecs.components.stats.DamageModifier;
import ecs.items.IOnCollectBehavior.DeletingItemInWorld;
import ecs.items.IOnDropBehavior.DeleteOnDropAndSpawnInWorld;
import ecs.items.IOnUseBehavior.NothingOnUse;
import ecs.items.ItemType;

/** Item from type emerald for the Boss */
public class Emerald extends Item {

    public Emerald(int value, String inventoryTexture, String worldTexture) {
        super(
                ItemType.Passive,
                inventoryTexture,
                worldTexture,
                "Emerald",
                "Emerald from the Boss, worth a big amount of money",
                new DeletingItemInWorld(),
                new DeleteOnDropAndSpawnInWorld(),
                new NothingOnUse(),
                new DamageModifier(),
                value);
    }
}
