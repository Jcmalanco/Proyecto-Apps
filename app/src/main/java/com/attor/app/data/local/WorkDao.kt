package com.attor.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.attor.app.data.Work

@Dao
interface WorkDao {
    @Query("SELECT * FROM works")
    suspend fun getAllWorks(): List<Work>

    @Query("SELECT * FROM works WHERE id = :id")
    suspend fun getWorkById(id: String): Work?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorks(works: List<Work>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWork(work: Work)

    @Query("SELECT COUNT(*) FROM works")
    suspend fun getCount(): Int

    @Query("SELECT * FROM works WHERE ownerName = :ownerName ORDER BY title ASC")
    suspend fun getWorksByOwner(ownerName: String): List<Work>

    @Query("DELETE FROM works WHERE id = :id")
    suspend fun deleteById(id: String)
}
