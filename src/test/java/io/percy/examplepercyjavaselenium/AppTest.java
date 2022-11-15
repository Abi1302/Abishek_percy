package io.percy.examplepercyjavaselenium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import io.percy.selenium.Percy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Unit test for example App.
 */
public class AppTest {
    private static final String TEST_URL = "https://browserstack.com";
    private static ExecutorService serverExecutor;
    private static HttpServer server;
    private static WebDriver driver;
    private static Percy percy;

    @BeforeEach
    public void startAppAndOpenBrowser() throws IOException {
        System.setProperty("webdriver.gecko.driver","/Users/abishek/Documents/Selenium Jars and Drivers/geckodriver");
        // Create a threadpool with 1 thread and run our server on it.
        serverExecutor = Executors.newFixedThreadPool(1);
        server = App.startServer(serverExecutor);
        FirefoxOptions options = new FirefoxOptions();
        options.setHeadless(true);
        driver = new FirefoxDriver(options);
        percy = new Percy(driver);
    }

    @AfterEach
    public void closeBrowser() {
        // Close our test browser.
        driver.quit();
        // Shutdown our server and make sure the threadpool also terminates.
        server.stop(1);
        serverExecutor.shutdownNow();
    }

    @Test
    public void loadsHomePage() {
        driver.get(TEST_URL);
        WebElement element = driver.findElement(By.xpath("//*[@id=\"post-26\"]"));
        assertNotNull(element);

        // Take a Percy snapshot.
        percy.snapshot("Home Page");
    }

    @Test
    public void acceptsANewTodo() {
        driver.get(TEST_URL+"/pricing");

        // We start with zero todos.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement checkBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"live-plans\"]")));
        assertNotNull(checkBox);
        percy.snapshot("pricing");
    }

    @Test
    public void letsYouCheckOffATodo() {
        driver.get(TEST_URL+"/integrations/automate");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement popIntgr = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"post-2245\"]")));
        assertNotNull(popIntgr);
        percy.snapshot("Automate");
    }
}
