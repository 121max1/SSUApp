package com.example.ssuapp.repository

import androidx.lifecycle.LiveData
import com.example.ssuapp.data.local.WalletDao
import com.example.ssuapp.model.Wallet

class WalletRepository(private val walletDao: WalletDao) {

    fun getWallets(): List<Wallet> {
        return walletDao.getAllWallets()
    }

    suspend fun addWallet(wallet: Wallet) {
        walletDao.insertWallet(wallet)
    }

    suspend fun deleteWallet(wallet: Wallet) {
        walletDao.deleteWallet(wallet)
    }
}