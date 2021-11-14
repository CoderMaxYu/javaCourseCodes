package com.yuwq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuwq.mapper.ProductMapper;
import com.yuwq.po.Product;
import com.yuwq.service.ProductService;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
}
