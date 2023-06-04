package level.bossmonsterLevel;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import graphic.Painter;
import graphic.PainterConfig;
import level.IOnLevelLoader;
import level.elements.ILevel;
import level.elements.tile.Tile;
import level.tools.LevelElement;

import java.util.HashMap;
import java.util.Map;

public class BossMonsterLevel {

    private final SpriteBatch batch;
    private final Painter painter;
    private final IOnLevelLoader onLevelLoader;
    private ILevel currentLevel;

    public BossMonsterLevel(SpriteBatch batch,
                            Painter painter, IOnLevelLoader onLevelLoader) {
        this.batch = batch;
        this.painter = painter;
        this.onLevelLoader = onLevelLoader;
        drawLevel();
    }

    private void drawLevel() {
        Map<String, PainterConfig> mapping = new HashMap<>();

        Tile[][] layout = currentLevel.getLayout();
        for (int y = 0; y < layout.length; y++) {
            for (int x = 0; x < layout[0].length; x++) {
                Tile t = layout[y][x];
                if (t.getLevelElement() != LevelElement.SKIP
                    && t.getLevelElement() != LevelElement.EXIT
                    && t.getLevelElement() != LevelElement.DOOR) {
                    String texturePath = t.getTexturePath();
                    if (!mapping.containsKey(texturePath)) {
                        mapping.put(texturePath, new PainterConfig(texturePath));
                    }
                    painter.draw(
                        t.getCoordinate().toPoint(), texturePath, mapping.get(texturePath));
                }
            }
        }
    }

}
