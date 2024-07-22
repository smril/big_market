package com.smril.domain.strategy.model.entity;

import com.smril.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RuleActionEntity<T extends RuleActionEntity.RaffleEntity> {

    private String code = RuleLogicCheckTypeVO.ALLOW.getCode();
    private String info = RuleLogicCheckTypeVO.ALLOW.getInfo();
    private String ruleModel;

    private T data;

    static public class RaffleEntity {

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    static public class RaffleBeforeEntity extends RaffleEntity {
        /* 前两个字段用于抽奖，第三个字段用于黑名单直接返回 */
        private Long strategyId;
        private String ruleWeightValueKey;
        private Integer awardId;
    }

    static public class RaffleCenterEntity extends RaffleEntity {

    }

    static public class RaffleAfterEntity extends RaffleEntity {

    }


}
