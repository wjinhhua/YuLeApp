package com.myapp.yuleapp.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 版权: ft626 版权所有(c) 2016
 * 作者: wjh
 * 版本: 1.0
 * 创建日期: 2016/6/30.23:09
 * 描述:
 **/
public class NewsBean {
    public PageBean pagebean;
    public int ret_code;
    public class PageBean{
        public int allNum;
        public int allPages;
        public List<News> contentlist = new ArrayList<>();
        public int currentPage;
        public int maxResult;
    }
    public class News{
       // public List<String> allList = new ArrayList<>();
        public String channelId;
        public String desc;
        public List<ImageUrls> imageurls = new ArrayList<>();
        public String link;
        public String nid;
        public String pubDate;
        public int sentiment_display;
        public SentimentTag sentiment_tag;
        public String source;
        public String title;
    }
    public class ImageUrls{
        public int height;
        public String url;
        public int width;
    }

    public class SentimentTag{
        public int count;
        public int dim;
        public int id;
        public int isbooked;
        public int ishot;
        public String name;
        public String type;
    }
}
