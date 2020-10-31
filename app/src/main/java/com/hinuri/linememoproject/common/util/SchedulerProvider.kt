package com.hinuri.linememoproject.common.util

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

/**
 * Provides different types of schedulers.
 */
class SchedulerProvider  // Prevent direct instantiation.
private constructor() : BaseSchedulerProvider {
    @NonNull
    override fun computation(): Scheduler {
        return Schedulers.computation()
    }

    @NonNull
    override fun io(): Scheduler {
        return Schedulers.io()
    }

    @NonNull
    override fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    companion object {
        @Nullable
        private var INSTANCE: SchedulerProvider? = null

        @get:Synchronized
        val instance: SchedulerProvider?
            get() {
                if (INSTANCE == null) {
                    INSTANCE = SchedulerProvider()
                }
                return INSTANCE
            }
    }
}
