package ecs.systems;

import com.badlogic.gdx.Gdx;
import configuration.KeyboardConfig;
import ecs.components.*;
import ecs.entities.Entity;
import ecs.entities.Faction;
import ecs.entities.Hero;
import ecs.tools.interaction.InteractionTool;
import starter.Game;


/** Used to control the player */
public class PlayerSystem extends ECS_System {

    /**
     * Use for SwordOnUse, key = 0 is left, key = 1 is up, key = 2 is right, key = 3 is down.
     */
    private static int key;
    private record KSData(Entity e, PlayableComponent pc, VelocityComponent vc) {}

    @Override
    public void update() {
        Game.getEntities().stream()
                .flatMap(e -> e.getComponent(PlayableComponent.class).stream())
                .map(pc -> buildDataObject((PlayableComponent) pc))
                .forEach(this::checkKeystroke);
    }

    private void checkKeystroke(KSData ksd) {
        if (Gdx.input.isKeyPressed(KeyboardConfig.LOOK_UP.get())) {
            key = 1;
        }
        else if (Gdx.input.isKeyPressed(KeyboardConfig.LOOK_DOWN.get())) {
            key = 3;
        }
        else if (Gdx.input.isKeyPressed(KeyboardConfig.LOOK_RIGHT.get())) {
            key = 2;
        }
        else if (Gdx.input.isKeyPressed(KeyboardConfig.LOOK_LEFT.get())) {
            key = 0;
        }

        if (Gdx.input.isKeyPressed(KeyboardConfig.MOVEMENT_UP.get())) {
            ksd.vc.setCurrentYVelocity(1 * ksd.vc.getYVelocity());
        }
        else if (Gdx.input.isKeyPressed(KeyboardConfig.MOVEMENT_DOWN.get())) {
            ksd.vc.setCurrentYVelocity(-1 * ksd.vc.getYVelocity());
        }
        else if (Gdx.input.isKeyPressed(KeyboardConfig.MOVEMENT_RIGHT.get())) {
            ksd.vc.setCurrentXVelocity(1 * ksd.vc.getXVelocity());
        }
        else if (Gdx.input.isKeyPressed(KeyboardConfig.MOVEMENT_LEFT.get())) {
            ksd.vc.setCurrentXVelocity(-1 * ksd.vc.getXVelocity());
        }

        if (Gdx.input.isKeyJustPressed(KeyboardConfig.SPACE.get())) {
           Hero curHero = (Hero)Game.getHero().get();
                    InventoryComponent ic = (InventoryComponent)curHero.getComponent(InventoryComponent.class).get();
                    if(ic.getCurMainItem() != null)
                    ic.getCurMainItem().triggerUse(curHero);
        }
        if(Gdx.input.isKeyPressed(KeyboardConfig.DROP_ITEM.get())) {
            for(Entity e: Game.getEntities()) {
                if(e.getFaction().equals(Faction.PLAYER)) {
                    InventoryComponent ic = (InventoryComponent)e.getComponent(InventoryComponent.class).get();
                    if(ic.getCurMainItem() != null) {
                        PositionComponent pc = (PositionComponent) e.getComponent(PositionComponent.class).get();
                        ic.getCurMainItem().triggerDrop(e, pc.getPosition());
                    }
                }
            }
        }

        if (Gdx.input.isKeyPressed(KeyboardConfig.INTERACT_WORLD.get()))
            InteractionTool.interactWithClosestInteractable(ksd.e);

        // check skills
        else if (Gdx.input.isKeyPressed(KeyboardConfig.FIRST_SKILL.get()))
            ksd.pc.getSkillSlot1().ifPresent(skill -> skill.execute(ksd.e));
        else if (Gdx.input.isKeyPressed(KeyboardConfig.SECOND_SKILL.get()))
            ksd.pc.getSkillSlot2().ifPresent(skill -> skill.execute(ksd.e));
    }

    private KSData buildDataObject(PlayableComponent pc) {
        Entity e = pc.getEntity();

        VelocityComponent vc =
                (VelocityComponent)
                        e.getComponent(VelocityComponent.class)
                                .orElseThrow(PlayerSystem::missingVC);

        return new KSData(e, pc, vc);
    }

    private static MissingComponentException missingVC() {
        return new MissingComponentException("VelocityComponent");
    }

    public static int getKey() { return key;}
}
