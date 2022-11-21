package com.dustrean.api.tasks

import lombok.Getter
import lombok.Setter
import java.util.function.Consumer

@Getter
abstract class RepeatSchedulerTask<T> constructor(
    schedule: Scheduler<SchedulerTask<T>, RepeatSchedulerTask<*>>
): SchedulerTask<T>(schedule) {

    private var filters: MutableList<TaskFilter> = mutableListOf()

    @Setter
    private val asyncFilters: Boolean = false

    fun addFilter(filter: TaskFilter): TaskFilter {
        filters.add(filter)
        return filter
    }

    fun filter(): Boolean {
        return filters.all { it.filter() }
    }

    fun filterAsync(consumer: Consumer<Boolean>) {
        if(filters.isEmpty()) {
            consumer.accept(true)
            return
        }
        this.scheduler.runTaskAsync {
            consumer.accept(filter())
        }
    }

}