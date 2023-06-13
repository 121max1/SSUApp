package com.example.ssuapp.ui.domain

data class WalletModel(
    val icon: Int,
    val background: Int,
    val name: String,
    val balance: String,
    val balanceUsd: String,
    val cryptocurrency: String,
    val address: String
)