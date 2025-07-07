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

    var status: String = status
        private set(value) {
            field = value
            notifyObservers()
        }

    var expectedDeliveryDateTimestamp: Long = expectedDeliveryDateTimestamp
        private set(value) {
            field = value
            notifyObservers()
        }

    var currentLocation: String = currentLocation
        private set(value) {
            field = value
            notifyObservers()
        }

    private val _notes = mutableListOf<String>()
    val notes: List<String>
        get() = _notes

    private val _updateHistory = mutableListOf<ShippingUpdate>()
    val updateHistory: List<ShippingUpdate>
        get() = _updateHistory


    fun addNote(update : ShippingUpdate) {
        _updateHistory.add(update)
        notifyObservers()
    }

    fun addUpdate(update : ShippingUpdate) {
        _updateHistory.add(update)
        notifyObservers()
    }

    fun markShipped(expectedDelivery : Long, timestamp : Long) {
        val oldStatus = status
        status = "shipped"
        expectedDeliveryDateTimestamp = expectedDelivery

        val update = ShippingUpdate(
            previousStatus = oldStatus,
            newStatus = status,
            timestamp = timestamp
        )
        addUpdate(update)
    }

    override fun registerObserver(observer: Observer) {
        observers.add(observer)
    }

    override fun removeObserver(observer: Observer) {
        observers.remove(observer)
    }

    override fun notifyObservers() {
        for (observer in observers) {
            observer.update(this)
        }
    }
}