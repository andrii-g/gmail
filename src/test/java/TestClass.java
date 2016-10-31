import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by User on 01.11.2016.
 */
public class TestClass {

    private WebDriver driver;

    @Before
    public void setupClass() {
        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
    }

    @Test
    public void test() {
        driver.navigate().to("https://mail.google.com");
    }

    @After
    public void tearDown() {
        driver.close();
    }

}
