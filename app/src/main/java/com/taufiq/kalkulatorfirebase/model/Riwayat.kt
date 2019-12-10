package com.ibnufth.kalkulatorfirebase.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Riwayat (var id : String,var hasil : String)
{
    constructor() : this("","") {

    }
}