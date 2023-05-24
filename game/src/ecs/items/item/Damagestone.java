package ecs.items.item;

import ecs.components.stats.DamageModifier;
import ecs.items.IOnCollectBehavior.DeletingItemInWorld;
import ecs.items.IOnDropBehavior.DeleteOnDrop;
import ecs.items.IOnUseBehavior.DamageFoeMagicOnUse;
import ecs.items.ItemType;

/**
 * Item from type Damagestone
 * **/
public class Damagestone extends Item {
    /**
     * Create a PotionOfHealing with specified properties (All Foes -> Damage 5 HP)
     * **/
    public Damagestone() {
        super(ItemType.Active, "items/damagestone/damagestoneInventoryAnimation",
            "items/damagestone/damagestoneWorldAnimation","Damagestone",
            "Magic stone, that give every NPC with the Tag Foe, 5 HP damage",
            new DeletingItemInWorld(),
            new DeleteOnDrop(),
            new DamageFoeMagicOnUse() {
                @Override
                public int getDamageAmount() {
                    return 5;
                }
            },
            new DamageModifier(),30);
    }
}
