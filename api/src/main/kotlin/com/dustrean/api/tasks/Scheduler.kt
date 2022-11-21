package com.dustrean.api.tasks

import java.util.concurrent.TimeUnit

abstract class Scheduler<T: SchedulerTask<*>, K: RepeatSchedulerTask<*>> {

    abstract fun runTaskAsync(task: Runnable)
    abstract fun runTask(task: Runnable): K

    abstract fun runTaskLaterAsync(task: Runnable, delay: Long, unit: TimeUnit)
    abstract fun runTaskLater(task: Runnable, delay: Long, unit: TimeUnit): K

    abstract fun runTaskTimerAsync(task: Runnable, delay: Long, period: Long, unit: TimeUnit)
    abstract fun runTaskTimer(task: Runnable, delay: Long, period: Long, unit: TimeUnit): K

    abstract fun isMainThread(): Boolean

    fun createRepeatTask(schedulerTask: K, runnable: Runnable): K{
        //TODO: Implement
        return schedulerTask
    }

}