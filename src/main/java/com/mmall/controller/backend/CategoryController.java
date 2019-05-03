package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 关于权限校验和用户是否校验可写可不写、后期集成shiro框架做权限校验
 */
@Controller
@RequestMapping("/manage/category")
public class CategoryController {

    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "save.do" ,method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse save(String name, Integer showOrder, @RequestParam(defaultValue = "0") Integer parentId, HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null ){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登陆");
        }
        return iCategoryService.save(name, showOrder, parentId);
    }

    @RequestMapping(value = "check_isRepeat.do")
    @ResponseBody
    public ServerResponse isRepeat(Category category,HttpSession session){
        User user = (User )session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if(category.getId() == null){
            return iCategoryService.checkRepeat(category.getName(), category.getParentId());
        }else{
            return iCategoryService.checkRepeat(category.getId(), category.getName(), category.getParentId());
        }
    }

    @RequestMapping(value = "set_category_name.do")
    @ResponseBody
    public ServerResponse setCategoryName(Category category,HttpSession session){
        User user = (User )session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        return iCategoryService.updateCategory(category);
    }

    @RequestMapping(value = "find_by_parentId.do")
    @ResponseBody
    public ServerResponse<List<Category>> findByParentId(@RequestParam(defaultValue = "0") Integer parentId,HttpSession session){
        User user = (User )session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        return iCategoryService.findByParentId(parentId);
    }

    @RequestMapping(value = "find_all_child.do" , method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<List<Integer>> findAllChildsByParentId(@RequestParam(defaultValue = "0")Integer parentId,HttpSession session){
        User user = (User )session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByError(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        return iCategoryService.findAllChildByParentId(parentId);
    }

}
