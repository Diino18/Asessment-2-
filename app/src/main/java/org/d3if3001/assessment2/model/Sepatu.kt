package org.d3if3001.assessment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sepatu")
class Sepatu (
    @PrimaryKey(autoGenerate = true)
    val id:Long = 0L,
    val nama: String,
    val ukuran: Int,
    val wilayah: String
)