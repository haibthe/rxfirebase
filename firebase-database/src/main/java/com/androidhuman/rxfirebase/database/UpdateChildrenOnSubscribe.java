package com.androidhuman.rxfirebase.database;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

import com.androidhuman.rxfirebase.common.model.TaskResult;

import android.support.annotation.NonNull;

import java.util.Map;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

final class UpdateChildrenOnSubscribe implements SingleOnSubscribe<TaskResult> {

    private final DatabaseReference ref;

    private final Map<String, Object> update;

    UpdateChildrenOnSubscribe(DatabaseReference ref, Map<String, Object> update) {
        this.ref = ref;
        this.update = update;
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

        ref.updateChildren(update)
                .addOnCompleteListener(listener);
    }
}
