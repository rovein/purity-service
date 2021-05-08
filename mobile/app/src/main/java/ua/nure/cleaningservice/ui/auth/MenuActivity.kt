package ua.nure.cleaningservice.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ua.nure.cleaningservice.R
import ua.nure.cleaningservice.data.User
import ua.nure.cleaningservice.ui.contracts.ContractsActivity
import ua.nure.cleaningservice.ui.profile.*

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (User.getInstance().userRole == getString(R.string.cleaning_provider)) {
            setContentView(R.layout.activity_cleaning_provider_menu)
        } else if (User.getInstance().userRole == getString(R.string.placement_owner)) {
            setContentView(R.layout.activity_placement_owner_menu)
        }
        init()
    }

    private fun init() {
        if (User.getInstance().userRole == getString(R.string.cleaning_provider)) {
            findViewById<View>(R.id.services_btn).setOnClickListener {
                goTo(ServicesActivity::class.java)
            }
        } else if (User.getInstance().userRole == getString(R.string.placement_owner)) {
            findViewById<View>(R.id.rooms_btn).setOnClickListener {
                goTo(PlacementsActivity::class.java)
            }
            findViewById<View>(R.id.search_btn).setOnClickListener {
                goTo(SearchActivity::class.java)
            }
        }
        findViewById<View>(R.id.profile_btn).setOnClickListener {
            goTo(ProfileActivity::class.java)
        }
        findViewById<View>(R.id.contracts_btn).setOnClickListener {
            goTo(ContractsActivity::class.java)
        }
    }

    private fun goTo(cls: Class<*>) {
        val intent = Intent(this@MenuActivity, cls)
        startActivity(intent)
    }
}
