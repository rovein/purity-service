package ua.nure.cleaningservice.ui.profile

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
import ua.nure.cleaningservice.data.Service
import ua.nure.cleaningservice.network.JsonPlaceHolderApi
import ua.nure.cleaningservice.network.NetworkService
import ua.nure.cleaningservice.ui.add.AddServicesActivity
import ua.nure.cleaningservice.ui.auth.MenuActivity
import ua.nure.cleaningservice.ui.rva.ServicesRVA
import ua.nure.cleaningservice.ui.util.LoadingDialog
import java.util.*

class ServicesActivity : AppCompatActivity() {

    private var mServices: MutableList<Service>? = null
    private var mRecyclerView: RecyclerView? = null
    private var mApi: JsonPlaceHolderApi? = null
    private val loadingDialog = LoadingDialog(this@ServicesActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_services)
        mRecyclerView = findViewById<View>(R.id.services_rv) as RecyclerView
        val mBackButton = findViewById<ImageButton>(R.id.back_btn)
        val mAddButton = findViewById<ImageButton>(R.id.add_service_btn)
        mApi = NetworkService.getInstance().apiService
        val llm = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = llm
        mRecyclerView!!.setHasFixedSize(true)
        mBackButton.setOnClickListener { v: View? ->
            navigateToScreen(MenuActivity::class.java)
            finish()
        }
        mAddButton.setOnClickListener { v: View? ->
            navigateToScreen(AddServicesActivity::class.java)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        mServices = ArrayList()
        initializeData()
    }

    private fun initializeData() {
        loadingDialog.start()
        val email = User.getInstance().email
        val token = "Bearer " + User.getInstance().token
        mApi!!.getServiceData(token, email).enqueue(serviceCallback)
    }

    var serviceCallback: Callback<ArrayList<Service>> = object : Callback<ArrayList<Service>> {
        override fun onResponse(call: Call<ArrayList<Service>>,
                                response: Response<ArrayList<Service>>) {
            if (!response.isSuccessful) {
                println(response.code())
                loadingDialog.dismiss()
                return
            }
            val serviceList = response.body()!!
            for (service in serviceList) {
                mServices!!.add(Service(service.id, service.name,
                        service.description, service.minArea,
                        service.maxArea, service.placementType,
                        service.pricePerMeter))
            }
            initializeAdapter()
        }

        override fun onFailure(call: Call<ArrayList<Service>>, t: Throwable) {
            println(t)
            Log.i(TAG, t.message!!)
            loadingDialog.dismiss()
        }
    }

    private fun initializeAdapter() {
        val adapter = ServicesRVA(this, mServices)
        mRecyclerView!!.adapter = adapter
        loadingDialog.dismiss()
    }

    private fun navigateToScreen(cls: Class<*>) {
        val intent = Intent(this@ServicesActivity, cls)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "ServicesActivity"
    }
}
