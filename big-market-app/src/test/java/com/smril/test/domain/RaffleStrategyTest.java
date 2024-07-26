package com.smril.test.domain;

import com.alibaba.fastjson.JSON;
import com.smril.domain.strategy.model.entity.RaffleAwardEntity;
import com.smril.domain.strategy.model.entity.RaffleFactorEntity;
import com.smril.domain.strategy.service.IRaffleStrategy;
import com.smril.domain.strategy.service.armory.IStrategyArmory;
import com.smril.domain.strategy.service.rule.filter.impl.RuleLockLogicFilter;
import com.smril.domain.strategy.service.rule.filter.impl.RuleWeightLogicFilter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RaffleStrategyTest {

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IRaffleStrategy raffleStrategy;

    @Resource
    private RuleWeightLogicFilter ruleWeightLogicFilter;
    @Autowired
    private RuleLockLogicFilter ruleLockLogicFilter;

    @Before
    public void setUp() {
         log.info("测试结果: {}", strategyArmory.assembleLotteryStrategy(10001L));
         log.info("测试结果: {}", strategyArmory.assembleLotteryStrategy(10002L));
         log.info("测试结果: {}", strategyArmory.assembleLotteryStrategy(10003L));

        ReflectionTestUtils.setField(ruleWeightLogicFilter, "userScore", 4500L);
        ReflectionTestUtils.setField(ruleLockLogicFilter, "userRaffleCount", 0L);

    }

    @Test
    public void test_performRaffle() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("smril")
                .strategyId(10002L)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_performRaffle_blacklist() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("user003")  // 黑名单用户 user001,user002,user003
                .strategyId(10002L)
                .build();
        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);
        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }

    @Test
    public void test_raffle_center_rule_lock() {
        RaffleFactorEntity raffleFactorEntity = RaffleFactorEntity.builder()
                .userId("smril")
                .strategyId(10003L)
                .build();

        RaffleAwardEntity raffleAwardEntity = raffleStrategy.performRaffle(raffleFactorEntity);

        log.info("请求参数：{}", JSON.toJSONString(raffleFactorEntity));
        log.info("测试结果：{}", JSON.toJSONString(raffleAwardEntity));
    }
}
