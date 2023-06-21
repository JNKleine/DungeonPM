package ecs.entities;

/**
 * Use an enum to define which faction an entity is in.
 *
 * <p>Specific fractions are used to determine the ratio of entities, for example when two entities'
 * hitbox collides
 */
public enum Faction {
    /** FOE is for enemies NPC */
    FOE,

    /**
     * NEUTRAL is for any passive NPC NEUTRAL is for any passive NPC that shouldn't take any damage.
     */
    NEUTRAL,

    /** PLAYER is for any playable entity */
    PLAYER,

    /** BOSSMONSTER is only in use for a BOSSMONSTER */
    BOSSMONSTER,

    /** TRAP is for any trap */
    TRAP
}
