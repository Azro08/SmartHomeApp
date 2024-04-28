package com.example.smarthomeapp.data.repository

import android.util.Log
import com.example.smarthomeapp.data.model.Service
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ServicesRepository @Inject constructor(
    firebaseFirestore: FirebaseFirestore
) {

    private val serviceCollection = firebaseFirestore.collection("services")

    suspend fun addService(service: Service): String {
        return try {
            serviceCollection.document(service.id).set(service).await()
            "Done"
        } catch (e: FirebaseFirestoreException) {
            Log.d("ServicesRepository", "addService: ${e.message}")
            e.message.toString()
        }
    }

    suspend fun getServices(): List<Service> {
        return serviceCollection.get().await().toObjects(Service::class.java)
    }

    suspend fun getService(id: String): Service? {
        return try {
            serviceCollection.document(id).get().await().toObject(Service::class.java)!!
        } catch (e: FirebaseFirestoreException) {
            Log.d("ServicesRepository", "getService: ${e.message}")
            null
        }
    }

    suspend fun deleteService(service: Service): String {
        return try {
            serviceCollection.document(service.id).delete().await()
            "Done"
        } catch (e: FirebaseFirestoreException) {
            Log.d("ServicesRepository", "deleteService: ${e.message}")
            e.message.toString()
        }
    }

}