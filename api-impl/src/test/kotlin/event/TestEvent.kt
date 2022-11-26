package event

import net.dustrean.api.event.EventType
import net.dustrean.api.event.CoreEvent

class TestEvent : CoreEvent(
    EventType.LOCAL
){
    lateinit var message: String
}