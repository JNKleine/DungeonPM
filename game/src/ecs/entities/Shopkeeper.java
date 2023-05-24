package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.items.ItemData;
import ecs.items.item.Damagestone;
import ecs.items.item.PotionOfHealing;
import ecs.items.item.Telestone;
import ecs.systems.DialogueSystem;
import graphic.Animation;
import starter.Game;

import java.util.Random;

public class Shopkeeper extends Entity {

    private ItemData[] possibleItemsInShop = new ItemData[]{new Telestone().getItemData(), new Damagestone().getItemData(),
        new PotionOfHealing().getItemData()};
    public static int moduloForLevelSpawn = 1;
    private boolean haggle;
    private int currentPriceFactor;
    private int discount;
    private final String pathToIdle = "character/npc/shop";

    /**
     * creates an Entity Shopkeeper with given Components
     */
    public Shopkeeper() {
        super(0, Faction.NEUTRAL);
        Random rn = new Random();
        addPositionComponent();
        addInteractionComponent();
        addAnimationComponent();
        addInventoryComponent(8);
        fillInventory();
        haggle = false;
        currentPriceFactor = 100;
        discount = rn.nextInt(10, 20);
    }

    //add PositionComponent
    private void addPositionComponent() {
        new PositionComponent(this);
    }

    private void addInteractionComponent() {
        new InteractionComponent(this, 3, true,
            new OpenDialogueOnInteraction(
                this, "Hello! Wanna trade?\nIf you want to haggle i will role a dice for you.\n" +
                "If you get a even number you get to be lucky\nand get a discount.\nBut if you get an odd number " +
                "you will be punished \nand the items are getting more expensive", true));
    }

    //add AnimationComponent
    private void addAnimationComponent() {
        Animation idle = AnimationBuilder.buildAnimation(this.pathToIdle);
        new AnimationComponent(this, idle, idle);
    }

    private void addInventoryComponent(int inventorySpace) {
        InventoryComponent ic = new InventoryComponent(this, inventorySpace);
    }

    private void fillInventory() {
        Random rd = new Random();
        InventoryComponent ic = (InventoryComponent) this.getComponent(InventoryComponent.class).get();
        for (int i = 0; i < ic.getMaxSize(); i++) {
            ic.addItem(possibleItemsInShop[rd.nextInt(0, possibleItemsInShop.length)]);
        }
    }

    private String sellItem(InventoryComponent ic, InventoryComponent icH, Hero hero) {
        if (ic.getCurMainItem() != null) {
            if (hero.getMoney() >= ic.getCurMainItem().getValue()) {
                if (icH.emptySlots() > 0) {
                    ic.removeItem(ic.getCurMainItem());
                    icH.addItem(ic.getCurMainItem());
                    hero.decreaseMoney(ic.getCurMainItem().getValue());
                    ic.setCurMainItem(null);
                    return "Congratulations to your new item!";
                } else {
                    return "Your inventory is full!";
                }
            } else {
                return "You don't have enough coins!";
            }
        } else {
            return "You never chosen any item!";
        }
    }

    private void buyItem() {
    }

    private String getPrice(InventoryComponent ic) {
        if (ic.getCurMainItem() != null) {
            return ic.getCurMainItem().getValue() + " Coins for this " + ic.getCurMainItem().getItemName() + "!";
        } else {
            return "You never chosen any item!";
        }
    }

    private String rollDice() {
        Random rdm = new Random();
        int dice = rdm.nextInt(5) + 1;
        if (dice % 2 == 0) {
            currentPriceFactor -= 0.1f;
            return "you rolled a " + dice + ",\nthat mean's you won\nyou now get a 10% discount on my items";
        }
        else {
            currentPriceFactor += 0.1f;
            return "you rolled a " + dice +",\nthat mean's you lost\nmy items are now 10% more expensive";
        }
    }


    @Override
    public String getAnswer(String text) {
        Hero hero = (Hero) Game.getHero().get();
        InventoryComponent icFromHero = (InventoryComponent) hero.getComponent(InventoryComponent.class).get();
        InventoryComponent icFromShop = (InventoryComponent) this.getComponent(InventoryComponent.class).get();
        if (text.toLowerCase().contains("trade") || text.toLowerCase().contains("your inventory")) {
            DialogueSystem.hideInventoryHUD();
            DialogueSystem.callInventoryHUD(this);
            return "Here are all my items";
        } else if (text.toLowerCase().contains("my inventory") || text.toLowerCase().contains("my items")) {
            DialogueSystem.hideInventoryHUD();
            DialogueSystem.callInventoryHUD(Game.getHero().get());
            return "What do you wanna sell?";
        } else if (text.toLowerCase().contains("haggle")) {
            if (haggle) {
                return "We've already haggled!";
            } else {
                haggle = true;
                return rollDice();
            }
        } else if (text.toLowerCase().contains("how much i get")) {
            return getPrice(icFromHero);
        } else if (text.toLowerCase().contains("costs") || text.toLowerCase().contains("how much")) {
            return getPrice(icFromShop);
        } else if (text.toLowerCase().contains("buy this item")) {
            return sellItem(icFromShop, icFromHero, hero);
        } else if (text.toLowerCase().contains("sell this item")) {
            //Verkauf des Items vom Hero
            return "";
        } else if (text.matches("[0-9]+")) {
            return "I don't know, what to do with\n all this numbers, sorry!";
        } else if (text.matches("[A-Za-z ]+")) {
            return "I don't understand these words!";
        } else {
            return "I cant understand you!";
        }
    }
}
