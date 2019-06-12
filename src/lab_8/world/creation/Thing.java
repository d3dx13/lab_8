package lab_8.world.creation;

import lab_8.world.IdMaster;
import lab_8.world.base.Affected;
import lab_8.world.base.Existing;
import lab_8.world.state.AffectState;
import lab_8.world.state.DynamicsState;
import lab_8.world.state.PositionState;

import java.io.Serializable;

public class Thing extends Existing implements Affected, Serializable {

    public Thing(String newName){
        this.name = newName;
        this.id = IdMaster.create();
    }

    @Override
    public void affectBy(AffectState state) {
        switch (state){
            case WATCH_OUT:
                System.out.println("Кто-то космотрел из " + this.toString());
        }
    }

    @Override
    public void setDynamics(DynamicsState newState) {
        this.dynamicsStateState = newState;
        System.out.println("*" + this.toString() + " теперь " + newState + "*");
    }

    @Override
    public void setPosition(PositionState newState) {
        this.positionState = newState;
        System.out.println("*" + this.toString() + " теперь " + newState + "*");
    }

    @Override
    public int hashCode() {
        return id;
    }
    @Override
    public String toString() {
        return name;
    }
    @Override
    public boolean equals(Object obj) {
        return (this.id == ((Thing) obj).id);
    }
    protected int id;
    public String name;
}
