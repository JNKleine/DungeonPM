package level.monstergenerator;

import ecs.entities.*;
import ecs.entities.Trap.ExplosionTrap;
import ecs.entities.Trap.SpikeTrap;
import ecs.entities.Trap.Trap;
import starter.Game;

import java.util.ArrayList;
import java.util.Random;
/**
 * Used to determine the spawn rate of known entities
 *
 * <p>By using the methods for getting a list of certain Entities to spawn,
 * a list of these Entities can be obtained,
 * which can then be used further.
 * These Entitites are monsters, traps, ghost and the grave, and the shop.</p>
 * */
public class EntitySpawnRateSetter {
    //List of all types of monster, that are allowed to spawn
    private final Monster[] monsters = new Monster[]{new SlimeGuard(), new PillowOfBadDreams(), new WallWalker()};

    private final Trap[] traps = new Trap[] {new ExplosionTrap(), new SpikeTrap()};
    private final Entity[] ghostAndGrave = new Entity[]{new Ghost(), new Gravestone()};

    private final Entity shop = new Shopkeeper();
    //Object from type Random
    private final Random random = new Random();


    /**
     * Returns a list of entities to spawn
     *
     * <p>calculates the amount of monsters that will be spawned based on the spawn probabilities,
     * the current Level
     * and the maximum number of every single monster type</p>
     *
     * @return Returns a ArrayList from Entities
     */
    public ArrayList<Entity> getListOfMonsterToSpawnVariableProbability() {
        ArrayList<Entity> toSpawn = new ArrayList<>();
        for (Monster m : monsters) {
            for (int i = 0; i < m.getMaximumOfMonster(); i++) {
                float curProb = random.nextFloat(0.01f, 1.0f) + Game.currentLevelNumber / 100f;
                if (curProb >= 1.0f - m.getSpawnProbability()) {
                    try {
                        Class klass = Class.forName(m.getClass().getName());
                        toSpawn.add((Monster) klass.newInstance());
                    } catch (ClassNotFoundException e) {
                        System.out.println("No such Class found" + m.getClass().getName());
                    } catch (InstantiationException e) {
                        System.out.println("Can not instantiate" + m.getClass().getName());
                    } catch (IllegalAccessException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        return toSpawn;
    }

    /**
     * Returns a list of entities from type trap to spawn
     *
     * <p>calculates the amount of traps that will be spawned based on the spawn probabilities
     * and the maximum number of every single trap type</p>
     *
     * @return Returns a ArrayList from Entities
     */
    public ArrayList<Entity> getListOfTrapsToSpawn() {
        ArrayList<Entity> toSpawn = new ArrayList<>();

        for (Trap t : traps) {
            for (int i = 0; i < t.getMaxTrapInLevel(); i++) {
                float curProb = random.nextFloat(0.01f, 1.0f) + Game.currentLevelNumber / 100f;
                if (curProb >= 1.0f - t.getSpawnProb()) {
                    try {
                        Class klass = Class.forName(t.getClass().getName());
                        toSpawn.add((Trap) klass.newInstance());
                    } catch (ClassNotFoundException e) {
                        System.out.println("No such Class found" + t.getClass().getName());
                    } catch (InstantiationException e) {
                        System.out.println("Can not instantiate" + t.getClass().getName());
                    } catch (IllegalAccessException e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
        }
        return toSpawn;
    }


    /**
     *
     * 100% spawn the gravestone and ghost at the specified level.
     * Otherwise, a ghost will appear with the given probability.
     *
     * @param levelOnSpawn Level in which the entity spawn
     * @return ArrayList, type Entity
     */
    public ArrayList<Entity> spawnGraveAndGhost(int levelOnSpawn) {
        ArrayList<Entity> toSpawn = new ArrayList<>();
        if (Game.currentLevelNumber == levelOnSpawn) {
            for (Entity et : ghostAndGrave) {
                try {
                    Class klass = Class.forName(et.getClass().getName());
                    toSpawn.add((Entity) klass.newInstance());
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

        } else {
            float curProb = random.nextFloat(0.01f, 1.0f);
            Monster m = (Monster) ghostAndGrave[0];
            if (curProb >= 1.0f - m.getSpawnProbability()) {
                try {
                    Class klass = Class.forName(m.getClass().getName());
                    toSpawn.add((Monster) klass.newInstance());
                } catch (ClassNotFoundException e) {
                    System.out.println("No such Class found" + m.getClass().getName());
                } catch (InstantiationException e) {
                    System.out.println("Can not instantiate" + m.getClass().getName());
                } catch (IllegalAccessException e) {
                    System.out.println(e.getMessage());
                }

            }

        }
        return toSpawn;
    }

    /**
     * A shopkeeper spawns every x levels.
     * x is defined in the Shopkeeper class.
     */
    public Entity spawnShop() {
        Entity toSpawn = null;
        if(Game.currentLevelNumber%Shopkeeper.moduloForLevelSpawn == 0) {
            try {
                Class klass = Class.forName(shop.getClass().getName());
                toSpawn = (Entity) klass.newInstance();
            } catch (ClassNotFoundException e) {
                System.out.println("No such Class found" + shop.getClass().getName());
            } catch (InstantiationException e) {
                System.out.println("Can not instantiate" + shop.getClass().getName());
            } catch (IllegalAccessException e) {
                System.out.println(e.getMessage());
            }
        }
        return toSpawn;
    }
}

