package ecs.items;

import ecs.items.item.Backpack;
import ecs.items.item.Damagestone;
import ecs.items.item.PotionOfHealing;
import ecs.items.item.Telestone;
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
    private Random rand = new Random();

    /**
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
}
