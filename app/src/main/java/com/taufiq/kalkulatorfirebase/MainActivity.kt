package com.ibnufth.kalkulatorfirebase

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.Tag
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.ibnufth.kalkulatorfirebase.adapter.RiwayarAdapter
import com.ibnufth.kalkulatorfirebase.model.Riwayat
import java.util.*

class MainActivity : AppCompatActivity() {

    val TAG : String ="MainActivity"

    private lateinit var  mFirestore : FirebaseFirestore
    lateinit var mQuery: Query
    private var firestoreListener : ListenerRegistration? = null

    lateinit var mAdapter : RiwayarAdapter
    lateinit var rvRiwayat : RecyclerView

    lateinit var btnHitung : Button
    lateinit var radioGroup: RadioGroup
    lateinit var btnRadio : RadioButton
    lateinit var input1 : EditText
    lateinit var input2 :EditText
    lateinit var hasil : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        radioGroup =findViewById(R.id.rg_oprasional)
        btnHitung =findViewById(R.id.btn_hitung)
        hitung()

        mFirestore= FirebaseFirestore.getInstance()
        mQuery=mFirestore.collection("riwayat")

        rvRiwayat=findViewById(R.id.rv_riwayat)
        showRercyler()

        firestoreListener =mFirestore.collection("riwayat")
            .addSnapshotListener(EventListener{documentSnapshots , e ->
                if (e !== null){
                    Log.d(TAG,"Listen Filed",e)
                    return@EventListener
                }
                val riwayatList = mutableListOf<Riwayat>()

                for (doc in documentSnapshots!!){
                    val riwayat = doc.toObject(Riwayat::class.java)
                    riwayat.id=doc.id
                    riwayatList.add(riwayat)
                }
                mAdapter= RiwayarAdapter(riwayatList,applicationContext,mFirestore)
                rvRiwayat.adapter=mAdapter
            })
    }

    private fun hitung(){
        btnHitung.setOnClickListener {
                val selected_id =radioGroup.checkedRadioButtonId
                btnRadio = findViewById(selected_id)
                input1=findViewById(R.id.et_bil_1)
                input2=findViewById(R.id.et_bil_2)
                hasil=findViewById(R.id.tv_hasil)

                val angka1 = input1.text.toString().toDouble()
                val angka2 = input2.text.toString().toDouble()
                var hasilHitungan=0.0
                lateinit var textHasil : String

                when(btnRadio.text){
                    "Tambah" ->{hasilHitungan= angka1+angka2
                                textHasil="$angka1 + $angka2 = $hasilHitungan"
                            }
                    "Kurang" ->{hasilHitungan = angka1-angka2
                                textHasil="$angka1 - $angka2 = $hasilHitungan"
                            }
                    "Kali" ->{hasilHitungan= angka1*angka2
                                textHasil="$angka1 * $angka2 = $hasilHitungan"
                            }
                    "Bagi" ->{hasilHitungan =angka1/angka2
                                textHasil="$angka1 / $angka2 = $hasilHitungan"}
                            }
                hasil.setText(hasilHitungan.toString())
                addRiwayatHasil(textHasil)
        }
    }
    private fun addRiwayatHasil(hasil : String){
        val map  = hashMapOf<String,String>()
        map.put("hasil",hasil)
        val db= mFirestore.collection("riwayat")
            db.add(map)
    }

    fun showRercyler(){
        mFirestore.collection("riwayat")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    val riwayatList = mutableListOf<Riwayat>()

                    for (doc in task.result!!){
                        val riwayat = doc.toObject(Riwayat::class.java)
                        riwayat.id=doc.id
                        riwayatList.add(riwayat)
                    }
                    mAdapter= RiwayarAdapter(riwayatList,applicationContext,mFirestore)
                    val mLayoutManager =LinearLayoutManager(applicationContext)
                    rvRiwayat.layoutManager = mLayoutManager
                    rvRiwayat.itemAnimator = DefaultItemAnimator()
                    rvRiwayat.adapter = mAdapter
                }
                else {
                    Log.d(TAG, "Error mendapatkan dokument",task.exception)
                }
            }

    }

}
