
package ecs.entities;

import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.FollowWalk;
import ecs.components.ai.idle.RadiusWalk;
import ecs.components.ai.transition.RangeTransition;
import starter.Game;

import java.util.Random;
import java.util.regex.Pattern;

/**
 * The Ghost is a NPC (non player character).
 * He sometimes accompanies the hero,
 * sometimes just walks around, and sometimes disappears.
 * It's an entity in the ECS.
 * This class helps to set up the ghost
 * with all its components and attributes .
 */
public class Ghost extends Monster {

    public static String name;

    /**
     * Creates an object from type ghost
     *
     * <p>Within this constructor, all defined and required values
     * are passed to the Monster superclass.
     * In addition, Ghost receives specific components here that were not
     * generally received in monsters.</p>
     */
    public Ghost() {
        super(0.1f, 0.1f, 0, 0, 0.9f, 1, "npc/ghost/idleRight",
            "npc/ghost/idleLeft", "npc/ghost/runRight", "npc/ghost/runLeft", Faction.NEUTRAL);
        addHitBox();
        addInteractionComponent();
        Random random = new Random();
        if (random.nextInt(0, 2) == 0 || Game.currentLevelNumber == 10) {
            addAIComponent();
        } else {
            alternativeAIComponent();
        }
        this.name = getRandomName();
    }

    private void addInteractionComponent() {
        new InteractionComponent(this, 1f, true, new OpenDialogueOnInteraction(this,"How can I help you?"));
    }

    //add HitBoxComponent
    private void addHitBox() {
        new HitboxComponent(
            this,
            (you, other, direction) -> System.out.println("GhostCollisionEnter"),
            (you, other, direction) -> System.out.println("GhostCollisionLeave"));
    }

    //add AIComponent
    private void addAIComponent() {
        CollideAI fightAI = new CollideAI(3f);
        FollowWalk idleAI = new FollowWalk();
        RangeTransition transitionAI = new RangeTransition(1f);

        new AIComponent(this, fightAI, idleAI, transitionAI);

    }

    //add alternative AIComponent
    private void alternativeAIComponent() {
        CollideAI fightAI = new CollideAI(3f);
        RadiusWalk idleAI = new RadiusWalk(10, 2);
        RangeTransition transitionAI = new RangeTransition(1f);

        new AIComponent(this, fightAI, idleAI, transitionAI);
    }

    /**
     * Determine what should happen when this entity collides with another
     *
     * @param hb HitBoxComponent from other entity
     */
    @Override
    public void onHit(HitboxComponent hb) {
    }

    private String getRandomName() {
        String[] randomNames = {"Gilbert, Hans, Peter, Joe, Jones, Lena, Paul, Erik, Francis"};
        Random rdm = new Random();
        return randomNames[rdm.nextInt(randomNames.length)];
    }

    @Override
    public String getAnswer(String text) {
        if(text.toLowerCase().contains("your") && text.toLowerCase().contains("name")) {
            return "My Name is"+name;

        }
        else if(text.toLowerCase().contains("How") &&text.toLowerCase().contains("deep")) {
            return "We are at Dungeon level "+Game.currentLevel+"!";
        }
        else if(text.matches("[0-9]+")) {
            return "I don't know, what to do with all this numbers, sorry!";
        }
        else if(text.matches("[A-Za-z]+")) {
            return "I don't understand these words, I last spoke hundreds of years ago";
        }
        else {
            return "I cant understand you!";
        }
    }
}



