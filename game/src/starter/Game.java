package starter;

import static com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT;
import static logging.LoggerConfig.initBaseLogger;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import configuration.Configuration;
import configuration.KeyboardConfig;
import controller.AbstractController;
import controller.ScreenController;
import controller.SystemController;
import ecs.components.*;
import ecs.entities.*;
import ecs.items.ItemData;
import ecs.items.ItemDataGenerator;

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
import level.elements.TileLevel;
import level.elements.tile.ExitTile;
import level.elements.tile.Tile;
import level.elements.tile.TileFactory;
import level.generator.IGenerator;
import level.generator.postGeneration.WallGenerator;
import level.generator.randomwalk.RandomWalkGenerator;
import level.monstergenerator.EntitySpawnRateSetter;
import level.tools.Coordinate;
import level.tools.DesignLabel;
import level.tools.LevelElement;
import level.tools.LevelSize;
import tools.Constants;
import tools.Point;

/**
 * The heart of the framework. From here all strings are pulled.
 */
public class Game extends ScreenAdapter implements IOnLevelLoader {

    /** boolean to check if the next ladder gets you to the boss*/
    public static boolean bossRoom = false;


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

    public static boolean questHUDIsOn = false;

    /** All entities that are currently active in the dungeon */
    private static ArrayList<ScreenController> listOfCurWindows = new ArrayList<>();

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

    public static InventoryMenu<Actor> inventory;

    public static DialogueMenu<Actor> dialogueMenu;

    public static GameOverHUD<Actor> gameOverHUD;

    public static PlayerHUD<Actor> playerHUD;

    public static QuestHUD<Actor> questHUD;
    private static Entity hero;

    private Logger gameLogger;

    public static Telepeter telepeter;

    public static ILevel bossLevel;


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
        questHUD = new QuestHUD<>();
        controller.add(pauseMenu);
        controller.add(inventory);
        controller.add(dialogueMenu);
        controller.add(playerHUD);
        controller.add(gameOverHUD);
        controller.add(questHUD);
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
        if (Gdx.input.isKeyJustPressed(Input.Keys.P) && !inventoryIsOn && !dialogueIsOn && !questHUDIsOn) togglePause();
        if(Gdx.input.isKeyJustPressed(Input.Keys.I) && !isPaused && !dialogueIsOn && !questHUDIsOn) callInventory(getHero().get());
        if(Gdx.input.isKeyJustPressed(Input.Keys.PERIOD) && !isPaused && !dialogueIsOn && !inventoryIsOn) callQuestHUD();
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) closeRecentWindow();
    }

    @Override
    public void onLevelLoad() {
        currentLevel = levelAPI.getCurrentLevel();
        if ( bossRoom == false) {
            currentLevelNumber++;
            EntitySpawnRateSetter entitySpawner = new EntitySpawnRateSetter();
            entities.clear();
            entitiesToAdd.clear();
            addItem();
            addEntityList(entitySpawner.getListOfMonsterToSpawnVariableProbability());
            addEntityList(entitySpawner.getListOfTrapsToSpawn());
            addEntity(entitySpawner.spawnShop());
            if (currentLevelNumber <= 10) {
                addEntityList(entitySpawner.spawnGraveAndGhost(10));
            }
        } else {
            entities.clear();
            entitiesToAdd.clear();
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
        if ( telepeter != null && bossRoom == true) {
            HealthComponent hC = (HealthComponent) telepeter.getComponent(HealthComponent.class).get();
            if ( hC.getCurrentHealthpoints() <= 0) {
                addExitToBossLevel();
                bossRoom = false;
            }
        } else {
            if (isOnEndTile(hero) && bossRoom == false) levelAPI.loadLevel(LEVELSIZE);
            else if (isOnEndTile(hero) && bossRoom == true) startBossMonsterLevel();
        }
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
                    listOfCurWindows.add(pauseMenu);
                } else {
                    pauseMenu.hideMenu();
                    listOfCurWindows.remove(pauseMenu);
                }
            }
        }

    /**Call the inventoryHUD and show it**/
    public static void callQuestHUD() {
        questHUDIsOn = !questHUDIsOn;

        if (questHUD != null) {
            if (questHUDIsOn) {

                questHUD.createQuestHUD();
                questHUD.showMenu();
                listOfCurWindows.add(questHUD);
            } else {
                questHUD.removeHUD();
                listOfCurWindows.remove(questHUD);

            }
        }
    }


    /**Call the inventoryHUD and show it**/
    public static void callInventory(Entity e) {
            inventoryIsOn = !inventoryIsOn;

            if (inventory != null) {
                if (inventoryIsOn) {

                    inventory.createInventory(e);
                    inventory.showMenu();
                    listOfCurWindows.add(inventory);
                } else {
                    inventory.removeInventory();
                    inventory.hideMenu();
                    listOfCurWindows.remove(inventory);

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
                listOfCurWindows.add(dialogueMenu);
            } else {
                dialogueMenu.removeDialogueMenu();
                dialogueMenu.hideMenu();
                listOfCurWindows.remove(dialogueMenu);
            }
            dialogueIsOn = !dialogueIsOn;

        }
    }

    /**Add a window to the currently open windows
     * @param window: ScreenController, that is closable
     * **/
    public static void addRecentWindow(ScreenController window) {
        listOfCurWindows.add(window);
    }

    /**
     * Close the last window that was opened
     * **/
    public static void closeRecentWindow() {
        if(listOfCurWindows.size() > 0) {
            ScreenController curWindow = listOfCurWindows.get(listOfCurWindows.size()-1);
            listOfCurWindows.remove(curWindow);
            if(curWindow.equals(inventory)) {
                callInventory(getHero().get());
            }
            else if(curWindow.equals(dialogueMenu)) {
                callDialogue("",true);
            }
            else if(curWindow.equals(pauseMenu)) {
                togglePause();
            }
            else if(curWindow.equals(gameOverHUD)) {
                gameOverHUD.hideMenu();
            }
            else if(curWindow.equals(questHUD)) {
                callQuestHUD();
            }
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
        try {InventoryComponent inv = (InventoryComponent) entity.getComponent(InventoryComponent.class).get();
            DropLoot drop = new DropLoot();
            if ( inv.getCurMainItem() != null)
                drop.onDeath(entity);}
        catch ( NoSuchElementException e ) {
        }
    }

    public static void sound(Entity e) {}

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
        new QuestSystem();
    }

    /**
     * Restarts the Game.
     * Called when clicking the restart-Button when the game is over (-> GameOverHUD).
     * Deletes the whole inventory of Hero, beside the start items and gives the hero full life
     * Places Hero on another Tile in the same Level
     */
    public void restart() {
        gameLogger = Logger.getLogger(this.getClass().getName());
        gameLogger.info("Restart. Hero is reset to starting values");
        currentLevelNumber = 1;
        new PlayerHUDSystem();
        Ghost.setName();
        HealthComponent hc = (HealthComponent) hero.getComponent(HealthComponent.class).get();
        hc.setCurrentHealthpoints(hc.getMaximalHealthpoints());
        gameOverHUD.hideMenu();
        getHero().get().removeComponent(InventoryComponent.class);
        ((Hero)hero).setupInventoryComponent();
        placeOnLevelStart(hero);
    }

    /**
     * Starts the bossmonster level and creates a bossmonster
     */
    public void startBossMonsterLevel() {
        Tile[][] tiles = createTilesTest();
        bossLevel = new TileLevel(tiles,"BossMonsterLevel");
        levelAPI.setLevel(bossLevel);
        this.telepeter = new Telepeter();
      }

    private Tile[][] createTilesTest() {
        Tile[][] tiles = new Tile[32][32];
        for ( int i = 0; i < 32; i++) {
            for ( int j = 0; j < 32; j++) {
                tiles[i][j] = TileFactory.createTile("dungeon/default/floor/empty.png", new Coordinate(i,j), LevelElement.SKIP, null);
            }
        }
        for ( int i = 8; i < 24;i++) {
            for ( int j = 8; j < 24; j++) {
                if (i == 8 && j == 8) {
                    tiles[i][j] = TileFactory.createTile("dungeon/special/wall_spec_outerCorner_bottomLeft.png", new Coordinate(j, i), LevelElement.WALL, null);
                } else if (i == 8 && j == 23) {
                    tiles[i][j] = TileFactory.createTile("dungeon/special/wall_spec_outerCorner_bottomRight.png", new Coordinate(j, i), LevelElement.WALL, DesignLabel.randomDesign());
                } else if (i == 23 && j == 8) {
                    tiles[i][j] = TileFactory.createTile("dungeon/special/wall_spec_outerCorner_upperLeft.png", new Coordinate(j, i), LevelElement.WALL, DesignLabel.randomDesign());
                } else if (i == 23 && j == 23) {
                    tiles[i][j] = TileFactory.createTile("dungeon/special/wall_spec_outerCorner_upperRight.png", new Coordinate(j, i), LevelElement.WALL, DesignLabel.randomDesign());
                } else if (i == 8 && j > 8 && j < 23) {
                    tiles[i][j] = TileFactory.createTile("dungeon/special/wall_spec_bottom.png", new Coordinate(j,i), LevelElement.WALL, DesignLabel.randomDesign());
                } else if (i > 8 && i < 23 && j == 8) {
                    tiles[i][j] = TileFactory.createTile("dungeon/special/wall_spec_left.png", new Coordinate(j, i), LevelElement.WALL, DesignLabel.randomDesign());
                } else if (i == 23 && j > 8 && j < 23) {
                    tiles[i][j] = TileFactory.createTile("dungeon/special/wall_spec_top.png", new Coordinate(j, i), LevelElement.WALL, DesignLabel.randomDesign());
                } else if (i > 8 && i < 23 && j == 23) {
                    tiles[i][j] = TileFactory.createTile("dungeon/special/wall_spec_right.png", new Coordinate(j, i), LevelElement.WALL, DesignLabel.randomDesign());
                } else {
                    tiles[i][j] = TileFactory.createTile("dungeon/default/floor/floor_1.png", new Coordinate(j, i), LevelElement.FLOOR, DesignLabel.randomDesign());
                }
            }
        }

        return tiles;
    }

    private void addExitToBossLevel() {
        PositionComponent pC = (PositionComponent) telepeter.getComponent(PositionComponent.class).get();
        Coordinate cordOfBoss = pC.getPosition().toCoordinate();
        ExitTile exit = new ExitTile("dungeon/default/floor/floor_ladder.png",cordOfBoss,DesignLabel.DEFAULT, bossLevel);
        bossLevel.setRandomEnd();
        levelAPI.update();
    }
}
