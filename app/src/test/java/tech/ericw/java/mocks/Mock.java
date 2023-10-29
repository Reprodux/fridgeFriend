package tech.ericw.java.mocks;

import java.util.ArrayList;
import java.util.List;

public abstract class Mock {

    private final List<Call> calls = new ArrayList<>();

    /**
     * Checks whether the provided method name has been called (as recorded by {@link Mock#addCall(String, Object...)})
     * @param method method name to check
     * @return true if call stack contains a call to the function name provided, false otherwise
     */
    public final boolean toHaveBeenCalled(String method) {
        return calls.contains(new Call(method));
    }

    /**
     * Checks whether the provided method name has been called with the given arguments (as recorded by {@link Mock#addCall(String, Object...)})
     * @param method method name to check
     * @param arguments arguments to check if it was called with
     * @return true if call stack contains a call to the function name provided with the provided arguments, false otherwise
     */
    public final boolean toHaveBeenCalledWith(String method, Object... arguments) {
        return calls.contains(new Call(method, arguments));
    }

    /**
     * Checks whether the provided method name has been called (as recorded by {@link Mock#addCall(String, Object...)}) exactly the provided number of times
     * @param method method name to check
     * @param times exactly how many times the function should of been called
     * @return true if the number of calls to the function name in the call stack equals times, false otherwise
     */
    public final boolean toHaveBeenCalledExactly(String method, int times) {
        Call call = new Call(method);
        int count = countCalls(call);
        return count == times;
    }

    /**
     * Checks whether the provided method name has been called with the given arguments (as recorded by {@link Mock#addCall(String, Object...)}) exactly the provided number of times.
     * This function will only count calls where method and arguments match in the call stack
     * @param method method name to check
     * @param times exactly how many times the function should of been called
     * @param args arguments to check it was called with
     * @return true if the number of calls to the function with the given arguments in the call stack equals times, false otherwise
     */
    public final boolean toHaveBeenCalledExactlyWith(String method, int times, Object... args) {
        Call call = new Call(method, args);
        int count = countCalls(call);
        return count == times;
    }

    /**
     * Checks whether the provided method name has been called (as recorded by {@link Mock#addCall(String, Object...)}) at least the provided number of times
     * @param method method name to check
     * @param min exactly minimum number of times the function should of been called
     * @return true if the number of calls to the function name in the call stack is at least min, false otherwise
     */
    public final boolean toHaveBeenCalledAtLeast(String method, int min) {
        Call call = new Call(method);
        int count = countCalls(call);
        return count >= min;
    }

    /**
     * Checks whether the provided method name has been called with the given arguments (as recorded by {@link Mock#addCall(String, Object...)}) at least the provided number of times.
     * This function will only count calls where method and arguments match in the call stack
     * @param method method name to check
     * @param min minimum number of times the function should of been called
     * @param args arguments to check it was called with
     * @return true if the number of calls to the function with the given arguments in the call stack is at least min, false otherwise
     */
    public final boolean toHaveBeenCalledAtLeastWith(String method, int min, Object... args) {
        Call call = new Call(method, args);
        int count = countCalls(call);
        return count >= min;
    }

    /**
     * Checks if the last recorded call to the given method name was with the given arguments (as recorded by {@link Mock#addCall(String, Object...)})
     * @param method method name to check
     * @param args arguments to check if it was last called with
     * @return true if the last call to the function name on the call stack had the given arguments
     */
    public final boolean lastCalledWith(String method, Object... args) {
        Call call = new Call(method);
        int ind = calls.lastIndexOf(call);
        if (ind == -1) return false;
        return calls.get(ind).equals(new Call(method, args));
    }

    /**
     * Reset the call stack
     */
    public void reset() {
        calls.clear();
    }

    /**
     * Get the call stacks
     * @return get the list of function calls with their name and arguments in the order they were recorded by {@link Mock#addCall(String, Object...)}
     */
    public final List<Call> getCalls() {
        return new ArrayList<>(calls);
    }

    /**
     * Record a call to a function with arguments
     * @param method method name to record the call of
     * @param args arguments to record with the call
     */
    protected final void addCall(String method, Object... args) {
        calls.add(new Call(method, args));
    }

    private int countCalls(Call call) {
        int count = 0;
        for(Call c: calls) {
            if (c.equals(call)) {
                count++;
            }
        }
        return count;
    }
}
