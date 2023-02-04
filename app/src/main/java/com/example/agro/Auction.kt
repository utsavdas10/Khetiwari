package com.example.agro

class Auction {
    private var currentPrice: String = ""
    private var name: String = ""

    constructor()

    constructor(currentPrice: String, name: String){
        this.currentPrice = currentPrice
        this.name = name
    }

    fun getCP() : String {
        return currentPrice
    }
    fun setCP(mobile: String) {
        this.currentPrice = mobile
    }
    fun getName() : String {
        return name
    }
    fun setName(mobile: String) {
        this.name = mobile
    }
}