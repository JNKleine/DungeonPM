package ecs.entities;

import ecs.components.Component;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

import ecs.components.HitboxComponent;
import semanticAnalysis.types.DSLContextPush;
import semanticAnalysis.types.DSLType;
import starter.Game;

/** Entity is a unique identifier for an object in the game world */
@DSLType(name = "game_object")
@DSLContextPush(name = "entity")
public class Entity {
    private static int nextId = 0;

    public final int id = nextId++;
    private Faction faction = Faction.NEUTRAL;
    private final int initDamage;
    private HashMap<Class, Component> components;
    private final Logger entityLogger;

    /**
     * Initialize an object of type entity
     */
    public Entity() {
        this.initDamage = 0;
        components = new HashMap<>();
        Game.addEntity(this);
        entityLogger = Logger.getLogger(this.getClass().getName());
        entityLogger.info("The entity '" + this.getClass().getSimpleName() + "' was created.");
    }

    /**
     * Initialize an object of type entity
     *
     * <p>Create an object with defined damage and faction</p>
     *
     * @param initDamage int amount of damage, this entity make
     * @param faction faction from Faction
     */
    public Entity(int initDamage,Faction faction) {
        this.faction = faction;
        this.initDamage = initDamage;
        components = new HashMap<>();
        Game.addEntity(this);
        entityLogger = Logger.getLogger(this.getClass().getName());
        entityLogger.info("The entity '" + this.getClass().getSimpleName() + "' was created.");
    }

    /**
     * Add a new component to this entity
     *
     * @param component The component
     */
    public void addComponent(Component component) {
        components.put(component.getClass(), component);
    }

    /**
     * Remove a component from this entity
     *
     * @param klass Class of the component
     */
    public void removeComponent(Class klass) {
        components.remove(klass);
    }

    /**
     * Get the component
     *
     * @param klass Class of the component
     * @return Optional that can contain the requested component
     */
    public Optional<Component> getComponent(Class klass) {
        return Optional.ofNullable(components.get(klass));
    }
    public int getDamage() {
        return initDamage;
    }

    public Faction getFaction() {return faction;}

    /**
     *Regulates what this specific entity should do when it collides with another entity
     *
     * @param hb HitboxComponent from the other entity
     * */
    public void onHit(HitboxComponent hb) {};
}
