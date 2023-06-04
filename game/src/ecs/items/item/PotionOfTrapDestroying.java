package ecs.items.item;

import ecs.components.stats.DamageModifier;
import ecs.items.IOnCollectBehavior.DeletingItemInWorld;
import ecs.items.IOnDropBehavior.DeleteOnDrop;
import ecs.items.IOnUseBehavior.DestroyTrapsOnUse;
import ecs.items.ItemType;

public class PotionOfTrapDestroying extends Item{
    /**
     * Create a PotionOfTrapDestroying with specified properties
     * **/
    public PotionOfTrapDestroying() {
        super(ItemType.Active, "items/potionOfTrapDestroying/potionOfTrapDestroyingInventoryAnimation",
            "items/potionOfTrapDestroying/potionOfTrapDestroyingWorldAnimation","Potion of Trapdestroying",
            "If you drink this you will lose 5 HP but all traps in this level are gone",
            new DeletingItemInWorld(),
            new DeleteOnDrop(),
            new DestroyTrapsOnUse(),
            new DamageModifier(),120);
    }
}
