package ecs.items.item;

import dslToGame.AnimationBuilder;
import ecs.components.stats.DamageModifier;
import ecs.items.IOnCollectBehavior.BackpackOnCollect;
import ecs.items.IOnCollectBehavior.DeletingItemInWorld;
import ecs.items.IOnDropBehavior.BackpackOnDrop;
import ecs.items.IOnDropBehavior.DeleteOnDrop;
import ecs.items.IOnUseBehavior.NothingOnUse;
import ecs.items.ItemData;
import ecs.items.ItemType;

/**
 * Item from type Backpack
 * **/
public class Backpack extends ItemData {
    int size;
    ItemType  storedItems;
    /**
     * Create a PotionOfHealing with specified properties (All Foes -> Damage 5 HP)
     * @param size: inventory size from the backpack
     * @param storedItems: Items from this type can be stored in this backpack
     * **/
    public Backpack(int size, ItemType storedItems) {
        super(ItemType.Backpack, AnimationBuilder.buildAnimation("items/backpack/backpackInventoryAnimation"),
            AnimationBuilder.buildAnimation("items/backpack/backpackWorldAnimation"),"Backpack",
            "This Backpack grants you 4 new possibilities to store items from type active",
            new BackpackOnCollect(size),
            new BackpackOnDrop(),
            new NothingOnUse(),
            new DamageModifier());
        this.size = size;
        this.storedItems = storedItems;
    }
}
