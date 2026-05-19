package edu.touro.mcon364.finalreview.orderflowhandoff.exercises;

import edu.touro.mcon364.finalreview.model.Action;

import java.util.ArrayDeque;
import java.util.Optional;

/**
 * In-class Exercise 1 — Action History
 *
 * A simple editor needs to remember actions so the user can undo and redo work.
 *
 * Requirements:
 * - perform(action) records a newly completed action.
 * - undo() removes and returns the action that should be undone next.
 * - redo() removes and returns the action that should be redone next.
 * - undo() returns Optional.empty() when there is nothing available to undo.
 * - redo() returns Optional.empty() when there is nothing available to redo.
 * - performing a new action after one or more undo operations makes the old redo path invalid.
 * - getUndoCount() returns how many actions are currently available to undo.
 * - getRedoCount() returns how many actions are currently available to redo.
 *
 * You may add private fields and private helper methods.
 * Do not change the public method signatures.
 * Before coding, decide:
 * - What information does this class need to remember?
 * - What is the appropriate data structure
 * - Which operation should be fastest?
 * - When an action is undone, where should it go so it can be redone later?
 * - What should happen to redo history after a brand-new action is performed?

 */
public class ActionHistory {
    ArrayDeque<Action> undo = new ArrayDeque<>();
    ArrayDeque<Action> redo = new ArrayDeque<>();

    public void perform(Action action) {
        undo.push(action);
        // TODO: implement based on the requirements above
        redo.clear();
    }

    public Optional<Action> undo() {
        // TODO: implement based on the requirements above
        if (undo.isEmpty()) {
            return Optional.empty();
        }
        Action undid= undo.pop();
        redo.push(undid);
        return Optional.of(redo.peek());
    }

    public Optional<Action> redo() {
        // TODO: implement based on the requirements above
        // TODO: implement based on the requirements above
        if (redo.isEmpty()) {
            return Optional.empty();
        }
        Action undid= redo.pop();
        undo.push(undid);

        return Optional.of(undo.peek());
    }

    public int getUndoCount() {
        // TODO: implement based on the requirements above
        return undo.size();

    }

    public int getRedoCount() {
        // TODO: implement based on the requirements above
        return redo.size();
    }
}
