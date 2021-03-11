package com.example.projetphoto.db.pictures

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pictures(
    @PrimaryKey val id : Int,
    @ColumnInfo val title : String,
    @ColumnInfo val link : String
)