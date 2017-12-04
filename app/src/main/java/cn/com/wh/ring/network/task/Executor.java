package cn.com.wh.ring.network.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Hui on 2017/12/4.
 */

public class Executor {
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);

    private static List<Long> postPublishIds = new ArrayList<>();

    public static void execute(Long postPublishId) {
        if (!postPublishIds.contains(postPublishId)) {
            fixedThreadPool.execute(new PostPublishRunnable(postPublishId));
            postPublishIds.add(postPublishId);
        }
    }

    public static void deleteId(Long postPublishId) {
        postPublishIds.remove(postPublishId);
    }
}
