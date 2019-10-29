package com.taotao.service.impl;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.ItemMapper;
import com.taotao.service.SearchItemService;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 索引维护service
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private CloudSolrServer cloudSolrServer;

    @Override
    public TaotaoResult importAllItems() {
        try {
            //查询商品列表
            List<SearchItem> itemList = itemMapper.getItemList();
            //遍历商品列表
            for (SearchItem searchItem : itemList) {
                //创建文档对象
                SolrInputDocument document  =new SolrInputDocument();
                //向文档对象中添加域
                document.addField("id",searchItem.getId());
                document.addField("item_title",searchItem.getTitle());
                document.addField("item_sell_point",searchItem.getSell_point());
                document.addField("item_price",searchItem.getPrice());
                document.addField("item_image",searchItem.getImage());
                document.addField("item_category_name",searchItem.getCategory_name());
                //把对象文档写入索引库
                cloudSolrServer.add(document);
            }
            //提交
            cloudSolrServer.commit();
            //返回导入成功
            return TaotaoResult.ok();
        }catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500,"数据导入失败");
        }
    }

    public TaotaoResult addDocument(long itemId) throws Exception {
        // 1、根据商品id查询商品信息。
        SearchItem searchItem = itemMapper.getItemById(itemId);
        // 2、创建一SolrInputDocument对象。
        SolrInputDocument document = new SolrInputDocument();
        // 3、使用SolrServer对象写入索引库。
        document.addField("id", searchItem.getId());
        document.addField("item_title", searchItem.getTitle());
        document.addField("item_sell_point", searchItem.getSell_point());
        document.addField("item_price", searchItem.getPrice());
        document.addField("item_image", searchItem.getImage());
        document.addField("item_category_name", searchItem.getCategory_name());
        document.addField("item_desc", searchItem.getItem_desc());
        // 5、向索引库中添加文档。
        cloudSolrServer.add(document);
        cloudSolrServer.commit();
        // 4、返回成功，返回e3Result。
        return TaotaoResult.ok();
    }
}
