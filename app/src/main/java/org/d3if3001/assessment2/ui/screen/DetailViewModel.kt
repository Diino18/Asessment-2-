package org.d3if3001.assessment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3001.assessment2.database.SepatuDao
import org.d3if3001.assessment2.model.Sepatu

class DetailViewModel(private val dao: SepatuDao) : ViewModel(){
    fun insert(nama: String, nilai: Int, Wilayah: String){
        val sepatu = Sepatu(
            nama = nama,
            ukuran = nilai,
            wilayah = Wilayah
        )

        viewModelScope.launch(Dispatchers.IO) { dao.insert(sepatu) }
    }

    suspend fun getSepatu(id: Long): Sepatu? {
        return dao.getSepatuById(id)
    }
    fun update(id: Long, nama: String, nilai: Int, Wilayah: String){
        val sepatu = Sepatu(
            id = id,
            nama = nama,
            ukuran = nilai,
            wilayah = Wilayah
        )

        viewModelScope.launch(Dispatchers.IO) { dao.update(sepatu) }
    }

    fun delete(id: Long){
        viewModelScope.launch(Dispatchers.IO) { dao.deleteById(id) }
    }
}