import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by User on 01.11.2016.
 */
public class TestClass {

    private WebDriver driver;
    WebDriverWait wait;
    private String baseUrl = "https://mail.google.com";
    private String nextButtonLocator = "//input[@id='next']";
    private String expectedErrorText = "Введите адрес электронной почты.";
    private String errorLocator = "//*[@id='errormsg_0_Email']";
    private String emailInputLocator = "//*[@id='Email']";
    private String passwordLocator = "//*[@id='Passwd']";
    private String signInLocator = "//*[@id='signIn']";
    private String invalidEmail = "@%";
    private String expectedEmailErrorText = "Введите допустимый адрес электронной почты.";
    private String validEmail = "andrygrigoryk@gmail.com";
    private String emailPass = "salmon123";
    private String staySignedInCheckboxLocator = "//*[@id='PersistentCookie']";
    private String accountIconLocator = "//*[contains(@class, 'gb_b gb_db gb_R')]";
    private String accountEmailLocator = "//*[@class='gb_vb']";

    @Before
    public void setup() {
        ChromeDriverManager.getInstance().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 10);

    }

    @Test
    public void testInvalidData() {
        driver.navigate().to(baseUrl);
        driver.findElement(By.xpath(nextButtonLocator)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(errorLocator)));
        String actualErrorText = driver.findElement(By.xpath(errorLocator)).getText();
        assertEquals("error message text isn't as expected", expectedErrorText, actualErrorText);

        driver.findElement(By.xpath(emailInputLocator)).sendKeys(invalidEmail);
        driver.findElement(By.xpath(nextButtonLocator)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(errorLocator)));
        String actualEmailErrorText = driver.findElement(By.xpath(errorLocator)).getText();
        assertEquals("error message text isn't as expected", expectedEmailErrorText, actualEmailErrorText);
    }

    @Test
    public void testValidData() throws InterruptedException {
        driver.navigate().to(baseUrl);
        driver.findElement(By.xpath(emailInputLocator)).sendKeys(validEmail);
        driver.findElement(By.xpath(nextButtonLocator)).click();
        driver.findElement(By.xpath(passwordLocator)).sendKeys(emailPass);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(staySignedInCheckboxLocator)));
        if (driver.findElement(By.xpath(staySignedInCheckboxLocator)).isSelected()){
            driver.findElement(By.xpath(staySignedInCheckboxLocator)).click();
        }
        driver.findElement(By.xpath(signInLocator)).click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(accountIconLocator)));
        driver.findElement(By.xpath(accountIconLocator)).click();

        String actualAccount = driver.findElement(By.xpath(accountEmailLocator)).getText();
        assertTrue("wrong account", actualAccount.contains(validEmail));
    }

    @After
    public void tearDown() {
        driver.close();
    }

}