package ua.nure.cleaningservice.ui.edit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.cleaningservice.R
import ua.nure.cleaningservice.data.User
import ua.nure.cleaningservice.data.Service
import ua.nure.cleaningservice.network.JsonPlaceHolderApi
import ua.nure.cleaningservice.network.NetworkService
import ua.nure.cleaningservice.ui.profile.ServicesActivity
import ua.nure.cleaningservice.ui.util.LoadingDialogUtil
import java.util.*

class EditServicesActivity : AppCompatActivity() {

    lateinit var mCancelButton: Button
    lateinit var mSaveButton: Button
    var mService: Service? = null
    lateinit var labelTV: TextView
    var mNameET: EditText? = null
    var mRoomTypeET: EditText? = null
    var mDescET: EditText? = null
    var mMinAreaET: EditText? = null
    var mMaxAreaET: EditText? = null
    var mPricePMET: EditText? = null
    private var mApi: JsonPlaceHolderApi? = null
    var token: String? = null
    private val loadingDialog = LoadingDialogUtil(this@EditServicesActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_service)
        mCancelButton = findViewById(R.id.cancel_add_service_btn)
        mSaveButton = findViewById(R.id.save_add_service_btn)
        labelTV = findViewById(R.id.add_service_label)
        mNameET = findViewById(R.id.add_name_input)
        mRoomTypeET = findViewById(R.id.add_type_input)
        mDescET = findViewById(R.id.add_desc_input)
        mMinAreaET = findViewById(R.id.add_min_area_input)
        mMaxAreaET = findViewById(R.id.add_max_area_input)
        mPricePMET = findViewById(R.id.add_price_pm_input)
        labelTV.setText(R.string.edit_service)
        token = "Bearer " + User.getInstance().token
        mApi = NetworkService.getInstance().apiService
        service
        mCancelButton.setOnClickListener { finish() }
        mSaveButton.setOnClickListener { saveServices() }
    }

    private val service: Unit
        get() {
            loadingDialog.start()
            mApi!!.getService(token, intent.getIntExtra("sId", -1)).enqueue(serviceCallBack)
        }
    var serviceCallBack: Callback<Service> = object : Callback<Service> {
        override fun onResponse(call: Call<Service>, response: Response<Service>) {
            if (response.isSuccessful) {
                mNameET!!.setText(response.body()!!.name)
                mRoomTypeET!!.setText(response.body()!!.placementType)
                mDescET!!.setText(response.body()!!.description)
                mMinAreaET!!.setText(String.format(Locale.getDefault(), "%d", response.body()!!.minArea))
                mMaxAreaET!!.setText(String.format(Locale.getDefault(), "%d", response.body()!!.maxArea))
                mPricePMET!!.setText(response.body()!!.pricePerMeter.toString())
                mService = Service(response.body()!!.id, response.body()!!.name, response.body()!!.description, response.body()!!.minArea, response.body()!!.maxArea, response.body()!!.placementType, response.body()!!.pricePerMeter)
            }
            loadingDialog.dismiss()
        }

        override fun onFailure(call: Call<Service>, t: Throwable) {
            Log.i(TAG, t.message!!)
            loadingDialog.dismiss()
        }
    }

    private fun saveServices() {
        mService!!.name = mNameET!!.text.toString()
        mService!!.placementType = mRoomTypeET!!.text.toString()
        mService!!.description = mDescET!!.text.toString()
        mService!!.minArea = mMinAreaET!!.text.toString().toInt()
        mService!!.maxArea = mMaxAreaET!!.text.toString().toInt()
        mService!!.pricePerMeter = mPricePMET!!.text.toString().toFloat()
        val email = User.getInstance().email
        loadingDialog.start()
        mApi!!.updateService(token, email, mService).enqueue(editServiceCallback)
    }

    var editServiceCallback: Callback<Service?> = object : Callback<Service?> {
        override fun onResponse(call: Call<Service?>, response: Response<Service?>) {
            if (response.isSuccessful) {
                println(response.body())
                loadingDialog.dismiss()
                val intent = Intent(this@EditServicesActivity, ServicesActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        override fun onFailure(call: Call<Service?>, t: Throwable) {
            Log.i(TAG, t.message!!)
            loadingDialog.dismiss()
            Toast.makeText(
                this@EditServicesActivity,
                t.message,
                Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val TAG = "EditServicesActivity"
    }
}
