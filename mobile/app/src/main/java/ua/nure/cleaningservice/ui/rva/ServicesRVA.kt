package ua.nure.cleaningservice.ui.rva

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import ua.nure.cleaningservice.R
import ua.nure.cleaningservice.data.Service
import ua.nure.cleaningservice.data.User
import ua.nure.cleaningservice.network.JsonPlaceHolderApi
import ua.nure.cleaningservice.network.NetworkService
import ua.nure.cleaningservice.ui.rva.ServicesRVA.ServicesViewHolder


class ServicesRVA(val mServices: MutableList<Service>?) : RecyclerView.Adapter<ServicesViewHolder>() {

    private var mApi: JsonPlaceHolderApi? = null
    var token: String? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ServicesViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.services_card, viewGroup, false)
        mApi = NetworkService.getInstance().apiService
        token = "Bearer " + User.getInstance().token
        return ServicesViewHolder(v)
    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {
        val name = mServices?.get(position)?.name
        val id = mServices!![position].id
        val nameAndId = "$id - $name"
        holder.mCardView.id = id
        holder.mNameTV.text = nameAndId
        holder.mDescTV.text = mServices[position].description
        holder.mMinAreaTV.text = mServices[position].minArea.toString()
        holder.mMaxAreaTV.text = mServices[position].maxArea.toString()
        holder.mRoomTypeTV.text = mServices[position].placementType
        holder.mPriceTV.text = mServices[position].pricePerMeter.toString()
    }

    override fun getItemCount(): Int {
        return mServices!!.size
    }

    inner class ServicesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCardView: CardView = itemView.findViewById<View>(R.id.services_cv) as CardView
        var mNameTV: TextView = itemView.findViewById<View>(R.id.name_service_label) as TextView
        var mDescTV: TextView = itemView.findViewById<View>(R.id.desc_text) as TextView
        var mMinAreaTV: TextView = itemView.findViewById<View>(R.id.min_area_text) as TextView
        var mMaxAreaTV: TextView = itemView.findViewById<View>(R.id.max_area_text) as TextView
        var mRoomTypeTV: TextView = itemView.findViewById<View>(R.id.room_type_text) as TextView
        var mPriceTV: TextView = itemView.findViewById<View>(R.id.price_text) as TextView
    }
}
