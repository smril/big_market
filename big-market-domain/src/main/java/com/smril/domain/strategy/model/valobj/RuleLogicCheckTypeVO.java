package com.smril.domain.strategy.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RuleLogicCheckTypeVO {

    ALLOW("0000","放行，不受规则影响"),
    TAKE_OVER("0001","接管，受规则影响"),
        ;

    private final String code;
    private final String info;
}
