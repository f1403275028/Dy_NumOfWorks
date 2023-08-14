import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wb {
    private static final Pattern PATTERN = Pattern.compile("\\d+");
    public static void sendWb(String wbId) {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver-win64\\chromedriver.exe");

        // 创建 ChromeDriver 实例
        WebDriver driver = new ChromeDriver();

        String Num = null;
        try {
            // 导航到网页
            String nex = "https://weibo.com/";
            String af = "?refer_flag=1001030103_";
            String url = nex+wbId+af;
            driver.get(url);

            Thread.sleep(10000);

            // 使用显式等待，等待动态加载内容出现
            WebElement element = new WebDriverWait(driver, 10)
                    .until(ExpectedConditions.visibilityOfElementLocated(By.className("container")));

            // 获取动态加载内容
            String content = element.getText();
            //System.out.println(content);

            String[] lines = content.split("\n");

            if (lines.length >= 2) {
                String firstLine = lines[0];
                String secondLine = lines[1];

                Matcher matcher = PATTERN.matcher(firstLine);

                if (matcher.find()) {
                    String number = matcher.group();
                   // System.out.println("作品数为：" + number);

                   // System.out.println("用户名为：" + secondLine);

                    File file = new File("D:\\"+secondLine+"的微博作品数");
                    if(file.exists()){
                        System.out.println("存在");
                    }else{
                        try{
                            file.createNewFile();

                        }catch (IOException e){
                            e.printStackTrace();

                        }
                    }
                    try{
                        FileWriter fw = new FileWriter(file);
                        fw.write("作品数为"+number);
                        fw.flush();
                        fw.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 关闭浏览器
            driver.quit();
        }
    }
}


