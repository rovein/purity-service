package ua.nure.cleaningservice.ui.profile

import android.content.Context
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
import ua.nure.cleaningservice.data.Address
import ua.nure.cleaningservice.data.User
import ua.nure.cleaningservice.network.NetworkService
import ua.nure.cleaningservice.ui.auth.SignInActivity
import ua.nure.cleaningservice.ui.util.LoadingDialog

class ProfileActivity : AppCompatActivity() {
    var mNameTV: TextView? = null
    var mEmailTV: TextView? = null
    var mPhoneTV: TextView? = null
    var mCountryTV: TextView? = null
    var mCityTV: TextView? = null
    var mStreetTV: TextView? = null
    var mHouseTV: TextView? = null
    var mNameET: EditText? = null
    var mEmailET: EditText? = null
    var mPhoneET: EditText? = null
    var mCountryET: EditText? = null
    var mCityET: EditText? = null
    var mStreetET: EditText? = null
    var mHouseET: EditText? = null
    var mPasswordET: EditText? = null
    lateinit var mEditButton: Button
    lateinit var mBack: ImageView
    lateinit var mLogOut: ImageView
    var context: Context? = null
    var token: String? = null
    private val loadingDialog = LoadingDialog(this@ProfileActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        init()
        data
    }

    private val data: Unit
        get() {
            loadingDialog.start()
            val callback: Callback<User> = object : Callback<User> {
                override fun onResponse(call: Call<User>,
                                        response: Response<User>) {
                    if (!response.isSuccessful) {
                        Log.i(TAG, response.message())
                        println(response)
                    } else {
                        User.getInstance().name = response.body()!!.name
                        User.getInstance().phoneNumber = response.body()!!.phoneNumber
                        Address.getInstance().setCountry(
                                response.body()!!.address.country)
                                .setCity(response.body()!!.address.city)
                                .setStreet(response.body()!!.address.street).houseNumber = response.body()!!.address
                                .houseNumber
                        mNameTV!!.text = User.getInstance().name
                        mPhoneTV!!.text = User.getInstance().phoneNumber
                        mCityTV!!.text = Address.getInstance().city
                        mCountryTV!!.text = Address.getInstance().country
                        mStreetTV!!.text = Address.getInstance().street
                        mHouseTV!!.text = Address.getInstance().houseNumber
                    }
                    loadingDialog.dismiss()
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.i(TAG, t.toString())
                    println(t.toString())
                    loadingDialog.dismiss()
                }
            }
            val email = User.getInstance().email
            if (User.getInstance().userRole
                    == getString(R.string.cleaning_provider)) {
                NetworkService.getInstance().apiService
                        .getCleaningProviderData(token, email).enqueue(callback)
            } else if (User.getInstance().userRole
                    == getString(R.string.placement_owner)) {
                NetworkService.getInstance().apiService
                        .getPlacementOwnerData(token, email).enqueue(callback)
            }
        }

    private fun init() {
        context = this
        token = "Bearer " + User.getInstance().token
        mEmailTV = findViewById(R.id.profile_email)
        mNameTV = findViewById(R.id.profile_name)
        mPhoneTV = findViewById(R.id.profile_phone)
        mCountryTV = findViewById(R.id.profile_country)
        mCityTV = findViewById(R.id.profile_city)
        mStreetTV = findViewById(R.id.profile_street)
        mHouseTV = findViewById(R.id.profile_house)
        mNameET = findViewById(R.id.profile_name_edit)
        mEmailET = findViewById(R.id.profile_email_edit)
        mPhoneET = findViewById(R.id.profile_phone_edit)
        mCountryET = findViewById(R.id.profile_country_edit)
        mCityET = findViewById(R.id.profile_city_edit)
        mStreetET = findViewById(R.id.profile_street_edit)
        mHouseET = findViewById(R.id.profile_house_edit)
        mPasswordET = findViewById(R.id.profile_password_edit)
        mEditButton = findViewById(R.id.edit_profile_btn)
        mEditButton.setOnClickListener(View.OnClickListener { v: View? -> startEditing() })
        mBack = findViewById(R.id.back_btn)
        mBack.setOnClickListener(View.OnClickListener { v: View? -> finish() })
        mLogOut = findViewById(R.id.exit_btn)
        mLogOut.setOnClickListener(View.OnClickListener { v: View? ->
            User.getInstance().setToken(null).name = null
            startActivity(
                    Intent(this@ProfileActivity, SignInActivity::class.java))
        })
        showTV()
    }

    private fun showTV() {
        mEditButton.setText(R.string.edit)
        mNameET!!.visibility = View.GONE
        mEmailET!!.visibility = View.GONE
        mPhoneET!!.visibility = View.GONE
        mCountryET!!.visibility = View.GONE
        mCityET!!.visibility = View.GONE
        mStreetET!!.visibility = View.GONE
        mHouseET!!.visibility = View.GONE
        mPasswordET!!.visibility = View.GONE
        mNameTV!!.visibility = View.VISIBLE
        mEmailTV!!.visibility = View.VISIBLE
        mPhoneTV!!.visibility = View.VISIBLE
        mCityTV!!.visibility = View.VISIBLE
        mCountryTV!!.visibility = View.VISIBLE
        mStreetTV!!.visibility = View.VISIBLE
        mHouseTV!!.visibility = View.VISIBLE
        mNameTV!!.text = User.getInstance().name
        mEmailTV!!.text = User.getInstance().email
    }

    private fun showET() {
        mEditButton.setText(R.string.done)
        mNameTV!!.visibility = View.GONE
        mEmailTV!!.visibility = View.GONE
        mPhoneTV!!.visibility = View.GONE
        mCityTV!!.visibility = View.GONE
        mCountryTV!!.visibility = View.GONE
        mStreetTV!!.visibility = View.GONE
        mHouseTV!!.visibility = View.GONE
        mNameET!!.visibility = View.VISIBLE
        mEmailET!!.visibility = View.VISIBLE
        mPhoneET!!.visibility = View.VISIBLE
        mCountryET!!.visibility = View.VISIBLE
        mCityET!!.visibility = View.VISIBLE
        mStreetET!!.visibility = View.VISIBLE
        mHouseET!!.visibility = View.VISIBLE
        mPasswordET!!.visibility = View.VISIBLE
        mNameET!!.setText(User.getInstance().name)
        mEmailET!!.setText(User.getInstance().email)
        mPhoneET!!.setText(User.getInstance().phoneNumber)
        mCityET!!.setText(Address.getInstance().city)
        mCountryET!!.setText(Address.getInstance().country)
        mStreetET!!.setText(Address.getInstance().street)
        mHouseET!!.setText(Address.getInstance().houseNumber)
    }

    private fun startEditing() {
        showET()
        data
        mEditButton.setOnClickListener { v: View? -> submitEditing() }
    }

    //TODO edit profile with location
    private fun submitEditing() {
        loadingDialog.start()
        val callback: Callback<User?> = object : Callback<User?> {
            override fun onResponse(call: Call<User?>,
                                    response: Response<User?>) {
                if (!response.isSuccessful) {
                    Toast.makeText(context, response.message(),
                            Toast.LENGTH_LONG).show()
                } else {
                    mEditButton!!.setOnClickListener { v: View? -> startEditing() }
                    data
                    showTV()
                }
                loadingDialog.dismiss()
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
                loadingDialog.dismiss()
            }
        }
        mNameET!!.visibility = View.GONE
        mEmailET!!.visibility = View.GONE
        mPhoneET!!.visibility = View.GONE
        mCountryET!!.visibility = View.GONE
        mCityET!!.visibility = View.GONE
        mStreetET!!.visibility = View.GONE
        mHouseET!!.visibility = View.GONE
        mPasswordET!!.visibility = View.GONE
        User.getInstance().setName(mNameET!!.text.toString())
                .setEmail(mEmailET!!.text.toString())
                .setPhoneNumber(mPhoneET!!.text.toString()).address = Address(mCountryET!!.text.toString(),
                mCityET!!.text.toString(),
                mStreetET!!.text.toString(),
                mHouseET!!.text.toString(), "0", "0")

        if (User.getInstance().userRole
                == getString(R.string.cleaning_provider)) {
            NetworkService.getInstance().apiService
                    .updateCleaningProviderProfile(token, User.getInstance())
                    .enqueue(callback)
        } else if (User.getInstance().userRole
                == getString(R.string.placement_owner)) {
            NetworkService.getInstance().apiService
                    .updatePlacementOwnerProfile(token, User.getInstance())
                    .enqueue(callback)
        }
    }

    companion object {
        private const val TAG = "ProfileActivity"
    }
}
