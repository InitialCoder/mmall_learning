package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

public interface ICategoryService {

    ServerResponse save(String name, Integer showOrder, Integer parentId);

    ServerResponse checkRepeat(String name,Integer parentId);

    ServerResponse checkRepeat(Integer categoryId,String name,Integer parentId);

    ServerResponse updateCategory(Category category);

    ServerResponse<List<Category>> findByParentId(Integer parentId);

    ServerResponse<List<Integer>> findAllChildByParentId(Integer parentId);
}

