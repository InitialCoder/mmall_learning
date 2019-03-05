package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUserName(String username);

    int checkEmail(String email);

    User selectlogin(@Param("username") String username, @Param("password") String password);

    String selectQuestionByUserName(String username);

    int checkQuestion(@Param("username") String username,@Param("question") String question,@Param("answer") String answer);

    int updateUserPasswordByUserName(@Param("username") String username, @Param("password") String password);

    int checkPassword( @Param("password") String password,@Param("userId") Integer userId);

    int checkEmailByUserId(@Param("userId") Integer userId,@Param("email") String email);
}