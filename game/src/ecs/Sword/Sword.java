package ecs.Sword;

import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.items.ItemType;
import starter.Game;

import java.util.Optional;

public class Sword {
    private ItemType item = ItemType.Active;
    private DamageType damageType = DamageType.PHYSICAL;

    private Damage damage;

    public Sword(){

       // this.damage = new Damage(10,this.damageType,);
    }
}
