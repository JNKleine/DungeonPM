package configuration;

import com.badlogic.gdx.Input;
import configuration.values.ConfigIntValue;

@ConfigMap(path = {"keyboard"})
public class KeyboardConfig {

    public static final ConfigKey<Integer> MOVEMENT_UP =
            new ConfigKey<>(new String[] {"movement", "up"}, new ConfigIntValue(Input.Keys.W));
    public static final ConfigKey<Integer> MOVEMENT_DOWN =
            new ConfigKey<>(new String[] {"movement", "down"}, new ConfigIntValue(Input.Keys.S));
    public static final ConfigKey<Integer> MOVEMENT_LEFT =
            new ConfigKey<>(new String[] {"movement", "left"}, new ConfigIntValue(Input.Keys.A));
    public static final ConfigKey<Integer> MOVEMENT_RIGHT =
            new ConfigKey<>(new String[] {"movement", "right"}, new ConfigIntValue(Input.Keys.D));
    public static final ConfigKey<Integer> INVENTORY_OPEN =
            new ConfigKey<>(new String[] {"inventory", "open"}, new ConfigIntValue(Input.Keys.I));
    public static final ConfigKey<Integer> INTERACT_WORLD =
            new ConfigKey<>(new String[] {"interact", "world"}, new ConfigIntValue(Input.Keys.E));
    public static final ConfigKey<Integer> FIRST_SKILL =
            new ConfigKey<>(new String[] {"skill", "first"}, new ConfigIntValue(Input.Keys.Z));
    public static final ConfigKey<Integer> SECOND_SKILL =
            new ConfigKey<>(new String[] {"skill", "second"}, new ConfigIntValue(Input.Keys.R));
    public static final ConfigKey<Integer> SPACE =
            new ConfigKey<>(new String[] {"interact", "use"}, new ConfigIntValue(Input.Keys.SPACE));
    public static final ConfigKey<Integer> DROP_ITEM =
        new ConfigKey<>(new String[] {"interact", "drop"}, new ConfigIntValue(Input.Keys.Q));
    public static final ConfigKey<Integer> LOOK_UP =
        new ConfigKey<>(new String[] {"look", "up"}, new ConfigIntValue(Input.Keys.UP));
    public static final ConfigKey<Integer> LOOK_LEFT =
        new ConfigKey<>(new String[] {"look", "left"}, new ConfigIntValue(Input.Keys.LEFT));
    public static final ConfigKey<Integer> LOOK_DOWN =
        new ConfigKey<>(new String[] {"look", "down"}, new ConfigIntValue(Input.Keys.DOWN));
    public static final ConfigKey<Integer> LOOK_RIGHT =
        new ConfigKey<>(new String[] {"look", "right"}, new ConfigIntValue(Input.Keys.RIGHT));
}
