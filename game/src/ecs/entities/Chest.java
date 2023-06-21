package ecs.entities;

import ecs.components.*;
import ecs.items.ItemData;
import ecs.items.ItemDataGenerator;
import graphic.Animation;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import level.tools.LevelElement;
import starter.Game;
import tools.Point;

/** Chest holds all information about a chest and their stored items * */
public class Chest extends Entity {

    Logger chestLogger = Logger.getLogger(this.getClass().getName());

    public static final float defaultInteractionRadius = 1f;
    public static final List<String> DEFAULT_CLOSED_ANIMATION_FRAMES =
            List.of("objects/treasurechest/chest_full_open_anim_f0.png");
    public static final List<String> DEFAULT_OPENING_ANIMATION_FRAMES =
            List.of(
                    "objects/treasurechest/chest_full_open_anim_f0.png",
                    "objects/treasurechest/chest_full_open_anim_f1.png",
                    "objects/treasurechest/chest_full_open_anim_f2.png",
                    "objects/treasurechest/chest_empty_open_anim_f2.png");

    /**
     * small Generator which uses the Item#ITEM_REGISTER
     *
     * @return a configured Chest
     */
    public static Chest createNewChest() {
        Random random = new Random();
        ItemDataGenerator itemDataGenerator = new ItemDataGenerator();

        List<ItemData> itemData =
                IntStream.range(0, random.nextInt(1, 4))
                        .mapToObj(i -> itemDataGenerator.generateSafeItemData())
                        .toList();
        Chest c =
                new Chest(
                        itemData,
                        Game.currentLevel
                                .getRandomTile(LevelElement.FLOOR)
                                .getCoordinate()
                                .toPoint());
        c.addInteractionComponent();
        return c;
    }

    /**
     * small Generator which uses the Item#ITEM_REGISTER
     *
     * @param position The position, the Chest should spawn
     * @return a configured Chest
     */
    public static Chest createNewChest(Point position) {
        Random random = new Random();
        ItemDataGenerator itemDataGenerator = new ItemDataGenerator();

        List<ItemData> itemData =
                IntStream.range(0, random.nextInt(1, 4))
                        .mapToObj(i -> itemDataGenerator.generateSafeItemData())
                        .toList();
        Chest c = new Chest(itemData, position);
        c.addInteractionComponent();
        return c;
    }

    /**
     * Creates a new Chest which drops the given items on interaction
     *
     * @param itemData which the chest is supposed to drop
     * @param position the position where the chest is placed
     */
    public Chest(List<ItemData> itemData, Point position) {
        new PositionComponent(this, position);
        InventoryComponent ic = new InventoryComponent(this, itemData.size());
        itemData.forEach(ic::addItem);
        for (ItemData i : ic.getItems()) {
            System.out.println(i.getItemName());
        }
        AnimationComponent ac =
                new AnimationComponent(
                        this,
                        new Animation(DEFAULT_CLOSED_ANIMATION_FRAMES, 3, false),
                        new Animation(DEFAULT_OPENING_ANIMATION_FRAMES, 3, false));
    }

    /**
     * Drop the items, that are in the chest
     *
     * @param entity : Entity that drops the items *
     */
    public void dropItems(Entity entity) {
        InventoryComponent inventoryComponent =
                entity.getComponent(InventoryComponent.class)
                        .map(InventoryComponent.class::cast)
                        .orElseThrow(
                                () ->
                                        createMissingComponentException(
                                                InventoryComponent.class.getName(), entity));
        PositionComponent positionComponent =
                entity.getComponent(PositionComponent.class)
                        .map(PositionComponent.class::cast)
                        .orElseThrow(
                                () ->
                                        createMissingComponentException(
                                                PositionComponent.class.getName(), entity));
        List<ItemData> itemData = inventoryComponent.getItems();
        double count = itemData.size();

        IntStream.range(0, itemData.size())
                .forEach(
                        index ->
                                itemData.get(index)
                                        .triggerDrop(
                                                entity,
                                                calculateDropPosition(
                                                        positionComponent, index / count)));
        entity.getComponent(AnimationComponent.class)
                .map(AnimationComponent.class::cast)
                .ifPresent(x -> x.setCurrentAnimation(x.getIdleRight()));
    }

    /**
     * small Helper to determine the Position of the dropped item simple circle drop
     *
     * @param positionComponent The PositionComponent of the Chest
     * @param radian of the current Item
     * @return a Point in a unit Vector around the Chest
     */
    private static Point calculateDropPosition(PositionComponent positionComponent, double radian) {
        return new Point(
                (float) Math.cos(radian * Math.PI) + positionComponent.getPosition().x,
                (float) Math.sin(radian * Math.PI) + positionComponent.getPosition().y);
    }

    /**
     * Helper to create a MissingComponentException with a bit more information
     *
     * @param Component the name of the Component which is missing
     * @param e the Entity which did miss the Component
     * @return the newly created Exception
     */
    private static MissingComponentException createMissingComponentException(
            String Component, Entity e) {
        return new MissingComponentException(
                Component
                        + " missing in "
                        + Chest.class.getName()
                        + " in Entity "
                        + e.getClass().getName());
    }

    /** Add interactionComponent * */
    public void addInteractionComponent() {
        new InteractionComponent(
                this,
                1f,
                false,
                new IInteraction() {
                    /** determines, what happens, if the player interact with the chest * */
                    @Override
                    public void onInteraction(Entity entity) {
                        Hero hero = (Hero) Game.getHero().get();
                        InventoryComponent ic =
                                (InventoryComponent)
                                        hero.getComponent(InventoryComponent.class).get();
                        ItemData mainItem = ic.getCurMainItem();
                        if (mainItem.getItemName().equals("Key")) {
                            ic.removeItem(mainItem);
                            ic.setCurMainItem(null);
                            dropItems(entity);
                            chestLogger.info("Chest was opened with key");
                        } else {
                            Game.lockPickHUD.toOpen = entity;
                            Game.callLockPickHUD(true);
                            chestLogger.info("Minigame in Chest is started");
                        }
                    }
                });
    }
}
