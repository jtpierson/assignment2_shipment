package org.example.project.data

class ShippingUpdate (
    previousStatus : String,
    newStatus : String,
    timestamp : Long,
){
    var previousStatus : String = previousStatus
        private set

    var newStatus : String = newStatus
        private set

    var timestamp : Long = timestamp
        private set
}