package com.smril.test.domain;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.smril.domain.strategy.service.armory.IStrategyArmory;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyArmoryTest {
    @Resource
    private IStrategyArmory strategyArmory;

    @Test
    public void test_strategy_armory() {
        strategyArmory.assembleLotteryStrategy(10002L);
    }

    @Test
    public void test_getssembleRandomVal() {
        for(int i = 0; i < 100; i++) {
            log.info("测试结果: {}", strategyArmory.getRandomAwardId(10002L));
        }
    }
}
