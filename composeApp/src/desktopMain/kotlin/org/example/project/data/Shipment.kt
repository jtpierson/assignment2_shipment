package org.example.project.data

import org.example.project.observer.Observer
import org.example.project.observer.Subject

class Shipment(
    id : String,
    status : String,
    expectedDeliveryDateTimestamp : Long,
    currentLocation : String
) : Subject {

    private val observers = mutableListOf<Observer>()

    var id: String = id
        private set

    private var status: String = status
        set(value) {
            field = value
            notifyObservers()
        }


    private var expectedDeliveryDateTimestamp: Long = expectedDeliveryDateTimestamp
        set(value) {
            field = value
            notifyObservers()
        }

    private var currentLocation: String = currentLocation
        set(value) {
            field = value
            notifyObservers()
        }

    private val _notes = mutableListOf<String>()
    val notes: List<String>
        get() = _notes

    private val _updateHistory = mutableListOf<ShippingUpdate>()
    val updateHistory: List<ShippingUpdate>
        get() = _updateHistory

    override fun registerObserver(observer: Observer) {
        TODO("Not yet implemented")
    }

    override fun removeObserver(observer: Observer) {
        TODO("Not yet implemented")
    }

    override fun notifyObservers() {
        TODO("Not yet implemented")
    }
}