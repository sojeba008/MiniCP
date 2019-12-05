/*
 * mini-cp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License  v3
 * as published by the Free Software Foundation.
 *
 * mini-cp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY.
 * See the GNU Lesser General Public License  for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with mini-cp. If not, see http://www.gnu.org/licenses/lgpl-3.0.en.html
 *
 * Copyright (c)  2018. by Laurent Michel, Pierre Schaus, Pascal Van Hentenryck
 */

package minicp.search;

import minicp.state.StateManager;
import minicp.util.exception.InconsistencyException;
import minicp.util.exception.NotImplementedException;
import minicp.util.Procedure;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.function.Supplier;


import minicp.state.Trailer;
import minicp.state.Trail;
/**
 * Depth First Search Branch and Bound implementation
 */
public class DFSearch {

    //    private Trailer trail = new Trailer();
    private Trailer trail = new Trailer();
    private Supplier<Procedure[]> branching;
    private StateManager sm;

    private List<Procedure> solutionListeners = new LinkedList<Procedure>();
    private List<Procedure> failureListeners = new LinkedList<Procedure>();

    /**
     * Creates a Depth First Search object with a given branching
     * that defines the search tree dynamically.
     *
     * @param sm the state manager that will be saved and restored
     *           at each node of the search tree
     * @param branching a generator of closures in charge of defining the ordered
     *                  children nodes at each node of the depth-first-search tree.
     *                  When it returns an empty array, a solution is found.
     *                  A backtrack occurs when a {@link InconsistencyException}
     *                  is thrown.
     */
    public DFSearch(StateManager sm, Supplier<Procedure[]> branching) {
        this.sm = sm;
        this.branching = branching;
    }

    /**
     * Adds a listener that is called on each solution.
     *
     * @param listener the closure to be called whenever a solution is found
     */
    public void onSolution(Procedure listener) {
        solutionListeners.add(listener);
    }

    /**
     * Adds a listener that is called whenever a failure occurs
     * and the search backtracks.
     * This happensthat when a {@link InconsistencyException} is thrown
     * when executing the closure generated by the branching.
     *
     * @param listener the closure to be called whenever a failure occurs and
     *                 the search need to backtrack
     */
    public void onFailure(Procedure listener) {
        failureListeners.add(listener);
    }

    private void notifySolution() {
        solutionListeners.forEach(s -> s.call());
    }

    private void notifyFailure() {
        failureListeners.forEach(s -> s.call());
    }

    private SearchStatistics solve(SearchStatistics statistics, Predicate<SearchStatistics> limit) {
        sm.withNewState(() -> {
//            System.out.println("zeeeebi");
            try {
                dfs(statistics, limit);
                statistics.setCompleted();
            } catch (StopSearchException ignored) {
            } catch (StackOverflowError e) {
                throw new NotImplementedException("dfs with explicit stack needed to pass this test");
            }
        });
        return statistics;
    }


    /**
     * Effectively start a depth first search
     * looking for every solution.
     *
     * @return an object with the statistics on the search
     */
    public SearchStatistics solve() {
        SearchStatistics statistics = new SearchStatistics();
        return solve(statistics, stats -> false);
    }

    /**
     * Effectively start a depth first search
     * with a given predicate called at each node
     * to stop the search when it becomes true.
     *
     * @param limit a predicate called at each node
     *             that stops the search when it becomes true
     * @return an object with the statistics on the search
     */
    public SearchStatistics solve(Predicate<SearchStatistics> limit) {
        SearchStatistics statistics = new SearchStatistics();
        return solve(statistics, limit);
    }

    /**
     * Executes a closure prior to effectively
     * starting a depth first search
     * with a given predicate called at each node
     * to stop the search when it becomes true.
     * The state manager saves the state
     * before executing the closure
     * and restores it after the search.
     * Any {@link InconsistencyException} that may
     * be throw when executing the closure is also catched.
     *
     * @param limit a predicate called at each node
     *             that stops the search when it becomes true
     * @param subjectTo the closure to execute prior to the search starts
     * @return an object with the statistics on the search
     */
    public SearchStatistics solveSubjectTo(Predicate<SearchStatistics> limit, Procedure subjectTo) {
        SearchStatistics statistics = new SearchStatistics();
        sm.withNewState(() -> {
            System.out.println("Hope in");
            try {
                subjectTo.call();
                solve(statistics, limit);
            } catch (InconsistencyException e) {
            }
        });
        return statistics;
    }

    /**
     * Effectively start a branch and bound
     * depth first search with a given objective.
     *
     * @param obj the objective to optimize that is tightened each
     *            time a new solution is found
     * @return an object with the statistics on the search
     */
    public SearchStatistics optimize(Objective obj) {
        return optimize(obj, stats -> false);
    }

    /**
     * Effectively start a branch and bound
     * depth first search with a given objective
     * and with a given predicate called at each node
     * to stop the search when it becomes true.
     *
     * @param obj the objective to optimize that is tightened each
     *            time a new solution is found
     * @param limit a predicate called at each node
     *             that stops the search when it becomes true
     * @return an object with the statistics on the search
     */
    public SearchStatistics optimize(Objective obj, Predicate<SearchStatistics> limit) {
        SearchStatistics statistics = new SearchStatistics();
        onSolution(() -> obj.tighten());
        return solve(statistics, limit);
    }

    /**
     * Executes a closure prior to effectively
     * starting a branch and bound depth first search
     * with a given objective to optimize
     * and a given predicate called at each node
     * to stop the search when it becomes true.
     * The state manager saves the state
     * before executing the closure
     * and restores it after the search.
     * Any {@link InconsistencyException} that may
     * be throw when executing the closure is also catched.
     *
     * @param obj the objective to optimize that is tightened each
     *            time a new solution is found
     * @param limit a predicate called at each node
     *             that stops the search when it becomes true
     * @param subjectTo the closure to execute prior to the search starts
     * @return an object with the statistics on the search
     */
    public SearchStatistics optimizeSubjectTo(Objective obj, Predicate<SearchStatistics> limit, Procedure subjectTo) {
        SearchStatistics statistics = new SearchStatistics();

        sm.withNewState(() -> {
            System.out.println("Breath itttt");
            try {
                subjectTo.call();
                optimize(obj, limit);
            } catch (InconsistencyException e) {
            }
        });
        return statistics;
    }

    Stack<Integer> levelStack = new Stack<>();
    private void dfs(SearchStatistics statistics, Predicate<SearchStatistics> limit) {
        if (limit.test(statistics))
            throw new StopSearchException();


        Procedure[] branches = branching.get();
//        System.out.println("Length "+branches.length);
        if (branches.length == 0) {
            statistics.incrSolutions();
            notifySolution();
        } else {
            for (Procedure b : branches) {
//                System.out.println("State 1 "+levelStack);
                sm.saveState();
                levelStack.push(levelStack.size()+1);
//                    System.out.println("State 2 "+levelStack);
                try {
                    statistics.incrNodes();
                    b.call();
                    dfs(statistics, limit);
                } catch (InconsistencyException e) {
                    statistics.incrFailures();
                    notifyFailure();
                }
                sm.restoreState();
                levelStack.pop();
//                System.out.println(sm.getLevel());
            }
        }
    }



    private void dfsold(SearchStatistics statistics, Predicate<SearchStatistics> limit) {
        // create stacks
        Stack<Procedure[]> stack = new Stack<>();
        Stack<Integer> indices = new Stack<>();
        // add base index
        indices.push(0);

        // get first alternatives
        Procedure[] alternatives = branching.get();
        // and add it to the stack
        stack.push(alternatives);

        // while queue is not empty
        while (!(stack.isEmpty())) {
            if (limit.test(statistics))
                throw new StopSearchException();

            // look to the top of the alternatives stack
            alternatives = stack.peek();
            // look and destroy last index
            int index = indices.pop();

            // if no more alternatives
            // we have a solution
            if (alternatives.length == 0) {
                // destroy last alternatives
                stack.pop();
                // increase solutions count
                statistics.incrSolutions();				// notify the listeners that a solution has been found
                notifySolution();
                // go up in the tree
                sm.restoreState();
            } else {
                try {
                    //AKOBA ZONE
                    // or == (for safety)
                    // test for backtrack
                    if (alternatives.length <= index) {
                        // destroy last alternatives
                        stack.pop();

                        // if no more alternatives
                        // why continue loop ?
                        if (stack.isEmpty())
                            break;

                        // go up
                        sm.restoreState();

//						if(!stack.isEmpty())
//							state.pop();

                    } else {
                        // go down
                        sm.saveState();
                        // call current alternative
                        // which is selected thanks to the
                        // index saved
                        alternatives[index].call(); // throws InconsistencyException
                        // update the alternatives
                        // by calling another time
                        alternatives = branching.get();

                        // add the new alternatives
                        stack.push(alternatives);

                        // increase the nodes count
                        statistics.incrNodes();

                        // update the current index
                        // and add it
                        indices.push(index + 1);
                        // add one more index
                        // which is used for the next alternatives
                        indices.push(0);
                    }
                } catch (InconsistencyException e) {
                    // notify the listeners that we encountered
                    // a failure
                    notifyFailure();
                    // increase the failures count
                    statistics.incrFailures();
                    // go up in tree
                    sm.restoreState();
                }
            }
        }
    }



}
