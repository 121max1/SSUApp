package com.example.ssuapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ssuapp.model.Wallet

@Dao
interface WalletDao {
    @Query("SELECT * FROM wallets")
    fun getAllWallets(): List<Wallet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: Wallet)

    @Delete
    suspend fun deleteWallet(wallet: Wallet)
}