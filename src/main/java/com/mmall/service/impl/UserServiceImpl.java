package com.mmall.service.impl;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.common.TokenCache;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import com.mmall.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    public ServerResponse<User> login(String username, String password){
        int resultCount = userMapper.checkUserName(username);
        if(resultCount == 0){
            return ServerResponse.createByError("用户名不存在！");
        }

        //MD5 加密密码
        password = MD5Util.MD5EncodeUtf8(password);

        User user = userMapper.selectlogin(username, password);
        if(user == null ){
            return ServerResponse.createByError("密码错误！");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
    }

    public ServerResponse<String> register(User user){
        ServerResponse validResponse = this.checkValid(user.getUsername(),Const.USERNAME);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(),Const.EMAIL);
        if(!validResponse.isSuccess()){
            return validResponse;
        }
        user.setRole(Const.ROLE.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        int resultCount = userMapper.insert(user);
        if(resultCount == 0){
            return validResponse.toError("注册失败！");
        }

        return validResponse.toSuccess("注册成功！");
    }

    public ServerResponse<String> checkValid(String str,String type){
        if(StringUtils.isNoneBlank(type)){
            int resultCount;
            if(Const.USERNAME.equals(type)){
                resultCount= userMapper.checkUserName(str);
                if(resultCount > 0){
                    return ServerResponse.createByError("用户名已存在！");
                }
            }
            if(Const.EMAIL.equals(type)){
                resultCount = userMapper.checkEmail(str);
                if(resultCount > 0){
                    return ServerResponse.createByError("邮箱已存在！");
                }
            }
        }else{
            return ServerResponse.createByError("参数错误！");
        }
        return ServerResponse.createBySuccess("校验成功");
    }

    public ServerResponse<String> selectQuestion(String username){
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if(validResponse.isSuccess()){
            return validResponse.toError("用户不存在！");
        }
        String question = userMapper.selectQuestionByUserName(username);
        if(StringUtils.isNotBlank(question)){
            return validResponse.toSuccess(question);
        }
        return validResponse.toError("找回密码的问题是空的！");

    }

    public ServerResponse<String> checkAnswer(String username,String question,String answer){
        int resultCount = userMapper.checkQuestion(username,question,answer);
        if(resultCount >0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username,forgetToken);
            return ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByError("回答错误！");
    }

    public ServerResponse forgetResetPassword(String username,String newPassword,String token){
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByError("无效token");
        }
        ServerResponse validResponse = this.checkValid(username, Const.USERNAME);
        if(validResponse.isSuccess()){
            return validResponse.toError("用户不存在");
        }
        String validtoken = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(validtoken)){
            return validResponse.toError("Token无效或者过期");
        }
        if(StringUtils.equals(token, validtoken)){
            newPassword = MD5Util.MD5EncodeUtf8(newPassword);
           int resultCount = userMapper.updateUserPasswordByUserName(username,newPassword);
           if(resultCount>0){
               return validResponse.toSuccess("修改密码成功");
           }
        }else{
            return validResponse.toError("token错误,请重新获取重置密码的token");
        }
        return validResponse.toError("修改失败");
    }

   public ServerResponse<String> ResetPassword(String oldPassword,String newPassword,User user){
        int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword),user.getId());
        if(resultCount == 0){
            return ServerResponse.createByError("旧密码验证错误");
        }
        user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
        resultCount = userMapper.updateByPrimaryKeySelective(user);
        if(resultCount > 0){
            return ServerResponse.createBySuccess("密码更新成功");
        }
        return ServerResponse.createByError("密码更新失败");
   }

   public ServerResponse<User> updateUserDetails(User user){
        int resultCount = userMapper.checkEmailByUserId(user.getId(),user.getEmail());
        if(resultCount > 0){
            return ServerResponse.createByError("此邮箱已被占用，请使用其他邮箱");
        }
        User updateUser = new User();
        updateUser.setEmail(user.getEmail());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setPhone(user.getPhone());
        resultCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(resultCount > 0){
            return ServerResponse.createBySuccess("修改信息成功");
        }
        return ServerResponse.createByError("修改信息失败");
   }

   public ServerResponse<User> getInformation(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByError("未找到用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess(user);
   }

   public ServerResponse isAdminRole(User user){
        if( user!= null &&Const.ROLE.ROLE_ADMIN == user.getRole()){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
   }
}
