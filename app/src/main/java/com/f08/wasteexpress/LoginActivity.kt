package com.f08.wasteexpress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import com.f08.wasteexpress.Models.User
import com.f08.wasteexpress.Utils.mUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_login.*
import kotlinx.android.synthetic.main.layout_register.*
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class LoginActivity : AppCompatActivity() {
    lateinit var mAuth : FirebaseAuth
    lateinit var mRef:DatabaseReference
    private lateinit var viewsList:ArrayList<View>
    lateinit var mUtils: mUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewsList = arrayListOf(edt_login_email,edt_login_password,edt_register_email,edt_register_password,login_btn,register_btn)
        mAuth= FirebaseAuth.getInstance()
        mRef = FirebaseDatabase.getInstance().reference.child("users")
        mUtils = mUtils(this)

        goLoginLabel.setOnClickListener {
            login_layout_included.visibility= View.VISIBLE
            register_layout_included.visibility= View.GONE
        }
        goRegisterLabel.setOnClickListener {
            login_layout_included.visibility= View.GONE
            register_layout_included.visibility= View.VISIBLE
        }
        val editEmailLogin = findViewById<EditText>(R.id.edt_login_email)
        val editPasswordLogin = findViewById<EditText>(R.id.edt_login_password)

        val editFirstName = findViewById<EditText>(R.id.edt_register_firstname)
        val editLastName = findViewById<EditText>(R.id.edt_register_lastname)
        val editEmailRegister = findViewById<EditText>(R.id.edt_register_email)
        val editPasswordRegister = findViewById<EditText>(R.id.edt_register_password)

        login_btn.setOnClickListener {
            if(editEmailLogin.text.isEmpty()){
                MotionToast.createColorToast(this,
                    "Erreur de données",
                    "Veuillez saisir votre adresse e-mail et réessayer",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null)
                editEmailLogin.error = "Veuillez saisir votre adresse e-mail"
            }else if(editPasswordLogin.text.isEmpty()){
                MotionToast.createColorToast(this,
                    "Erreur de données",
                    "Veuillez saisir votre mot de passe et réessayer",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null)
                editPasswordLogin.error = "Veuillez saisir votre mot de passe"
            }else{
                goLogin(editEmailLogin.text.toString().trim(),editPasswordLogin.text.toString().trim())
            }
        }

        register_btn.setOnClickListener {
            if(editFirstName.text.isEmpty()){
                MotionToast.createColorToast(this,
                    "Erreur de données",
                    "Veuillez saisir votre prénom et réessayer",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null)
                editFirstName.error = "Veuillez saisir votre prénom"
            } else if(editLastName.text.isEmpty()){
                MotionToast.createColorToast(this,
                    "Erreur de données",
                    "Veuillez saisir votre nom de famille et réessayer",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null)
                editLastName.error = "Veuillez saisir votre nom de famille"
            } else if(editEmailRegister.text.isEmpty()){
                MotionToast.createColorToast(this,
                    "Erreur de données",
                    "Veuillez saisir votre adresse e-mail et réessayer",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null)
                editEmailRegister.error = "Veuillez saisir votre adresse e-mail"
            } else if(editPasswordRegister.text.isEmpty()){
                MotionToast.createColorToast(this,
                    "Erreur de données",
                    "Veuillez saisir votre mot de passe et réessayer",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null)
                editPasswordRegister.error = "Veuillez saisir votre mot de passe"
            } else {
                goRegister(editEmailRegister.text.toString().trim(), editPasswordRegister.text.toString().trim(),
                editFirstName.text.toString().trim(),editLastName.text.toString().trim())
            }
        }




    }

    fun goLogin(email: String, password: String) {
        mUtils(this).runLoading(viewsList, login_signup_layout)
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userUid = mAuth.currentUser!!.uid
                if (task.isSuccessful) {
                    MotionToast.createColorToast(this,
                        "Opération réussie",
                        "Vous êtes connecté à votre compte avec succès",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null)
                    startActivity(Intent(this, MainActivity::class.java))
                    mUtils(this).stopLoading(viewsList, login_signup_layout)
                    finish()

                } else {
                    MotionToast.createColorToast(this,
                        "Une erreur s'est produite. Veuillez vérifier votre connexion Internet.",
                        task.exception!!.message!!,
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        null)
                    mUtils(this).stopLoading(viewsList, login_signup_layout)
                }
            } else {
                MotionToast.createColorToast(this,
                    "Une erreur s'est produite. Veuillez vérifier votre connexion Internet.",
                    task.exception!!.message!!,
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null)
                mUtils(this).stopLoading(viewsList, login_signup_layout)
            }
        }
    }


    fun goRegister(email:String,password:String,first_name:String,last_name:String){
        mUtils(this).runLoading(viewsList,login_signup_layout)
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                val userUid = mAuth.currentUser!!.uid
                val radioGroup = findViewById<RadioGroup>(R.id.rg_user_type)
                val client_type = getCheckedRadioButtonText(radioGroup)
                val user = User(userUid,email,first_name,last_name,client_type)
                mRef.child(userUid).setValue(user).addOnCompleteListener {
                    if (task.isSuccessful){
                        MotionToast.createColorToast(this,
                            "Opération réussie",
                            "Compte créé avec succès",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            null)
                        mUtils.saveUser(user)
                        mUtils.stopLoading(viewsList,login_signup_layout)
                        startActivity(Intent(this,MainActivity::class.java))
                        finish()
                    }else{
                        MotionToast.createColorToast(this,
                            "Une erreur s'est produite. Veuillez vérifier votre connexion Internet",
                            task.exception!!.message!!,
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            null)
                        mUtils(this).stopLoading(viewsList,login_signup_layout)
                    }
                }

            }else{
                MotionToast.createColorToast(this,
                    "Une erreur s'est produite. Veuillez vérifier votre connexion Internet",
                    task.exception!!.message!!,
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    null)
                mUtils(this).stopLoading(viewsList,login_signup_layout)
            }

        }
    }

    fun getCheckedRadioButtonText(radioGroup: RadioGroup): String {
        // Get the ID of the checked RadioButton
        val checkedRadioButtonId = radioGroup.checkedRadioButtonId

        // If no RadioButton is checked, return an empty string
        if (checkedRadioButtonId == -1) {
            return ""
        }

        // Get the checked RadioButton
        val checkedRadioButton = radioGroup.findViewById<RadioButton>(checkedRadioButtonId)

        // Get the text of the checked RadioButton and return it
        return checkedRadioButton.text.toString()
    }





}