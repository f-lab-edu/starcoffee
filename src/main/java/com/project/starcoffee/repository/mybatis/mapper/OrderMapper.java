package com.project.starcoffee.repository.mybatis.mapper;

import com.project.starcoffee.domain.order.OrderStatus;
import com.project.starcoffee.dto.ItemDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface OrderMapper {

    int saveOrder(@Param("memberId") UUID memberId,
                  @Param("items") List<ItemDTO> items,
                  @Param("storeId") long storeId,
                  @Param("itemCount") int itemCount,
                  @Param("finalPrice") int finalPrice);

}
