package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    // Saved Designs
    @Query("SELECT * FROM saved_designs ORDER BY createdAt DESC")
    fun getAllSavedDesigns(): Flow<List<SavedDesign>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedDesign(design: SavedDesign)

    @Delete
    suspend fun deleteSavedDesign(design: SavedDesign)

    // Inquiries
    @Query("SELECT * FROM inquiries ORDER BY createdAt DESC")
    fun getAllInquiries(): Flow<List<Inquiry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInquiry(inquiry: Inquiry)

    // Project Updates
    @Query("SELECT * FROM project_updates ORDER BY updatedAt DESC")
    fun getProjectUpdates(): Flow<List<ProjectUpdate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjectUpdate(update: ProjectUpdate)

    @Query("DELETE FROM project_updates")
    suspend fun clearProjectUpdates()

    // Chat Messages
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getChatMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatMessage(message: ChatMessage)
}
