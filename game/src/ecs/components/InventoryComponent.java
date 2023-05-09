package ecs.components;

import ecs.entities.Entity;
import ecs.items.ItemData;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ecs.items.ItemType;
import logging.CustomLogLevel;

/** Allows an Entity to carry Items */
public class InventoryComponent extends Component {

    private List<ItemData> inventory;

    private ItemData curMainItem;
    /**Size from backpack and regular inventory**/
    public int maxSize;

    /**Size from backpack**/
    public int extraSpaceBackPack;

    /**Size from inventory**/
    public int defaultSpace;

/**Determines if a backpack has already been picked up**/
    public boolean backpackIsCollected = false;

    /**Determines, what Items can be stored at the current backpack**/
    public static ItemType backpackItemTypeStorage;
    private final Logger inventoryLogger = Logger.getLogger(this.getClass().getName());

    /**
     * creates a new InventoryComponent
     *
     * @param entity the Entity where this Component should be added to
     * @param curMaxSpace the maximal size of the inventory
     */
    public InventoryComponent(Entity entity, int curMaxSpace) {
        super(entity);
        inventory = new ArrayList<>(maxSize);
        maxSize = curMaxSpace;
        defaultSpace = maxSize;
        extraSpaceBackPack = 0;
    }

    /**
     * Adding an Element to the Inventory does not allow adding more items than the size of the
     * Inventory (regular Inventory+backpack).
     *
     * @param itemData the item which should be added
     * @return true if the item was added, otherwise false
     */
    public boolean addItem(ItemData itemData) {
        if ((inventory.size() >= defaultSpace && !itemData.getItemType().equals(backpackItemTypeStorage)) ||
            inventory.size() >= maxSize) return false;
        inventoryLogger.log(
                CustomLogLevel.DEBUG,
                "Item '"
                        + this.getClass().getSimpleName()
                        + "' was added to the inventory of entity '"
                        + entity.getClass().getSimpleName()
                        + "'.");
        return inventory.add(itemData);
    }

    /**
     When a backpack is picked up, the properties of the backpack are set
     @param size: size from the backpack
     @param type: ItemType which can be stored in backpack
     **/
    public void addBackpack(int size, ItemType type) {
        extraSpaceBackPack = size;
        maxSize = defaultSpace+extraSpaceBackPack;
        backpackItemTypeStorage = type;
        backpackIsCollected = true;
    }

    /**
     When a backpack is removed, the properties of the backpack are set to default
     **/
    public void removeBackpack() {
        extraSpaceBackPack = 0;
        maxSize = defaultSpace;
        backpackIsCollected = false;
    }

    /**
     * removes the given Item from the inventory
     *
     * @param itemData the item which should be removed
     * @return true if the element was removed, otherwise false
     */
    public boolean removeItem(ItemData itemData) {
        inventoryLogger.log(
                CustomLogLevel.DEBUG,
                "Removing item '"
                        + this.getClass().getSimpleName()
                        + "' from inventory of entity '"
                        + entity.getClass().getSimpleName()
                        + "'.");
        return inventory.remove(itemData);
    }

    /**
     * @return the number of slots already filled with items
     */
    public int filledSlots() {
        return inventory.size();
    }

    /**
     * @return the number of slots still empty
     */
    public int emptySlots() {
        return maxSize - inventory.size();
    }

    /**
     * @return the size of the inventory (inventory+backpack)
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * @return the size of the regular inventory
     */
    public int getDefaultSize() {return defaultSpace;}

    /**
     * @return the size of the backpack
     */
    public int getBackpackSize() {return extraSpaceBackPack;}

    /**
     * @return a copy of the inventory
     */
    public List<ItemData> getItems() {
        return new ArrayList<>(inventory);
    }

    /** Set the current main item (the item, which is currently in use) **/
    public void setCurMainItem(ItemData item) {
        curMainItem = item;
    }

    /** Get the current main item (the item, which is currently in use) **/
    public ItemData getCurMainItem(){
        return curMainItem;
    }

}
