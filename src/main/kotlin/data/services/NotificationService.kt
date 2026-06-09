package com.data.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification

class NotificationService {
    fun sendPush(fcmToken: String, title: String, body: String) {
        try {
            val notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build()

            val message = Message.builder()
                .setToken(fcmToken)
                .setNotification(notification)
                .build()

            val response = FirebaseMessaging.getInstance().sendAsync(message).get()
            println("Successfully sent message: $response")
        } catch (e: Exception) {
            println("Failed to send push: ${e.message}")
        }
    }
}