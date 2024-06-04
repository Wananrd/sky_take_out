package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.core.annotation.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {
    /**
     * 插入订单数据
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 用于替换微信支付更新数据库状态的问题
     * @param orderStatus
     * @param orderPaidStatus
     */
    @Update("update sky_take_out.orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{check_out_time}" +
            " where number = #{orderNumber}")
    void updateStatus(Integer orderStatus, Integer orderPaidStatus, LocalDateTime check_out_time, String orderNumber);

    List<Orders> list(OrdersPageQueryDTO ordersPageQueryDTO);

    Orders getById(Long id);

    void update(Orders orders);

    @Select("select * from sky_take_out.orders where status=#{status} and order_time < #{time};")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime time);

    @Select("select * from sky_take_out.orders where number=#{orderNumber};")
    Orders getByOrderNumber(String orderNumber);

    Double sumByMap(Map map);

    Integer countOrderByMap(Map map);

    List<GoodsSalesDTO> getSalesTop10(LocalDateTime begin, LocalDateTime end);
}
