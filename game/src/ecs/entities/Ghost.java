
package ecs.entities;

import ecs.Quests.Quest;
import ecs.Quests.QuestBuilder;
import ecs.components.*;
import ecs.components.ai.AIComponent;
import ecs.components.ai.fight.CollideAI;
import ecs.components.ai.idle.FollowWalk;
import ecs.components.ai.idle.RadiusWalk;
import ecs.components.ai.transition.RangeTransition;
import starter.Game;

import java.util.Random;

/**
 * The Ghost is a NPC (non player character).
 * He sometimes accompanies the hero,
 * sometimes just walks around, and sometimes disappears.
 * It's an entity in the ECS.
 * This class helps to set up the ghost
 * with all its components and attributes .
 */
public class Ghost extends Monster {

    // Generates a random name for the ghost
    private static String name = getRandomName();

    private Quest q = QuestBuilder.getRandomSideQuest();
    private boolean questIsSuggested = false;
    /** boolean if the ghost's suggested quest is accepted (by the player) */
    public static boolean questIsAccepted = false;
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
    }

    // add InteractionComponent
    private void addInteractionComponent() {
        new InteractionComponent(this, 1f, true, new OpenDialogueOnInteraction(
            this,"Hello there",true));
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

    // generate a random name for the ghost
    private static String getRandomName() {
        String[] randomNames = fillNames();
        Random rdm = new Random();
        //System.out.println(randomNames[rdm.nextInt(randomNames.length)]);
        return randomNames[rdm.nextInt(randomNames.length)];
    }

    /** Setter for changing the name of the ghost */
    public static void setName() {
        name = getRandomName();
    }

    /**
     * Getter for the name of this entity
     *
     * @return name of the ghost
     */
    public static String getName() { return name; }

    @Override
    public String getAnswer(String text) {
        entityLogger.info("Hero requests a response from "+this.getClass().getSimpleName()+" Input: "+
            text);
        if (text.toLowerCase().contains("your") && text.toLowerCase().contains("name")) {
            return "My Name is " + name;

        } else if (text.toLowerCase().contains("how") && text.toLowerCase().contains("deep")) {
            return "We are at Dungeon level " + Game.currentLevelNumber + "!";
        } else if(text.toLowerCase().contains("help") && text.toLowerCase().contains("you")) {
            return "How you can help me? It would be awfully\nkind if you could take me to my grave!";
        } else if(text.toLowerCase().contains("quest") && !questIsAccepted) {
            questIsSuggested = true;
            return q.getQuestDescription()+"\nDo you accept this quest?";
        } else if(text.toLowerCase().contains("quest"))  {
            return "I gave you a quest already!";
        }
        else if(text.toLowerCase().contains("yes") && questIsSuggested) {
            QuestLogComponent qLC = (QuestLogComponent) Game.getHero().get().getComponent(QuestLogComponent.class).get();
            if(!qLC.questIsInLog(q)) {
                qLC.addQuestToLog(q);
                questIsAccepted = true;
            }
            return "Ok, great! The Quest is now in the QuestLog";
        }
        else if (text.matches("[0-9]+")) {
            return "I don't know, what to do with\n all this numbers, sorry!";
        } else if (text.matches("[A-Za-z ]+")) {
            return "I don't understand these words,\nI last spoke hundreds of years ago";
        } else {
            return "I cant understand you!";
        }
    }

    private static String[]  fillNames(){
        String[] randomNames = new String[10];
        randomNames[0] = "Gilbert";
        randomNames[1] = "Hans";
        randomNames[2] = "Peter";
        randomNames[3] = "Joe";
        randomNames[4] = "David";
        randomNames[5] = "Paul";
        randomNames[6] = "Erik";
        randomNames[7] = "Franz";
        randomNames[8] = "Max";
        randomNames[9] = "Achim";
        return randomNames;
    }
}



