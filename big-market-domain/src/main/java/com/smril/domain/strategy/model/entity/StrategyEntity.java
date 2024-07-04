package com.smril.domain.strategy.model.entity;

import com.smril.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyEntity {
    private Long strategyId;
    private String strategyDesc;
    private String ruleModels;

    public String[] ruleModels() {
        if(StringUtils.isBlank(ruleModels)) return null;
        return ruleModels.split(Constants.SPLIT);
    }

    public String getRuleWeight() {
        String[] ruleModels = this.ruleModels();
        for(String ruleModel : ruleModels) {
            if("rule_weight".equals(ruleModel)) return ruleModel;
        }
        return null;
    }
}
