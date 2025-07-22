package org.example.project.data

import org.example.project.observer.Observer
import org.example.project.observer.Subject

abstract class Shipment(
    id : String,
    status : String,
    expectedDeliveryDateTimestamp : Long,
    createdAtTimestamp : Long,
    currentLocation : String
) : Subject<Shipment> {

    private val observers = mutableListOf<Observer<Shipment>>()

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
    // Added for assignment 3
    var createdAtTimestamp: Long = createdAtTimestamp
        private set

    private val _violations = mutableListOf<String>()
    val violations: List<String>
        get() = _violations

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
        checkForViolations()
    }

    fun markRelocated(newLocation : String, timestamp : Long) {
        val oldStatus = status
        status = "relocated"
        val update = ShippingUpdate(
            previousStatus = oldStatus,
            newStatus = status,
            timestamp = timestamp
        )

        currentLocation = newLocation
        addUpdate(update)
        checkForViolations()
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
        checkForViolations()
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
        checkForViolations()
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
        checkForViolations()
    }

    fun markCanceled(timestamp : Long) {
        val oldStatus = status
        status = "canceled"

        val update = ShippingUpdate(
            previousStatus = oldStatus,
            newStatus = status,
            timestamp = timestamp
        )

        addUpdate(update)
        checkForViolations()
    }

    override fun registerObserver(observer: Observer<Shipment>) {
        observers.add(observer)
    }

    override fun removeObserver(observer: Observer<Shipment>) {
        observers.remove(observer)
    }

    override fun notifyObservers() {
        for (observer in observers) {
            observer.update(this)
        }
    }

    // Added for Assignment 3
    protected fun setViolation(message : String) {
        _violations.clear()
        _violations.add(message)
        notifyObservers()
    }

    protected fun clearViolations() {
        _violations.clear()
        notifyObservers()
    }

    abstract fun checkForViolations()
}