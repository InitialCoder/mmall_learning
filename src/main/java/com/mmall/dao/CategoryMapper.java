package com.mmall.dao;

import com.mmall.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    int selectByNameAndParentId(@Param("name") String name, @Param("parentId")Integer parentId);

    int selectByNameAndParentIdExceptId(@Param("categoryId") Integer categoryId,@Param("name") String name,
                                        @Param("parentId")  Integer parentId);

    List<Category> selectByParentId(Integer parentId);
}