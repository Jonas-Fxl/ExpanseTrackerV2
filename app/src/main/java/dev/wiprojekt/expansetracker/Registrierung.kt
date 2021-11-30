package dev.wiprojekt.expansetracker

import dev.wiprojekt.expansetracker.databinding.ActivityRegistrierungBinding

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class Registrierung : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegistrierungBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val et_reg_email = binding.etRegEmail
        val et_lreg_passwort = binding.etLregPasswort

        binding.tvLog.setOnClickListener {
            intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        binding.btnReg.setOnClickListener {
            //Checken ob die Eingabefelder leer sind
            when {
                TextUtils.isEmpty(et_reg_email.text.toString().trim { it <= ' ' }) -> { // trim entfernt alle Leerzeichen
                    Toast.makeText(
                        this@Registrierung,
                        "Bitte gebe eine Email Adresse ein!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(et_lreg_passwort.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(
                        this@Registrierung,
                        "Bitte gebe eine Passwort ein!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else -> {
                    val email = et_reg_email.text.toString().trim { it <= ' ' }
                    val pw = et_lreg_passwort.text.toString().trim { it <= ' ' }

                    /**
                     * Es wird ein Firebase Nutzer im Authentication bereich erstellt
                     * @return ist eine Erfolgs / Fehlschalgsmeldung als Toast
                     */
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pw)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                //Firebase Nutzer registrieren mit !null Abfrage
                                val fireBaseUser: FirebaseUser = task.result!!.user!!
                                saveUser(email, pw)
                                storeUser(email, pw, fireBaseUser.uid)
                                Toast.makeText(
                                    this@Registrierung,
                                    "Registrierung war erfolgreich",
                                    Toast.LENGTH_SHORT
                                ).show()
                                // Nutzer wird registriert und eingeloggt
                                val intent = Intent(this@Registrierung, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // cleart überflüssige Activitys im Background
                                intent.putExtra("user_id", fireBaseUser.uid)
                                startActivity(intent)
                                finish()
                            } else {
                                // Fehlerhafte Eingabe abfangen,
                                Toast.makeText(
                                    this@Registrierung,
                                    task.exception!!.message.toString(), // Gibt den Grund des Fehlens aus, maybe noch auf simple Deutsche meldung abändern und den Fehler loggen in Firebase
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        }
    }

    /**
     * @param email : Die eingegebene E-mail-Adresse aus dem Eingabefeld et_reg_email
     * @param passwort: Das eingegebene Passwort aus dem Eingabefeld et_lreg_passwort
     */
    fun saveUser(email: String, passwort : String){
        val db = FirebaseFirestore.getInstance()
        val nutzer = hashMapOf(
            "E-Mail" to email,
            "Passwort" to passwort)
        db.collection("Nutzer").document(email)
            .set(nutzer)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }


    fun storeUser(email: String, passwort : String, userID : String){
        val db = FirebaseDatabase.getInstance().getReference("user")
        val nutzer = hashMapOf(
            "userID" to userID,
            "email" to email,
            "passwort" to passwort,
            "budget" to 0.0 )
        db.child(userID).setValue(nutzer)
        .addOnSuccessListener { Log.d(TAG, "Nutzer wurde Erfolgreich in der Datenbank erstellt") }
        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

}