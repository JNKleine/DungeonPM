package ecs.items.item;

import ecs.components.stats.DamageModifier;
import ecs.items.IOnCollectBehavior.CoinOnCollect;
import ecs.items.IOnCollectBehavior.DeletingItemInWorld;
import ecs.items.IOnDropBehavior.DeleteOnDrop;
import ecs.items.IOnDropBehavior.DeleteOnDropAndSpawnInWorld;
import ecs.items.IOnUseBehavior.NothingOnUse;
import ecs.items.ItemType;

/**
 * Item from Type Coin
 */
public class Coin extends Item{


    /**
     * Creates a Coin, coins are the currency in this game
     * @param value value of the Coun
     * @param inventoryTexture
     * @param worldTexture
     */
    public Coin(int value, String inventoryTexture, String worldTexture){
            super(ItemType.Passive, inventoryTexture,
                worldTexture, "Coin", "Coin to buy items at the shop",
                new CoinOnCollect(value),
                new DeleteOnDropAndSpawnInWorld(),
                new NothingOnUse(),
                new DamageModifier(),value);
    }

}
