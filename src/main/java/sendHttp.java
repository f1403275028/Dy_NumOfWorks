import java.util.Scanner;
import java.util.concurrent.*;

class SendHttp {
    public static void main(String[] args) throws InterruptedException {
        // 核心线程数
        int corePoolSize = 1;
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要查询的抖音id");
        String idDy = sc.next();
        System.out.println("请输入要查询的快手id");
        String idKs = sc.next();
        System.out.println("请输入要查询的微博id");
        String wbId = sc.next();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(corePoolSize);

        // 延迟0分钟后开始执行任务，每隔30分钟执行一次
        scheduler.scheduleAtFixedRate(() ->
                Dy.sendDy(idDy),
                0,
                30,
                TimeUnit.MINUTES);


        // 延迟0分钟后开始执行任务，每隔30分钟执行一次
        scheduler.scheduleAtFixedRate(() ->
                {
                   Ks.sendKs(idKs);
                },
                5
                ,
                1800,
                TimeUnit.SECONDS);

        // 延迟0分钟后开始执行任务，每隔30分钟执行一次
        scheduler.scheduleAtFixedRate(() ->
                {
                    Wb.sendWb(wbId);
                },
                5
                ,
                1800,
                TimeUnit.SECONDS);

    }

}