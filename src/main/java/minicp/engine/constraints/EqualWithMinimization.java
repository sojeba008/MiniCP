package minicp.engine.constraints;

import minicp.engine.core.AbstractConstraint;
import minicp.engine.core.IntVar;

/**
 * Less or equal constraint between two variables
 */
public class EqualWithMinimization extends AbstractConstraint { // x = sol

    private final IntVar x;
    private final IntVar sol;

    public EqualWithMinimization(IntVar x, IntVar sol) {
        super(x.getSolver());
        this.x = x;
        this.sol = sol;
    }

    @Override
    public void post() {
        sol.propagateOnBoundChange(this);
        x.propagateOnBoundChange(this);

        propagate();
    }

    @Override
    public void propagate() {

//        System.out.println(x+" "+y);
        if(sol.isBound()) x.assign(sol.min());
        if(x.isBound()) sol.assign(x.min());

        if (x.max() == sol.min())
            setActive(false);
    }
}
