package com.github.linyuzai.concept.sample.sync.waiting;

import com.github.linyuzai.sync.waiting.core.caller.SyncCaller;
import com.github.linyuzai.sync.waiting.core.concept.ConditionSyncWaitingConcept;
import com.github.linyuzai.sync.waiting.core.concept.SyncWaitingConcept;
import com.github.linyuzai.sync.waiting.core.exception.SyncWaitingTimeoutException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/concept-sync-waiting")
public class SyncWaitingController {

    /**
     * 新建一个 {@link SyncWaitingConcept} 对象
     * 或是可以直接注入一个全局使用
     */
    private final SyncWaitingConcept concept = new ConditionSyncWaitingConcept();

    /**
     * 下发命令，阻塞线程直到数据上报或超时
     *
     * @param key 每条命令唯一的id
     * @return 设备上报的数据
     */
    @RequestMapping("/send")
    public String send(@RequestParam String key) {
        try {
            return concept.waitSync(key, new SyncCaller() {
                @Override
                public void call(Object k) {
                    System.out.println("Cmd id: " + k);
                }
            }, 0, 0);
        } catch (SyncWaitingTimeoutException e) {
            return "下发命令超时";
        }
    }

    /**
     * 接收设备上报的数据，唤醒下发命令的线程
     *
     * @param key   一般需要从上报数据中附带命令id
     * @param value 上报数据
     */
    @RequestMapping("/receive")
    public void receive(@RequestParam String key, @RequestParam String value) {
        concept.notifyAsync(key, value);
    }
}
