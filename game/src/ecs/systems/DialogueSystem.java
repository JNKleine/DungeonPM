package ecs.systems;
import graphic.hud.DialogueMenu;
import starter.Game;

public class DialogueSystem extends ECS_System {
    static String answerFromEntity = "";
    static String currentTextFromPlayer = "";
    @Override
    public void update() {
        if(Game.dialogueIsOn) {
            if (Game.dialogueMenu.getUserText() != null && DialogueMenu.sayIsClicked) {
                DialogueMenu.sayIsClicked = false;
                currentTextFromPlayer = Game.dialogueMenu.getUserText();
                System.out.println(currentTextFromPlayer);
                //callAnswerFromEntity();
            }
        }
    }

    public DialogueSystem() {
    }


    public static void callDialogueHUD(String answerFromEntity) {
        Game.callDialogue(answerFromEntity);
    }
}
