package dev.wiprojekt.expansetracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar


class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val tvReg = findViewById<TextView>(R.id.tv_reg)
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val etLoginEmail = findViewById<EditText>(R.id.et_login_email)
        val etLoginPasswort = findViewById<EditText>(R.id.et_login_passwort)

        tvReg.setOnClickListener {
            val intent = Intent(this@Login, Registrierung::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            //Checken ob die Eingabefelder leer sind
            when {
                TextUtils.isEmpty(etLoginEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@Login,
                        "Bitte gebe eine Email Adresse ein!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(etLoginPasswort.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@Login,
                        "Bitte gebe eine Passwort ein!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email = etLoginEmail.text.toString().trim { it <= ' ' }
                    val pw = etLoginPasswort.text.toString().trim { it <= ' ' }
                    login_user(email, pw)
                }
            }
        }
    }

    fun login_user(email : String, pw : String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pw)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    Toast.makeText(
                        this@Login,
                        "Erfolgreich Angemeldet",
                        Toast.LENGTH_SHORT
                    ).show()
                    // Nutzer wird registriert und eingeloggt
                    val intent = Intent(this@Login, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // cleart überflüssige Activitys im Background

                    //Nutzer daten werden aus der Datenbank geladen
                    //val database = FirebaseDatabase.getInstance().getReference("user")
                    intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser!!.uid)
                    startActivity(intent)
                    updateUser()
                    finish()
                } else {
                    // Fehlerhafte Eingabe abfangen,
                    Toast.makeText(
                        this@Login,
                        task.exception!!.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun updateUser() {
        val data = Calendar.getInstance().time
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val database = FirebaseDatabase.getInstance().getReference("user")
        val update = mapOf<String, Any>(
            "lLT" to data.toString()
        )
        database.child(userId).updateChildren(update).addOnSuccessListener {
            Toast.makeText(
                this,
                userId, // Gibt den Grund des Fehlens aus, maybe noch auf simple Deutsche meldung abändern und den Fehler loggen in Firebase
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}