package com.project.starcoffee.service;

import com.project.starcoffee.dao.CartDAO;
import com.project.starcoffee.dto.ItemDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class CartService {

    private final CartDAO cartDAO;

    @Autowired
    public CartService(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }

    public UUID insertCart(List<ItemDTO> itemDTO) {
        UUID cartId = cartDAO.saveItem(itemDTO);
        return cartId;
    }

    public List<ItemDTO> findCart(UUID cartId) {
        List<ItemDTO> items = cartDAO.findItem(cartId);
        return items;
    }

    public void deleteItem(UUID cartId) {
        cartDAO.deleteItem(cartId);
    }
}
