package minicp.engine.constraints;

import minicp.engine.core.AbstractConstraint;
import minicp.engine.core.IntVar;

/**
 * Less or equal constraint between two variables
 */
public class Equal extends AbstractConstraint { // x = y

    private final IntVar x;
    private final IntVar y;

    public Equal(IntVar x, IntVar y) {
        super(x.getSolver());
        this.x = x;
        this.y = y;
    }

    @Override
    public void post() {
        x.propagateOnBoundChange(this);
        y.propagateOnBoundChange(this);
        propagate();
    }

    @Override
    public void propagate() {

//        System.out.println(x+" "+y);
        if(x.isBound()) y.assign(x.min());
        if(y.isBound()) x.assign(y.min());
        if (x.max() == y.min())
            setActive(false);
    }
}
