package edu.co.icesi.semana9kotlina

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import edu.co.icesi.semana9kotlina.model.User
import edu.co.icesi.semana9kotlina.util.Constants
import edu.co.icesi.semana9kotlina.util.HTTPSWebUtilDomi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginBtn.setOnClickListener(::login)
    }

    private fun login(view: View) {
        val user = User(UUID.randomUUID().toString(), usernameET.text.toString(), Date().time)
        val gson = Gson()
        val json = gson.toJson(user)

        lifecycleScope.launch(Dispatchers.IO){
            HTTPSWebUtilDomi().PUTRequest("${Constants.BASE_URL}users/${user.nombre}.json", json)
            val i = Intent(this@MainActivity, ApuntesActivity::class.java)
            i.putExtra("username", user.nombre)
            startActivity(i)
        }

    }
}