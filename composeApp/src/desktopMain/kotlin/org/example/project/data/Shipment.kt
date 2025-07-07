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


    fun addNote(note : String) {
        _notes.add(note)
        notifyObservers()
    }

    fun addUpdate(update : ShippingUpdate) {
        _updateHistory.add(update)
        notifyObservers()
    }

    // *** markX functions encapsulate the state changes that are triggered by the UpdateStrategy classes
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

    fun markRelocated(newLocation : String, timestamp : Long) {
        val update = ShippingUpdate(
            previousStatus = status,
            newStatus = status,
            timestamp = timestamp
        )

        currentLocation = newLocation
        addUpdate(update)
    }

    fun markDelivered(timestamp: Long) {
        val oldStatus = status
        status = "delivered"

        val update = ShippingUpdate(
            previousStatus = oldStatus,
            newStatus = status,
            timestamp = timestamp
        )

        addUpdate(update)
    }

    fun markDelayed(newExpectedDelivery : Long, timestamp : Long) {
        val oldStatus = status
        status = "late"

        expectedDeliveryDateTimestamp = newExpectedDelivery

        val update = ShippingUpdate (
            previousStatus = oldStatus,
            newStatus = status,
            timestamp = timestamp
            )

        addUpdate(update)
    }

    fun markLost(timestamp : Long) {
        val oldStatus = status
        status = "lost"

        val update = ShippingUpdate(
            previousStatus = oldStatus,
            newStatus = status,
            timestamp = timestamp
        )

        addUpdate(update)
    }

    fun markCanceled(timestamp : Long) {
        val oldStatus = status
        status = "canceled"

        val update = ShippingUpdate(
            previousStatus = oldStatus,
            newStatus = status,
            timestamp = timestamp
        )
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