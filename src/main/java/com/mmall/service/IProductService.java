package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductVO;

public interface IProductService {

    ServerResponse saveOrUpdate(Product product);

    ServerResponse updateProductStatus(Integer productId,Integer status);

    ServerResponse<ProductVO> manageProductDetail(Integer productId);

    ServerResponse<PageInfo<ProductVO>> getProductList( int pageIndex, int pageSize);

    ServerResponse<PageInfo<ProductVO>> mgrProductList(String productName, Integer productId,int pageIndex, int pageSize);
}
