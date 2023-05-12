package ecs.items.item;

import ecs.components.stats.DamageModifier;
import ecs.items.IOnCollectBehavior.DeletingItemInWorld;
import ecs.items.IOnDropBehavior.DeleteOnDrop;
import ecs.items.IOnUseBehavior.SwordOnUse;;
import ecs.items.ItemType;


/**
 * Item from type Sword
 * **/
public class Sword extends Item {

    /**
     * Create a Sword with specified properties (Hitrange: 1, Damage: 10)
     * **/
    public Sword(){
        super(ItemType.Active, "items/sword/swordInventoryAnimation",
            "items/sword/swordWorldAnimation","Sword",
            "Sword to hit and kill Monsters",
            new DeletingItemInWorld(),
            new DeleteOnDrop(),
            new SwordOnUse(1,10),
            new DamageModifier());
    }
}
