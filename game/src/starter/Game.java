package starter;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static logging.LoggerConfig.initBaseLogger;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import configuration.Configuration;
import configuration.KeyboardConfig;
import controller.AbstractController;
import controller.SystemController;
import ecs.components.HealthComponent;
import ecs.components.InventoryComponent;
import ecs.components.MissingComponentException;
import ecs.components.PositionComponent;
import ecs.entities.*;
import ecs.items.ItemData;
import ecs.items.ItemDataGenerator;

import ecs.items.ItemType;
import ecs.items.WorldItemBuilder;
import ecs.systems.*;
import graphic.DungeonCamera;
import graphic.Painter;
import graphic.hud.*;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import level.IOnLevelLoader;
import level.LevelAPI;
import level.elements.ILevel;
import level.elements.tile.Tile;
import level.generator.IGenerator;
import level.generator.postGeneration.WallGenerator;
import level.generator.randomwalk.RandomWalkGenerator;
import level.monstergenerator.EntitySpawnRateSetter;
import level.tools.LevelSize;
import tools.Constants;
import tools.Point;

/**
 * The heart of the framework. From here all strings are pulled.
 */
public class Game extends ScreenAdapter implements IOnLevelLoader {

    private final LevelSize LEVELSIZE = LevelSize.SMALL;

    /**
     * The batch is necessary to draw ALL the stuff. Every object that uses draw need to know the
     * batch.
     */
    protected SpriteBatch batch;

    /**
     * Contains all Controller of the Dungeon
     */
    public static List<AbstractController<?>> controller;

    public static DungeonCamera camera;
    /**
     * Draws objects
     */
    protected Painter painter;


    protected LevelAPI levelAPI;
    /**
     * Generates the level
     */
    protected IGenerator generator;
    public static InputMultiplexer inputMultiplexer = new InputMultiplexer();


    private boolean doSetup = true;
    public static boolean paused = false;

    public static boolean dialogueIsOn = false;

    public static boolean isPaused = false;

    public static boolean inventoryIsOn = false;

    /**
     * All entities that are currently active in the dungeon
     */
    private static final Set<Entity> entities = new HashSet<>();
    /**
     * All entities to be removed from the dungeon in the next frame
     */
    private static final Set<Entity> entitiesToRemove = new HashSet<>();
    /**
     * All entities to be added from the dungeon in the next frame
     */
    private static final Set<Entity> entitiesToAdd = new HashSet<>();

    /**
     * List of all Systems in the ECS
     */
    public static SystemController systems;

    public static ILevel currentLevel;
    /**
     * Get the value, in which level the hero is
     */
    public static int currentLevelNumber = 0;
    private static PauseMenu<Actor> pauseMenu;

    private static InventoryMenu<Actor> inventory;

    public static DialogueMenu<Actor> dialogueMenu;

    public static GameOverHUD<Actor> gameOverHUD;

    public static PlayerHUD<Actor> playerHUD;
    private static Entity hero;

    private Logger gameLogger;

    public static void main(String[] args) {
        // start the game
        try {
            Configuration.loadAndGetConfiguration("dungeon_config.json", KeyboardConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DesktopLauncher.run(new Game());
    }

    /**
     * Main game loop. Redraws the dungeon and calls the own implementation (beginFrame, endFrame
     * and onLevelLoad).
     *
     * @param delta Time since last loop.
     */
    @Override
    public void render(float delta) {
        if (doSetup) setup();
        batch.setProjectionMatrix(camera.combined);
        frame();
        clearScreen();
        levelAPI.update();
        controller.forEach(AbstractController::update);
        camera.update();
    }

    /**
     * Called once at the beginning of the game.
     */
    protected void setup() {
        doSetup = false;
        controller = new ArrayList<>();
        setupCameras();
        painter = new Painter(batch, camera);
        generator = new RandomWalkGenerator();
        levelAPI = new LevelAPI(batch, painter, generator, this);
        initBaseLogger();
        gameLogger = Logger.getLogger(this.getClass().getName());
        systems = new SystemController();
        controller.add(systems);
        gameOverHUD = new GameOverHUD<>();
        pauseMenu = new PauseMenu<>();
        dialogueMenu = new DialogueMenu<>();
        inventory = new InventoryMenu<>();
        playerHUD = new PlayerHUD<>();
        controller.add(pauseMenu);
        controller.add(inventory);
        controller.add(dialogueMenu);
        controller.add(playerHUD);
        controller.add(gameOverHUD);
        hero = new Hero();
        levelAPI = new LevelAPI(batch, painter, new WallGenerator(new RandomWalkGenerator()), this);
        levelAPI.loadLevel(LEVELSIZE);
        Gdx.input.setInputProcessor(inputMultiplexer);
        createSystems();
    }

    /**
     * Called at the beginning of each frame. Before the controllers call <code>update</code>.
     */
    protected void frame() {
        setCameraFocus();
        manageEntitiesSets();
        getHero().ifPresent(this::loadNextLevelIfEntityIsOnEndTile);
        if (Gdx.input.isKeyJustPressed(Input.Keys.P) && !inventoryIsOn && !dialogueIsOn) togglePause();
        if (Gdx.input.isKeyJustPressed(Input.Keys.I) && !isPaused && !dialogueIsOn) callInventory();
    }

    @Override
    public void onLevelLoad() {
        currentLevelNumber++;
        currentLevel = levelAPI.getCurrentLevel();
        EntitySpawnRateSetter entitySpawner = new EntitySpawnRateSetter();
        entities.clear();
        entitiesToAdd.clear();
        addItem();
        addEntityList(entitySpawner.getListOfMonsterToSpawnVariableProbability());
        if (currentLevelNumber <= 10) {
            addEntityList(entitySpawner.spawnGraveAndGhost(10));
        }
        getHero().ifPresent(this::placeOnLevelStart);
    }

    private void manageEntitiesSets() {
        entities.removeAll(entitiesToRemove);
        entities.addAll(entitiesToAdd);
        for (Entity entity : entitiesToRemove) {
            gameLogger.info("Entity '" + entity.getClass().getSimpleName() + "' was deleted.");
        }
        for (Entity entity : entitiesToAdd) {
            gameLogger.info("Entity '" + entity.getClass().getSimpleName() + "' was added.");
        }
        entitiesToRemove.clear();
        entitiesToAdd.clear();
    }

    private void setCameraFocus() {
        if (getHero().isPresent()) {
            PositionComponent pc =
                (PositionComponent)
                    getHero()
                        .get()
                        .getComponent(PositionComponent.class)
                        .orElseThrow(
                            () ->
                                new MissingComponentException(
                                    "PositionComponent"));
            camera.setFocusPoint(pc.getPosition());

        } else camera.setFocusPoint(new Point(0, 0));
    }

    private void loadNextLevelIfEntityIsOnEndTile(Entity hero) {
        if (isOnEndTile(hero)) levelAPI.loadLevel(LEVELSIZE);
    }

    private boolean isOnEndTile(Entity entity) {
        PositionComponent pc =
            (PositionComponent)
                entity.getComponent(PositionComponent.class)
                    .orElseThrow(
                        () -> new MissingComponentException("PositionComponent"));
        Tile currentTile = currentLevel.getTileAt(pc.getPosition().toCoordinate());
        return currentTile.equals(currentLevel.getEndTile());
    }

    private void placeOnLevelStart(Entity hero) {
        entities.add(hero);
        PositionComponent pc =
            (PositionComponent)
                hero.getComponent(PositionComponent.class)
                    .orElseThrow(
                        () -> new MissingComponentException("PositionComponent"));
        pc.setPosition(currentLevel.getStartTile().getCoordinate().toPoint());
    }

    /**
     * Toggle between pause and run
     */
    public static void togglePause() {
        isPaused = !isPaused;
        paused = !paused;
        if (systems != null) {
            systems.forEach(ECS_System::toggleRun);
        }
        if (pauseMenu != null) {
            if (paused) {
                pauseMenu.showMenu();
            } else {
                pauseMenu.hideMenu();
            }
        }

    }

    /**
     * Call the inventoryHUD and show it
     **/
    public static void callInventory() {
        inventoryIsOn = !inventoryIsOn;
        paused = !paused;
        if (systems != null) {
            systems.forEach(ECS_System::toggleRun);
        }
        if (inventory != null) {
            if (paused) {
                inventory.createInventory();
                inventory.showMenu();
            } else {
                inventory.removeInventory();
                inventory.hideMenu();

            }
        }
    }

    /**
     * Call the DialogueHUD and show it
     **/
    public static void callDialogue(String stringText, boolean inputTextAfterFirstShownIsOn) {

        if (dialogueMenu != null) {
            if (!dialogueIsOn) {
                dialogueMenu.createDialogueMenu(stringText, inputTextAfterFirstShownIsOn);
                dialogueMenu.showMenu();
            } else {
                dialogueMenu.removeDialogueMenu();
                dialogueMenu.hideMenu();
            }
            dialogueIsOn = !dialogueIsOn;

        }
    }


    private void addItem() {
        ItemDataGenerator ig = new ItemDataGenerator();
        ItemData id = ig.generateItemData();
        if (id != null) {
            addEntity(WorldItemBuilder.buildWorldItem(id));
        }
    }

    /**
     * Given entity will be added to the game in the next frame
     *
     * @param entity will be added to the game next frame
     */
    public static void addEntity(Entity entity) {
        if (entity != null) entitiesToAdd.add(entity);
    }

    /**
     * Given list will be added to the game
     *
     * @param entitiesList: List of entities, which will be added
     */
    public static void addEntityList(ArrayList<Entity> entitiesList) {
        for (Entity entity : entitiesList) {
            addEntity(entity);
        }
    }

    /**
     * Given entity will be removed from the game in the next frame
     *
     * @param entity will be removed from the game next frame
     */
    public static void removeEntity(Entity entity) {
        entitiesToRemove.add(entity);
    }

    /**
     * @return Set with all entities currently in game
     */
    public static Set<Entity> getEntities() {
        return entities;
    }

    /**
     * @return Set with all entities that will be added to the game next frame
     */
    public static Set<Entity> getEntitiesToAdd() {
        return entitiesToAdd;
    }

    /**
     * @return Set with all entities that will be removed from the game next frame
     */
    public static Set<Entity> getEntitiesToRemove() {
        return entitiesToRemove;
    }

    /**
     * @return the player character, can be null if not initialized
     */
    public static Optional<Entity> getHero() {
        return Optional.ofNullable(hero);
    }

    /**
     * set the reference of the playable character careful: old hero will not be removed from the
     * game
     *
     * @param hero new reference of hero
     */
    public static void setHero(Entity hero) {
        Game.hero = hero;
    }

    public void setSpriteBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT);
    }

    private void setupCameras() {
        camera = new DungeonCamera(null, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.zoom = Constants.DEFAULT_ZOOM_FACTOR;

        // See also:
        // https://stackoverflow.com/questions/52011592/libgdx-set-ortho-camera
    }

    private void createSystems() {
        new VelocitySystem();
        new DrawSystem(painter);
        new PlayerSystem();
        new AISystem();
        new CollisionSystem();
        new HealthSystem();
        new XPSystem();
        new SkillSystem();
        new ProjectileSystem();
        new DialogueSystem();
        new PlayerHUDSystem();
    }

    /**
     * Restarts the Game.
     * Called when clicking the restart-Button when the game is over (-> GameOverHUD).
     * Deletes Whole inventory of Hero, beside the Sword and gives the hero full live
     * Places Hero on another Tile in the same Level
     */
    public void restart() {
        currentLevelNumber = 1;
        new PlayerHUDSystem();
        Ghost.setName();
        HealthComponent hc = (HealthComponent) hero.getComponent(HealthComponent.class).get();
        hc.setCurrentHealthpoints(hc.getMaximalHealthpoints());
        gameOverHUD.hideMenu();
        InventoryComponent inv = (InventoryComponent) hero.getComponent(InventoryComponent.class).get();
        List<ItemData> items = inv.getItems();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemType().equals(ItemType.Backpack)) {
                inv.removeBackpack();
                inv.removeItem(items.get(i));
            }
            else if (!items.get(i).getItemName().equals("Sword"))
                inv.removeItem(items.get(i));
        }
        placeOnLevelStart(hero);
    }
}
