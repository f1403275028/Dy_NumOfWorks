import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Dy {

    public static String sendDy(String dyId){
        String fir = "https://www.douyin.com/search/";
        String nex=  "?aid=c8d732e9-ae01-429b-859c-315ead058fe8&publish_time=0&sort_type=0&source=search_history&type=general";

        String content = "";
        //可以换其他用户的id 从url中获取
        String url = fir+dyId+nex;
        String link;
        String trimmedLink;

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(
                    new BrowserType.LaunchOptions()
                            //取消无头模式，我们才能看见浏览器操作
                            .setHeadless(false)
                            //减慢执行速度，以免太快
                            .setSlowMo(100)
            );

            Page page = browser.newPage();
            page.navigate(url);

            // 等待5秒页面加载
            Thread.sleep(5000);

            JSHandle elementHandle = page.querySelector
                    ("#douyin-right-container > div.FtarROQM > div > div.HHwqaG_P > div:nth-child(1) > ul > li:nth-child(1) > div > div > div > div.AwIKR2fG > div > div > div.mSoL45jm > a");
            link = elementHandle.getProperty("href").toString();
            System.out.println(link);

            trimmedLink = link.substring(0, link.indexOf("?"));
            System.out.println(trimmedLink);

            //换一个浏览器
            Page page2 = browser.newPage();
            page2.navigate(trimmedLink);

            // 等待10秒
            if (page2.title().equals("验证码中间页")) {
                System.out.println("遇到验证码！");
                //验证码处理
                //收费验证码 5美元1千条左右
            }

            // 等待页面加载完成
            page2.waitForLoadState(LoadState.NETWORKIDLE);
            Thread.sleep(15000);

            content = (String) page2.evaluate
                    ("() => { return document.querySelector('.J6IbfgzH').textContent; }");
            System.out.println("该抖音用户的作品数为:"+content);

            File file = new File("D:\\"+dyId+"的抖音作品数");
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
                fw.write("抖音作品数为"+content);
                fw.flush();
                fw.close();
            }catch (IOException e){
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return content;
    }
}
