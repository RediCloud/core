package event

import com.dustrean.api.event.EventType
import com.dustrean.api.event.CoreEvent

class TestEvent : CoreEvent(
    EventType.LOCAL
){
    lateinit var message: String
}