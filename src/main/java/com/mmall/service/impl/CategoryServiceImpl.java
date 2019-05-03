package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    public ServerResponse save(String name,Integer showOrder,Integer parentId){
        ServerResponse response = checkRepeat(name,parentId);
        if(!response.isSuccess()){//已存在
            return response;
        }
        Category category =  new Category();
        category.setName(name);
        category.setParentId(parentId);
        category.setStatus(true);
        category.setSortOrder(showOrder);
        int resultCount = categoryMapper.insert(category);
        if(resultCount > 0 ){
            return response.toSuccess("新增成功");
        }
        return response.toError("新增失败");

    }

    public ServerResponse checkRepeat(String name,Integer parentId){
        int resultCount = categoryMapper.selectByNameAndParentId(name,parentId);
        if(resultCount > 0){
            return ServerResponse.createByError("已存在");
        }
        return ServerResponse.createBySuccess();
    }

    public ServerResponse checkRepeat(Integer categoryId,String name,Integer parentId){
        int resultCount = categoryMapper.selectByNameAndParentIdExceptId(categoryId,name,parentId);
        if(resultCount > 0){
            return ServerResponse.createByError("已存在");
        }
        return ServerResponse.createBySuccess();
    }

    public ServerResponse updateCategory(Category category){
        Category old = categoryMapper.selectByPrimaryKey(category.getId());
        if(old == null){
            return ServerResponse.createByError("无此类别");
        }
        ServerResponse response = checkRepeat(category.getId(),category.getName(), category.getParentId());
        if(!response.isSuccess()){
            return response.toError("已类别名称已存在");
        }
        Category updateCa = new Category();
        updateCa.setId(old.getId());
        updateCa.setName(category.getName());
        updateCa.setSortOrder(category.getSortOrder());

        int resultCount = categoryMapper.updateByPrimaryKeySelective(updateCa);
        if(resultCount > 0){
            return response.toSuccess("更改成功");
        }
        return response.toError("更改失败");
    }

    public ServerResponse<List<Category>> findByParentId(Integer parentId){
        List<Category> list = categoryMapper.selectByParentId(parentId);
        if(CollectionUtils.isEmpty(list)){
            logger.info("此父节点下无类别子节点："+parentId);
        }
        return ServerResponse.createBySuccess(list);
    }

    public ServerResponse<List<Integer>> findAllChildByParentId(Integer parentId){
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,parentId);

        List<Integer> lists = Lists.newArrayList();
        for (Category category:categorySet) {
            lists.add(category.getId());
        }
        return ServerResponse.createBySuccess(lists);
    }

    public Set<Category> findChildCategory(Set<Category> categorySet,Integer parentId){
        Category category = categoryMapper.selectByPrimaryKey(parentId);
        if(category != null ){
            categorySet.add(category);
        }
        ServerResponse<List<Category>> childs = findByParentId(parentId);
        for (Category cats:childs.getData() ) {
            findChildCategory(categorySet,cats.getId());
        }
        return categorySet;
    }


}
