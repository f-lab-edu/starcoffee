package com.project.starcoffee.mapper;

import com.project.starcoffee.dto.MemberCardDTO;
import com.project.starcoffee.dto.OrderDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface OrderMapper {

    int saveOrder(@Param("memberId") UUID memberId,
                  @Param("cartId") UUID cartId,
                  @Param("storeId") Long storeId,
                  @Param("totalItemCount") int totalItemCount,
                  @Param("totalFinalPrice") int totalFinalPrice);

    OrderDTO findByOrder(UUID cartId);

}
