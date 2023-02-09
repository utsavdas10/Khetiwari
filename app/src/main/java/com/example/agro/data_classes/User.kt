package com.example.agro.data_classes

class User {
    private var name: String=""
    private var email: String = ""
    private var mobile : String = ""
    private var dob : String = ""
    private var address : String = ""
    private var pin : String = ""
    constructor()
    constructor(email:String,name:String,mobile: String, dob: String, address: String, pin: String) {
        this.email = email
        this.name = name
        this.mobile = mobile
        this.dob = dob
        this.address = address
        this.pin = pin
    }
    fun getMobile() : String {
        return mobile
    }
    fun setMobile(mobile: String) {
        this.mobile = mobile
    }
    fun getName() : String {
        return name
    }
    fun setName(mobile: String) {
        this.name = mobile
    }
    fun getEmail() : String {
        return email
    }
    fun setEmail(mobile: String) {
        this.email = mobile
    }
    fun getDOB() : String {
        return dob
    }
    fun setDOB(username: String) {
        this.dob = username
    }
    fun getAddress() : String {
        return address
    }
    fun setAddress(username: String) {
        this.address= username
    }
    fun getPin() : String {
        return pin
    }
    fun setPin(username: String) {
        this.pin = username
    }
}