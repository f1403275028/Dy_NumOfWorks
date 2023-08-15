import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

class sendHttp {
    public static void main(String[] args) throws InterruptedException {
        
        // 核心线程数
        int corePoolSize = 6;
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要查询的抖音id");
        String idDy = sc.next();
        System.out.println("请输入要查询的快手id");
        String idKs = sc.next();
        System.out.println("请输入要查询的微博id");
        String wbId = sc.next();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(corePoolSize);
/*        // 延迟0分钟后开始执行任务，每隔30分钟执行一次
        scheduler.scheduleAtFixedRate(() ->
                {
                    Dy.sendDy(idDy);
                },
                5,
                1800,
                TimeUnit.SECONDS);*/
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                String dy = Dy.sendDy(idDy);
                // 对返回值进行处理
                System.out.println(dy); // 打印返回值
            }
        }, 5, 1800, TimeUnit.SECONDS);

/*        // 延迟0分钟后开始执行任务，每隔30分钟执行一次
        scheduler.scheduleAtFixedRate(() ->
                {
                   Ks.sendKs(idKs);
                },
                5
                ,
                1800,
                TimeUnit.SECONDS);*/
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                String ks=  Ks.sendKs(idKs);
                System.out.println(ks); // 打印返回值
            }
        }, 5, 1800, TimeUnit.SECONDS);
/*        // 延迟0分钟后开始执行任务，每隔30分钟执行一次
        scheduler.scheduleAtFixedRate(() ->
                {
                    Wb.sendWb(wbId);
                },
                5
                ,
                1800,
                TimeUnit.SECONDS);*/
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                String wb=  Wb.sendWb(wbId);
                System.out.println(wb); // 打印返回值
            }
        }, 5, 1800, TimeUnit.SECONDS);

    }

}