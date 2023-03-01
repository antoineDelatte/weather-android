package com.example.weather

class NotGrantedPermissionException: RuntimeException() {
    override val message: String
        get() = "Not granted permission!"
}