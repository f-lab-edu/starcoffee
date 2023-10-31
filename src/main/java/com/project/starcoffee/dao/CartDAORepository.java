package com.project.starcoffee.dao;

import com.project.starcoffee.dto.ItemDTO;

import java.util.List;
import java.util.UUID;

public interface CartDAORepository
{
    UUID saveItem(List<ItemDTO> itemDTO);

    List<ItemDTO> findItem(UUID cartId);

    void deleteItem(UUID cartId);

    void autoDeleteItem();


}
