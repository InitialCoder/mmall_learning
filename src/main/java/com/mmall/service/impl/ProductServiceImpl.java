package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.BeanUtil;
import com.mmall.util.DateUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductVO;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;
    @Transactional
    public ServerResponse saveOrUpdate(Product product){
        int resultCount ;
        if(null == product.getId()){
            resultCount = productMapper.insert(product);
            if(resultCount > 0){
                return ServerResponse.createBySuccess("新增成功");
            }else{
                return ServerResponse.createByError("新增失败");
            }
        }else{
            resultCount = productMapper.updateByPrimaryKeySelective(product);
            if(resultCount > 0){
                return ServerResponse.createBySuccess("修改成功");
            }else{
                return ServerResponse.createByError("修改失败");
            }
        }
    }

    public ServerResponse updateProductStatus(Integer productId,Integer status){
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int resultCount= productMapper.insertSelective(product);
        if(resultCount > 0){
            return ServerResponse.createBySuccess("修改状态成功");
        }
        return ServerResponse.createByError("修改状态失败！");
    }

    @Override
    public ServerResponse<ProductVO> manageProductDetail(Integer productId) {
        if(null == productId){
            return ServerResponse.createByError("商品id不能为空");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ServerResponse.createByError("商品已经下架");
        }
        ProductVO vo = new ProductVO();
        BeanUtil.copy(product, vo);
        vo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://image.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(null == category){
            vo.setParentCategoryId(0);
        }
        vo.setParentCategoryId(category.getParentId());
        vo.setCreateTime(DateUtil.DateToStr(product.getCreateTime(), DateUtil.STANDAR_FORMAT));
        vo.setUpdateTime(DateUtil.DateToStr(product.getUpdateTime(), DateUtil.STANDAR_FORMAT));
        return ServerResponse.createBySuccess(vo);
    }


    public ServerResponse<PageInfo<ProductVO>> getProductList(int pageIndex,int pageSize){
        PageHelper.startPage(pageIndex, pageSize);
        List<Product> list = productMapper.selectList();
        List<ProductVO> listvo = assemberProductListVO(list);
        PageInfo<ProductVO> pageResult = new PageInfo(listvo);
        return ServerResponse.createBySuccess(pageResult);
    }

    public ServerResponse<PageInfo<ProductVO>> mgrProductList(String productName,Integer productId,
                                                              int pageIndex,int pageSize){
        PageHelper.startPage(pageIndex, pageSize);
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuffer().append("&").append(productName).append("%").toString();
        }
        List<Product> list = productMapper.findByProductNameAndProductId(productName,productId);
        List<ProductVO> listvo = assemberProductListVO(list);
        PageInfo<ProductVO> pageResult = new PageInfo(listvo);
        return ServerResponse.createBySuccess(pageResult);
    }

    private List<ProductVO>  assemberProductListVO(List<Product> list){
        List<ProductVO> listVO = Lists.newArrayList();
        ProductVO vo;
        for (Product pojo:list) {
            vo = new ProductVO();
            BeanUtil.copy(pojo, vo);
            vo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://image.happymmall.com/"));
            listVO.add(vo);
        }
        return listVO;
    }
}
