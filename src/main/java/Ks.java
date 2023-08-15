import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Ks {
    public static String sendKs(String id_Ks) {
        // 设置 ChromeDriver 路径
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver-win64\\chromedriver.exe");

        // 创建 ChromeDriver 实例
        WebDriver driver = new ChromeDriver();

        String Num = null;
        try {
            // 导航到网页
            String url = "https://www.kuaishou.com/profile/" + id_Ks;
            driver.get(url);

            Thread.sleep(30000);

            // 使用显式等待，等待动态加载内容出现
            WebElement element = new WebDriverWait(driver, 60)
                    .until(ExpectedConditions.visibilityOfElementLocated(By.className("user-photo-list")));

            // 获取动态加载内容
            String content = element.getText();
            int count = countOccurrences(content, "喜欢");
            System.out.println("该快手用户的作品数为：" + count);
            Num = String.valueOf(count);

            File file = new File("D:\\" + id_Ks + "的快手作品数");
            if (file.exists()) {
                System.out.println("文件已存在");
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try (FileWriter fw = new FileWriter(file)) {
                fw.write("快手作品数为" + Num);
                fw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭浏览器
            driver.quit();
        }
        return Num;
    }

    private static int countOccurrences(String content, String searchStr) {
        int count = 0;
        int index = 0;

        while ((index = content.indexOf(searchStr, index)) != -1) {
            count++;
            index += searchStr.length();
        }

        return count;
    }
}