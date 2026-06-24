package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "saved_designs")
data class SavedDesign(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val furnitureType: String,
    val woodType: String,
    val finishType: String,
    val dimensions: String,
    val hardwareType: String,
    val estimatedPrice: Double,
    val createdAt: Long = System.currentTimeMillis()
) : Serializable

@Entity(tableName = "inquiries")
data class Inquiry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val spaceType: String,
    val requirements: String,
    val budgetRange: String,
    val clientName: String,
    val clientPhone: String,
    val status: String = "In Review",
    val createdAt: Long = System.currentTimeMillis()
) : Serializable

@Entity(tableName = "project_updates")
data class ProjectUpdate(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectTitle: String,
    val statusMessage: String,
    val progressPercent: Int, // e.g. 75 for 75%
    val currentStepName: String, // e.g. "Carpentry Works"
    val totalSteps: Int = 6,
    val currentStepIndex: Int = 4, // 1-indexed
    val clientName: String = "Damodar Suthar",
    val siteAddress: String = "Bandra West, Mumbai",
    val designerName: String = "Vinod Bhatia",
    val estimatedHandover: String = "Aug 15, 2026",
    val updatedAt: Long = System.currentTimeMillis()
) : Serializable

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val message: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) : Serializable
