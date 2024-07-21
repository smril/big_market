package com.smril.test.domain;

import com.smril.domain.strategy.service.armory.IStrategyDispatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.smril.domain.strategy.service.armory.IStrategyArmory;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyTest {

    /* 构造概率表的对象 */
    @Resource
    private IStrategyArmory strategyArmory;

    /* 随机抽奖的对象 */
    @Resource
    private IStrategyDispatch strategyDispatch;

    @Before
    public void test_strategyArmory() {
        boolean success = strategyArmory.assembleLotteryStrategy(10001L);
        log.info("Strategy armory: {}", success);
    }

    @Test
    public void test_getRandomAwardId() {
        for(int i = 0; i < 10; i++) {
            log.info("测试结果: {}", strategyDispatch.getRandomAwardId(10001L));
        }
    }

    @Test
    public void test_getRandomAwardId_ruleWeightValue() {
        for(int i = 0; i < 10; i++) {
            log.info("测试结果: {} 4000 策略配置", strategyDispatch.getRandomAwardId(10001L, "4000:102,103,104,105"));
            log.info("测试结果: {} 5000 策略配置", strategyDispatch.getRandomAwardId(10001L, "5000:102,103,104,105,106,107"));
            log.info("测试结果: {} 6000 策略配置", strategyDispatch.getRandomAwardId(10001L, "6000:102,103,104,105,106,107,108,109"));
        }
    }
}
