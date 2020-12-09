package tests.com.example.contacts

import android.content.ContentProviderOperation
import android.content.ContentValues
import android.content.Context
import android.provider.ContactsContract
import android.widget.Toast
import com.example.contacts.Contact
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue


fun <T> T?.checkThatNotNull(): T {
    assertNotNull(this)
    return this!!
}

inline fun <reified T> Any.checkType(): T {
    assertTrue(this is T)
    return this as T
}

fun Context.publishContact(contact : Contact) {

    val ops = ArrayList<ContentProviderOperation>()

    ops.add(
        ContentProviderOperation.newInsert(
            ContactsContract.RawContacts.CONTENT_URI
        )
            .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
            .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
            .build()
    )

    ops.add(
        ContentProviderOperation.newInsert(
            ContactsContract.Data.CONTENT_URI
        )
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
            )
            .withValue(
                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                contact.name
            ).build()
    )

    ops.add(
        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(
                ContactsContract.Data.MIMETYPE,
                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
            )
            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.phoneNumber)
            .withValue(
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
            )
            .build()
    )
    // Asking the Contact provider to create a new contact
    contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
}