package io.github.drumber.kitsune.domain.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.drumber.kitsune.domain.model.database.LocalLibraryEntryModification

@Dao
interface LibraryEntryModificationDao {

    @Query("SELECT * FROM library_entries_modifications")
    fun getAllLibraryEntryModificationsLiveData(): LiveData<List<LocalLibraryEntryModification>>

    @Query("SELECT * FROM library_entries_modifications")
    suspend fun getAllLibraryEntryModifications(): List<LocalLibraryEntryModification>

    @Query("SELECT * FROM library_entries_modifications WHERE id = :id")
    suspend fun getLibraryEntryModification(id: String): LocalLibraryEntryModification?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingle(libraryEntryModification: LocalLibraryEntryModification)

    @Update
    suspend fun updateLibraryEntryModification(libraryEntryModification: LocalLibraryEntryModification)

    @Delete
    suspend fun deleteLibraryEntryModification(libraryEntryModification: LocalLibraryEntryModification)

    @Query("DELETE FROM library_entries_modifications")
    suspend fun clearLibraryEntryModifications()

}