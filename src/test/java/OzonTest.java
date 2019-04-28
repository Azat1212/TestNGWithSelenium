import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.Test;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertTrue;

public class OzonTest {

    WebDriver driver;
    private List<WebElement> allElements;
    private Map<String,String> elements = new HashMap<String, String>();;
    private int numberElementMusic;

    @BeforeClass
    public void openFBPage(){
        System.setProperty("webdriver.gecko.driver", "C:\\Driver\\geckodriver.exe");
        driver = new FirefoxDriver();
    }

    @Test
    public void testOpenSite(){
        driver.get("https://www.ozon.ru");
    }

    @Test(dependsOnMethods={"testOpenSite"})
    public void testClickSection(){
        driver.findElements(By.xpath("//span[@data-test-id='menu-show-dropdown']")).get(0).click();
    }

    @Test(dependsOnMethods={"testClickSection"})
    public void testClickMusic(){
        driver.findElement(By.xpath("//a[contains(@data-test-id, 'menu-dropdown-item-level1') and contains(@title, 'Музыка')]")).click();
    }

    @Test(dependsOnMethods={"testClickMusic"})
    public void testVinylMusic(){
        driver.findElement(By.linkText("Виниловые пластинки (LP)")).click();
    }


    @Test(dependsOnMethods={"testVinylMusic"})
    public void testExistItemByVinyl(){
        allElements =  driver.findElements(By.className("tile"));
        assertTrue(allElements.size()>0);
    }

    @Test(dependsOnMethods={"testExistItemByVinyl"})
    public void testRandomElementNumber(){
        numberElementMusic = (int) (Math.random()*allElements.size());
        assertTrue(numberElementMusic >= 0 && numberElementMusic < allElements.size());
    }

    @Test(dependsOnMethods={"testRandomElementNumber"})
    public void testElementDiskClick(){
        allElements.get(numberElementMusic).click();
    }

    @Test(dependsOnMethods={"testElementDiskClick"})
    public void testSetSumAndNameMusic(){
        String diskName = driver.findElement(By.className("name")).getText();
        String diskCost = driver.findElement(By.xpath("//meta[@itemprop = 'price']")).getAttribute("content");
        elements.put(diskName, diskCost);

        assertTrue(elements.size() > 0);
    }

    @Test(dependsOnMethods={"testSetSumAndNameMusic"})
    public void testAddToCart(){
        driver.findElement(By.xpath("//button[@data-test-id='saleblock-add-to-cart-button']")).click();
    }

    @Test(dependsOnMethods={"testAddToCart"})
    public void testMoveToCart(){
        driver.findElement(By.xpath("(//a[@href='/context/cart'])")).click();
    }
    @Test(dependsOnMethods={"testMoveToCart"})
    public void testDiskInCartFirst(){
        //List<WebElement> disks =  driver.findElements(By.className("main split-item"));
        List<WebElement> disks =  driver.findElements(By.xpath("//div[@class='main split-item']"));

        assertTrue(disks.size()==1);
    }
    @AfterClass
    public void closeBrowser(){
        driver.quit();
    }

}
