package tests.com.example.contacts

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.*
import com.example.contacts.Contact
import org.hamcrest.Matchers
import org.junit.Assert
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class UiAutoTest {
    private lateinit var device: UiDevice
    private lateinit var context: Context


    init {
        // Launch the blueprint app
        val context =
            ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager
            .getLaunchIntentForPackage(SAMPLE_PACKAGE)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) // Clear out any previous instances
        context.startActivity(intent)

        context.publishContact(
            Contact(
                name = "Vasily",
                phoneNumber = "8 (800) 555-35-35"
            )
        )

        context.publishContact(
            Contact(
                name = "Vasya",
                phoneNumber = "1234567890"
            )
        )
    }

    @Before
    fun startMainActivityFromHomeScreen() {
        // Initialize UiDevice instance
        device =
            UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())

        // Start from the home screen
        device.pressHome()

        // Wait for launcher
        val launcherPackage = launcherPackageName
        Assert.assertThat(
            launcherPackage,
            Matchers.notNullValue()
        )
        device.wait(
            Until.hasObject(By.pkg(launcherPackage).depth(0)),
            LAUNCH_TIMEOUT
        )

        // Launch the blueprint app
        context =
            ApplicationProvider.getApplicationContext<Context>()
        val intent = context.packageManager
            .getLaunchIntentForPackage(SAMPLE_PACKAGE)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) // Clear out any previous instances
        context.startActivity(intent)

        context.publishContact(
            Contact(
                name = "Vasily",
                phoneNumber = "8 (800) 555-35-35"
            )
        )

        context.publishContact(
            Contact(
                name = "Vasya",
                phoneNumber = "1234567890"
            )
        )

        // Wait for the app to appear
        device.wait(
            Until.hasObject(By.pkg(SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT
        )
    }

    // online updates
    @Test
    fun smokeHasContacts() {
        val contacts: List<UiObject2> = device.findObjects(By.res(SAMPLE_PACKAGE, "line"))

        Assert.assertThat<List<UiObject2>>(
            contacts,
            Matchers.not(Matchers.empty<UiObject2>())
        )
    }

    @Test
    fun positiveSearchTest() {
        val searchFiled: UiObject2 = device.findObject(By.res(SAMPLE_PACKAGE, "inputSearch"))

        searchFiled.text = "vas"

        val contacts: List<UiObject2> = device.findObjects(By.res(SAMPLE_PACKAGE, "line"))

        Assert.assertThat<List<UiObject2>>(
            contacts,
            Matchers.not(Matchers.empty<UiObject2>())
        )
    }
    

    @Test
    fun onlineUpdateTest() {
        val contactsSize = device.findObjects(By.res(SAMPLE_PACKAGE, "line")).size
        device.pressHome()

        device.swipe(device.displayWidth / 2, device.displayHeight / 2, device.displayWidth / 2, 0, 5)
        device.findObject(By.desc("Telephone")).click()

        device.wait(
            Until.hasObject(By.pkg(SAMPLE_PACKAGE).depth(0)),
            LAUNCH_TIMEOUT
        )

        val newContactsSize = device.findObjects(By.res(SAMPLE_PACKAGE, "line")).size

        Assert.assertTrue(
            "Expected " + (contactsSize + 1) + " contacts, but got " + newContactsSize,
            contactsSize == newContactsSize - 1
        )
    }

    // Create launcher Intent
    private val launcherPackageName:

    // Use PackageManager to get the launcher package name
            String
        private get() {
            // Create launcher Intent
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)

            // Use PackageManager to get the launcher package name
            val pm =
                ApplicationProvider.getApplicationContext<Context>().packageManager
            val resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
            return resolveInfo.activityInfo.packageName
        }

    companion object {
        const val SAMPLE_PACKAGE = "com.example.contacts"
        const val LAUNCH_TIMEOUT: Long = 5000
    }
}