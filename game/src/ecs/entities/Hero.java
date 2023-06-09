package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.components.AnimationComponent;
import ecs.components.PositionComponent;
import ecs.components.VelocityComponent;
import ecs.components.skill.*;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.items.item.Key;
import ecs.items.item.PotionOfTrapDestroying;
import ecs.items.item.Sword;
import graphic.Animation;
import starter.Game;

/**
 * The Hero is the player character. It's entity in the ECS. This class helps to setup the hero with
 * all its components and attributes .
 */
public class Hero extends Entity {

    private final int fireballCoolDown = 5;
    private final float xSpeed = 0.3f;
    private final float ySpeed = 0.3f;

    private final String pathToIdleLeft = "knight/idleLeft";
    private final String pathToIdleRight = "knight/idleRight";
    private final String pathToRunLeft = "knight/runLeft";
    private final String pathToRunRight = "knight/runRight";
    private final String pathToGetDamage = "knight/onHitAnimation";
    private final String pathToDieAnim = "knight/onDieAnimation";
    private Skill firstSkill;

    private int money;

    /** Entity with Components */
    public Hero() {
        super(0, Faction.PLAYER);
        new PositionComponent(this);
        setupVelocityComponent();
        setupAnimationComponent();
        setupHitboxComponent();
        PlayableComponent pc = new PlayableComponent(this);
        setupHealthComponent();
        setupInventoryComponent();
        setUpQuestLogComponent();
        pc.setSkillSlot1(firstSkill);
    }

    private void setupVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(pathToRunLeft);
        new VelocityComponent(this, xSpeed, ySpeed, moveLeft, moveRight);
    }

    private void setupAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(pathToIdleLeft);
        new AnimationComponent(this, idleLeft, idleRight);
    }

    private void setupFireballSkill() {
        firstSkill =
                new Skill(
                        new FireballSkill(SkillTools::getCursorPositionAsPoint), fireballCoolDown);
    }

    private void setupHitboxComponent() {
        new HitboxComponent(
                this,
                (you, other, direction) -> System.out.println("heroCollisionEnter"),
                (you, other, direction) -> System.out.println("heroCollisionLeave"));
    }

    private void setupHealthComponent() {
        Animation getHitAnimation = AnimationBuilder.buildAnimation(pathToGetDamage);
        Animation getDieAnimation = AnimationBuilder.buildAnimationNotRepeatable(pathToDieAnim);
        HealthComponent hc =
                new HealthComponent(
                        this, 100, Game::removeEntity, getHitAnimation, getDieAnimation);
    }

    /** Set the inventory from the hero to the start values * */
    public void setupInventoryComponent() {
        InventoryComponent ic = new InventoryComponent(this, 4);
        ic.addItem(new Sword().getItemData());
        ic.addItem(new PotionOfTrapDestroying().getItemData());
        ic.addItem(new Key().getItemData());
        ic.setCurMainItem(ic.getItems().get(0));
    }

    /** Set the hero's QuestLogComponent to the size of 10 quests */
    public void setUpQuestLogComponent() {
        QuestLogComponent ql = new QuestLogComponent(this, 10);
    }

    /**
     * Determines what happens to the Hero, if an Entity collides with the Hero
     *
     * @param hb: Hitbox from the other Entity *
     */
    public void onHit(HitboxComponent hb) {
        Entity e = hb.getEntity();
        if (e.getFaction().equals(Faction.FOE)) {
            HealthComponent hc = (HealthComponent) e.getComponent(HealthComponent.class).get();
            hc.receiveHit(new Damage(getDamage(), DamageType.PHYSICAL, this));
        }
    }
    /** Increase current Money from Hero* */
    public void increaseMoney(int value) {
        this.money += value;
    }

    /** Decrease current Money from Hero* */
    public void decreaseMoney(int value) {
        this.money -= value;
    }

    /** Get current amount of Money* */
    public int getMoney() {
        return this.money;
    }
}
