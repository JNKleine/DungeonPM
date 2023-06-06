package ecs.entities;

import dslToGame.AnimationBuilder;
import ecs.Quests.Quest;
import ecs.Quests.QuestBuilder;
import ecs.components.*;
import ecs.items.ItemData;
import ecs.items.ItemType;
import ecs.items.item.Damagestone;
import ecs.items.item.PotionOfHealing;
import ecs.items.item.PotionOfTrapDestroying;
import ecs.items.item.Telestone;
import ecs.systems.DialogueSystem;
import graphic.Animation;
import starter.Game;

import java.util.Random;

/**
 * Shop of the game, where your able to buy and sell items, you can also haggle with the shopkeeper
 */
public class Shopkeeper extends Entity {
    private Quest q = null;
    private boolean questIsSuggested = false;
    private static boolean specialQuestIsAccepted = false;
    private boolean questIsAccepted= false;

    private ItemData[] possibleItemsInShop = new ItemData[]{new Telestone().getItemData(), new Damagestone().getItemData(),
        new PotionOfHealing().getItemData(), new PotionOfTrapDestroying().getItemData()};
    public static int moduloForLevelSpawn = 15;
    private boolean haggle;
    private int dice;
    private float currentPriceFactor;
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
        currentPriceFactor = 1.2f;
    }

    //add PositionComponent
    private void addPositionComponent() {
        new PositionComponent(this);
    }

    private void addInteractionComponent() {
        new InteractionComponent(this, 3, true,
            new OpenDialogueOnInteraction(
                this, "Hello! Wanna trade?\nIf you want to haggle i will role a dice for you.\n" +
                "If you get an even number you get to be lucky\nand get a discount.\nBut if you get an odd number " +
                "you will be punished \nand the items are getting more expensive.\nOr wanna get a quest?\nTry to meet the challenge.", true));
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


    // Sells an item from the perspective of the shop
    private String sellItem(InventoryComponent ic,InventoryComponent icH,Hero hero) {
        if(ic.getCurMainItem() != null) {
            int itemPrice = (int)(ic.getCurMainItem().getValue()*currentPriceFactor);
            if(hero.getMoney() >= itemPrice) {
                if(icH.emptySlots() > 0) {
                    ic.removeItem(ic.getCurMainItem());
                    icH.addItem(ic.getCurMainItem());
                    hero.decreaseMoney(itemPrice);
                    ic.setCurMainItem(null);
                    DialogueSystem.hideInventoryHUD();
                    return "Congratulations to your new item!";
                } else {
                    return "Your inventory is full!";
                }
            } else {
                return "You don't have enough coins!";
            }
        } else {
            return "Chose a new item you want to buy!";
        }
    }

    // buys an item from the perspective of the shop
    private String buyItem(InventoryComponent icH,Hero hero) {
        if(icH.getCurMainItem() != null) {
                if(icH.getCurMainItem().getItemType() == ItemType.Backpack &&
                    icH.emptySlots() < icH.getBackpackSize()) {
                    return "Sorry, your backpack is still full of items!\nEmpty it, and I will buy it";
                }
                else icH.removeBackpack();
            hero.increaseMoney(icH.getCurMainItem().getValue());
            icH.removeItem(icH.getCurMainItem());
            icH.setCurMainItem(null);
            DialogueSystem.hideInventoryHUD();
            return "It was my pleasure doing business with you";
        }
        else return "Chose a new item you want to sell!";
    }

    // Gets the Price, when u ask him how much, for buy and sell
    private String getPrice(InventoryComponent ic,boolean itemIsForSell) {
        if(ic.getCurMainItem() != null) {
            if(itemIsForSell) {
                return "I want "+(int)(ic.getCurMainItem().getValue()*currentPriceFactor) +
                    " Coins for this " + ic.getCurMainItem().getItemName() + "!";
            }
            else
                return "This "+ic.getCurMainItem().getItemName()+" would be "+ic.getCurMainItem().getValue()+
                    " Coins worth!";
            }
        else {
            return "You never chosen any item!";
        }
    }

    // Minigame, roll a dice and if you get an even number u win a dicsout, if u you get an odd number items are more expensive
    private String rollDice() {
        if(!haggle) {
            Random rdm = new Random();
            dice = rdm.nextInt(5) + 1;
            haggle = true;
            if (dice % 2 == 0) {
                currentPriceFactor -= 0.1f;
                return "you rolled a " + dice + ",\nthat mean's you won\nyou now get a 10% discount on my items";
            } else if (dice % 2 == 1) {
                currentPriceFactor += 0.1f;
                return "you rolled a " + dice + ",\nthat mean's you lost\nmy items are now 10% more expensive";
            }
        }
        if (currentPriceFactor == 1.1f)
            return "We've already haggled and you rolled a "+ dice + "\nthat mean's you won\nyou now get a 10% discount on my items";
        else
            return "We've already haggled and you rolled a "+ dice + "\nthat mean's you lost\nmy items are now 10% more expensive";
    }


    /**
     * Calls methods when you speak to him, and returns the answers of that methods
     * @param text Entered input from the player in the dialogHUD
     * @return Answer from the Shop
     */
    @Override
    public String getAnswer(String text) {
        entityLogger.info("Hero requests a response from "+this.getClass().getSimpleName());
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
        }
        else if (text.toLowerCase().contains("haggle")) {
            return rollDice();
        }
        else if(text.toLowerCase().contains("how much i get")) {
            return getPrice(icFromHero,false);
        }
        else if(text.toLowerCase().contains("costs") || text.toLowerCase().contains("how much")) {
            return getPrice(icFromShop,true);
        }
        else if(text.toLowerCase().contains("buy this item")) {
            return sellItem(icFromShop,icFromHero,hero);
        }
        else if(text.toLowerCase().contains("sell this item")) {
            return buyItem(icFromHero,hero);
        } else if(text.toLowerCase().contains("special quest") && !specialQuestIsAccepted) {
            q = QuestBuilder.getSpecialQuest();
            questIsSuggested = true;
            return q.getQuestDescription()+"\nDo you accept this quest?";
        }
        else if(text.toLowerCase().contains("quest") && !questIsAccepted) {
            q = QuestBuilder.getRandomSideQuest();
            questIsSuggested = true;
            return q.getQuestDescription()+"\nDo you accept this quest?";
        } else if(text.toLowerCase().contains("quest"))  {
            return "I gave you a quest already!";
        }
        else if(text.toLowerCase().contains("yes") && questIsSuggested) {
            QuestLogComponent qLC = (QuestLogComponent) Game.getHero().get().getComponent(QuestLogComponent.class).get();
            if(!qLC.questIsInLog(q)) {
                qLC.addQuestToLog(q);
                if(QuestBuilder.getSpecialQuest().equals(q)) {
                    specialQuestIsAccepted = true;
                    Game.bossRoom = true;
                }
                else questIsAccepted = true;
            }
            return "Ok, great! The Quest is now in the QuestLog";
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
