package com.example.ssuapp.di

import android.app.Application
import androidx.room.Room
import com.example.ssuapp.data.local.WalletDatabase
import com.example.ssuapp.data.local.WalletDao
import com.example.ssuapp.repository.WalletRepository
import com.example.ssuapp.service.CryptoBalancesServiceImpl
import com.example.ssuapp.service.CryptoPricesServiceImpl
import com.example.ssuapp.service.Interfaces.CryptoBalancesService
import com.example.ssuapp.service.Interfaces.CryptoPricesService
import com.example.ssuapp.service.Interfaces.WalletsService
import com.example.ssuapp.service.WalletServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule// Empty constructor
    () {

    @Singleton
    @Provides
    fun provideWalletDatabase(application: Application): WalletDatabase {
        return Room.databaseBuilder(
            application,
            WalletDatabase::class.java,
            "wallet_database"
        ).allowMainThreadQueries().build()
    }

    @Singleton
    @Provides
    fun provideWalletRepository(walletDao: WalletDao): WalletRepository {
        return WalletRepository(walletDao)
    }

    @Singleton
    @Provides
    fun provideWalletDao(walletDatabase: WalletDatabase): WalletDao {
        return walletDatabase.walletDao()
    }

    @Singleton
    @Provides
    fun provideCryptoBalancesService(): CryptoBalancesService {
        return CryptoBalancesServiceImpl()
    }

    @Singleton
    @Provides
    fun provideCryptoPricesService(): CryptoPricesService{
        return CryptoPricesServiceImpl()
    }

    @Singleton
    @Provides
    fun provideWalletsService(
        walletRepository: WalletRepository,
        cryptoBalancesService: CryptoBalancesService,
        cryptoPricesService: CryptoPricesService
    ): WalletsService {
        return WalletServiceImpl(
            walletRepository,
            cryptoBalancesService,
            cryptoPricesService
        )
    }
}