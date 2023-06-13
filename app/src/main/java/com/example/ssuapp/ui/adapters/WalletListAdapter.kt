package com.example.ssuapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ssuapp.R
import com.example.ssuapp.ui.domain.WalletModel

class WalletListAdapter(private var wallets: List<WalletModel>) : RecyclerView.Adapter<WalletListAdapter.WalletViewHolder>() {

    inner class WalletViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val currencyIcon: ImageView = itemView.findViewById(R.id.currency_icon)
        val currencyName: TextView = itemView.findViewById(R.id.currency_name)
        val currencyBalance: TextView = itemView.findViewById(R.id.currency_balance)
        val currencyBalanceUsd: TextView = itemView.findViewById(R.id.currency_balance_usd)
        val walletItemLayout: LinearLayout = itemView.findViewById(R.id.wallet_item_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.wallet_item, parent, false)
        return WalletViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        val wallet = wallets[position]
        holder.currencyIcon.setImageResource(wallet.icon)
        holder.currencyName.text = wallet.name
        holder.currencyBalance.text = wallet.balance
        holder.currencyBalanceUsd.text = wallet.balanceUsd
        holder.walletItemLayout.setBackgroundResource(wallet.background)
    }

    override fun getItemCount() = wallets.size

    fun updateWallets(newWallets: List<WalletModel>) {
        wallets = newWallets
        notifyDataSetChanged()
    }
}