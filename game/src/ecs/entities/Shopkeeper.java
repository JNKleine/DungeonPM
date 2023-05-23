package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.components.*;
import ecs.items.ItemData;
import ecs.items.item.Damagestone;
import ecs.items.item.PotionOfHealing;
import ecs.items.item.Telestone;
import graphic.Animation;
import starter.Game;

import java.util.Random;

public class Shopkeeper extends Entity {

    private ItemData[] possibleItemsInShop = new ItemData[]{new Telestone().getItemData(),new Damagestone().getItemData(),
        new PotionOfHealing().getItemData()};
    public static int moduloForLevelSpawn = 20;
    private boolean haggle;
    private int currentPriceFactor;
    private int discount;
    private final String pathToIdle = "character/npc/shop";

    /** creates an Entity Shopkeeper with given Components */
    public Shopkeeper() {
        super(0,Faction.NEUTRAL);
        Random rn = new Random();
        addPositionComponent();
        addInteractionComponent();
        addAnimationComponent();
        addInventoryComponent(rn.nextInt(4,10));
        fillInventory();
        haggle = false;
        currentPriceFactor = 100;
        discount = rn.nextInt(10,20);
    }

    //add PositionComponent
    private void addPositionComponent() {
        new PositionComponent(this);
    }

    private void addInteractionComponent() {
        new InteractionComponent(this,3,true,
            new OpenDialogueOnInteraction(
                this,"Hello! Wanna trade?",true));
    }

    //add AnimationComponent
    private void addAnimationComponent() {
        Animation idle = AnimationBuilder.buildAnimation(this.pathToIdle);
        new AnimationComponent(this,idle,idle);
    }

    private void addInventoryComponent(int inventorySpace) {
        InventoryComponent ic = new InventoryComponent(this,inventorySpace);
    }

    private void fillInventory() {
        Random rd = new Random();
        InventoryComponent ic = (InventoryComponent) this.getComponent(InventoryComponent.class).get();
        for(int i = 0; i < ic.getMaxSize();i++) {
            ic.addItem(possibleItemsInShop[rd.nextInt(0,possibleItemsInShop.length)]);
        }
    }

    @Override
    public String getAnswer(String text) {
        if(text.toLowerCase().contains("trade") || text.toLowerCase().contains("your inventory")) {

            Game.inventory.createInventory(this);
            return "Here are all my items";
        }
        else if(text.toLowerCase().contains("my inventory") || text.toLowerCase().contains("my items")) {
            Game.inventory.createInventory(Game.getHero().get());
            return "What do you wanna sell?";
        }
        else if(text.toLowerCase().contains("haggle")) {
            if(haggle) {return "We've already haggled!";}
            else {//Minigame
                return "";
                 }
        }
        else if(text.toLowerCase().contains("costs") || text.toLowerCase().contains("how much")) {
            //Ausgabe des Preises
            return "";
        }
        else if(text.toLowerCase().contains("buy this item")) {
            //Kauf vom ausgewählten Item
            return "";
        }
        else if(text.toLowerCase().contains("sell this item")) {
            //Verkauf des Items vom Hero
            return "";
        }
        else if (text.matches("[0-9]+")) {
            return "I don't know, what to do with\n all this numbers, sorry!";
        } else if (text.matches("[A-Za-z ]+")) {
            return "I don't understand these words!";
        } else {
            return "I cant understand you!";
        }
    }
}
