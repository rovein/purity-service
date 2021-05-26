package ua.nure.cleaningservice.ui.edit

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import java.util.*

class EditPlacementActivity : AppCompatActivity() {
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
    private val loadingDialog = LoadingDialogUtil(this@EditPlacementActivity)

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
        labelTV.setText(R.string.edit_room)
        token = "Bearer " + User.getInstance().token
        mApi = NetworkService.getInstance().apiService
        room
        mCancelButton.setOnClickListener(View.OnClickListener { v: View? -> finish() })
        mSaveButton.setOnClickListener(View.OnClickListener { v: View? -> saveRoom() })
    }

    private val room: Unit
        get() {
            loadingDialog.start()
            mApi!!.getPlacement(token, intent.getIntExtra("rId", -1)).enqueue(roomCallBack)
        }
    var roomCallBack: Callback<Placement> = object : Callback<Placement> {
        override fun onResponse(call: Call<Placement>, response: Response<Placement>) {
            if (response.isSuccessful) {
                mRoomTypeET!!.setText(response.body()!!.placementType)
                mAreaET!!.setText(response.body()!!.area.toString())
                mFloorET!!.setText(String.format(Locale.getDefault(), "%d", response.body()!!.floor))
                mWinCountET!!.setText(String.format(Locale.getDefault(), "%d", response.body()!!.windowsCount))
                mPlacement = Placement(response.body()!!.id, response.body()!!.placementType, response.body()!!.floor, response.body()!!.windowsCount, response.body()!!.area, response.body()!!.lastCleaning, response.body()!!.smartDevice)
            }
            loadingDialog.dismiss()
        }

        override fun onFailure(call: Call<Placement>, t: Throwable) {
            Log.i(TAG, t.message!!)
            loadingDialog.dismiss()
        }
    }

    private fun saveRoom() {
        loadingDialog.start()
        mPlacement!!.placementType = mRoomTypeET!!.text.toString()
        mPlacement!!.floor = mFloorET!!.text.toString().toInt()
        mPlacement!!.windowsCount = mWinCountET!!.text.toString().toInt()
        mPlacement!!.area = mAreaET!!.text.toString().toFloat()
        val email = User.getInstance().email
        mApi!!.updatePlacement(token, email, mPlacement).enqueue(editRoomCallback)
    }

    var editRoomCallback: Callback<Placement?> = object : Callback<Placement?> {
        override fun onResponse(call: Call<Placement?>, response: Response<Placement?>) {
            if (response.isSuccessful) {
                println(response.body())
                loadingDialog.dismiss()
                val intent = Intent(this@EditPlacementActivity, MenuActivity::class.java)
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
        private const val TAG = "EditRoomActivity"
    }
}
