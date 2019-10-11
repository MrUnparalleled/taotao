package com.taotao.dao;


import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 商品搜索dao
 */
@Repository
public class SearchDao {

    @Autowired
    private CloudSolrServer cloudSolrServer;

    /**
     * 根据查询条件查询索引库
     * @param query
     * @return
     */
    public SearchResult search(SolrQuery query) throws SolrServerException {
        //根据查询条件查询索引库
        QueryResponse response = cloudSolrServer.query(query);
        //取查询条件的总记录数
        SolrDocumentList solrDocumentlist = response.getResults();
        long numFound = solrDocumentlist.getNumFound();
        //创建一个返回对象
        SearchResult result = new SearchResult();
        result.setRecourdCount(Math.toIntExact(numFound));
        //创建一个商品列表对象
        List<SearchItem> itemList = new ArrayList<>();
        //取商品结果
        //取高亮后的结果
        Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
        for (SolrDocument solrDocument : solrDocumentlist) {
            //取商品信息
            SearchItem searchItem = new SearchItem();
            searchItem.setCategory_name((String) solrDocument.get("category_name"));
            searchItem.setId((String) solrDocument.get("id"));
            searchItem.setImage((String) solrDocument.get("item_image"));
            searchItem.setPrice((Long) solrDocument.get("item_price"));
            searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
            //取高亮结果
            List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
            String itemTitle = "";
            if (list!=null && list.size()!=0){
                itemTitle = list.get(0);
            }else {
                itemTitle = (String) solrDocument.get("item_title");
            }

            searchItem.setTitle(itemTitle);
            //添加到商品列表
            itemList.add(searchItem);

        }
        //把列表放到返回结果对象中
        result.setItemList(itemList);
        return result;
    }

}
