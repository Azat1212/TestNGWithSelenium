import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class OzonTest {

    WebDriver driver;
    private List<WebElement> allElements;
    private Map<String, String> elements = new HashMap<String, String>();
    private int numberElementMusic;

    @BeforeClass
    public void openFBPage() {
        System.setProperty("webdriver.gecko.driver", "C:\\Driver\\geckodriver.exe");
        driver = new FirefoxDriver();
    }

    @Test
    public void testOpenSite() {
        driver.get("https://www.ozon.ru");
    }

    @Test(dependsOnMethods = {"testOpenSite"})
    public void testSelectSectionMusic() {
        driver.findElements(By.xpath("//span[@data-test-id='menu-show-dropdown']")).get(0).click();
    }

    @Test(dependsOnMethods = {"testSelectSectionMusic"})
    public void testClickVinylDisks() {
        driver.findElement(By.xpath("//a[contains(@data-test-id, 'menu-dropdown-item-level1') and contains(@title, 'Музыка')]")).click();
    }

    @Test(dependsOnMethods = {"testClickVinylDisks"})
    public void testItemsOnPage() {
        driver.findElement(By.linkText("Виниловые пластинки (LP)")).click();
    }

    @Test(dependsOnMethods = {"testItemsOnPage"})
    public void testSumItemsOnPage() {
        allElements = driver.findElements(By.className("tile"));
        assertTrue(allElements.size() > 0);
    }

    @Test(dependsOnMethods = {"testSumItemsOnPage"})
    public void testRandomNumberOfItems() {
        numberElementMusic = (int) (Math.random() * allElements.size());
        assertTrue(numberElementMusic >= 0 && numberElementMusic < allElements.size());
    }

    @Test(dependsOnMethods = {"testRandomNumberOfItems"})
    public void testClickRememberedItem() {
        allElements.get(numberElementMusic).click();
    }

    @Test(dependsOnMethods = {"testClickRememberedItem"})
    public void testSetSumAndNameMusic() {
        String diskName = driver.findElement(By.className("name")).getText();
        String diskCost = driver.findElement(By.xpath("//meta[@itemprop = 'price']")).getAttribute("content");
        elements.put(diskCost, diskName);
        assertTrue(elements.size() > 0);
    }

    @Test(dependsOnMethods = {"testSetSumAndNameMusic"})
    public void testAddToCart() {
        driver.findElement(By.xpath("//button[@data-test-id='saleblock-add-to-cart-button']")).click();
    }

    @Test(dependsOnMethods = {"testAddToCart"})
    public void testMoveToCart() {
        driver.findElement(By.xpath("(//a[@href='/context/cart'])")).click();
    }

    @Test(dependsOnMethods = {"testMoveToCart"})
    public void testDiskInCartFirst() throws InterruptedException {
        TimeUnit.SECONDS.sleep(6);
        List<WebElement> disksInCart = driver.findElements(By.xpath("//a[@data-test-id='cart-item-title']"));
        assertEquals(disksInCart.size(), 1);
        String nameByCart = disksInCart.get(0).getText();
        String rememberedName = elements.entrySet().iterator().next().getValue();
        assertTrue(nameByCart.equals(rememberedName));
    }

    @Test(dependsOnMethods = {"testDiskInCartFirst"})
    public void testPreviousVinylDisksPage() {
        driver.navigate().back(); // to disk card
        driver.navigate().back(); // to vinyl list
    }

    @Test(dependsOnMethods = {"testPreviousVinylDisksPage"})
    public void testRandomNumberOfDisks() {
        numberElementMusic = (int) (Math.random() * allElements.size());
        assertTrue(numberElementMusic >= 0 && numberElementMusic < allElements.size());
    }

    @Test(dependsOnMethods = {"testRandomNumberOfDisks"})
    public void testClickRememberedDisk() {
        allElements = driver.findElements(By.className("tile"));
        allElements.get(numberElementMusic).click();
    }

    @Test(dependsOnMethods = {"testClickRememberedDisk"})
    public void testSetSumAndNameDisk() {
        String diskName = driver.findElement(By.className("name")).getText();
        String diskCost = driver.findElement(By.xpath("//meta[@itemprop = 'price']")).getAttribute("content");
        elements.put(diskCost, diskName);
        assertTrue(elements.size() > 1);
    }

    @Test(dependsOnMethods = {"testSetSumAndNameDisk"})
    public void testAddDiskToCart() {
        driver.findElement(By.xpath("//button[@data-test-id='saleblock-add-to-cart-button']")).click();
    }

    @Test(dependsOnMethods = {"testAddDiskToCart"})
    public void testCheckCart() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        assertTrue(driver.findElement(By.className("count")).getText().equals("2"));
    }

    @Test(dependsOnMethods = {"testCheckCart"})
    public void testMoveToNewCart() {
        driver.findElement(By.xpath("(//a[@href='/context/cart'])")).click();
    }

    @Test(dependsOnMethods = {"testMoveToNewCart"})
    public void testDiskInCartSecond() throws InterruptedException {
        TimeUnit.SECONDS.sleep(6);
        List<WebElement> disksInCart = driver.findElements(By.xpath("//a[@data-test-id='cart-item-title']"));
        assertEquals(disksInCart.size(), 2);
        int price = 0;
        for (Map.Entry item : elements.entrySet()) {
            int priceDisk = Integer.parseInt((String) item.getKey());
            price = price + priceDisk;
        }
        String totalPrice = driver.findElement(By.xpath("//div[@data-test-id='total-price-block']")).getText();
        totalPrice = totalPrice.replaceAll("[^0-9]", "");
        assertEquals((Integer.parseInt(totalPrice)), price);
    }

    @Test(dependsOnMethods = {"testDiskInCartSecond"})
    public void testDeleteDisk() {
        driver.findElement(By.xpath("//button[@data-test-id='cart-delete-selected-btn']")).click();
        driver.findElement(By.xpath("//button[@data-test-id='checkcart-confirm-modal-confirm-button']")).click();
    }

    @Test(dependsOnMethods = {"testDeleteDisk"})
    public void testEmptyCart() {
        driver.findElement(By.xpath("//div[@data-test-id='empty-cart']"));
    }

    @AfterClass
    public void closeBrowser() {
        driver.quit();
    }
}
