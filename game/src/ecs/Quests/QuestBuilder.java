package ecs.Quests;

import ecs.Quests.QuestBehavior.CollectCoinsBehavior;
import ecs.Quests.QuestBehavior.KillFoeQuestBehavior;
import ecs.Quests.QuestBehavior.KillTelepeterBehavior;
import ecs.entities.Hero;
import starter.Game;

import java.util.ArrayList;
import java.util.Random;

public class QuestBuilder {

    private static ArrayList<Quest> questSelection = new ArrayList<>();
    private static Quest specialQUest =  new Quest("Kill Telepeter", "You need to kill the Telepeter, a strong enemy.\nThe ladder will lead you to him", new KillTelepeterBehavior(), QuestTag.KILL_TELEPETER, "Emerald is your reward");

    //Roll random Quest targets
    private static void reRollQuests() {
        Random rn = new Random();
        int numberOfMonsters = rn.nextInt(15, 30);
        int numberOfCoins = ((Hero) Game.getHero().get()).getMoney() + rn.nextInt(100, 200);
        questSelection.add(new Quest("Kill Monsters", "You need to kill " + numberOfMonsters + " opposing Monsters", new KillFoeQuestBehavior(numberOfMonsters), QuestTag.KILL_MONSTER, "Completed - get Coins"));
        questSelection.add(new Quest("Collect Coins", "You need a net worth of " + numberOfCoins + " gold at some point", new CollectCoinsBehavior(numberOfCoins), QuestTag.SAVE_COINS, "Completed - get random Item"));
    }

    /**
     * Get a random "side quest"
     * @return The random quest
     * **/
    public static Quest getRandomSideQuest() {
        Random rn =new Random();
        reRollQuests();
        return questSelection.get(rn.nextInt(questSelection.size()));
    }

    /**
     * Get the special Quest (currently "Kill Telepeter")
     * @return The Quest for killing Telepeter
     * **/
    public static Quest getSpecialQuest() {
        return specialQUest;
    }

}
