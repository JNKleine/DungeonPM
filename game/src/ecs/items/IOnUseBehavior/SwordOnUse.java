package ecs.items.IOnUseBehavior;

import ecs.components.AnimationComponent;
import ecs.components.PositionComponent;
import ecs.entities.Entity;
import ecs.entities.Faction;
import ecs.entities.Hero;
import ecs.items.IOnUse;
import ecs.items.ItemData;
import ecs.systems.PlayerSystem;
import graphic.Animation;
import level.elements.tile.FloorTile;
import level.tools.Coordinate;
import starter.Game;
import tools.Point;

import java.util.ArrayList;
import java.util.Set;


public class SwordOnUse implements IOnUse {

    @Override
    public void onUse(Entity e, ItemData item) {

    }

    public ArrayList<FloorTile> getFloorTilesToHit(){
        ArrayList<FloorTile> ft = (ArrayList<FloorTile>) Game.currentLevel.getFloorTiles();
        ArrayList<FloorTile> floorTilesToHit = new ArrayList<>();
        Set<Entity> entities = Game.getEntities();
        Hero hero = null;
        for ( Entity ente : entities){
            if ( ente.getFaction() == Faction.PLAYER){
                hero = (Hero) ente;
            }
        }
        PositionComponent pos = (PositionComponent) hero.getComponent(PositionComponent.class).get();
        Point heroPoint = pos.getPosition();
        PlayerSystem lastKeyStroke = new PlayerSystem();
        for ( FloorTile floor : ft) {
            if ( heroPoint.toCoordinate().x == floor.getCoordinate().x && heroPoint.toCoordinate().y == floor.getCoordinate().y) {
                    // Hit nach links
                    if ( lastKeyStroke.getKey() == 0 ) {

                        // Hit nach oben
                    } else if ( lastKeyStroke.getKey() == 1) {

                        // Hit nach rechts
                    } else if ( lastKeyStroke.getKey() == 2) {

                        // Hit nach unten
                    } else if ( lastKeyStroke.getKey() == 3) {

                    }
            }
        }
    return floorTilesToHit;
    }
}
