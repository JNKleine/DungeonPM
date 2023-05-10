package ecs.components;

import ecs.entities.Entity;
import ecs.systems.DialogueSystem;
import starter.Game;

/**
 * Determines what should happen when the player
 * interacts with something that owns InteractionComponent
 * **/
public class OpenDialogueOnInteraction implements IInteraction{
    @Override
    public void onInteraction(Entity entity) {
        DialogueSystem.callDialogueHUD("How can I help you?");
        }
    }

