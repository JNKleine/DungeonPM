package ecs.items.item;

import ecs.components.stats.DamageModifier;
import ecs.items.IOnCollectBehavior.CoinOnCollect;
import ecs.items.IOnDropBehavior.DeleteOnDropAndPlaceAtSamePosition;
import ecs.items.IOnUseBehavior.NothingOnUse;
import ecs.items.ItemType;

/** Item from Type Coin */
public class Coin extends Item {

    /**
     * Creates a coin, coins are the currency in this game
     *
     * @param value value of the coin
     * @param inventoryTexture Texture when a coin is in the Inventory
     * @param worldTexture Texture when a coin is in the world
     */
    public Coin(int value, String inventoryTexture, String worldTexture) {
        super(
                ItemType.Passive,
                inventoryTexture,
                worldTexture,
                "Coin",
                "Coin to buy items at the shop",
                new CoinOnCollect(value),
                new DeleteOnDropAndPlaceAtSamePosition(),
                new NothingOnUse(),
                new DamageModifier(),
                value);
    }
}
