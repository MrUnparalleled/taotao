package com.taotao.service;

import com.taotao.common.pojo.SearchResult;

public interface SearchService {

    public SearchResult search(String keyWord, int page, int rows) throws Exception;

}
