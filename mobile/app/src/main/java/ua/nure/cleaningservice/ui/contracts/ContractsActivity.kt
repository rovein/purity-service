package ua.nure.cleaningservice.ui.contracts

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.cleaningservice.R
import ua.nure.cleaningservice.data.User
import ua.nure.cleaningservice.data.Contract
import ua.nure.cleaningservice.network.JsonPlaceHolderApi
import ua.nure.cleaningservice.network.NetworkService
import ua.nure.cleaningservice.ui.auth.MenuActivity
import ua.nure.cleaningservice.ui.rva.ContractsRVA
import ua.nure.cleaningservice.ui.util.LoadingDialog
import java.util.*

class ContractsActivity : AppCompatActivity() {
    private var mContracts: MutableList<Contract>? = null
    private lateinit var mRecyclerView: RecyclerView
    private var mApi: JsonPlaceHolderApi? = null
    private lateinit var mBackButton: ImageButton
    private val loadingDialog = LoadingDialog(this@ContractsActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contracts)
        mRecyclerView = findViewById(R.id.contracts_rv)
        mBackButton = findViewById(R.id.back_btn)
        mApi = NetworkService.getInstance().apiService
        val llm = LinearLayoutManager(this)
        mRecyclerView.layoutManager = llm
        mRecyclerView.setHasFixedSize(true)
        mBackButton.setOnClickListener(View.OnClickListener {
            navigateToMenuScreen()
            finish()
        })
    }

    override fun onResume() {
        super.onResume()
        initializeData()
    }

    private fun initializeData() {
        loadingDialog.start()
        mContracts = ArrayList()
        val company = User.getInstance()
        val email = company.email
        val token = "Bearer " + company.token
        val role = company.userRole
        if (role == getString(R.string.cleaning_provider)) {
            mApi!!.getCleaningProviderContracts(token, email).enqueue(contractsCallback)
        } else if (role == getString(R.string.placement_owner)) {
            mApi!!.getPlacementOwnerContracts(token, email).enqueue(contractsCallback)
        }
    }

    private var contractsCallback: Callback<ArrayList<Contract>> = object : Callback<ArrayList<Contract>> {
        override fun onResponse(call: Call<ArrayList<Contract>>,
                                response: Response<ArrayList<Contract>>) {
            if (!response.isSuccessful) {
                println(response.code())
                loadingDialog.dismiss()
                return
            }
            val contracts: List<Contract> = response.body()!!
            for (contract in contracts) {
                mContracts!!.add(Contract(contract.date,
                        contract.price, contract.serviceName,
                        contract.placementId, contract.providerServiceId,
                        contract.id, contract.cleaningProviderName,
                        contract.placementOwnerName))
            }
            initializeAdapter()
        }

        override fun onFailure(call: Call<ArrayList<Contract>>, t: Throwable) {
            println(t)
            Log.i(TAG, t.message!!)
            loadingDialog.dismiss()
        }
    }

    private fun initializeAdapter() {
        val adapter = ContractsRVA(this, mContracts)
        mRecyclerView.adapter = adapter
        loadingDialog.dismiss()
    }

    private fun navigateToMenuScreen() {
        val intent = Intent(this@ContractsActivity, MenuActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "ContractsActivity"
    }
}
