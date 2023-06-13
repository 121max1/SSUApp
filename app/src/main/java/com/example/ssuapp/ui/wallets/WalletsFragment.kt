package com.example.ssuapp.ui.wallets

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ssuapp.R
import com.example.ssuapp.model.Cryptocurrency
import com.example.ssuapp.model.Wallet
import com.example.ssuapp.repository.WalletRepository
import com.example.ssuapp.service.Interfaces.CryptoBalancesService
import com.example.ssuapp.service.Interfaces.WalletsService
import com.example.ssuapp.ui.domain.WalletModel
import com.example.ssuapp.ui.adapters.WalletListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class WalletsFragment : Fragment(), CoroutineScope by MainScope() {

    private lateinit var walletsRecyclerView: RecyclerView
    private lateinit var adapter: WalletListAdapter
    private lateinit var addWalletButton: ImageButton

    private val viewModel: WalletsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_wallets, container, false)

        walletsRecyclerView = root.findViewById(R.id.wallets_recycler_view)
        walletsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        val dividerDrawable = context?.let { ContextCompat.getDrawable(it, R.drawable.divider) }
        val dividerItemDecoration = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
        dividerDrawable?.let { dividerItemDecoration.setDrawable(it) }
        walletsRecyclerView.addItemDecoration(dividerItemDecoration)

        adapter = WalletListAdapter(emptyList())
        walletsRecyclerView.adapter = adapter

        viewModel.wallets.observe(viewLifecycleOwner) { wallets ->
            adapter.updateWallets(wallets)
        }

        addWalletButton = root.findViewById(R.id.btn_add_wallet)
        addWalletButton.setOnClickListener {
            showAddWalletDialog()
        }

        return root
    }

    private fun showAddWalletDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_add_wallet, null)

        dialogBuilder.setView(dialogView)
            .setTitle("Add Wallet")
            .setPositiveButton("OK") { _, _ ->
                val cryptocurrencySpinner =
                    dialogView.findViewById<Spinner>(R.id.spinner_cryptocurrency)
                val addressEditText = dialogView.findViewById<EditText>(R.id.edit_text_address)
                val nameEditText = dialogView.findViewById<EditText>(R.id.edit_text_name)
                val cryptocurrency = cryptocurrencySpinner.selectedItem.toString()
                val address = addressEditText.text.toString()
                val name = nameEditText.text.toString()

                if (cryptocurrency.isNotBlank() && address.isNotBlank()) {
                    runBlocking {
                        viewModel.addWallet(cryptocurrency = GetCryptocurrencyBySpinnerValue(cryptocurrency), address, name);
                        viewModel.loadWallets()
                    }
                }
            }
            .setNegativeButton("Cancel", null)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    private fun GetCryptocurrencyBySpinnerValue(name: String) : Cryptocurrency{
        return when (name) {
            "BTC" -> Cryptocurrency.BITCOIN
            "ETH" -> Cryptocurrency.ETHEREUM
            "LTC" -> Cryptocurrency.LITECOIN
            else -> Cryptocurrency.BITCOIN
        }
    }
}