package com.example.ssuapp.service.Interfaces

import com.example.ssuapp.model.Cryptocurrency
import com.example.ssuapp.model.Wallet
import com.example.ssuapp.model.WalletInfo

interface  WalletsService {
    suspend fun getInfoAboutWallets() : List<WalletInfo>

    suspend fun addWallet(wallet: Wallet)

    suspend fun deleteWallet(wallet: Wallet)

    suspend fun getPriceByCryptocurrency(cryptocurrency: Cryptocurrency, wallet: String) : Double
}