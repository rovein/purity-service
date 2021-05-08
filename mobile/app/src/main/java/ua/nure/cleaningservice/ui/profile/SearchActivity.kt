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
import ua.nure.cleaningservice.network.JsonPlaceHolderApi
import ua.nure.cleaningservice.network.NetworkService
import ua.nure.cleaningservice.ui.auth.MenuActivity
import ua.nure.cleaningservice.ui.rva.SearchRVA
import ua.nure.cleaningservice.ui.util.LoadingDialog
import java.util.*

class SearchActivity : AppCompatActivity() {

    private var mUsers: MutableList<User>? = null
    private var mRecyclerView: RecyclerView? = null
    private var mApi: JsonPlaceHolderApi? = null
    private val loadingDialog = LoadingDialog(this@SearchActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        mRecyclerView = findViewById<View>(R.id.search_rv) as RecyclerView
        val mBackButton = findViewById<ImageButton>(R.id.back_btn)
        mApi = NetworkService.getInstance().apiService
        val llm = LinearLayoutManager(this)
        mRecyclerView!!.layoutManager = llm
        mRecyclerView!!.setHasFixedSize(true)
        mBackButton.setOnClickListener { v: View? ->
            navigateToScreen(MenuActivity::class.java)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        mUsers = ArrayList()
        initializeData()
    }

    private fun initializeData() {
        loadingDialog.start()
        val token = "Bearer " + User.getInstance().token
        mApi!!.getCleaningProviders(token).enqueue(searchCallback)
    }

    var searchCallback: Callback<ArrayList<User>> = object : Callback<ArrayList<User>> {
        override fun onResponse(call: Call<ArrayList<User>>,
                                response: Response<ArrayList<User>>) {
            if (!response.isSuccessful) {
                println(response.code())
                return
            }
            val companies = response.body()!!
            for (company in companies) {
                mUsers!!.add(
                    User(
                        company.id, company.name,
                        company.email, company.phoneNumber
                    )
                )
            }
            initializeAdapter()
        }

        override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
            println(t)
            Log.i(TAG, t.message!!)
            loadingDialog.dismiss()
        }
    }

    private fun initializeAdapter() {
        val adapter = SearchRVA(this, mUsers)
        mRecyclerView!!.adapter = adapter
        loadingDialog.dismiss()
    }

    private fun navigateToScreen(cls: Class<*>) {
        val intent = Intent(this@SearchActivity, cls)
        startActivity(intent)
    }

    companion object {
        private const val TAG = "SearchActivity"
    }
}
