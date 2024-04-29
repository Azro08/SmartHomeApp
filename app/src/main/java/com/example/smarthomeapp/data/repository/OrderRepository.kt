package com.example.smarthomeapp.data.repository

import android.util.Log
import com.example.smarthomeapp.data.model.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class OrderRepository @Inject constructor(
    firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
    private val usersRepository: UsersRepositoryImpl
) {

    private val collection = firestore.collection("orders")

    suspend fun saveOrder(order: Order): String {
        Log.d("OrderRepository", "saveOrder: $order")
        return try {
            collection.document(order.id).set(order).await()
            "Done"
        } catch (e: FirebaseFirestoreException) {
            Log.d("OrderRepository", e.message.toString())
            e.message.toString()
        }
    }

    suspend fun getMastersOrders(): List<Order> {
        return try {
            val mastersId = firebaseAuth.currentUser?.uid ?: ""
            val mastersName = usersRepository.getUser(mastersId)?.fullName ?: ""
            val mastersNameAndId = "$mastersName: $mastersId"
            val orders = collection
                .whereEqualTo("pickedMaster", mastersNameAndId)
                .get().await().toObjects(Order::class.java)
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

}