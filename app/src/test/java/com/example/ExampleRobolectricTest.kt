package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Kids Food Chef", appName)
  }

  @Test
  fun `verify chef level calculation`() {
    val junior = com.example.data.model.ChefLevel.fromXp(50)
    assertEquals(com.example.data.model.ChefLevel.JUNIOR_CHEF, junior)

    val superChef = com.example.data.model.ChefLevel.fromXp(1200)
    assertEquals(com.example.data.model.ChefLevel.SUPER_CHEF, superChef)
  }
}
