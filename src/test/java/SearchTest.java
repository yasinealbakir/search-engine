import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class SearchTest {

    WebDriver driver;
    WebDriverWait wait;
    ChromeOptions chromeOptions;
    By txtGoogleSearch = By.name("q");
    By googleResult = By.xpath("//div[@class='MjjYud']/div[@jscontroller='SC7lYd']/div");

    By txtYandexSearch = By.id("text");
    By yandexResult = By.xpath("//ul[@id='search-result']/li");
    List<Result> result = new ArrayList<Result>();

    public List<WebElement> finds(By locator) throws InterruptedException {
        return waitElementToAppear(locator, 10).findElements(locator);
    }

    public WebElement waitElementToAppear(By locator, int timeOutSecond) throws InterruptedException {
        slowDown(0.5);
        wait = new WebDriverWait(driver, timeOutSecond);
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void slowDown(double second) throws InterruptedException {
        Thread.sleep((long) (second * 1000));
    }

    @BeforeTest
    public void setup() {
        WebDriverManager.chromedriver().setup();
        chromeOptions = new ChromeOptions();
        chromeOptions.setHeadless(false);
        chromeOptions.addArguments("start-maximized");
        driver = new ChromeDriver(chromeOptions);
    }

    @Test
    public void googleSearch() throws InterruptedException {
        driver.get("https://www.google.com/");
        driver.findElement(txtGoogleSearch).sendKeys("Yazılım Testi", Keys.ENTER);
        slowDown(1);

        List<WebElement> resultList = finds(googleResult);

        for (int i = 0; i < resultList.size(); i++) {
            String listElementItem = resultList.get(i).getText();
            String lines[] = listElementItem.split("\\r?\\n");


            String title = lines[0];
            String url = lines[1];
            String description = lines[2];

            String[] splitUrl = url.split(">", 2);
            for (String a : splitUrl)
                result.add(new Result(title, a, description));
        }

    }


    @Test
    public void yandexSearch() throws InterruptedException {
        driver.get("https://yandex.com.tr/");
        driver.findElement(txtYandexSearch).sendKeys("Yazılım Testi", Keys.ENTER);
        slowDown(1);

        List<WebElement> resultList = finds(yandexResult);

        for (int i = 0; i < resultList.size(); i++) {
            String listElementItem = resultList.get(i).getText();
            String lines[] = listElementItem.split("\\r?\\n");

            String title = lines[0];
            String url = lines[1];
            String description = lines[2];

            String[] splitUrl = url.split("www.", 2);
            for (String a : splitUrl)
                result.add(new Result(title, a, description));


        }
    }

    @AfterTest
    public void clean() {
        result.forEach((n) -> System.out.println(n.url));
        driver.quit();

    }
}
