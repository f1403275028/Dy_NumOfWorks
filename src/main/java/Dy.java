import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Dy {

    public static String sendDy(String dyId) {
        String result = "";

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(100));

            Page page = browser.newPage();
            page.navigate("https://www.douyin.com/search/" + dyId + "?aid=c8d732e9-ae01-429b-859c-315ead058fe8&publish_time=0&sort_type=0&source=search_history&type=general");

            Thread.sleep(5000);

            ElementHandle elementHandle = page.querySelector("#douyin-right-container > div.FtarROQM > div > div.HHwqaG_P > div:nth-child(1) > ul > li:nth-child(1) > div > div > div > div.AwIKR2fG > div > div > div.mSoL45jm > a");
            String link = elementHandle.getProperty("href").toString();
            System.out.println(link);

            String trimmedLink = link.substring(0, link.indexOf("?"));
            System.out.println(trimmedLink);

            Page page2 = browser.newPage();
            page2.navigate(trimmedLink);

            if (page2.title().equals("验证码中间页")) {
                System.out.println("遇到验证码！");
                // 验证码处理
                // 收费验证码 5美元1千条左右
            }

            page2.waitForLoadState(LoadState.NETWORKIDLE);
            Thread.sleep(15000);

            String content = (String) page2.evaluate("() => { return document.querySelector('.J6IbfgzH').textContent; }");
            result = "该抖音用户的作品数为：" + content;
            System.out.println(result);

            File file = new File("D:\\" + dyId + "的抖音作品数");
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
                fw.write("抖音作品数为：" + content);
                fw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}