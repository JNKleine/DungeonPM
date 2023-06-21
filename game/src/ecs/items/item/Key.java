package ecs.items.item;

import ecs.components.stats.DamageModifier;
import ecs.items.IOnCollectBehavior.DeletingItemInWorld;
import ecs.items.IOnDropBehavior.DeleteOnDropAndSpawnInWorld;
import ecs.items.IOnUseBehavior.NothingOnUse;
import ecs.items.ItemType;

public class Key extends Item{

    public Key() {
        super(ItemType.Passive, "items/key/keyInventoryAnimation/key.png","items/key/keyWorldAnimation/key.png", "Key",
            "This Key can open ANY chest",
            new DeletingItemInWorld(),
            new DeleteOnDropAndSpawnInWorld(),
            new NothingOnUse(),
            new DamageModifier(), 300);

    }
}
