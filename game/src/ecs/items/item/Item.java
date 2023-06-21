package ecs.items.item;

import dslToGame.AnimationBuilder;
import ecs.components.stats.DamageModifier;
import ecs.items.*;

/** Class Item is the superclass for all Items. * */
public class Item {
    /** Items have a itemData object * */
    private ItemData itemData;

    private int itemValue;

    /**
     * Constructor for Items
     *
     * @param itemType Type of the item
     * @param inventoryTexture Texture from item in inventory
     * @param worldTexture Texture from item in world
     * @param itemName Name from item
     * @param description Description from item
     * @param iOC IOnCollectBehavior from item
     * @param iOD IONDropBehavior from item
     * @param iOU IONUseBehavior from item
     * @param dmgM DamageModifier from item
     * @param itemValue Coin-worth from Item *
     */
    public Item(
            ItemType itemType,
            String inventoryTexture,
            String worldTexture,
            String itemName,
            String description,
            IOnCollect iOC,
            IOnDrop iOD,
            IOnUse iOU,
            DamageModifier dmgM,
            int itemValue) {
        itemData =
                new ItemData(
                        itemType,
                        AnimationBuilder.buildAnimation(inventoryTexture),
                        AnimationBuilder.buildAnimation(worldTexture),
                        itemName,
                        description,
                        itemValue,
                        iOC,
                        iOD,
                        iOU,
                        dmgM);
    }

    /** Get the ItemData from this object* */
    public ItemData getItemData() {
        return itemData;
    }

    public int getValue() {
        return itemValue;
    }
}
