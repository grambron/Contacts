package com.example.contacts

import android.content.ContentValues
import android.content.Context
import android.provider.ContactsContract

data class Contact(  val name:  String,  val phoneNumber:  String)

fun Context.fetchAllContacts():  Set< Contact> {
    contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
        .use { cursor ->
            if (cursor ==  null) return emptySet()
            val builder =  HashSet< Contact>()
            while (cursor.moveToNext()) {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))?:  "N/A"
                var phoneNumber =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        ?: "N/A"
                phoneNumber = parse (phoneNumber)
                builder.add(Contact(name, phoneNumber))
            }
            return builder
        }
}

fun parse(str: String): String {
    var ans = str
    var i = 0
    while (i < ans.length) {
        if (ans[i] == ' ' || ans[i] == '-') {
            val first = ans.subSequence(0, i)
            val second = ans.subSequence(i + 1, ans.length)
            ans = first.toString() + second.toString()
            i = 0
        } else {
            i++
        }
    }
    if (ans.length > 1 && ans[0].toString() + ans[1].toString() == "+7") {
        ans = "8" + ans.subSequence(2, ans.length)
    }
    return ans
}
