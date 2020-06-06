package cn.shiva.core;

import cn.shiva.utils.CommonUtil;
import cn.shiva.utils.Consts;
import cn.shiva.utils.HttpUtil;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author shiva   2020/6/5 19:23
 */
@Configuration
public class HandleService {

    @Resource
    private ThreadPool threadPool;

    public static List<ConcurrentHashMap<String, Integer>> result = new ArrayList<>();

    /**
     * 每页开线程读取，拿到每页数据后进行数据处理
     */
    public void handle(int num){
        if (num <= 0){
            return ;
        }
        reset();
        //返回最后一页页码，最后一页条数
        int[] page = CommonUtil.calc(num);
        //计数器，判断线程是否执行结束
        AtomicBoolean flag = new AtomicBoolean(false);
        CountDownLatch taskLatch = new CountDownLatch(page[1] + (page[0])*20);
        threadPool.asyncServiceExecutor().execute(() -> {
            try {
                //开始等待
                taskLatch.await();
                System.out.println("任务执行结束");
                flag.set(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        //遍历开始
        for (int i = 1; i < page[0]; i++) {
            int finalIndex = i;
            threadPool.asyncServiceExecutor().execute(() -> busHandle(finalIndex,20));
        }
        threadPool.asyncServiceExecutor().execute(() -> busHandle(page[0], page[1]));

        
        try {
            while (!flag.get()){
                taskLatch.getCount();
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(1);
    }


    /**
     * 业务处理，每个线程获取网页文本
     * @param pageNo 需要获取的页码
     * @param itemNum 该页中获取的条数
     */
    private void busHandle(int pageNo, int itemNum ){
        String competeUrl = Consts.URL_PREFIX + pageNo + Consts.URL_SUFFIX;
        try {
            //获得该页的网页 string
            String html = HttpUtil.get(competeUrl);
            String trItem = CommonUtil.subHtml(html);
            //每一个tr都是一期
            String[] items = trItem.split("<tr>");
            //遍历每一期
            for (int j = 0; j < itemNum; j++) {
                String[] tdItems = items[j].split("<td");
                if (tdItems.length < 9){
                    continue ;
                }
                for (int i = 2; i < 9; i++) {
                    String str = tdItems[i];
                    int finalIdex = i - 2;
                    threadPool.asyncServiceExecutor().execute(() -> dataHandle(result.get(finalIdex), str));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 最终的数据处理
     * 针对每一位，统计数据
     */
    private void dataHandle(ConcurrentHashMap<String, Integer> currMap, String str){
        //先把未处理的字符串，选出数组
        String num = str.substring(str.indexOf("\">"), str.indexOf("</td>"));
        if (currMap.containsKey(num)){
            //存在则加1
            currMap.put(num, currMap.get(num) + 1);
        }else {
            //不存在添加
            currMap.put(num, 1);
        }
    }

    private static void reset(){
        result.clear();
        //默认塞上容器
        for (int i = 0; i < 7; i++) {
            result.add(new ConcurrentHashMap<>());
        }
    }
}
