package com.androidhuman.rxfirebase.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import com.androidhuman.rxfirebase.common.model.TaskResult;

import android.support.annotation.NonNull;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

final class SetValueWithPriorityOnSubscribe<T> implements SingleOnSubscribe<TaskResult> {

    private final DatabaseReference ref;

    private final T value;

    private final Object priority;

    SetValueWithPriorityOnSubscribe(DatabaseReference ref, T value, Object priority) {
        this.ref = ref;
        this.value = value;
        this.priority = priority;
    }

    @Override
    public void subscribe(final SingleEmitter<TaskResult> emitter) {
        final OnCompleteListener<Void> listener = new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!emitter.isDisposed()) {
                    if (!task.isSuccessful()) {
                        emitter.onSuccess(TaskResult.failure(task.getException()));
                    } else {
                        emitter.onSuccess(TaskResult.success());
                    }
                }
            }
        };

        ref.setValue(value, priority)
                .addOnCompleteListener(listener);
    }
}
