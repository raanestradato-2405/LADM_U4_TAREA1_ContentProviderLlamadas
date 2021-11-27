package mx.tecnm.tepic.ladm_u4_tarea1_contentprovidersllamadas

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CallLog
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main2.*
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList

class MainActivity2 : AppCompatActivity() {

    val siLlamadaPerdida = 1
    val siLlamada = 2
    val siLecturaContactos = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_CONTACTS )!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.READ_CONTACTS),siLecturaContactos)

        }

        btnLlamadasEntrantes.setOnClickListener {
            textView.setText("Llamadas Entrantes")
            mostrarallamadasEntrantes()
        }

        btnLlamadasPerdidas.setOnClickListener {
            textView.setText("Llamadas Perdidas")
            mostrarllamadasPerdidas()

        }
    }

    @SuppressLint("Range")
    private fun mostrarllamadasPerdidas() {
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG),siLlamadaPerdida)

        }else{
            var llamada = ArrayList<String>()
            val selection: String = CallLog.Calls.TYPE + " = " + CallLog.Calls.MISSED_TYPE
            var  cursor:Cursor?=null

            try {
                cursor = contentResolver.query(Uri.parse("content://call_log/calls"),null,selection,null,null,null)

                var registros = ""

                while (cursor?.moveToNext()!!){
                    val nombre = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
                    val numero = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                    val tipo = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))
                    val fecha = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE))

                    registros = "Nombre: "+nombre+"\nTelefono: "+numero+"\nTipo: "+ tipo+"\nFecha: "+Date(fecha.toLong())
                    llamada.add(registros)

                }


            }catch (ex: java.lang.Exception){
                Toast.makeText(this,ex.message,Toast.LENGTH_LONG)
            } finally {
                listaRegistros.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,llamada)
                cursor?.close()
            }
        }



    }

    @SuppressLint("Range")
    private fun mostrarallamadasEntrantes() {
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_CALL_LOG),siLlamada)
        }else{
            var llamadas = ArrayList<String>()
            var cursor: Cursor? = null

            try{
                cursor = contentResolver.query(Uri.parse("content://call_log/calls"),null,null,null,null)

                var registro = ""

                while (cursor?.moveToNext()!!){
                    val nombre = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME))
                    val numero = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER))
                    val tipo  = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE))
                    val fecha = cursor.getString(cursor.getColumnIndex(CallLog.Calls.DATE))

                    registro = "Nombre: "+nombre+"\nTelefono: "+numero+"\nTipo: "+ tipo+"\nFecha: "+Date(fecha.toLong())
                    llamadas.add(registro)

                }

            }catch (ex : Exception){
                Toast.makeText(this,ex.message,Toast.LENGTH_LONG)
            } finally {
                listaRegistros.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,llamadas)
                cursor?.close()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == siLecturaContactos){
            setTitle("PERMISO OTORGADO")
        }
    }
}