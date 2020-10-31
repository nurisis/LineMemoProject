package com.hinuri.linememoproject.common.util

import androidx.annotation.NonNull
import io.reactivex.rxjava3.core.Scheduler

interface BaseSchedulerProvider {
    @NonNull
    fun computation(): Scheduler

    @NonNull
    fun io(): Scheduler

    @NonNull
    fun ui(): Scheduler
}