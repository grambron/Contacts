package com.example.contacts

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import android.net.Uri.fromParts
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import com.example.myapplicationkotlin.R


class MainActivity : AppCompatActivity() {

    private val myRequestId = 10
    var contacts = emptySet<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        my_recycler_view.layoutManager = LinearLayoutManager(this)
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS),
                myRequestId
            )
        } else {
            run()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            myRequestId -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    run()
                } else {
                    Toast
                        .makeText(
                            this,
                            "Don't work with out your permission",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
                return
            }
        }
    }

    fun run (){
        contacts = fetchAllContacts()
        send(contacts)
        inputSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val ans: MutableSet<Contact> = filtration(contacts, p0)
                send(ans)
            }
        })
    }

    fun send(set: Set<Contact>) {
        val ans: MutableList<Contact> = mutableListOf()
        for (i in set) {
            ans.add(i)
        }
        my_recycler_view.adapter = UserAdapter(ans) {
            Toast
                .makeText(
                    this@MainActivity,
                    "Clicked on user ${it.name}",
                    Toast.LENGTH_SHORT
                )
                .show()
            val phoneIntent = Intent(
                Intent.ACTION_DIAL, fromParts(
                    "tel", it.phoneNumber, null
                )
            )
            startActivity(phoneIntent)
        }
    }

}

fun filtration(contacts: Set<Contact>, charSequence: CharSequence?): MutableSet<Contact> {
    var ans: MutableSet<Contact> = mutableSetOf()
    if (charSequence == null) {
        ans = contacts as MutableSet<Contact>
    } else {
        for (i in contacts) {
            if (i.name.contains(charSequence, ignoreCase = true)) {
                ans.add(i)
            } else {
                val re = Regex("[^\\d^\\w]")
                val numberNumbers = re.replace(i.phoneNumber, "");
                val sequenceNumbers = re.replace(charSequence, "");
                if (numberNumbers.contains(sequenceNumbers)) {
                    ans.add(i)
                }
            }
        }
    }
    return ans
}
