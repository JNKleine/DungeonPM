
package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.damage.Damage;
import ecs.damage.DamageType;
import ecs.items.item.Coin;
import graphic.Animation;

import java.util.Random;

/**
 * Monster represents the superclass of all monsters. Every monster shall inherit from this class
 *
 * <p>
 * The monster class defines which characteristics
 * a monster must have in any case.</p>
 * */
 public class Monster extends Entity {

    private final float SPAWN_PROBABILITY;
    private final int MAXIMUM_MONSTER_IN_LEVEL;

    /**String to Folder for idle right animation*/
    protected String pathToIdleRight;
     /**String to Folder for idle left animation*/
    protected String pathToIdleLeft;
     /**String to Folder for run right animation*/
    protected String pathToRunRight;
     /**String to Folder for run left animation*/
    protected String pathToRunLeft;
     /**float with xSpeed per Frame*/
    protected float xSpeed;
     /**float with ySpeed per Frame*/
    protected float ySpeed;
     /**int with the initial amount of hitpoints*/
    protected int initHitpoints;
    /**int with the initial amount of damage, this monster make */
    protected int initDamage;


    /**
     * Constructor for any given Monster
     * @param xSpeed: float Value for speed per frame in x-direction
     * @param ySpeed: float value for speed per frame in y-direction
     * @param initDamage: initial int value for damage per hit
     * @param initHitpoints: initial int value for Livepoints
     * @param spawnProbability: float probability of a monster to spawn
     * @param maximumMonsterInLevel: int maximum of possible monster of this kind
     * @param pathToIdleLeft: String path idle left animation
     * @param pathToIdleRight: String path idle right animation
     * @param pathToRunLeft: String path run left animation
     * @param pathToRunRight: String path run left animation
     *
     **/
    public Monster(float xSpeed, float ySpeed, int initHitpoints,
                   int initDamage, float spawnProbability, int maximumMonsterInLevel,
                   String pathToIdleRight, String pathToIdleLeft, String pathToRunRight, String pathToRunLeft,Faction faction) {
        super(initDamage,faction);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.initHitpoints = initHitpoints;
        this.initDamage = initDamage;
        this.SPAWN_PROBABILITY = spawnProbability;
        this.MAXIMUM_MONSTER_IN_LEVEL = maximumMonsterInLevel;
        this.pathToIdleRight = pathToIdleRight;
        this.pathToIdleLeft = pathToIdleLeft;
        this.pathToRunRight = pathToRunRight;
        this.pathToRunLeft = pathToRunLeft;
        addPositionComponent();
        addAnimationComponent();
        addVelocityComponent();
        addInventoryComponent();
    }
    // add InventoryComponent
    private void addInventoryComponent(){
        InventoryComponent inv = new InventoryComponent(this, 1);
        addCoin();
    }

    //add PositionComponent
    private void addPositionComponent() {
        new PositionComponent(this);
    }

    //add AnimationComponent
    private void addAnimationComponent() {
        Animation idleRight = AnimationBuilder.buildAnimation(this.pathToIdleRight);
        Animation idleLeft = AnimationBuilder.buildAnimation(this.pathToIdleLeft);
        new AnimationComponent(this,idleRight,idleLeft);
    }
    //add VelocityComponent
    private void addVelocityComponent() {
        Animation moveRight = AnimationBuilder.buildAnimation(this.pathToRunRight);
        Animation moveLeft = AnimationBuilder.buildAnimation(this.pathToRunLeft);
        new VelocityComponent(this, xSpeed,ySpeed,moveLeft,moveRight);
    }

    // Adds a Coin to the inventory of a monster
    private void addCoin() {
        java.util.Random rdm = new Random();
        Coin coin = null;
        int rdmNm = rdm.nextInt(11);
        if ( rdmNm < 6) {
            coin = new Coin(1, "items/coins/coinI", "items/coins/coinI");
        } else if (rdmNm >= 6 && rdmNm <= 9 ) {
            coin = new Coin(5,"items/coins/coinV","items/coins/coinV");
        } else {
            coin = new Coin(10,"items/coins/coinX","items/coins/coinX");
        }
        InventoryComponent inv = (InventoryComponent) this.getComponent(InventoryComponent.class).get();
        inv.addItem(coin.getItemData());
    }

    /**
     * Gives the spawn probability of the monster
     *
     * @return float value of the spawn probability
     * */
    public float getSpawnProbability() {
        return SPAWN_PROBABILITY;
    }

     /**
      * Gives the maximum possible amount of monsters
      *
      * @return int value of maximum possible amount of monsters
      * */
    public int getMaximumOfMonster() {
        return MAXIMUM_MONSTER_IN_LEVEL;
    }

    /**
     * Overrides Entity's onHit() method
     *
     * @param hb HitboxComponent from other entity
     */
    @Override
    public void onHit(HitboxComponent hb) {
        Entity e = hb.getEntity();
        if (e.getFaction().equals(Faction.PLAYER)) {
        HealthComponent hc = (HealthComponent)e.getComponent(HealthComponent.class).get();
            hc.receiveHit(new Damage(getDamage(),
                DamageType.PHYSICAL,this));
        }
    }





}
