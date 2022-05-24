import com.example.rollcall.helpers.HelperFunctions.GetDayEnding
import org.junit.Assert.assertEquals
import org.junit.Test

class GetDayEndingTest {
    @Test
    @Throws(Exception::class)
    fun getDayEnding_valid_first() {
        assertEquals("st", GetDayEnding(1))
    }
    @Test
    @Throws(Exception::class)
    fun getDayEnding_valid_second() {
        assertEquals("nt", GetDayEnding(2))
    }
    @Test
    @Throws(Exception::class)
    fun getDayEnding_valid_third() {
        assertEquals("rd", GetDayEnding(3))
    }
    @Test
    @Throws(Exception::class)
    fun getDayEnding_valid_fourth() {
        assertEquals("th", GetDayEnding(4))
    }
}