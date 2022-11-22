//
//  ThirdPartyAPI.swift
//  Veracoin
//
//  This is support code for the Mock Mobile Development environment
//  You should not revise this code or it may break the lesson. 
//
//  Singleton that manages fake Third Party API
//

package kotlinapp

object ThirdPartyAPI {

    public fun start(accountName: String) {
        kotlinapp.gWebSocket?.let { ws -> 
            ws.send("3|$accountName")
        }   
    }
    
}