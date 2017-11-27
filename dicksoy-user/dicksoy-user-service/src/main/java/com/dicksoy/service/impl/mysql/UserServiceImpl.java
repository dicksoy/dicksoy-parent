package com.dicksoy.service.impl.mysql;


import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.dicksoy.common.po.User;
import com.dicksoy.service.dao.UserDao;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User>{

}