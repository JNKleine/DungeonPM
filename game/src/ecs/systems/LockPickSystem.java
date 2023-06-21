package ecs.systems;

import ecs.entities.Chest;
import starter.Game;

import java.util.logging.Logger;

/**
 * LockPickSystem controls whether desired shifts in the LockPick minigame are possible.
 * If so, they will be executed. This is done by checking whether the victory
 * condition has been met.
 * If so, the minigame will end and the chest will open
 * **/
public class LockPickSystem extends ECS_System{

    private final Logger lockLogger = Logger.getLogger(this.getClass().getName());
    /**
     * update-Methode is called every frame
     * **/
    @Override
    public void update() {
        if(Game.lockPickHUDisOn) {
            int indexEmpty = Game.lockPickHUD.indexEmptyFieldClicked;
            int indexNumber = Game.lockPickHUD.indexNumberClicked;
            if(indexNumber >= 0 && indexEmpty >= 0) {
                if(indexNumber-1 == indexEmpty || indexNumber+1 == indexEmpty || indexNumber+3 == indexEmpty || indexNumber-3 == indexEmpty) {
                    Game.lockPickHUD.swapValues(indexNumber, indexEmpty);
                    Game.callLockPickHUD(false);
                    Game.callLockPickHUD(false);
                    Game.lockPickHUD.indexNumberClicked = -1;
                    Game.lockPickHUD.indexEmptyFieldClicked = -1;
                    lockLogger.info("Swap field at index position "+indexEmpty+" with "+indexNumber);
                }
            }
            else {
                boolean finished = Game.lockPickHUD.checkCondition();
                if(finished) {
                    Game.callLockPickHUD(false);
                    ((Chest)(Game.lockPickHUD.toOpen)).dropItems(Game.lockPickHUD.toOpen);
                    lockLogger.info("Minigame solved, chest is open");
                }
            }
        }
    }
}
