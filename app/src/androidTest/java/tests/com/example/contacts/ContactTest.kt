package tests.com.example.contacts

import com.example.contacts.Contact
import com.example.contacts.filtration
import org.junit.Assert.*
import org.junit.Test
import java.util.*

class ContactTest() {
    private val contact = Contact(
        name = "Vasily",
        phoneNumber = "8 (800) 555-35-35"
    )

    private val contactList = HashSet<Contact>()

    init {
        contactList.add(contact)
    }

    @Test
    fun getContactsByName() {
        assertTrue(filtration(contactList, "vas").size > 0)
        assertTrue(filtration(contactList, "v").size > 0)
        assertTrue(filtration(contactList, "").size > 0)
        assertTrue(filtration(contactList, "ily").size > 0)
        assertTrue(filtration(contactList, "vaS").size > 0)
        assertFalse(filtration(contactList, "AAA").size > 0)
        assertFalse(filtration(contactList, "Vas ily").size > 0)
    }

    @Test
    fun getContactsByPhoneNumber() {
        assertTrue(filtration(contactList, "8 (800) 555-35-35").size > 0)
        assertTrue(filtration(contactList, "88005553535").size > 0)
        assertTrue(filtration(contactList, "88--  005()-55-35+35").size > 0)
        assertFalse(filtration(contactList, "7-77").size > 0)
    }
}