package com.example.smartsevak

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDate

class ReportActivity : AppCompatActivity() {
    private val footer = Footer(this)
    private val header = Header(this)
    private lateinit var reportTypeDropDown: AutoCompleteTextView
    private lateinit var reportLocationET: EditText
    private lateinit var reportDescET: EditText
    private lateinit var reportSubmitButton: Button
    private lateinit var reportProgressBar: ProgressBar
    private val db = Firebase.firestore
    private lateinit var userName: String
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        header.onClickHeader()
        footer.onCLickFooter()

        val languages = resources.getStringArray(R.array.report_type)
        // create an array adapter and pass the required parameter
        // in our case pass the context, drop down layout , and array.
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, languages)
        // get reference to the autocomplete text view
        val autocompleteTV = findViewById<AutoCompleteTextView>(R.id.reportTypeDropDown)
        // set adapter to the autocomplete tv to the arrayAdapter
        autocompleteTV.setAdapter(arrayAdapter)

        inIt()
        reportSubmitButton.setOnClickListener {
            reportProgressBar.visibility = View.VISIBLE
            val userMap = hashMapOf(
                "Date" to (LocalDate.now().toString()),
                "userName" to userName,
                "ReportType" to (reportTypeDropDown.text.toString()),
                "Location" to (reportLocationET.text.toString()),
                "ReportDescription" to (reportDescET.text.toString())
            )
            db.collection("userReport").document(userId).set(userMap)
                .addOnSuccessListener {
                    Toast.makeText(this,"Reported",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,MainActivity::class.java))
                    reportProgressBar.visibility = View.VISIBLE
                }
                .addOnFailureListener {
                    Toast.makeText(this,"Failed to report",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun inIt(){
        reportTypeDropDown = findViewById(R.id.reportTypeDropDown)
        reportLocationET = findViewById(R.id.reportLocationET)
        reportDescET = findViewById(R.id.reportDescET)
        reportSubmitButton = findViewById(R.id.reportSubmitButton)
        reportProgressBar = findViewById(R.id.reportProgressBar)
        reportProgressBar.visibility = View.INVISIBLE
        val docRef = db.collection("user").document(userId)
        docRef.get()
            .addOnSuccessListener {document ->
                if(document != null){
                    userName = document.data!!["name"].toString()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this,"Failed to Retrieve Data", Toast.LENGTH_SHORT).show()
            }
    }
}