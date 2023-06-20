package ecs.items.item;

import ecs.components.stats.DamageModifier;
import ecs.items.IOnCollectBehavior.DeletingItemInWorld;
import ecs.items.IOnDropBehavior.DeleteOnDrop;
import ecs.items.IOnDropBehavior.DeleteOnDropAndSpawnInWorld;
import ecs.items.IOnUseBehavior.DestroyTrapsOnUse;
import ecs.items.ItemType;

/**
 * PotionOfTrapDestroying is an Item for destroying traps
 * **/
public class PotionOfTrapDestroying extends Item{
    /**
     * Create a PotionOfTrapDestroying with specified properties
     * **/
    public PotionOfTrapDestroying() {
        super(ItemType.Active, "items/potionOfTrapDestroying/potionOfTrapDestroyingInventoryAnimation",
            "items/potionOfTrapDestroying/potionOfTrapDestroyingWorldAnimation","Potion of Trapdestroying",
            "If you drink this you will lose 5 HP but all traps in this level are gone",
            new DeletingItemInWorld(),
            new DeleteOnDropAndSpawnInWorld(),
            new DestroyTrapsOnUse(),
            new DamageModifier(),120);
    }
}
