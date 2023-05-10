package ecs.systems;
import ecs.components.InteractionComponent;
import ecs.entities.Entity;
import graphic.hud.DialogueMenu;
import starter.Game;

public class DialogueSystem extends ECS_System {

    public static Entity e = null;
    static String currentTextFromPlayer = "";
    @Override
    public void update() {
        if(Game.dialogueIsOn) {
            if (Game.dialogueMenu.getUserText() != null && DialogueMenu.sayIsClicked) {
                DialogueMenu.sayIsClicked = false;
                currentTextFromPlayer = Game.dialogueMenu.getUserText();
                System.out.println(callAnswerFromEntity(currentTextFromPlayer));
                Game.callDialogue("");
                Game.callDialogue(callAnswerFromEntity( currentTextFromPlayer));
            }
        }
    }
    public static void callDialogueHUD(String answerFromEntity) {
        Game.callDialogue(answerFromEntity);
    }

    public static String callAnswerFromEntity(String currentTextFromPlayer){
        return e.getAnswer(currentTextFromPlayer);
    }
}
