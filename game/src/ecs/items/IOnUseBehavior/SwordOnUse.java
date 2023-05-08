package ecs.items.IOnUseBehavior;

import ecs.components.HealthComponent;
import ecs.components.PositionComponent;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.entities.Entity;
import ecs.entities.Faction;
import ecs.entities.Hero;
import ecs.items.IOnUse;
import ecs.items.ItemData;
import ecs.systems.PlayerSystem;
import starter.Game;
import tools.Point;

import java.util.ArrayList;
import java.util.Set;


public class SwordOnUse implements IOnUse {

    private Hero hero;

    private int hitRange;
    private int damage;

    public SwordOnUse(int hitRange, int damage) {
        this.hitRange = hitRange;
        this.damage = damage;
    }

    @Override
    public void onUse(Entity e, ItemData item) {
        this.hero = (Hero) e;
        ArrayList<Entity> entities = getEntitiesToHit();
        for(Entity entity: entities) {
            HealthComponent hc = (HealthComponent) entity.getComponent(HealthComponent.class).get();
            hc.receiveHit(new Damage(damage, DamageType.PHYSICAL,e));
        }
    }

    private ArrayList<Entity> getEntitiesToHit() {
        Set<Entity> entities = Game.getEntities();
        ArrayList<Entity> listOfEntities = new ArrayList<>();
        ArrayList<Entity> entsToHit = new ArrayList<>();
        for (Entity e : entities) {
            if (e.getFaction() == Faction.FOE)
                listOfEntities.add(e);
        }

        // Hit nach links
        if (PlayerSystem.getKey() == 0) {
             entsToHit = hitInDirection(0,listOfEntities);
            // Hit nach oben
        } else if (PlayerSystem.getKey() == 1) {
            entsToHit = hitInDirection(1,listOfEntities);
            // Hit nach rechts
        } else if (PlayerSystem.getKey() == 2) {
            entsToHit = hitInDirection(2,listOfEntities);
            // Hit nach unten
        } else if (PlayerSystem.getKey() == 3) {
            entsToHit = hitInDirection(3,listOfEntities);
        }
        return entsToHit;
    }
    private ArrayList<Entity> hitInDirection(int key, ArrayList<Entity> entities) {
        PositionComponent pos = (PositionComponent) hero.getComponent(PositionComponent.class).get();
        Point heroPoint = pos.getPosition();
        ArrayList<Entity> entitiesToHit = new ArrayList<>();
        for ( Entity e : entities) {
            PositionComponent posOfEntity = (PositionComponent) e.getComponent(PositionComponent.class).get();
            Point pointOfEntity = posOfEntity.getPosition();
            boolean checkRight = (heroPoint.toCoordinate().x >= pointOfEntity.toCoordinate().x-this.hitRange && heroPoint.toCoordinate().y == pointOfEntity.toCoordinate().y);
            boolean checkLeft = (heroPoint.toCoordinate().x <= pointOfEntity.toCoordinate().x+this.hitRange && heroPoint.toCoordinate().y == pointOfEntity.toCoordinate().y);
            boolean checkUpper = (heroPoint.toCoordinate().y >= pointOfEntity.toCoordinate().y-this.hitRange && heroPoint.toCoordinate().x == pointOfEntity.toCoordinate().x);
            boolean checkDown = (heroPoint.toCoordinate().y <= pointOfEntity.toCoordinate().y+this.hitRange && heroPoint.toCoordinate().x == pointOfEntity.toCoordinate().x);
            boolean checkUpperRight = (checkRight && checkUpper);
            boolean checkUpperLeft = (checkLeft && checkUpper);
            boolean checkDownRight = (checkRight && checkDown);
            boolean checkDownLeft = (checkLeft && checkDown);
             if ( key == 0 && ( checkLeft || checkUpperLeft || checkDownLeft)) {
                 entitiesToHit.add(e);
             }
              else if ( key == 1 && ( checkUpper || checkUpperLeft || checkUpperRight )) {
                 entitiesToHit.add(e);
             }
              else if ( key == 2 && ( checkRight || checkUpperRight || checkDownRight)) {
                 entitiesToHit.add(e);
             }
              else if ( key == 3 && ( checkDown || checkDownLeft || checkDownRight)) {
                 entitiesToHit.add(e);
             }
        }
        return entitiesToHit;
    }
}
