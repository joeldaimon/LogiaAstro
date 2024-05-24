package com.joguco.logiaastro.model

import android.os.Parcel
import android.os.Parcelable
import com.joguco.logiaastro.database.entities.UserEntity

/*
data class User(
    var name: String?,
    var password: String?,
    var mail: String?,
    var day:Int,
    var month: Int,
    var year: Int,
    var hour: String?,
    var place: String?,
    var image: String?,
    var chartList: ArrayList<NatalChart>?
    ): Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        chartList = ArrayList<NatalChart>()
        ) {
    }

    constructor(entity: UserEntity) : this(
        entity.username,
        entity.password,
        entity.mail,
        entity.day,
        entity.month,
        entity.year,
        entity.hour,
        entity.place,
        entity.image,
        null
    ){
    }

    constructor(name: String, pwd: String, mail: String, hour: String, place: String, image: String) : this(
        name,
        pwd,
        mail,
        0,
        0,
        0,
        hour,
        place,
        image,
        chartList = ArrayList<NatalChart>()
    ){}

    fun addChart(chart: NatalChart){
        chartList?.add(chart)
    }

    fun getChart(nombre: String): NatalChart {
        if(chartList != null){
            for(chart in chartList!!){
                if(chart.name == nombre){
                    return chart
                }
            }
        }
        throw Exception("No natal chart with this name.")
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}*/
