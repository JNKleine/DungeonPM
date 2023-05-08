package ecs.items.IOnUseBehavior;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;
import ecs.components.PositionComponent;
import ecs.entities.Entity;
import ecs.entities.Faction;
import ecs.entities.Hero;
import ecs.items.IOnUse;
import ecs.items.ItemData;
import ecs.systems.PlayerSystem;
import level.elements.tile.FloorTile;
import level.elements.tile.Tile;
import starter.Game;
import tools.Point;

import java.util.ArrayList;
import java.util.Set;


public class SwordOnUse implements IOnUse {

    private Hero hero;

    @Override
    public void onUse(Entity e, ItemData item) {
        this.hero = (Hero) e;
    }

    public ArrayList<Entity> getFloorTilesToHit() {
        Set<Entity> entities = Game.getEntities();
        ArrayList<Entity> ents = new ArrayList<>();
        for (Entity ente : entities) {
            if (ente.getFaction() == Faction.FOE)
                ents.add(ente);
        }
        Hero hero = (Hero) Game.getHero().get();
        PositionComponent pos = (PositionComponent) hero.getComponent(PositionComponent.class).get();
        Point heroPoint = pos.getPosition();
        PlayerSystem lastKeyStroke = new PlayerSystem();
        // Hit nach links
        if (lastKeyStroke.getKey() == 0) {
            System.out.println("hit left funzt");
            // Hit nach oben
        } else if (lastKeyStroke.getKey() == 1) {
            System.out.println("hit oben funzt");
            // Hit nach rechts
        } else if (lastKeyStroke.getKey() == 2) {
            System.out.println("hit rechts funzt");
            // Hit nach unten
        } else if (lastKeyStroke.getKey() == 3) {
            System.out.println("hit unten funzt");
        }
        return ents;
    }
}
