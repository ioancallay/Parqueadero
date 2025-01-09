package com.example.parqueadero

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.json.JSONException
import org.json.JSONObject
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.android.volley.Request
import android.os.Handler
import android.os.Looper


class MainActivity : AppCompatActivity() {

    lateinit var txtUsuario: EditText
    lateinit var txtPassword: EditText
    lateinit var btnIngresar: Button

    val apis: String = "https://agenda.ioasystem.com/api_login_parking.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        mapeo()

        btnIngresar.setOnClickListener {
            when{
                txtUsuario.text.isEmpty()->{
                    Toast.makeText(applicationContext, "Ingrese su usuario", Toast.LENGTH_SHORT).show()
                }
                txtPassword.text.isEmpty()->{
                    Toast.makeText(applicationContext, "Ingrese su contraseña", Toast.LENGTH_SHORT).show()
                }
                else-> {
                    login(txtUsuario.text.toString(), txtPassword.text.toString())
                }
            }
        }
    }

    fun mapeo(){
        txtUsuario = findViewById(R.id.txt_usuario)
        txtPassword = findViewById(R.id.txt_password)
        btnIngresar = findViewById(R.id.btn_ingresar)
    }

    fun limpiarCajas(){
        txtUsuario.setText("")
        txtPassword.setText("")
    }
/*
    private fun login(email: String, passwd:String){
        val campos = JSONObject()
        campos.put("accion", "login")
        campos.put("email", email)
        campos.put("password", passwd)
        val rq = Volley.newRequestQueue(this)
        val jsoresp = JsonObjectRequest(Request.Method.POST, apis, campos,
            {
                    s->
                try {
                    val obj = (s)
                    if(obj.getBoolean("estado")){
                        val array = obj.getJSONArray("data")
                        val dato = array.getJSONObject(0)
                        val cod_persona = dato.getString("cod_usuario")
                        val fullname = dato.getString("nom_usuario") + " " + dato.getString("ape_usuario")
                        Toast.makeText(applicationContext, obj.getString("response").toString(), Toast.LENGTH_SHORT).show()
                        val form2 = Intent(this, form_registrar::class.java)
                        val bundle = Bundle()
                        bundle.putString("cod_usuario", cod_persona)
                        bundle.putString("fullname", fullname)
                        form2.putExtras(bundle)
                        startActivity(form2)
                    }
                    else{
                        Toast.makeText(applicationContext, obj.getString("response").toString(), Toast.LENGTH_SHORT).show()
                        limpiarCajas()
                    }

                } catch (e: JSONException){
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
                }
            },
            { volleyError-> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show() })
        rq.add(jsoresp)
    }

 */

    private var loginAttempts = 0

    private fun login(email: String, passwd: String) {
        val campos = JSONObject()
        campos.put("accion", "login")
        campos.put("email", email)
        campos.put("password", passwd)
        val rq = Volley.newRequestQueue(this)
        val jsoresp = JsonObjectRequest(Request.Method.POST, apis, campos,
            { response ->
                try {
                    val obj = response
                    if (obj.getBoolean("estado")) {
                        loginAttempts = 0

                        val array = obj.getJSONArray("data")
                        val dato = array.getJSONObject(0)
                        val cod_persona = dato.getString("cod_usuario")
                        val fullname = dato.getString("nom_usuario") + " " + dato.getString("ape_usuario")
                        Toast.makeText(applicationContext, obj.getString("response").toString(), Toast.LENGTH_SHORT).show()

                        val form2 = Intent(this, form_registrar::class.java)
                        val bundle = Bundle()
                        bundle.putString("cod_usuario", cod_persona)
                        bundle.putString("fullname", fullname)
                        form2.putExtras(bundle)
                        startActivity(form2)
                    } else {
                        Toast.makeText(applicationContext, obj.getString("response").toString(), Toast.LENGTH_SHORT).show()

                        loginAttempts++
                        if (loginAttempts >= 3) {
                            Toast.makeText(applicationContext, "Demasiados intentos fallidos. Intenta más tarde.", Toast.LENGTH_SHORT).show()
                            bloquearLogin()
                        } else {
                            Toast.makeText(applicationContext, obj.getString("response").toString(), Toast.LENGTH_SHORT).show()
                            limpiarCajas()
                        }
                    }
                } catch (e: JSONException) {
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
                }
            },
            { volleyError ->
                Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
            })
        rq.add(jsoresp)
    }

    private fun bloquearLogin() {
        Toast.makeText(applicationContext, "Demasiados intentos fallidos. Intenta más tarde.", Toast.LENGTH_SHORT).show()
        val loginButton: Button = findViewById(R.id.btn_ingresar)
        loginButton.isEnabled = false



        Handler(Looper.getMainLooper()).postDelayed({
            loginAttempts = 0
            loginButton.isEnabled = true
        }, 30000)
    }

}