package id.co.ibnunaufal.template.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
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

        holder.idTitle.text = info.id
        holder.idDesc.text = info._id
        holder.ver.text = info.versionCode
        holder.urgencyStatus.text = info.isUrgent.toString()
        holder.migrateStatus.text = info.statusMigrasi.toString()
    }

    override fun getItemCount(): Int {
        return infoList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val idTitle : TextView = itemView.findViewById(R.id.tvIdTitle)
        val idDesc : TextView = itemView.findViewById(R.id.tvIdDesc)
        val ver : TextView = itemView.findViewById(R.id.tvVer)
        val urgencyStatus : TextView = itemView.findViewById(R.id.tvUrgencyStatus)
        val migrateStatus : TextView = itemView.findViewById(R.id.tvMigrationStatus)

        private val deleteButton: ImageView = itemView.findViewById(R.id.btnDelete)
        private val editButton: ImageView = itemView.findViewById(R.id.btnEdit)

        init {
            deleteButton.setOnClickListener {
                val position = adapterPosition // Get the position of the clicked item
                val infoModel = infoList[position]
                val infoId = infoModel._id // Replace 'id' with the actual property name
                onDeleteClickListener.onDeleteClick(infoId)
            }

            editButton.setOnClickListener {
                val position = adapterPosition
                val infoModel = infoList[position]
                val infoId = infoModel._id // Use 'id' or the actual property name
                onEditClickListener.onEditClick(infoId)
            }
        }

    }

    interface OnDeleteClickListener {
        fun onDeleteClick(infoId: String)
    }

    private lateinit var onDeleteClickListener: OnDeleteClickListener

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
        this.onDeleteClickListener = listener
    }

    interface OnEditClickListener {
        fun onEditClick(infoId: String)
    }

    private lateinit var onEditClickListener: OnEditClickListener

    fun setOnEditClickListener(listener: OnEditClickListener) {
        this.onEditClickListener = listener
    }


}