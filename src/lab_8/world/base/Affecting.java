package lab_8.world.base;

import lab_8.world.state.AffectState;

public interface Affecting {
    public void affectOn(Affected object, AffectState state);
}
