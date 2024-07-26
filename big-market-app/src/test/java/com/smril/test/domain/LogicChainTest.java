package com.smril.test.domain;

import com.smril.domain.strategy.service.armory.IStrategyArmory;
import com.smril.domain.strategy.service.rule.chain.ILogicChain;
import com.smril.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.smril.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;
import com.smril.domain.strategy.service.rule.filter.impl.RuleLockLogicFilter;
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
public class LogicChainTest {

    @Resource
    private DefaultChainFactory chainFactory;

    @Resource
    private IStrategyArmory strategyArmory;
    @Autowired
    private RuleWeightLogicChain ruleWeightLogicChain;
    @Autowired
    private RuleLockLogicFilter ruleLockLogicFilter;

    @Before
    public void setUp() {
        log.info("测试结果: {}", strategyArmory.assembleLotteryStrategy(10001L));
        log.info("测试结果: {}", strategyArmory.assembleLotteryStrategy(10002L));
        log.info("测试结果: {}", strategyArmory.assembleLotteryStrategy(10003L));

        ReflectionTestUtils.setField(ruleWeightLogicChain, "userScore", 4500L);
        ReflectionTestUtils.setField(ruleLockLogicFilter, "userRaffleCount", 0L);

    }

    @Test
    public void testLogicChain_rule_blacklist() {
        ILogicChain logicChain = chainFactory.openLogicChain(10002L);
        Integer awardId = logicChain.logic("user001", 10002L);
        log.info("测试结果: {}", awardId);
    }

    @Test
    public void testLogicChain_rule_Weight() {
        ILogicChain logicChain = chainFactory.openLogicChain(10002L);
        Integer awardId = logicChain.logic("smril", 10002L);
        log.info("测试结果: {}", awardId);
    }

    @Test
    public void testLogicChain_default() {
        ILogicChain logicChain = chainFactory.openLogicChain(10003L);
        Integer awardId = logicChain.logic("smril", 10003L);
        log.info("测试结果: {}", awardId);
    }
}
