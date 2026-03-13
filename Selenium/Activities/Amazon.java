package selenium;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Amazon {

    public static void main(String[] args) {

        WebDriver driver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get("https://www.amazon.in/");
        System.out.println("Page Title is: " + driver.getTitle());

        WebElement searchBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("twotabsearchtextbox")));

        searchBox.sendKeys("iphone 17 pro");

        driver.findElement(By.id("nav-search-submit-button")).click();
WebElement product = wait.until(
        ExpectedConditions.visibilityOfElementLocated(
        By.xpath("//div[@data-component-type='s-search-result'][1]//h2/span")));

        String productName = product.getText();

        WebElement price = driver.findElement(By.xpath("(//span[@class='a-price-whole'])[1]"));
        String productPrice = price.getText();

        WebElement shipping = driver.findElement(By.xpath("(//span[contains(text(),'delivery') or contains(text(),'Delivery')])[1]"));
        String shippingDate = shipping.getText();

        System.out.println("Product Name: " + productName);
        System.out.println("Price: ₹" + productPrice);
        System.out.println("Shipping Info: " + shippingDate);

        driver.quit();
    }
}