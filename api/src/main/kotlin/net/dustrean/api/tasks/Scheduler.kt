package net.dustrean.api.tasks

import kotlin.time.Duration

abstract class Scheduler<T : SchedulerTask<*>, K : RepeatSchedulerTask<*>> {
    abstract fun runTaskAsync(task: Runnable)
    abstract fun runTask(task: Runnable)

    abstract fun runTaskLaterAsync(task: Runnable, delay: Duration)
    abstract fun runTaskLater(task: Runnable, delay: Duration)

    abstract fun runTaskTimerAsync(task: Runnable, delay: Duration, period: Duration): K
    abstract fun runTaskTimer(task: Runnable, delay: Duration, period: Duration): K

    abstract fun isMainThread(): Boolean

    fun createRepeatTask(schedulerTask: K, runnable: Runnable): Runnable {
        return Runnable {
            if ((schedulerTask.asyncFilters && !isMainThread()) || !schedulerTask.asyncFilters) {
                if (!schedulerTask.filter()) {
                    schedulerTask.cancel()
                    return@Runnable
                }
                runnable.run()
                return@Runnable
            }

            schedulerTask.filterAsync { filterState ->
                run {
                    if (!filterState) {
                        schedulerTask.cancel()
                        return@filterAsync
                    }
                    runnable.run()
                }
            }
        }
    }
}