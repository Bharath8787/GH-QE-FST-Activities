package selenium;

import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class FindThirdIphoneAmazonIn {
    public static void main(String[] args) {
        // If geckodriver is not in PATH, set it like:
        // System.setProperty("webdriver.gecko.driver", "/path/to/geckodriver");

        WebDriver driver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));

        try {
            // 1) Open Amazon India
            driver.get("https://www.amazon.in/");
            System.out.println("Page title: " + driver.getTitle());

            // 2) Type "iphone" in search box and submit
            WebElement searchBox = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("twotabsearchtextbox"))
            );
            searchBox.clear();
            searchBox.sendKeys("iphone");
            searchBox.submit();

            // 3) Wait for search results to load
            By resultCards = By.cssSelector("div.s-main-slot div[data-component-type='s-search-result']");
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(resultCards, 2));

            List<WebElement> cards = driver.findElements(resultCards);

            // 4) From those cards, collect ones whose visible title contains "iPhone" (case-insensitive)
            List<WebElement> iphoneCards = new ArrayList<>();
            for (WebElement card : cards) {
                try {
                    WebElement titleEl = card.findElement(By.cssSelector("h2 a span"));
                    String title = titleEl.getText();
                    if (title != null && title.toLowerCase().contains("iphone")) {
                        iphoneCards.add(card);
                    }
                } catch (NoSuchElementException ignored) {
                    // ignore cards without normal product title (like banners)
                }
            }

            // 5) Ensure we have at least 3 iPhone items
            if (iphoneCards.size() < 3) {
                System.out.println("Found fewer than 3 iPhone items. Count = " + iphoneCards.size());
                return;
            }

            WebElement thirdIphoneCard = iphoneCards.get(2); // 0-based => third item

            // 6) Extract details: title, link, price (if present)
            WebElement titleLink = thirdIphoneCard.findElement(By.cssSelector("h2 a"));
            String title = titleLink.findElement(By.cssSelector("span")).getText();
            String href = titleLink.getAttribute("href");

            String priceText = "";
            try {
                // Common price locations in Amazon search result cards
                WebElement priceWhole = thirdIphoneCard.findElement(By.cssSelector("span.a-price > span.a-offscreen"));
                priceText = priceWhole.getText(); // e.g., ₹59,999.00
            } catch (NoSuchElementException e) {
                priceText = "(price not found on card)";
            }

            System.out.println("3rd iPhone title: " + title);
            System.out.println("3rd iPhone link:  " + href);
            System.out.println("3rd iPhone price: " + priceText);

            // 7) (Optional) Click it
            // titleLink.click();

        } catch (TimeoutException te) {
            System.out.println("Timed out waiting for page elements: " + te.getMessage());
        } finally {
            // Close the browser
            driver.quit();
        }
    }
}