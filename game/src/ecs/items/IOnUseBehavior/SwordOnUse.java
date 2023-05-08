package ecs.items.IOnUseBehavior;

import ecs.components.PositionComponent;
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

    public SwordOnUse(int hitRange) {
        this.hitRange = hitRange;
    }

    @Override
    public void onUse(Entity e, ItemData item) {
        this.hero = (Hero) e;
        ArrayList<Entity> entities = getEntitiesToHit();
    }

    private ArrayList<Entity> getEntitiesToHit() {
        Set<Entity> entities = Game.getEntities();
        ArrayList<Entity> ents = new ArrayList<>();
        ArrayList<Entity> entsToHit = new ArrayList<>();
        for (Entity ente : entities) {
            if (ente.getFaction() == Faction.FOE)
                ents.add(ente);
        }
        PlayerSystem lastKeyStroke = new PlayerSystem();
        // Hit nach links
        if (lastKeyStroke.getKey() == 0) {
             entsToHit = hitInDirection(0,ents);
            // Hit nach oben
        } else if (lastKeyStroke.getKey() == 1) {
            entsToHit = hitInDirection(1,ents);
            // Hit nach rechts
        } else if (lastKeyStroke.getKey() == 2) {
            entsToHit = hitInDirection(2,ents);
            // Hit nach unten
        } else if (lastKeyStroke.getKey() == 3) {
            entsToHit = hitInDirection(3,ents);
        }
        return entsToHit;
    }
    private ArrayList<Entity> hitInDirection(int key, ArrayList<Entity> entities) {
        Hero hero = (Hero) Game.getHero().get();
        PositionComponent pos = (PositionComponent) hero.getComponent(PositionComponent.class).get();
        Point heroPoint = pos.getPosition();
        ArrayList<Entity> entsToHit = new ArrayList<>();
        for ( Entity ente : entities) {
            PositionComponent posOfEnte = (PositionComponent) ente.getComponent(PositionComponent.class).get();
            Point pointOfEnte = posOfEnte.getPosition();
            boolean checkRight = heroPoint.toCoordinate().x+this.hitRange <= pointOfEnte.toCoordinate().x;
            boolean checkLeft = heroPoint.toCoordinate().x-this.hitRange <= pointOfEnte.toCoordinate().x;
            boolean checkUpper = heroPoint.toCoordinate().y-this.hitRange <= pointOfEnte.toCoordinate().y;
            boolean checkDown = heroPoint.toCoordinate().y+this.hitRange <= pointOfEnte.toCoordinate().y;
            boolean checkUpperRight = (checkRight && checkUpper);
            boolean checkUpperLeft = (checkLeft && checkUpper);
            boolean checkDownRight = (checkRight && checkDown);
            boolean checkDownLeft = (checkLeft && checkDown);
            System.out.println(checkLeft + " : " + checkUpperLeft +  " : " + checkDownLeft + " test in Swordonuse Z 73"
            + checkUpper + " : " + checkDown);
             if ( key == 0 && ( checkLeft || checkUpperLeft || checkDownLeft)) {
                 entsToHit.add(ente);
             }
              else if ( key == 1 && ( checkUpper || checkUpperLeft || checkUpperRight )) {
                 entsToHit.add(ente);
             }
              else if ( key == 2 && ( checkRight || checkUpperRight || checkDownRight)) {
                 entsToHit.add(ente);
             }
              else if ( key == 3 && ( checkDown || checkDownLeft || checkDownRight)) {
                 entsToHit.add(ente);
             }
        }
        return entsToHit;
    }
}
