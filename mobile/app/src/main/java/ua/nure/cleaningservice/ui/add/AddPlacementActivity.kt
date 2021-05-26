package ua.nure.cleaningservice.ui.add

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
import ua.nure.cleaningservice.data.Placement
import ua.nure.cleaningservice.network.JsonPlaceHolderApi
import ua.nure.cleaningservice.network.NetworkService
import ua.nure.cleaningservice.ui.auth.MenuActivity
import ua.nure.cleaningservice.ui.util.LoadingDialogUtil
import java.text.SimpleDateFormat
import java.util.*

class AddPlacementActivity : AppCompatActivity() {
    lateinit var mCancelButton: Button
    lateinit var mSaveButton: Button
    var mPlacement: Placement? = null
    lateinit var labelTV: TextView
    var mRoomTypeET: EditText? = null
    var mFloorET: EditText? = null
    var mWinCountET: EditText? = null
    var mAreaET: EditText? = null
    private var mApi: JsonPlaceHolderApi? = null
    var token: String? = null
    private val loadingDialog = LoadingDialogUtil(this@AddPlacementActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_placement)
        mCancelButton = findViewById(R.id.cancel_add_room_btn)
        mSaveButton = findViewById(R.id.save_add_room_btn)
        labelTV = findViewById(R.id.add_room_label)
        mRoomTypeET = findViewById(R.id.add_type_input)
        mFloorET = findViewById(R.id.add_floor_input)
        mWinCountET = findViewById(R.id.add_win_count_input)
        mAreaET = findViewById(R.id.add_area_input)
        labelTV.setText(R.string.add_room)
        token = "Bearer " + User.getInstance().token
        mApi = NetworkService.getInstance().apiService
        mPlacement = Placement()
        mCancelButton.setOnClickListener { finish() }
        mSaveButton.setOnClickListener { addRoom() }
    }

    private fun addRoom() {
        mPlacement!!.placementType = mRoomTypeET!!.text.toString()
        mPlacement!!.floor = mFloorET!!.text.toString().toInt()
        mPlacement!!.windowsCount = mWinCountET!!.text.toString().toInt()
        mPlacement!!.area = mAreaET!!.text.toString().toFloat()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX")
        mPlacement!!.lastCleaning = dateFormat.format(Date())
        val email = User.getInstance().email
        loadingDialog.start()
        mApi!!.addPlacement(token, email, mPlacement).enqueue(editRoomCallback)
    }

    var editRoomCallback: Callback<Placement?> = object : Callback<Placement?> {
        override fun onResponse(call: Call<Placement?>, response: Response<Placement?>) {
            if (response.isSuccessful) {
                println(response.body())
                loadingDialog.dismiss()
                val intent = Intent(this@AddPlacementActivity, MenuActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        override fun onFailure(call: Call<Placement?>, t: Throwable) {
            Log.i(TAG, t.message!!)
            loadingDialog.dismiss()
        }
    }

    companion object {
        private const val TAG = "AddRoomActivity"
    }
}
