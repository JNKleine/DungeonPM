package ecs.components;

import ecs.entities.Entity;
import ecs.systems.DialogueSystem;
import starter.Game;

/**
 * Determines what should happen when the player
 * interacts with something that owns InteractionComponent
 * **/
public class OpenDialogueOnInteraction implements IInteraction{

    private final Entity e;

    private final boolean textInputIsOn;

    private String initText;

    /**
     * Construct a new OpenDialogueOnInteraction object
     * @param entityWithComponent The entity, that uses this OnInteractionBehavior
     * @param initText The initial text, that the dialogHUD shows
     * @param textInputIsOn Determines whether the dialog window redisplays the input text and
     *                  the associated button after the player has already used the input text.
     * **/
    public OpenDialogueOnInteraction(Entity entityWithComponent,String initText,boolean textInputIsOn) {
    this.e = entityWithComponent;
    this.initText = initText;
    this.textInputIsOn = textInputIsOn;
    }

    /**
     * @param newInitText change the initial text in the dialogHUD
     * **/
    public void setInitText(String newInitText) {
        this.initText = newInitText;
    }

    @Override
    public void onInteraction(Entity entity) {
        DialogueSystem.setEntityThatUseInteractionComponent(this.e);
        DialogueSystem.callDialogueHUD(initText,textInputIsOn);
        }
    }

