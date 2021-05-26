package ua.nure.cleaningservice.ui.contracts

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.cleaningservice.R
import ua.nure.cleaningservice.data.Contract
import ua.nure.cleaningservice.data.Placement
import ua.nure.cleaningservice.data.Service
import ua.nure.cleaningservice.data.User
import ua.nure.cleaningservice.network.JsonPlaceHolderApi
import ua.nure.cleaningservice.network.NetworkService
import ua.nure.cleaningservice.ui.util.LoadingDialog
import java.text.SimpleDateFormat
import java.util.*


class SignContractActivity : AppCompatActivity() {

    private var mApi: JsonPlaceHolderApi? = null
    lateinit var mBack: ImageView
    lateinit var mSignContractButton: Button
    lateinit var mServicesSpinner: Spinner
    lateinit var mRoomsSpinner: Spinner
    lateinit var timeButton: Button
    lateinit var dateButton: Button
    var mContract: Contract? = null
    var roomId = 0
    var serviceId = 0
    lateinit var servicesId: IntArray
    var mServices: List<Service>? = null
    var mPlacements: List<Placement>? = null
    var token: String? = null
    private val loadingDialog = LoadingDialog(this@SignContractActivity)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_contract_activity)
        mBack = findViewById(R.id.back_btn)
        mBack.setOnClickListener{
            navigateToMenuScreen()
            finish()
        }

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

        timeButton = findViewById(R.id.timeButton)
        timeButton.setOnClickListener { v -> setTime(v) }

        dateButton = findViewById(R.id.dateButton)
        dateButton.setOnClickListener { v -> setDate(v) }

        currentDateTime= findViewById(R.id.currentDateTime)
        setInitialDateTime()
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun signContract() {
        loadingDialog.start()
        mContract!!.providerServiceId = servicesId[serviceId]
        mContract!!.placementId = roomId
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", resources.configuration.locales.get(0))
        mContract!!.date = dateFormat.format(Date(dateAndTime.timeInMillis))
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

    fun setDate(v: View?) {
        DatePickerDialog(
            this@SignContractActivity, d,
            dateAndTime[Calendar.YEAR],
            dateAndTime[Calendar.MONTH],
            dateAndTime[Calendar.DAY_OF_MONTH]
        )
            .show()
    }

    fun setTime(v: View?) {
        TimePickerDialog(
            this@SignContractActivity, t,
            dateAndTime[Calendar.HOUR_OF_DAY],
            dateAndTime[Calendar.MINUTE], true
        )
            .show()
    }

    var d =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            dateAndTime[Calendar.YEAR] = year
            dateAndTime[Calendar.MONTH] = monthOfYear
            dateAndTime[Calendar.DAY_OF_MONTH] = dayOfMonth
            setInitialDateTime()
        }

    var t =
        OnTimeSetListener { view, hourOfDay, minute ->
            dateAndTime[Calendar.HOUR_OF_DAY] = hourOfDay
            dateAndTime[Calendar.MINUTE] = minute
            setInitialDateTime()
        }

    private fun setInitialDateTime() {
        currentDateTime!!.text = DateUtils.formatDateTime(
            this,
            dateAndTime.timeInMillis,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
                    or DateUtils.FORMAT_SHOW_TIME
        )
    }

    private fun navigateToMenuScreen() {
        val intent = Intent(this@SignContractActivity, MenuActivity::class.java)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "SignContractActivity"
    }
}
