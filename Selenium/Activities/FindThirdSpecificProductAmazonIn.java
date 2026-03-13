package selenium;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class FindThirdSpecificProductAmazonIn {

    public static void main(String[] args) {
        // If geckodriver is not in PATH, set it like:
        // System.setProperty("webdriver.gecko.driver", "/path/to/geckodriver");

        WebDriver driver = new FirefoxDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(25));

        // === Configure your target here ===
        final String searchQuery = "iphone 17 pro";         // what to type in the search box
        final String titleMustContain = "iphone 17 pro";    // phrase to find in product titles (case-insensitive)
        final int nthIndex = 2; // 0-based index: 2 => 3rd item

        // Optional: set to true to skip sponsored items
        final boolean skipSponsored = true;

        try {
            // 1) Open Amazon India
            driver.get("https://www.amazon.in/");

            // 2) Find the search box, search, and submit
            WebElement searchBox = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("twotabsearchtextbox"))
            );
            searchBox.clear();
            searchBox.sendKeys(searchQuery);
            searchBox.submit();

            // 3) Wait for the product result cards
            By resultCards = By.cssSelector("div.s-main-slot div[data-component-type='s-search-result']");
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(resultCards, 2));
            List<WebElement> cards = driver.findElements(resultCards);

            // 4) Filter cards (skip sponsored if enabled, and title contains the phrase)
            List<WebElement> matchingCards = new ArrayList<>();
            for (WebElement card : cards) {
                // Skip sponsored if required
                if (skipSponsored) {
                    boolean isSponsored = !card.findElements(By.xpath(".//span[normalize-space()='Sponsored']")).isEmpty();
                    if (isSponsored) continue;
                }

                // Grab the title
                String title = null;
                try {
                    WebElement titleEl = card.findElement(By.cssSelector("h2 a span"));
                    title = titleEl.getText();
                } catch (NoSuchElementException ignored) {}

                if (title == null || title.isBlank()) continue;

                if (title.toLowerCase().contains(titleMustContain.toLowerCase())) {
                    matchingCards.add(card);
                }
            }

            // 5) Ensure we have at least 3 matches
            if (matchingCards.size() <= nthIndex) {
                System.out.println("Found fewer than " + (nthIndex + 1) + " matches for phrase: '"
                        + titleMustContain + "'. Count = " + matchingCards.size());
                // Optional: print what we did find
                for (int i = 0; i < matchingCards.size(); i++) {
                    String t = matchingCards.get(i).findElement(By.cssSelector("h2 a span")).getText();
                    System.out.println("Match " + (i + 1) + ": " + t);
                }
                return;
            }

            // 6) Extract info from the Nth (3rd) matching card
            WebElement targetCard = matchingCards.get(nthIndex);

            // Title + link from search card
            WebElement titleLink = targetCard.findElement(By.cssSelector("h2 a"));
            String title = titleLink.findElement(By.cssSelector("span")).getText();
            String href = titleLink.getAttribute("href");

            // Price on search card (if present)
            String listPrice = getSafeText(targetCard, By.cssSelector("span.a-price > span.a-offscreen"));
            if (listPrice.isBlank()) listPrice = "(price not found on search card)";

            System.out.println("=== 3rd matching item on results page ===");
            System.out.println("Title: " + title);
            System.out.println("Link : " + href);
            System.out.println("Price on results card: " + listPrice);

            // 7) Open product in a new tab, switch, and read product page price + delivery
            openInNewTab(driver, href);
            switchToLastTab(driver);

            // Wait for product page to have a title or buy box present
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(By.id("productTitle")),
                    ExpectedConditions.presenceOfElementLocated(By.id("buybox"))
            ));

            // Re-read title (optional, for verification)
            String pdpTitle = getFirstNonBlank(
                    getSafeText(driver, By.id("productTitle")),
                    title
            );

            // Price on PDP (robust fallbacks)
            String pdpPrice = getFirstNonBlank(
                    // Standard price block
                    getSafeText(driver, By.cssSelector("#corePrice_feature_div span.a-price span.a-offscreen")),
                    // Alternate price blocks
                    getSafeText(driver, By.cssSelector("#apex_desktop span.a-price span.a-offscreen")),
                    getSafeText(driver, By.cssSelector("#apex_desktop_renewed span.a-price span.a-offscreen")),
                    getSafeText(driver, By.cssSelector("span.a-price.aok-align-center span.a-offscreen")),
                    listPrice // fall back to search card price if still empty
            );

            // Delivery message (multiple robust fallbacks)
            String deliveryMessage = getFirstNonBlank(
                    // Newer mir-layout delivery slots
                    getSafeText(driver, By.cssSelector("#mir-layout-DELIVERY_BLOCK-slot-PRIMARY_DELIVERY_MESSAGE")),
                    getSafeText(driver, By.cssSelector("#mir-layout-DELIVERY_BLOCK-slot-DELIVERY_MESSAGE")),
                    // Common legacy ids
                    getSafeText(driver, By.cssSelector("#deliveryMessageMirId")),
                    getSafeText(driver, By.cssSelector("#deliveryBlockMessage")),
                    getSafeText(driver, By.cssSelector("div#ddmDeliveryMessage")),
                    // Sometimes fine-print under buybox
                    getSafeText(driver, By.cssSelector("#buybox span.a-color-success")),
                    // As a last resort, any green success text near delivery
                    getSafeText(driver, By.xpath("//*[contains(@class,'a-color-success') and (contains(.,'Get it') or contains(.,'Delivery') or contains(.,'by'))]"))
            );

            System.out.println("\n=== Product page details ===");
            System.out.println("PDP Title : " + pdpTitle);
            System.out.println("PDP Price : " + pdpPrice);
            System.out.println("Delivery  : " + (deliveryMessage.isBlank() ? "(delivery info not found)" : deliveryMessage));

            // Close the product tab and return to results (optional)
            // driver.close();
            // switchToFirstTab(driver);

        } catch (TimeoutException te) {
            System.out.println("Timed out waiting for page elements: " + te.getMessage());
        } finally {
            driver.quit();
        }
    }

    // ---------- Helpers ----------

    private static String getSafeText(SearchContext ctx, By locator) {
        try {
            WebElement el = ctx.findElement(locator);
            String txt = el.getText();
            return txt == null ? "" : txt.trim();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    private static String getFirstNonBlank(String... vals) {
        for (String v : vals) {
            if (v != null && !v.isBlank()) return v.trim();
        }
        return "";
    }

    private static void openInNewTab(WebDriver driver, String url) {
        ((JavascriptExecutor) driver).executeScript("window.open(arguments[0], '_blank');", url);
    }

    private static void switchToLastTab(WebDriver driver) {
        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(handles.get(handles.size() - 1));
    }

    @SuppressWarnings("unused")
    private static void switchToFirstTab(WebDriver driver) {
        List<String> handles = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(handles.get(0));
    }
}