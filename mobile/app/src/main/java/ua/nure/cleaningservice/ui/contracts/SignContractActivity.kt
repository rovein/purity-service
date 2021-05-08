package ua.nure.cleaningservice.ui.contracts

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.cleaningservice.R
import ua.nure.cleaningservice.data.User
import ua.nure.cleaningservice.data.Contract
import ua.nure.cleaningservice.data.Placement
import ua.nure.cleaningservice.data.Service
import ua.nure.cleaningservice.network.JsonPlaceHolderApi
import ua.nure.cleaningservice.network.NetworkService
import ua.nure.cleaningservice.ui.util.LoadingDialog
import java.text.SimpleDateFormat
import java.util.*

class SignContractActivity : AppCompatActivity() {

    private val mContracts: List<Contract>? = null
    private var mApi: JsonPlaceHolderApi? = null
    var mBack: ImageView? = null
    lateinit var mSignContractButton: Button
    lateinit var mServicesSpinner: Spinner
    lateinit var mRoomsSpinner: Spinner
    var mContract: Contract? = null
    var roomId = 0
    var serviceId = 0
    lateinit var servicesId: IntArray
    var mServices: List<Service>? = null
    var mPlacements: List<Placement>? = null
    var token: String? = null
    private val loadingDialog = LoadingDialog(this@SignContractActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_contract_activity)
        mBack = findViewById(R.id.back_btn)
        mSignContractButton = findViewById(R.id.sign_contract_btn)
        mSignContractButton.setOnClickListener(View.OnClickListener { v: View? -> signContract() })
        mApi = NetworkService.getInstance().apiService
        mContract = Contract()
        mServicesSpinner = findViewById(R.id.get_service_spinner)
        mServicesSpinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                serviceId = position
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
        mRoomsSpinner = findViewById(R.id.get_room_spinner)
        mRoomsSpinner.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                roomId = mPlacements!![position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        data
    }

    private val data: Unit
        get() {
            loadingDialog.start()
            val email = User.getInstance().email
            token = "Bearer " + User.getInstance().token
            mApi!!.getServiceData(token, intent.getStringExtra("cEmail")).enqueue(serviceCallback)
            mApi!!.getPlacementData(token, email).enqueue(roomsCallback)
        }

    var serviceCallback: Callback<ArrayList<Service>?> = object : Callback<ArrayList<Service>?> {
        override fun onResponse(call: Call<ArrayList<Service>?>, response: Response<ArrayList<Service>?>) {
            if (!response.isSuccessful) {
                println(response.code())
                loadingDialog.dismiss()
                return
            }
            mServices = response.body()
            val names = arrayOfNulls<String>(mServices!!.size)
            servicesId = IntArray(mServices!!.size)
            var i = 0
            for (service in mServices!!) {
                names[i] = service.name
                servicesId[i] = service.id
                i++
            }
            initServicesAdapter(names)
        }

        override fun onFailure(call: Call<ArrayList<Service>?>, t: Throwable) {
            println(t)
            Log.i(TAG, t.message!!)
            loadingDialog.dismiss()
        }
    }
    var roomsCallback: Callback<ArrayList<Placement>?> = object : Callback<ArrayList<Placement>?> {
        override fun onResponse(call: Call<ArrayList<Placement>?>, response: Response<ArrayList<Placement>?>) {
            if (!response.isSuccessful) {
                println(response.code())
                loadingDialog.dismiss()
                return
            }
            mPlacements = response.body()
            val ids = arrayOfNulls<Int>(mPlacements!!.size)
            var i = 0
            for (placement in mPlacements!!) {
                ids[i] = placement.id
                i++
            }
            initRoomsAdapter(ids)
        }

        override fun onFailure(call: Call<ArrayList<Placement>?>, t: Throwable) {
            println(t)
            Log.i(TAG, t.message!!)
            loadingDialog.dismiss()
        }
    }

    private fun initServicesAdapter(servicesData: Array<String?>) {
        val adapter: ArrayAdapter<String?> = ArrayAdapter<String?>(this,
                android.R.layout.simple_list_item_1, servicesData)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mServicesSpinner.adapter = adapter
        loadingDialog.dismiss()
    }

    private fun initRoomsAdapter(roomsData: Array<Int?>) {
        val adapter: ArrayAdapter<Int?> = ArrayAdapter<Int?>(this,
                android.R.layout.simple_list_item_1, roomsData)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mRoomsSpinner.adapter = adapter
        loadingDialog.dismiss()
    }

    private fun signContract() {
        loadingDialog.start()
        mContract!!.providerServiceId = servicesId[serviceId]
        mContract!!.placementId = roomId
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        mContract!!.date = dateFormat.format(Date())
        NetworkService.getInstance()
                .apiService
                .signContract(token, mContract)
                .enqueue(signContractCallback)
    }

    var signContractCallback: Callback<Contract?> = object : Callback<Contract?> {
        override fun onResponse(call: Call<Contract?>, response: Response<Contract?>) {
            if (!response.isSuccessful) {
                println(response.code())
                loadingDialog.dismiss()
                return
            }
            loadingDialog.dismiss()
            finish()
        }

        override fun onFailure(call: Call<Contract?>, t: Throwable) {
            println(t)
            Log.i(TAG, t.message!!)
            loadingDialog.dismiss()
        }
    }

    companion object {
        private const val TAG = "SignContractActivity"
    }
}
