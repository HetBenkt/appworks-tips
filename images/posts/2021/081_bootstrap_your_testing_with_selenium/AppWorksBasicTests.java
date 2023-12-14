package nl.bos.basics;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.logging.Logger;

public class AppWorksBasicTests {
    private Logger LOGGER = Logger.getLogger(this.getClass().getSimpleName());
    private WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "c:\\temp\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.get("http://192.168.56.107:8080/home/appworks_tips/app/start");
    }

    @Test
    public void openCloseBrowserTest() {
        LOGGER.info("startBrowserTest");

        driver.manage().window().maximize();

        Assert.assertEquals("AppWorks Platform Login", driver.getTitle());
    }

    @Test
    public void loginLogoutTest() {
        LOGGER.info("loginTest");

        //Login
        WebDriverWait waitForLoginScreenToLoad = new WebDriverWait(driver, 10);
        waitForLoginScreenToLoad.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonOK")));
        driver.findElement(By.id("username")).sendKeys("awdev");
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.id("buttonOK")).click();

        //Logout
        WebDriverWait waitForRuntimeScreenToLoad = new WebDriverWait(driver, 30);
        waitForRuntimeScreenToLoad.until(ExpectedConditions.visibilityOfElementLocated(By.className("page-host")));
        driver.findElement(By.cssSelector("button[aria-label='User options']")).click();
        driver.findElement(By.cssSelector("a[cmd='logout']")).click();

        WebDriverWait waitForLogoutIsDone = new WebDriverWait(driver, 10);
        waitForLogoutIsDone.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonOK")));

        Assert.assertEquals("AppWorks Platform Login", driver.getTitle());
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}