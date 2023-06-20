package ecs.items;

import ecs.items.item.*;

import java.util.List;
import java.util.Random;

/** Generator which creates a random ItemData based on the Templates prepared. */
public class ItemDataGenerator {
    private static final List<String> missingTexture = List.of("animation/missingTexture.png");

    private List<ItemData> templates =
            List.of(
                    new Telestone().getItemData(),
                    new Damagestone().getItemData(),
                    new PotionOfHealing().getItemData(),
                    new Backpack(4,ItemType.Active).getItemData()
            );

    private List<ItemData> loot =
        List.of(
            new Telestone().getItemData(),
            new Damagestone().getItemData(),
            new PotionOfHealing().getItemData(),
            new PotionOfTrapDestroying().getItemData()
        );
    private Random rand = new Random();

    /**
     * Get a random ItemData to 30% probability, else get null
     * @return a new randomItemData
     */
    public ItemData generateItemData() {
        Random rd = new Random();
        if(rd.nextInt(0,100) <= 30) {
            return templates.get(rand.nextInt(templates.size()));
        } else {
            return null;
        }
    }

    /**
     * Get a random ItemData
     * @return a new randomItemData
     */
    public ItemData generateSafeItemData() {
            return loot.get(rand.nextInt(0,loot.size()));
        }
    }
