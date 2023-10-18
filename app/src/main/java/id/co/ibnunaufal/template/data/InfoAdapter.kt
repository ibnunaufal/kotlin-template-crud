package id.co.ibnunaufal.template.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import id.co.ibnunaufal.template.R
import id.co.ibnunaufal.template.data.model.InfoModel

class InfoAdapter(private val infoList: List<InfoModel>) : RecyclerView.Adapter<InfoAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoAdapter.MyViewHolder {
        val itemView =  LayoutInflater.from(parent.context).inflate(R.layout.list_info,
            parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InfoAdapter.MyViewHolder, position: Int) {
        val info = infoList.get(position)

        holder.idTitle.text = info.id2
        holder.idDesc.text = info.id
        holder.ver.text = info.versionCode
        holder.urgencyStatus.text = info.isUrgent.toString()
        holder.migrateStatus.text = info.statusMigrasi.toString()
    }

    override fun getItemCount(): Int {
        return infoList.size
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val idTitle : TextView = itemView.findViewById(R.id.tvIdTitle)
        val idDesc : TextView = itemView.findViewById(R.id.tvIdDesc)
        val ver : TextView = itemView.findViewById(R.id.tvVer)
        val urgencyStatus : TextView = itemView.findViewById(R.id.tvUrgencyStatus)
        val migrateStatus : TextView = itemView.findViewById(R.id.tvMigrationStatus)

    }

}