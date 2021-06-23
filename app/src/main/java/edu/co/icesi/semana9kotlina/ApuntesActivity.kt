package edu.co.icesi.semana9kotlina

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import edu.co.icesi.semana9kotlina.model.Apunte
import edu.co.icesi.semana9kotlina.util.Constants
import edu.co.icesi.semana9kotlina.util.HTTPSWebUtilDomi
import kotlinx.android.synthetic.main.activity_apuntes.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ApuntesActivity : AppCompatActivity() {

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_apuntes)

        username = intent.extras?.getString("username")

        publishBtn.setOnClickListener(::publish)
        misApuntesBtn.setOnClickListener(::goToApuntesList)
        comunidadBtn.setOnClickListener(::goToComunidad)
    }

    private fun publish(view: View) {
        username?.let {
            val apunte = Apunte(
                UUID.randomUUID().toString(),
                newApunteET.text.toString(),
                Date().time,
                it
            )
            val gson = Gson()
            val json = gson.toJson(apunte)
            lifecycleScope.launch(Dispatchers.IO) {
                HTTPSWebUtilDomi().POSTRequest(
                    "${Constants.BASE_URL}apuntes/${apunte.username}.json",
                    json
                )
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@ApuntesActivity, "Publicado!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun goToApuntesList(view: View?) {
        val i = Intent(this, ListaApuntesActivity::class.java)
        i.putExtra("username", username)
        startActivity(i)
    }

    private fun goToComunidad(view: View?) {
        val i = Intent(this, ListaUsuariosActivity::class.java)
        startActivity(i)
    }
}