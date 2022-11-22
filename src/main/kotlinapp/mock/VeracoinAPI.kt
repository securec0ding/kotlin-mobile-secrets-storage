//
//  VeracoinAPI.swift
//  Veracoin
//
//  This is support code for the Mock Mobile Development environment
//  You should not revise this code or it may break the lesson. 
//
//  Singleton that manages fake Veracoin API
//

package kotlinapp

import android.location.Location

object VeracoinAPI {

    public fun reportPosition(location: Location) {
        kotlinapp.gWebSocket?.let { ws -> 
            val latitude = location.latitude
            val longitude = location.longitude
            ws.send("Loc|$latitude|$longitude")
        }   
    }
    
}