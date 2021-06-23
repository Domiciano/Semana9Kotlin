package edu.co.icesi.semana9kotlina

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import edu.co.icesi.semana9kotlina.model.Apunte
import edu.co.icesi.semana9kotlina.util.Constants
import edu.co.icesi.semana9kotlina.util.HTTPSWebUtilDomi
import kotlinx.android.synthetic.main.activity_lista_apuntes.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListaApuntesActivity : AppCompatActivity() {

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_apuntes)

        username = intent.extras?.getString("username")
        apuntesTitle.setText("Apuntes de ${username}")

        getApuntes()
    }

    private fun getApuntes() {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = HTTPSWebUtilDomi().GETRequest("${Constants.BASE_URL}apuntes/${username}.json")
            if (response != "null") {
                val type = object : TypeToken<Map<String, Apunte>>() {}.type
                val apuntes: Map<String, Apunte> = Gson().fromJson(response, type)
                apuntes.forEach {
                    withContext(Dispatchers.Main) {
                        apuntesTV.append("${it.value.body}\n")
                    }
                }
            }else{
                withContext(Dispatchers.Main){Toast.makeText(this@ListaApuntesActivity, "El usuario no tiene apuntes", Toast.LENGTH_LONG).show()}
            }


        }
    }
}