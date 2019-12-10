package com.ibnufth.kalkulatorfirebase.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ibnufth.kalkulatorfirebase.R
import com.ibnufth.kalkulatorfirebase.model.Riwayat

public class RiwayarAdapter(
    private val listRiwayat : MutableList<Riwayat>,
    private val context: Context,
    private val dbFirestore: FirebaseFirestore)
    : RecyclerView.Adapter<RiwayarAdapter.ViewHolder>() {

    private val TAG = "RiwayatAdapter"
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvRiwayat: TextView = view.findViewById(R.id.riwayat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.listview, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listRiwayat.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val riwayat = listRiwayat[position]
        holder.tvRiwayat.text = riwayat.hasil
        holder.itemView.setOnLongClickListener {
            deleteItem(riwayat.id,position)
        }
    }


    private fun deleteItem(id : String,position: Int) : Boolean {
        dbFirestore.collection("riwayat").document(id)
            .delete()
            .addOnCompleteListener {
                listRiwayat.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, listRiwayat.size)
                Toast.makeText(context, "Sukses Terhapus!", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { e -> Log.w(TAG, "Error deleting document", e) }
        return true
    }
}