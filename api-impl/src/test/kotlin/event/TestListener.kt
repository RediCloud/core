package event

import net.dustrean.api.event.CoreListener
import net.dustrean.api.event.EventPriority

class TestListener{

    @CoreListener(priority = EventPriority.HIGH)
    fun onTestEvent1(event: TestEvent){
        println("TestEvent: " + event.message + " Priority: HIGH")
    }

    @CoreListener
    fun onTestEvent2(event: TestEvent){
        println("TestEvent: " + event.message + " Priority: ---")
    }

    @CoreListener(priority = EventPriority.LOW)
    fun onTestEvent3(event: TestEvent){
        println("TestEvent: " + event.message + " Priority: LOW")
    }
}