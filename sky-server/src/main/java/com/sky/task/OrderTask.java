package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    /**
     * 处理超时订单
     */
    @Scheduled(cron = "0 * * * * ?") // 每分钟执行一次
    public void processTimeOutOrder() {
        log.info("定时处理超时订单: {}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(15);

        List<Orders> orders = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT, time);
        if (orders != null && !orders.isEmpty()) {
            for (Orders order : orders) {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("订单超时，自动取消");
                order.setCancelTime(LocalDateTime.now());
                orderMapper.update(order);
            }
        }
    }

    /**
     * 处理自动完成订单
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void processCompletedOrder() {
        log.info("定时处理待派送订单: {}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);

        List<Orders> orders = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, time);
        if (orders != null && !orders.isEmpty()) {
            for (Orders order : orders) {
                order.setStatus(Orders.COMPLETED);
                orderMapper.update(order);
            }
        }
    }
}
