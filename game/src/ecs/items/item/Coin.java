package ecs.items.item;

import ecs.components.stats.DamageModifier;
import ecs.items.IOnCollectBehavior.DeletingItemInWorld;
import ecs.items.IOnDropBehavior.DeleteOnDrop;
import ecs.items.IOnUseBehavior.NothingOnUse;
import ecs.items.ItemType;

public class Coin extends Item{

    private int value;

    public Coin(int value, String inventoryTexture, String worldTexture){
            super(ItemType.Passive, inventoryTexture,
                worldTexture, "Coin", "Coin to buy items at the shop",
                new DeletingItemInWorld(),
                new DeleteOnDrop(),
                new NothingOnUse(),
                new DamageModifier());
    }
}
