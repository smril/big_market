package com.smril.test.domain.strategy;

import com.smril.domain.strategy.service.armory.IStrategyArmory;
import com.smril.domain.strategy.service.rule.chain.ILogicChain;
import com.smril.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.smril.domain.strategy.service.rule.chain.impl.RuleWeightLogicChain;
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

    @Before
    public void setUp() {
        log.info("测试结果: {}", strategyArmory.assembleLotteryStrategy(100001L));
        log.info("测试结果: {}", strategyArmory.assembleLotteryStrategy(100002L));
        log.info("测试结果: {}", strategyArmory.assembleLotteryStrategy(100003L));

        ReflectionTestUtils.setField(ruleWeightLogicChain, "userScore", 4500L);

    }

    @Test
    public void testLogicChain_rule_blacklist() {
        ILogicChain logicChain = chainFactory.openLogicChain(100001L);
        DefaultChainFactory.StrategyAwardVO strategyAwardVO = logicChain.logic("user001", 100001L);
        log.info("测试结果: {}", strategyAwardVO.getAwardId());
    }

    @Test
    public void testLogicChain_rule_Weight() {
        ILogicChain logicChain = chainFactory.openLogicChain(100001L);
        DefaultChainFactory.StrategyAwardVO strategyAwardVO = logicChain.logic("smril", 100001L);
        log.info("测试结果: {}", strategyAwardVO.getAwardId());
    }

    @Test
    public void testLogicChain_default() {
        ILogicChain logicChain = chainFactory.openLogicChain(100003L);
        DefaultChainFactory.StrategyAwardVO strategyAwardVO = logicChain.logic("smril", 100003L);
        log.info("测试结果: {}", strategyAwardVO.getAwardId());
    }
}
