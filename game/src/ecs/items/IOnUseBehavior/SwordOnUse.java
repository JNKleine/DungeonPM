package ecs.items.IOnUseBehavior;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
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
import graphic.Animation;
import starter.Game;
import tools.Point;

import java.util.ArrayList;
import java.util.Set;


public class SwordOnUse implements IOnUse {

    /**
     * Entity that uses the sowrd
     */
    private Hero hero;

    /**
     * hitRange to hit enemy entities in range
     */
    private int hitRange;
    /**
     * initial damage used for the current "normal" sword
     */
    private int damage;

    private final String PATHTOHITTOLEFT = "knight/hitToLeft";

    private final String PATHTOHITTORIGHT = "knight/hitToRight";

    /**
     * Creates a new Sword with the given hitRange and damage
     *
     * @param hitRange
     * @param damage
     */
    public SwordOnUse(int hitRange, int damage) {
        this.hitRange = hitRange;
        this.damage = damage;
    }

    @Override
    public void onUse(Entity e, ItemData item) {
        this.hero = (Hero) e;
        ArrayList<Entity> entities = getEntitiesToHit();
        for (Entity entity : entities) {
            HealthComponent hc = (HealthComponent) entity.getComponent(HealthComponent.class).get();
            hc.receiveHit(new Damage(damage, DamageType.PHYSICAL, e));
        }
        AnimationComponent ac = (AnimationComponent) this.hero.getComponent(AnimationComponent.class).get();
        if (PlayerSystem.getKey() == 0) {
            ac.setCurrentAnimation(fillArrListForAnimation(0));
        } else if (PlayerSystem.getKey() == 1) {
            ac.setCurrentAnimation(fillArrListForAnimation(1));
        } else if (PlayerSystem.getKey() == 2) {
            ac.setCurrentAnimation(fillArrListForAnimation(2));
        } else if (PlayerSystem.getKey() == 3) {
            ac.setCurrentAnimation(fillArrListForAnimation(3));
        }
    }

    /*
        Method to sort out friendly enemies and give the direction key for the hit to the hitInDirection method
     */
    private ArrayList<Entity> getEntitiesToHit() {
        Set<Entity> entities = Game.getEntities();
        ArrayList<Entity> listOfEntities = new ArrayList<>();
        ArrayList<Entity> entsToHit = new ArrayList<>();
        for (Entity e : entities) {
            if (e.getFaction() == Faction.FOE)
                listOfEntities.add(e);
        }
        // Hit left
        if (PlayerSystem.getKey() == 0) {
            entsToHit = hitInDirection(0, listOfEntities);
            // Hit up
        } else if (PlayerSystem.getKey() == 1) {
            entsToHit = hitInDirection(1, listOfEntities);
            // Hit right
        } else if (PlayerSystem.getKey() == 2) {
            entsToHit = hitInDirection(2, listOfEntities);
            // Hit down
        } else if (PlayerSystem.getKey() == 3) {
            entsToHit = hitInDirection(3, listOfEntities);
        }
        return entsToHit;
    }

    /*
    Method to get all entitiies that are in the hitRange of the Hero
     */
    private ArrayList<Entity> hitInDirection(int key, ArrayList<Entity> entities) {
        PositionComponent pos = (PositionComponent) hero.getComponent(PositionComponent.class).get();
        Point heroPoint = pos.getPosition();
        ArrayList<Entity> entitiesToHit = new ArrayList<>();
        for (Entity e : entities) {
            PositionComponent posOfEntity = (PositionComponent) e.getComponent(PositionComponent.class).get();
            Point pointOfEntity = posOfEntity.getPosition();
            boolean checkRight = (heroPoint.toCoordinate().x >= pointOfEntity.toCoordinate().x - this.hitRange && heroPoint.toCoordinate().y == pointOfEntity.toCoordinate().y);
            boolean checkLeft = (heroPoint.toCoordinate().x <= pointOfEntity.toCoordinate().x + this.hitRange && heroPoint.toCoordinate().y == pointOfEntity.toCoordinate().y);
            boolean checkUpper = (heroPoint.toCoordinate().y >= pointOfEntity.toCoordinate().y - this.hitRange && heroPoint.toCoordinate().x == pointOfEntity.toCoordinate().x);
            boolean checkDown = (heroPoint.toCoordinate().y <= pointOfEntity.toCoordinate().y + this.hitRange && heroPoint.toCoordinate().x == pointOfEntity.toCoordinate().x);
            boolean checkUpperRight = (checkRight && checkUpper);
            boolean checkUpperLeft = (checkLeft && checkUpper);
            boolean checkDownRight = (checkRight && checkDown);
            boolean checkDownLeft = (checkLeft && checkDown);
            if (key == 0 && (checkLeft || checkUpperLeft || checkDownLeft)) {
                entitiesToHit.add(e);
            } else if (key == 1 && (checkUpper || checkUpperLeft || checkUpperRight)) {
                entitiesToHit.add(e);
            } else if (key == 2 && (checkRight || checkUpperRight || checkDownRight)) {
                entitiesToHit.add(e);
            } else if (key == 3 && (checkDown || checkDownLeft || checkDownRight)) {
                entitiesToHit.add(e);
            }
        }
        return entitiesToHit;
    }

    private Animation fillArrListForAnimation(int key) {
        ArrayList<String> animationList = new ArrayList<>();
        if (PlayerSystem.getKey() == 0) {
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToLeft\\knight_sword_rightToLeft_01.png");
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToLeft\\knight_sword_rightToLeft_02.png");
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToLeft\\knight_sword_rightToLeft_03.png");
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToLeft\\knight_sword_rightToLeft_04.png");
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToLeft\\knight_sword_rightToLeft_05.png");
        } else if ( PlayerSystem.getKey() == 1) {
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToUp\\knight_sword_downToUp_01.png");
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToUp\\knight_sword_downToUp_02.png");
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToUp\\knight_sword_downToUp_03.png");
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToUp\\knight_sword_downToUp_04.png");
        } else if ( PlayerSystem.getKey() == 2){
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToRight\\knight_sword_leftToRight_01.png");
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToRight\\knight_sword_leftToRight_02.png");
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToRight\\knight_sword_leftToRight_03.png");
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToRight\\knight_sword_leftToRight_04.png");
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToRight\\knight_sword_leftToRight_05.png");
        } else if ( PlayerSystem.getKey() == 3) {
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToDown\\knight_sword_upToDown_01.png");
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToDown\\knight_sword_upToDown_02.png");
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToDown\\knight_sword_upToDown_03.png");
            animationList.add("C:\\Users\\p\\OneDrive\\Dokumente\\GitHub\\DungeonPM\\game\\assets\\character\\knight\\hitToDown\\knight_sword_upToDown_04.png");
        }
        Animation hitAnimation = new Animation(animationList, 3, false);
        return hitAnimation;
    }
}
