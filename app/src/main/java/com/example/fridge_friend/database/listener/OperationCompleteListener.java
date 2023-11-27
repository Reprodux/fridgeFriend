package com.example.fridge_friend.database.listener;

/**
 * Generic Listener for operations that don't return data
 */
public interface OperationCompleteListener extends BaseListener {

    /**
     * Called when an operation completes successfully and there's nothing to return.
     * Invocation of this function merely signals the operation successfully completed and no further work is being done
     */
    void onSuccess();

}
