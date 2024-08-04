package com.smril.domain.activity.service;

import com.smril.domain.activity.repository.IActivityRepository;
import org.springframework.stereotype.Service;

/**
 * @author smril
 * @create 2024/8/4 21:55
 * @description
 */

@Service
public class RaffleActivityService extends AbstractRaffleActivity{

    public RaffleActivityService(IActivityRepository activityRepository) {
        super(activityRepository);
    }

}
