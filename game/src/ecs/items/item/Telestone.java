package ecs.items.item;

import dslToGame.AnimationBuilder;
import ecs.components.stats.DamageModifier;
import ecs.items.IOnDropBehavior.DeleteOnDrop;
import ecs.items.IOnCollectBehavior.DeletingItemInWorld;
import ecs.items.IOnUseBehavior.OnUseTeleport;
import ecs.items.ItemData;
import ecs.items.ItemType;

public class Telestone extends ItemData {
    public Telestone() {
        super(ItemType.Active,AnimationBuilder.buildAnimation("items/telestone/telestoneInventoryAnimation"),
            AnimationBuilder.buildAnimation("items/telestone/telestoneWorldAnimation"),"Telestone",
            "Magic stone, that can teleport you",
            new DeletingItemInWorld(),
            new DeleteOnDrop(),
            new OnUseTeleport(),
            new DamageModifier());
    }
}
