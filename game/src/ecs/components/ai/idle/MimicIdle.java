package ecs.components.ai.idle;

import dslToGame.AnimationBuilder;
import ecs.components.AnimationComponent;
import ecs.components.HealthComponent;
import ecs.entities.Entity;
import ecs.entities.Mimic;
import graphic.Animation;

public class MimicIdle implements IIdleAI{

    int healthPoints = -1;
    boolean setLife = true;
    boolean chestIsActive = false;
    @Override
    public void idle(Entity entity) {
        HealthComponent hc = (HealthComponent)entity.getComponent(HealthComponent.class).get();
        if(setLife) {
            healthPoints = hc.getCurrentHealthpoints();
            setLife = false;
        }
        if(!chestIsActive && ((Mimic) entity).interacted) {
            chestIsActive = true;
            if(entity.getClass().getSimpleName().equals("Mimic")) {
                ((Mimic)entity).addAIComponent(1f,30f);
                Animation newIdle = AnimationBuilder.buildAnimation("character/monster/mimic/alternativeIdle");
                entity.addComponent(new AnimationComponent(entity,newIdle,newIdle));
            }
        } else {
            //Logger
            hc.setCurrentHealthpoints(hc.getMaximalHealthpoints());
        }
        healthPoints = hc.getCurrentHealthpoints();
    }
}
