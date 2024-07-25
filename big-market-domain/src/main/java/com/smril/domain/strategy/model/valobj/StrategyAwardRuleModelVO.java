package com.smril.domain.strategy.model.valobj;

import com.smril.domain.strategy.service.rule.factory.DefaultLogicFactory;
import com.smril.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {
    private String ruleModels;

    public String[] raffleCenterRuleModelList() {

        List<String> ruleModelList = new ArrayList<>();

        String[] ruleModelValues = ruleModels.split(Constants.SPLIT);

        for(String ruleModelValue : ruleModelValues) {
            if(DefaultLogicFactory.LogicModel.isCenter(ruleModelValue)) {
                ruleModelList.add(ruleModelValue);
            }
        }
        return ruleModelList.toArray(new String[0]);
    }
}
