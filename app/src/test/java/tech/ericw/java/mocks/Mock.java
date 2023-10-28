package tech.ericw.java.mocks;

import java.util.ArrayList;
import java.util.List;

public abstract class Mock {

    private final List<Call> calls = new ArrayList<>();

    public final boolean toHaveBeenCalled(String method) {
        return calls.contains(new Call(method));
    }

    public final boolean toHaveBeenCalledWith(String method, Object... arguments) {
        return calls.contains(new Call(method, arguments));
    }

    public final boolean toHaveBeenCalledExactly(String method, int times) {
        Call call = new Call(method);
        int count = 0;
        for(Call c: calls) {
            if (c.equals(call)) {
                count++;
            }
        }
        return count == times;
    }

    public final boolean toHaveBeenCalledExactlyWith(String method, int times, Object... args) {
        Call call = new Call(method, args);
        int count = 0;
        for(Call c: calls) {
            if (c.equals(call)) {
                count++;
            }
        }
        return count == times;
    }

    public final boolean lastCalledWith(String method, Object... args) {
        Call call = new Call(method);
        int ind = calls.lastIndexOf(call);
        if (ind == -1) return false;
        return calls.get(ind).equals(new Call(method, args));
    }

    public void reset() {
        calls.clear();
    }

    public final List<Call> getCalls() {
        return new ArrayList<>(calls);
    }

    protected final void addCall(String method, Object... args) {
        calls.add(new Call(method, args));
    }
}
