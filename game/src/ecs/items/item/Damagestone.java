package ecs.items.item;

import dslToGame.AnimationBuilder;
import ecs.components.stats.DamageModifier;
import ecs.items.IOnCollectBehavior.DeletingItemInWorld;
import ecs.items.IOnDropBehavior.DeleteOnDrop;
import ecs.items.IOnUseBehavior.DamageFoeMagicOnUse;
import ecs.items.ItemData;
import ecs.items.ItemType;

public class Damagestone extends ItemData {
    public Damagestone() {
        super(ItemType.Active, AnimationBuilder.buildAnimation("items/damagestone/damagestoneInventoryAnimation"),
            AnimationBuilder.buildAnimation("items/damagestone/damagestoneWorldAnimation"),"Damagestone",
            "Magic stone, that give every NPC with the Tag Foe, 5 HP damage",
            new DeletingItemInWorld(),
            new DeleteOnDrop(),
            new DamageFoeMagicOnUse() {
                @Override
                public int getDamageAmount() {
                    return 5;
                }
            },
            new DamageModifier());
    }
}
