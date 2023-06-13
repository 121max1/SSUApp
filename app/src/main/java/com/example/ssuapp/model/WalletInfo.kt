package com.example.ssuapp.model

data class WalletInfo(
    var id: Long,
    var cryptocurrency: Cryptocurrency,
    var address: String,
    var balance: String,
    var balanceUsd: String,
    var name: String
)