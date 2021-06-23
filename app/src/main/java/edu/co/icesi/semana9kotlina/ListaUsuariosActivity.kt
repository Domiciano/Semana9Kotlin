package edu.co.icesi.semana9kotlina

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.co.icesi.semana9kotlina.model.Apunte
import edu.co.icesi.semana9kotlina.model.User
import edu.co.icesi.semana9kotlina.util.Constants
import edu.co.icesi.semana9kotlina.util.HTTPSWebUtilDomi
import kotlinx.android.synthetic.main.activity_lista_apuntes.*
import kotlinx.android.synthetic.main.activity_lista_usuarios.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaUsuariosActivity : AppCompatActivity() {

    private lateinit var usuarios: ArrayList<User>
    private lateinit var usuariosAdapter: ArrayAdapter<User>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_usuarios)

        usuarios = ArrayList()
        usuariosAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, usuarios)
        userList.adapter = usuariosAdapter

        userList.setOnItemClickListener(::onItemClicked)
        userList.setOnItemLongClickListener(::onItemLongClicked)

        getUsuarios()
    }



    private fun onItemClicked(adapterView: AdapterView<*>?, view: View?, position: Int, l: Long) {
        val i = Intent(this, ListaApuntesActivity::class.java)
        i.putExtra("username", usuarios[position].nombre)
        startActivity(i)

    }

    private fun onItemLongClicked(adapterView: AdapterView<*>?, view: View?, position: Int, l: Long): Boolean {
        lifecycleScope.launch(Dispatchers.IO){
            HTTPSWebUtilDomi().DELETERequest("${Constants.BASE_URL}users/${usuarios[position].nombre}.json")
            withContext(Dispatchers.Main){
                getUsuarios()
            }
        }
        return true
    }

    private fun getUsuarios() {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = HTTPSWebUtilDomi().GETRequest("${Constants.BASE_URL}users.json")
            val type = object : TypeToken<Map<String, User>>() {}.type
            val users: Map<String, User> = Gson().fromJson(response, type)

            usuarios.clear()
            users.forEach {
                withContext(Dispatchers.Main) {
                    usuarios.add(it.value)
                }
            }

            withContext(Dispatchers.Main) {
                usuariosAdapter.notifyDataSetChanged()
            }

        }
    }
}