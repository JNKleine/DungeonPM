package ecs.items.item;

import ecs.components.stats.DamageModifier;
import ecs.items.IOnCollectBehavior.DeletingItemInWorld;
import ecs.items.IOnDropBehavior.DeleteOnDrop;
import ecs.items.IOnUseBehavior.HealPlayerOnUse;
import ecs.items.ItemType;

/**
 * Item from type PotionOfHealing
 * **/
public class PotionOfHealing extends Item {

    /**
     * Create a PotionOfHealing with specified properties (20HP for user)
     * **/
    public PotionOfHealing() {
        super(ItemType.Active, "items/potionOfHealing/potionOfHealingInventoryAnimation",
            "items/potionOfHealing/potionOfHealingWorldAnimation","Potion of Healing",
            "drink this, you will receive 20 HP (trust me)",
            new DeletingItemInWorld(),
            new DeleteOnDrop(),
            new HealPlayerOnUse(20),
            new DamageModifier(),60);
    }
}
