package com.example.parqueadero

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class form_salida : AppCompatActivity() {

    lateinit var txtPayCedula : EditText
    lateinit var txtPayNombre : EditText
    lateinit var txtPayPlaca : EditText
    lateinit var txtPayEntrada : EditText
    lateinit var txtPaySalida : EditText
    lateinit var txtPayValor : EditText
    lateinit var txtPayHoras : EditText
    lateinit var txtPayCodCarro : EditText

    lateinit var btnPayPago : Button
    lateinit var btnPayCancelar : Button

    val apis: String = "https://agenda.ioasystem.com/api_carros.php"


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form_salida)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var bundle = intent.extras
        var cod_carro = bundle?.getString("cod_carro").toString()

/*
        txtPayCedula.setText(cedula_carro)
        txtPayNombre.setText(nombre_carro)
        txtPayPlaca.setText(placa_carro)
        txtPayEntrada.setText(hora_entrada)
        txtPaySalida.setText("fechaHoraFormateada")
        txtPayValor.setText("")
        txtPayCodCarro.setText(cod_carro)

 */

        mapeo()
        pagosDesactivar()
        editar(cod_carro)

        btnPayPago.setOnClickListener {
            pagar(cod_carro)
        }

        btnPayCancelar.setOnClickListener {
            val form_registrar = Intent(this, form_registrar::class.java)
            startActivity(form_registrar)
        }
    }

    fun mapeo(){
        txtPayCedula = findViewById(R.id.txt_pay_cedula)
        txtPayNombre = findViewById(R.id.txt_pay_nombre)
        txtPayPlaca = findViewById(R.id.txt_pay_placa)
        txtPayEntrada = findViewById(R.id.txt_pay_entrada)
        txtPaySalida = findViewById(R.id.txt_pay_salida)
        txtPayValor = findViewById(R.id.txt_pay_pago)
        txtPayHoras = findViewById(R.id.txt_pay_horas)
        txtPayCodCarro = findViewById(R.id.txt_pay_codcarro)
        btnPayPago = findViewById(R.id.btn_pay_pagar)
        btnPayCancelar = findViewById(R.id.btn_pay_cancelar)
    }

    fun pagosDesactivar(){
        txtPayCedula.isEnabled = false
        txtPayNombre.isEnabled = false
        txtPayPlaca.isEnabled = false
        txtPayEntrada.isEnabled = false
        txtPaySalida.isEnabled = false
        txtPayValor.isEnabled = false
        txtPayHoras.isEnabled = false
        txtPayCodCarro.isEnabled = false
    }

    private fun editar(cod_carro:String){
        txtPayCodCarro.setText(cod_carro)
        val campos = JSONObject()
        campos.put("accion", "generarPago")
        campos.put("cod_carro", cod_carro)
        val rq = Volley.newRequestQueue(this)
        val jsoresp = JsonObjectRequest(
            Request.Method.POST, apis, campos,
            {
                    s->
                try {
                    val obj = (s)
                    if(obj.getBoolean("estado")){
                        val array = obj.getJSONArray("data")
                        val dato = array.getJSONObject(0)
                        txtPayCedula.setText(dato.getString("cedula_carro"))
                        txtPayNombre.setText(dato.getString("nombre_carro"))
                        txtPayPlaca.setText(dato.getString("placa_carro"))
                        txtPayEntrada.setText(dato.getString("hora_entrada"))
                        txtPaySalida.setText(dato.getString("hora_salida"))
                        txtPayValor.setText(dato.getString("valor_pago"))
                        txtPayHoras.setText(dato.getString("horas_transcurridas"))
                    }
                    else{
                        Toast.makeText(applicationContext, obj.getString("response").toString(), Toast.LENGTH_LONG).show()
                    }

                } catch (e: JSONException){
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_LONG).show()
                }
            },
            { volleyError-> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show() })
        rq.add(jsoresp)
    }

    private fun pagar(cod_contacto: String){
        val campos = JSONObject()
        campos.put("accion", "pagar")
        campos.put("cod_carro", cod_contacto)
        campos.put("valor_pago", txtPayValor.text.toString())
        campos.put("numero_horas", txtPayHoras.text.toString())
        campos.put("estado_carro", "Pagado")

        val rq = Volley.newRequestQueue(this)
        val jsoresp = JsonObjectRequest(Request.Method.POST, apis, campos, {
                s->
            try {
                val obj = (s)
                if (obj.getBoolean("estado")){
                    Toast.makeText(applicationContext, obj.getString("response"), Toast.LENGTH_SHORT).show()
                    val form_registrar = Intent(this, form_registrar::class.java)
                    startActivity(form_registrar)
                } else{
                    Toast.makeText(applicationContext, obj.getString("response"), Toast.LENGTH_SHORT).show()
                }
            } catch (e: JSONException){
                Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
            }
        },
            {volleyError-> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_SHORT).show()})
        rq.add(jsoresp)
    }
}