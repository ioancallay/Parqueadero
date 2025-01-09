package com.example.parqueadero

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.android.volley.Request
import org.json.JSONException

class form_registrar : AppCompatActivity() {

    lateinit var txtCedula: EditText
    lateinit var txtNombre: EditText
    lateinit var txtPlaca: EditText
    lateinit var txtEntrada: EditText
    lateinit var txtSalida: EditText
    lateinit var txtValor: EditText
    lateinit var txtCodCarro: EditText
    lateinit var txtCodUsuario: EditText
    lateinit var btnNuevo: Button
    lateinit var btnGuardar: Button
    lateinit var btnPagar: Button
    lateinit var btnEliminar: Button
    lateinit var btnCancelar: Button
    lateinit var listarCarros: ListView

    val codigos_carros = ArrayList<String>()
    val apis: String = "https://agenda.ioasystem.com/api_carros.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_form_registrar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mapeo()
        desactivarCajas()
        desactivarBotones()
        todos()

        btnNuevo.setOnClickListener {
            nuevoIngreso()
            activarCajas()
        }

        btnCancelar.setOnClickListener {
            desactivarCajas()
            desactivarBotones()
            limpiarCajas()
        }

        btnPagar.setOnClickListener {
            val form_pago = Intent(this, form_salida::class.java)
            val bundle = Bundle()
            bundle.putString("cod_carro", txtCodCarro.text.toString())
            form_pago.putExtras(bundle)
            startActivity(form_pago)
        }

        listarCarros.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(applicationContext, codigos_carros[i], Toast.LENGTH_LONG).show()
            editar(codigos_carros[i])
            activarPago()
        }

        btnGuardar.setOnClickListener {
            when{
                txtCedula.text.isEmpty()->{
                    Toast.makeText(applicationContext, "Ingrese No. Cedula", Toast.LENGTH_LONG).show()
                }
                txtNombre.text.isEmpty()->{
                    Toast.makeText(applicationContext, "Ingrese el nombre", Toast.LENGTH_LONG).show()
                }
                txtPlaca.text.isEmpty()->{
                    Toast.makeText(applicationContext, "Ingrese No. Placa", Toast.LENGTH_LONG).show()
                }
                else->{
                    insertar()
                    desactivarCajas()
                    desactivarBotones()
                    limpiarCajas()
                }
            }
        }
    }

    fun mapeo(){
        txtCodCarro = findViewById(R.id.txt_codcarro)
        txtCodUsuario = findViewById(R.id.txt_codusuario)
        txtCedula = findViewById(R.id.txt_cedula)
        txtNombre = findViewById(R.id.txt_nombre)
        txtPlaca = findViewById(R.id.txt_placa)
        txtEntrada = findViewById(R.id.txt_entrada)
        txtSalida = findViewById(R.id.txt_salida)
        txtValor = findViewById(R.id.txt_valor)
        btnNuevo = findViewById(R.id.btn_nuevo)
        btnGuardar = findViewById(R.id.btn_guardar)
        btnPagar = findViewById(R.id.btn_pagar)
        btnEliminar = findViewById(R.id.btn_eliminar)
        btnCancelar = findViewById(R.id.btn_cancelar)
        listarCarros = findViewById(R.id.list_carros)
    }

    fun nuevoIngreso(){
        btnNuevo.isEnabled = false
        btnGuardar.isEnabled = true
        btnPagar.isEnabled = false
        btnEliminar.isEnabled = false
        btnCancelar.isEnabled = true

        btnNuevo.isVisible = false
        btnGuardar.isVisible = true
        btnPagar.isVisible = false
        btnEliminar.isVisible = false
        btnCancelar.isVisible = true

    }

    fun desactivarBotones(){
        btnNuevo.isEnabled = true
        btnGuardar.isEnabled = false
        btnPagar.isEnabled = false
        btnEliminar.isEnabled = false
        btnCancelar.isEnabled = false

        btnNuevo.isVisible = true
        btnGuardar.isVisible = false
        btnPagar.isVisible = false
        btnEliminar.isVisible = false
        btnCancelar.isVisible = false
    }

    fun activarCajas(){
        txtCedula.isEnabled = true
        txtNombre.isEnabled = true
        txtPlaca.isEnabled = true
        txtEntrada.isEnabled = true
        txtSalida.isEnabled = true
        txtSalida.isEnabled = false
        txtValor.isEnabled = false
        txtSalida.isVisible = false
        txtValor.isVisible = false
    }

    fun desactivarCajas(){
        txtCedula.isEnabled = false
        txtNombre.isEnabled = false
        txtPlaca.isEnabled = false
        txtEntrada.isEnabled = false
        txtSalida.isEnabled = false
        txtSalida.isEnabled = false
        txtValor.isEnabled = false
    }

    fun activarPago(){
        btnNuevo.isVisible = false
        btnGuardar.isVisible = false
        btnPagar.isVisible = true
        btnEliminar.isVisible = false
        btnCancelar.isVisible = true

        btnNuevo.isEnabled = false
        btnGuardar.isEnabled = false
        btnPagar.isEnabled = true
        btnEliminar.isEnabled = false
        btnCancelar.isEnabled = true

        txtSalida.isVisible = true
        txtValor.isVisible = true
    }

    fun limpiarCajas(){
        txtCedula.setText("")
        txtNombre.setText("")
        txtPlaca.setText("")
        txtEntrada.setText("")
        txtSalida.setText("")
        txtValor.setText("")
    }

    private fun todos(){
        codigos_carros.clear()

        val intrapersonal = ArrayList<String>()
        val campos = JSONObject()
        campos.put("accion", "todos")
        val rq = Volley.newRequestQueue(this)
        val jsoresp = JsonObjectRequest(Request.Method.POST, apis, campos,
            {
                    s->
                try {
                    val obj = (s)
                    if(obj.getBoolean("estado")){
                        val array = obj.getJSONArray("data")
                        for(i in 0..<array.length()){
                            val fila = array.getJSONObject(i)
                            val horaSalida = if (fila.isNull("hora_salida")) {
                                "Sin hora de salida"
                            } else {
                                fila.getString("hora_salida")
                            }

                            val pago = if(fila.isNull("valor_pago")){
                                "0"
                            } else{
                                fila.getString("valor_pago")
                            }

                            intrapersonal.add(

                                fila.getString("placa_carro")
                                        + "\n" + fila.getString("nombre_carro") + " " + fila.getString("cedula_carro")
                                        + "\nHora entrada: " + fila.getString("hora_entrada")
                                        + "\nHora salida: " + horaSalida
                                        + "\nValor: $ " + pago
                                        + "\nEstado: " + fila.getString("estado_carro")
                            )
                            codigos_carros.add(fila.getString("cod_carro"))
                        }
                        val adapterList = ArrayAdapter(this, android.R.layout.simple_list_item_1, intrapersonal)
                        listarCarros.adapter = adapterList
                        adapterList.notifyDataSetChanged()
                    }
                    else{
                        Toast.makeText(applicationContext, obj.getString("response").toString(), Toast.LENGTH_SHORT).show()
                    }

                } catch (e: JSONException){
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
                }
            },
            { volleyError-> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show() })
        rq.add(jsoresp)
    }

    private fun editar(cod_carro:String){
        txtCodCarro.setText(cod_carro)
        val campos = JSONObject()
        campos.put("accion", "unCarro")
        campos.put("cod_carro", cod_carro)
        val rq = Volley.newRequestQueue(this)
        val jsoresp = JsonObjectRequest(Request.Method.POST, apis, campos,
            {
                    s->
                try {
                    val obj = (s)
                    if(obj.getBoolean("estado")){
                        val array = obj.getJSONArray("data")
                        val dato = array.getJSONObject(0)
                        val horaSalida = if (dato.isNull("hora_salida")) {
                            "Sin hora de salida"
                        } else {
                            dato.getString("hora_salida")
                        }

                        val pago = if(dato.isNull("valor_pago")){
                            "0"
                        } else{
                            dato.getString("valor_pago")
                        }

                        txtCedula.setText(dato.getString("cedula_carro"))
                        txtNombre.setText(dato.getString("nombre_carro"))
                        txtPlaca.setText(dato.getString("placa_carro"))
                        txtEntrada.setText(dato.getString("hora_entrada"))
                        txtSalida.setText(horaSalida)
                        txtValor.setText(pago)
                        todos()
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

    private fun insertar(){
        val campos = JSONObject()
        campos.put("accion", "insertar")
        campos.put("cedula_carro", txtCedula.text.toString())
        campos.put("nombre_carro", txtNombre.text.toString())
        campos.put("placa_carro", txtPlaca.text.toString())
        campos.put("hora_entrada", txtEntrada.text.toString())
        campos.put("cod_usuario", txtCodUsuario.text.toString())
        campos.put("estado_carro", "Pendiente")

        val rq = Volley.newRequestQueue(this)
        val jsoresp = JsonObjectRequest(Request.Method.POST, apis, campos,
            {
                    s->
                try {
                    val obj=(s)
                    if (obj.getBoolean("estado")){
                        Toast.makeText(applicationContext, obj.getString("response").toString(), Toast.LENGTH_SHORT).show()
                        todos()
                    }
                    else{
                        Toast.makeText(applicationContext, obj.getString("response").toString(), Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException){
                    Toast.makeText(applicationContext, e.toString(), Toast.LENGTH_SHORT).show()
                }
            },
            { volleyError-> Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_SHORT).show() })
        rq.add(jsoresp)
    }
}