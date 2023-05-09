package ecs.components;

import ecs.entities.Entity;
import starter.Game;

/**
 * Determines what should happen when the player
 * interacts with something that owns InteractionComponent
 * **/
public class OpenDialogueOnInteraction implements IInteraction{
    @Override
    public void onInteraction(Entity entity) {
        Game.callDialogue();
        }
    }

