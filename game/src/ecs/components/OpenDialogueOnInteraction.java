package ecs.components;

import ecs.entities.Entity;
import ecs.systems.DialogueSystem;

/**
 * Determines what should happen when the player
 * interacts with something that owns InteractionComponent
 * **/
public class OpenDialogueOnInteraction implements IInteraction{

    private Entity e;
    private String initText;

    public OpenDialogueOnInteraction(Entity entityWithComponent,String initText) {
    this.e = entityWithComponent;
    this.initText = initText;
    }

    @Override
    public void onInteraction(Entity entity) {
        DialogueSystem.e = this.e;
        DialogueSystem.callDialogueHUD(initText);
        }
    }

