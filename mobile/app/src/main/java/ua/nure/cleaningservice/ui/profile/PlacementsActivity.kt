package ua.nure.cleaningservice.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.cleaningservice.R
import ua.nure.cleaningservice.data.Placement
import ua.nure.cleaningservice.data.User
import ua.nure.cleaningservice.network.JsonPlaceHolderApi
import ua.nure.cleaningservice.network.NetworkService
import ua.nure.cleaningservice.ui.add.AddPlacementActivity
import ua.nure.cleaningservice.ui.auth.MenuActivity
import ua.nure.cleaningservice.ui.edit.EditPlacementActivity
import ua.nure.cleaningservice.ui.rva.PlacementsRVA
import ua.nure.cleaningservice.ui.util.LoadingDialog
import java.util.*


class PlacementsActivity : AppCompatActivity() {
    private lateinit var mPlacements: MutableList<Placement>
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button
    private var mApi: JsonPlaceHolderApi? = null
    private val loadingDialog = LoadingDialog(this@PlacementsActivity)

    private var placementId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_placements)
        mRecyclerView = findViewById(R.id.rooms_rv)
        val backButton = findViewById<ImageButton>(R.id.back_btn)
        val addButton = findViewById<ImageButton>(R.id.add_room_btn)
        mApi = NetworkService.getInstance().apiService
        val llm = LinearLayoutManager(this)
        mRecyclerView.layoutManager = llm
        mRecyclerView.setHasFixedSize(true)
        backButton.setOnClickListener { v: View? ->
            navigateToScreen(MenuActivity::class.java)
            finish()
        }
        addButton.setOnClickListener { v: View? ->
            navigateToScreen(AddPlacementActivity::class.java)
            finish()
        }
        editButton = findViewById(R.id.edit_room_btn)
        editButton.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.enter_placement_number)

            val input = EditText(this)
            input.inputType = InputType.TYPE_CLASS_TEXT
            builder.setView(input)

            builder.setPositiveButton("OK") { _, _ -> editRoom(Integer.parseInt(input.text.toString())) }
            builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }

            builder.show()
        }

        deleteButton = findViewById(R.id.delete_room_btn)
        deleteButton.setOnClickListener {

        }
    }

    override fun onResume() {
        super.onResume()
        initializeData()
    }

    private fun initializeData() {
        loadingDialog.start()
        mPlacements = ArrayList()
        val email = User.getInstance().email
        val token = "Bearer " + User.getInstance().token
        mApi!!.getPlacementData(token, email).enqueue(roomCallback)
    }

    var roomCallback: Callback<ArrayList<Placement>> = object : Callback<ArrayList<Placement>> {
        override fun onResponse(call: Call<ArrayList<Placement>>, response: Response<ArrayList<Placement>>) {
            if (!response.isSuccessful) {
                println(response.code())
                loadingDialog.dismiss()
                return
            }
            val placementList = response.body()!!
            for (placement in placementList) {
                mPlacements!!.add(Placement(placement.id, placement.placementType, placement
                        .floor, placement.windowsCount, placement.area, placement
                        .lastCleaning, placement.smartDevice))
            }
            initializeAdapter()
        }

        override fun onFailure(call: Call<ArrayList<Placement>>, t: Throwable) {
            println(t)
            Log.i(TAG, t.message!!)
            loadingDialog.dismiss()
        }
    }

    private fun initializeAdapter() {
        val adapter = PlacementsRVA(this, mPlacements)
        mRecyclerView.adapter = adapter
        loadingDialog.dismiss()
    }

    private fun navigateToScreen(cls: Class<*>) {
        val intent = Intent(this@PlacementsActivity, cls)
        startActivity(intent)
    }

    private fun editRoom(id: Int) {
        val intent = Intent(this, EditPlacementActivity::class.java)
        intent.putExtra("rId", id)
        startActivity(intent)
    }

    private fun deleteRoom() {
        loadingDialog.start()
        mApi!!.deletePlacement(User.getInstance().token, 1).enqueue(deleteCallback)
    }

    var deleteCallback: Callback<Placement?> = object : Callback<Placement?> {
        override fun onResponse(call: Call<Placement?>, response: Response<Placement?>) {
            if (response.isSuccessful) {
                println(response.body())
                loadingDialog.dismiss()
            }
        }

        override fun onFailure(call: Call<Placement?>, t: Throwable) {
            println(t)
            loadingDialog.dismiss()
        }
    }

    companion object {
        private const val TAG = "ServicesActivity"
    }


}
