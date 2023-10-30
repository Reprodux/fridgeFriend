package tech.ericw.java.mocks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class Call {

    private final String function;
    private final Object[] arguments;

    Call(String function) {
        this.function = function;
        arguments = null;
    }

    Call(String function, Object[] arguments) {
        this.function = function;
        this.arguments = arguments;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Call)) {
            return false;
        }
        Call call = (Call) obj;
        if (!this.function.equals(call.function)) {
            return false;
        }
        if (this.arguments != null && call.arguments != null) {
            if (this.arguments.length != call.arguments.length) {
                return false;
            }
            for (int i = 0; i < this.arguments.length; i++) {
                if (!this.arguments[i].equals(call.arguments[i])) return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(function);
        result = 31 * result + Arrays.hashCode(arguments);
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return function + " with " + (arguments != null ? arguments.length : "any number of") + " arguments";
    }
}
