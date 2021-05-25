package ua.nure.cleaningservice.ui.rva

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ua.nure.cleaningservice.R
import ua.nure.cleaningservice.data.User
import ua.nure.cleaningservice.data.Placement
import ua.nure.cleaningservice.network.JsonPlaceHolderApi
import ua.nure.cleaningservice.network.NetworkService
import ua.nure.cleaningservice.ui.edit.EditPlacementActivity
import ua.nure.cleaningservice.ui.rva.PlacementsRVA.RoomsViewHolder
import ua.nure.cleaningservice.ui.util.LoadingDialog
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PlacementsRVA(private val mContext: Context, private var mPlacements: MutableList<Placement>?) : RecyclerView.Adapter<RoomsViewHolder>() {
    private var mRoomsViewHolder: RoomsViewHolder? = null
    private var mApi: JsonPlaceHolderApi? = null
    var token: String? = null
    private val loadingDialog = LoadingDialog(mContext as Activity)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): RoomsViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.placement_card, viewGroup, false)
        mApi = NetworkService.getInstance().apiService
        token = "Bearer " + User.getInstance().token
        return RoomsViewHolder(v)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RoomsViewHolder, position: Int) {
        holder.mCardView.id = mPlacements!![position].id
        holder.mRoomNumberTV.text = mPlacements!![position].id.toString()
        holder.mRoomTypeTV.text = mPlacements!![position].placementType
        holder.mFloorTV.text = String.format(Locale.getDefault(), "%d", mPlacements!![position].floor)
        holder.mWinCountTV.text = String.format(Locale.getDefault(), "%d", mPlacements!![position].windowsCount)
        holder.mAreaTV.text = mPlacements!![position].area.toString()
        holder.mAirQualityTV.text = mPlacements!![position].smartDevice.airQuality.toString()
        holder.mHumidityTV.text = mPlacements!![position].smartDevice.humidity.toString()
        var formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
        val dateTime = ZonedDateTime.parse(mPlacements!![position].lastCleaning, formatter)
        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        holder.mLastClDateTV.text = dateTime.format(formatter)
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int {
        return mPlacements!!.size
    }

    inner class RoomsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCardView: CardView
        var mRoomNumberTV: TextView
        var mRoomTypeTV: TextView
        var mFloorTV: TextView
        var mWinCountTV: TextView
        var mAreaTV: TextView
        var mAirQualityTV: TextView
        var mHumidityTV: TextView
        var mLastClDateTV: TextView

        init {
            mCardView = itemView.findViewById<View>(R.id.room_cv) as CardView
            mRoomTypeTV = itemView.findViewById<View>(R.id.room_type_text) as TextView
            mRoomNumberTV = itemView.findViewById<View>(R.id.room_number_text) as TextView
            mFloorTV = itemView.findViewById<View>(R.id.floor_text) as TextView
            mWinCountTV = itemView.findViewById<View>(R.id.win_count_text) as TextView
            mAreaTV = itemView.findViewById<View>(R.id.area_text) as TextView
            mAirQualityTV = itemView.findViewById<View>(R.id.air_quality_text) as TextView
            mHumidityTV = itemView.findViewById<View>(R.id.humidity_text) as TextView
            mLastClDateTV = itemView.findViewById<View>(R.id.last_cl_date_text) as TextView
        }
    }

    private fun editRoom(holder: RoomsViewHolder) {
        val intent = Intent(mContext, EditPlacementActivity::class.java)
        intent.putExtra("rId", holder.mCardView.id)
        mContext.startActivity(intent)
    }

    private fun deleteRoom() {
        loadingDialog.start()
        mApi!!.deletePlacement(token, mRoomsViewHolder!!.mCardView.id).enqueue(deleteCallback)
    }

    var deleteCallback: Callback<Placement?> = object : Callback<Placement?> {
        override fun onResponse(call: Call<Placement?>, response: Response<Placement?>) {
            if (response.isSuccessful) {
                println(response.body())
                mPlacements?.removeAt(mRoomsViewHolder!!.adapterPosition)
                notifyItemRemoved(mRoomsViewHolder!!.adapterPosition)
                loadingDialog.dismiss()
                //                notifyItemRangeChanged(mRoomsViewHolder.getAdapterPosition(), mRooms.size());
            }
        }

        override fun onFailure(call: Call<Placement?>, t: Throwable) {
            println(t)
            loadingDialog.dismiss()
        }
    }
}
