package ecs.items.item;

import dslToGame.AnimationBuilder;
import ecs.components.stats.DamageModifier;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.entities.Hero;
import ecs.items.IOnCollectBehavior.DeletingItemInWorld;
import ecs.items.IOnDropBehavior.DeleteOnDrop;
import ecs.items.IOnUseBehavior.SwordOnUse;
import ecs.items.ItemData;
import ecs.items.ItemType;
import starter.Game;

import java.util.Optional;
import java.util.Set;

/**
 * Item from type Sword
 * **/
public class Sword extends ItemData {

    /**
     * Create a Sword with specified properties (Hitrange: 1, Damage: 10)
     * **/
    public Sword(){
        super(ItemType.Active, AnimationBuilder.buildAnimation("items/sword/swordInventoryAnimation"),
            AnimationBuilder.buildAnimation("items/sword/swordWorldAnimation"),"Sword",
            "Sword to hit and kill Monsters",
            new DeletingItemInWorld(),
            new DeleteOnDrop(),
            new SwordOnUse(1,10),
            new DamageModifier());
    }
}
