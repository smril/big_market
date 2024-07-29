package com.smril.trigger.api;

import com.smril.trigger.api.dto.RaffleAwardListRequestDTO;
import com.smril.trigger.api.dto.RaffleAwardListResponseDTO;
import com.smril.trigger.api.dto.RaffleRequestDTO;
import com.smril.trigger.api.dto.RaffleResponseDTO;
import com.smril.types.model.Response;

import java.util.List;

/**
 * @author smril
 * @create 2024/7/29 19:08
 * @description 抽奖服务接口
 */

public interface IRaffleService {
    /* 策略装配接口 */
    Response<Boolean> strategyArmory(Long strategyId);

    Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO requestDTO);

    /* 随机抽奖接口 */
    Response<RaffleResponseDTO> randomRaffle(RaffleRequestDTO requestDTO);
}
