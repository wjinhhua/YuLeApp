package com.myapp.yuleapp.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/7/2.10:32
 * 描述:
 **/
public class CarToonBean {
    public String currentPage;
    public CPagebean pagebean;
    public int ret_code;
    public class CPagebean{
        public String allPages;
        public List<CarToon> contentlist = new ArrayList<>();
        public String maxResult;
    }
    public class CarToon{
        public String id;
        public String link;
        public String title;
    }
}
