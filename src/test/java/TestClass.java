import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by User on 01.11.2016.
 */
public class TestClass {

    private WebDriver driver;
    private WebDriverWait wait;
    private String screenshotPath = "src/test/resources/testScreenshot01.png";
    private String screenshotPath2 = "src/test/resources/testScreenshot02.png";
    private String absolutePath = null;
    private String baseUrl = "https://mail.google.com";
    private String nextButtonLocator = "//input[@id='next']";
//    private String expectedErrorText = "Введите адрес электронной почты.";
//    private String expectedEmailErrorText = "Введите допустимый адрес электронной почты.";
    private String expectedEmailErrorText = "Please enter a valid email address.";
    private String expectedErrorText = "Please enter your email.";
    private String errorLocator = "//*[@id='errormsg_0_Email']";
    private String emailInputLocator = "//*[@id='Email']";
    private String passwordLocator = "//*[@id='Passwd']";
    private String signInLocator = "//*[@id='signIn']";
    private String invalidEmail = "@%";
    private String validEmail = "andrygrigoryk@gmail.com";
    private String emailPass = "salmon123";
    private String staySignedInCheckboxLocator = "//*[@id='PersistentCookie']";
    private String accountIconLocator = "//*[contains(@class, 'gb_b gb_db gb_R')]";
    private String accountEmailLocator = "//*[@class='gb_vb']";
    private String newEmail = "//*[@id=':j2']//*[@role='button']";
    private String recepient = "//input[@class='wA']/following-sibling::textarea[@name='to']";
    private String subject = "//input[@name='subjectbox']";
    private String subjectText = "Test";
    private String sendButton = "//div[.='Отправить'][@role='button']";
    private String testEmail = "//div[@class='xT']//span[.='Test']";
    private String inboxEmail = "//a[contains(@title, 'Входящие')]";
    private String attachButton = "//*[contains(@class, 'aMZ')][contains(@class, 'a1')]";
    private String headerSubject = "//h2[@class='hP']";
    private String savedMessage = "//a[@class='dO']";
    private String attachedImage = "//img[@class='aQG aYB']";

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

        File screenShot = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenShot, new File(screenshotPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        File newFile = new File(screenshotPath);
        absolutePath = newFile.getAbsolutePath();

        System.out.println("Page screenshot was saved at: " + screenshotPath);
        System.out.println("Absolute path: " + absolutePath);

        driver.findElement(By.xpath(newEmail)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sendButton)));
        driver.findElement(By.xpath(recepient)).sendKeys(validEmail);
        driver.findElement(By.xpath(subject)).sendKeys(subjectText);

        driver.findElement(By.xpath(attachButton)).click();
        StringSelection selection = new StringSelection(absolutePath);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(savedMessage)));

        driver.findElement(By.xpath(sendButton)).click();
        driver.findElement(By.xpath(inboxEmail)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(testEmail)));
        driver.findElement(By.xpath(testEmail)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(headerSubject)));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(attachedImage)));

        File screenShot2 = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenShot2, new File(screenshotPath2));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Inbox email screenshot was saved at: " + screenshotPath2);
    }

    @After
    public void tearDown() {
        driver.close();
    }

}