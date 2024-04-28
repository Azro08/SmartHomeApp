package com.example.smarthomeapp.data.repository

import android.util.Log
import com.example.smarthomeapp.data.model.Order
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OrderRepository @Inject constructor(
    firestore: FirebaseFirestore
) {

    private val collection = firestore.collection("orders")

    suspend fun saveOrder(order: Order): String {
        return try {
            collection.document(order.id).set(order).await()
            "Done"
        } catch (e: FirebaseFirestoreException) {
            Log.d("OrderRepository", e.message.toString())
            e.message.toString()
        }
    }

    suspend fun getUsersOrders(userEmail: String): List<Order> {
        return try {
            val orders = collection.whereEqualTo("buyer", userEmail).get().await().toObjects(Order::class.java)
            orders
        } catch (e: FirebaseFirestoreException) {
            Log.d("OrderRepository", e.message.toString())
            emptyList()
        }
    }

    suspend fun getOrders(): List<Order> {
        return try {
            val orders = collection.get().await().toObjects(Order::class.java)
            orders
        } catch (e: FirebaseFirestoreException) {
            Log.d("OrderRepository", e.message.toString())
            emptyList()
        }
    }

    suspend fun removeOrder(id: String): String {
        return try {
            collection.document(id).delete().await()
            "Done"
        } catch (e: FirebaseFirestoreException) {
            Log.d("OrderRepository", e.message.toString())
            e.message.toString()
        }
    }

}