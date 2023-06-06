package ecs.Quests;

import ecs.Quests.QuestBehavior.CollectCoinsBehavior;
import ecs.Quests.QuestBehavior.KillFoeQuestBehavior;
import ecs.entities.Hero;
import starter.Game;

import java.util.ArrayList;
import java.util.Random;

public class QuestBuilder {

    private static ArrayList<Quest> questSelection = new ArrayList<>();
    private static Quest specialQUest =  new Quest("Collect Coins", "You need a net worth of " + 9999 + " gold at some point", new CollectCoinsBehavior(9999), QuestTag.SAVE_COINS, "Completed - get HP");

    private static void reRollQuests() {
        Random rn = new Random();
        int numberOfMonsters = rn.nextInt(15, 30);
        int numberOfCoins = ((Hero) Game.getHero().get()).getMoney() + rn.nextInt(100, 200);
        questSelection.add(new Quest("Kill Monsters", "You need to kill " + numberOfMonsters + " opposing Monsters", new KillFoeQuestBehavior(numberOfMonsters), QuestTag.KILL_MONSTER, "Completed - get Coins"));
        questSelection.add(new Quest("Collect Coins", "You need a net worth of " + numberOfCoins + " gold at some point", new CollectCoinsBehavior(numberOfCoins), QuestTag.SAVE_COINS, "Completed - get HP"));
    }

    public static Quest getRandomSideQuest() {
        Random rn =new Random();
        reRollQuests();
        return questSelection.get(rn.nextInt(questSelection.size()));
    }

    public static Quest getSpecialQuest() {
        return specialQUest;
    }

}
