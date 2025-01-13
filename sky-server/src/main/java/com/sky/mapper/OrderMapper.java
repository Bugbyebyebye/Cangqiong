package com.sky.mapper;

import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    void insert(Orders orders);

    void insertOrderDetail(List<OrderDetail> orderDetails);

    @Select("select * from orders where status = #{status} and order_time < #{orderTime}")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    @Update("update orders set status = #{status}, cancel_reason = #{cancelReason}, cancel_time = #{cancelTime} where id = #{id}")
    void update(Orders order);

    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);
}
