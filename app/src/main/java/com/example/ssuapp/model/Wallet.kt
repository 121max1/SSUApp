package com.example.ssuapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wallets")
data class Wallet(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "cryptocurrency")
    val cryptocurrency: Cryptocurrency,

    @ColumnInfo(name = "address")
    val address: String,

    @ColumnInfo(name = "name")
    val name: String
)