package com.project.starcoffee.redis;

import com.project.starcoffee.dto.ItemDTO;

import java.util.List;
import java.util.UUID;

public interface CartDAORedisRepository {

    public void addCart(UUID cartId, List<ItemDTO> itemDTO);
    public boolean duplicated(UUID cartId);
    public List<ItemDTO> getCartItems(UUID cartId);
    public void deleteCartItems(UUID cartId);


}
