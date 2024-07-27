package com.smril.test.infrastructure;

import com.alibaba.fastjson.JSON;
import com.smril.domain.strategy.model.valobj.RuleTreeNodeLineVO;
import com.smril.domain.strategy.model.valobj.RuleTreeNodeVO;
import com.smril.domain.strategy.model.valobj.RuleTreeVO;
import com.smril.domain.strategy.repository.IStrategyRepository;
import com.smril.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author smril
 * @create 2024/7/27 10:56
 * @description 仓储数据查询测试
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyRepositoryTest {

    @Resource
    IStrategyRepository strategyRepository;

    @Test
    public void queryRuleTreeVOByTreeId() {
        RuleTreeVO ruleTreeVO = strategyRepository.queryRuleTreeVOByTreeId("tree_lock");
        // log.info("测试结果: {}", JSON.toJSONString(ruleTreeVO));
        // System.out.println(ruleTreeVO.getTreeNodeMap());
        Map<String, RuleTreeNodeVO> tree_lock_map = ruleTreeVO.getTreeNodeMap();
        for(String key : tree_lock_map.keySet()) {
            log.info("测试结果: key: {}, value: {}", key, tree_lock_map.get(key).getRuleDesc());
            log.info("Node实体{}: ", key);
            RuleTreeNodeVO ruleTreeNodeVO = tree_lock_map.get(key);
            List<RuleTreeNodeLineVO> ruleNodeLine = ruleTreeNodeVO.getTreeNodeLineVOList();
            System.out.println(ruleNodeLine);
        }
    }

}
