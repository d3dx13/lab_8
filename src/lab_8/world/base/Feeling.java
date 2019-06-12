package lab_8.world.base;

import lab_8.world.state.FeelState;

public interface Feeling {
    public FeelState feel();
    public FeelState feel(FeelState newState);
}
