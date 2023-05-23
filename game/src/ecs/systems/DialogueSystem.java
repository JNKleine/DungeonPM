package ecs.systems;
import ecs.entities.Entity;
import graphic.hud.DialogueMenu;
import starter.Game;

public class DialogueSystem extends ECS_System {

    private static Entity e = null;

    private static boolean showInventory = false;
    private static Entity entityThatShowsInventory = null;

    private static boolean textInputAfterFirstShownIsOn;
    @Override
    public void update() {
        if(Game.dialogueIsOn) {
            if (Game.dialogueMenu.getUserText() != null && DialogueMenu.sayIsClicked) {
                DialogueMenu.sayIsClicked = false;
                String currentTextFromPlayer = Game.dialogueMenu.getUserText();
                System.out.println(callAnswerFromEntity(currentTextFromPlayer));
                Game.callDialogue("",true);
                Game.callDialogue(callAnswerFromEntity( currentTextFromPlayer),textInputAfterFirstShownIsOn);
                if(showInventory) Game.callInventory(entityThatShowsInventory);
            }
        }
    }

    /*
     *
     * @param currentTextFromPlayer The current input made by the player in the dialog
     * */
    private static String callAnswerFromEntity(String currentTextFromPlayer){
        return e.getAnswer(currentTextFromPlayer);
    }

    /**Set the entity that should use the DialogSystem at this moment
     * @param entity The current Entity that uses the DialogSystem**/
    public static void setEntityThatUseInteractionComponent(Entity entity) {
        e = entity;
    }

    /**
     * Call the DialogHUD
     * @param answerFromEntity The text, the entity to be interacted with.
     *                         This text is displayed in the dialog window
     * @param textInputAfterFirstIsOn Determines whether the dialog window redisplays the input text and
     *                                the associated button after the player has already used the input text.
     * **/
    public static void callDialogueHUD(String answerFromEntity,boolean textInputAfterFirstIsOn) {
        textInputAfterFirstShownIsOn = textInputAfterFirstIsOn;
        Game.callDialogue(answerFromEntity,true);
    }

    public static void callInventoryHUD(Entity e) {
        entityThatShowsInventory = e;
        showInventory = true;
    }

}
