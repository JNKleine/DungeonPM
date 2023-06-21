package ecs.systems;

import ecs.entities.Chest;
import starter.Game;

public class LockPickSystem extends ECS_System{
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
                }
            }
            else {
                boolean isfinished = Game.lockPickHUD.checkCondition();
                if(isfinished) {
                    Game.callLockPickHUD(false);
                    Game.lockPickHUD.finished = true;
                    ((Chest)(Game.lockPickHUD.toOpen)).dropItems(Game.lockPickHUD.toOpen);
                }
            }
        }
    }
}
