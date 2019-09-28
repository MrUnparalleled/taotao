package com.taotao;


import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import java.io.IOException;

public class SolrTest {

    private static  String URL ="http://192.168.25.130:8080/solr" ;

    /**
     * 添加文档
     */
    @Test
    public void test1(){
        //添加jar包到项目
        //创建一个SolrServer，使用HTTPSolrServer创建对象
        SolrServer solrServer = new HttpSolrServer(URL);
        //创建文档对象
        SolrInputDocument document = new SolrInputDocument();
        //向文档中添加域
        document.addField("id","test03");
        document.addField("item_title","测试商品");
        document.addField("item_price","199");
        //将文档添加到索引库当中
        try {
            solrServer.add(document);
            solrServer.commit();
        } catch (SolrServerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 简单查询
     */
    @Test
    public void test2() throws SolrServerException {
        //创建一个solrServer对象
        SolrServer solrServer = new HttpSolrServer(URL);
        //创建一个solrQuery对象
        SolrQuery query = new SolrQuery();
        //向solrQuery中添加查询条件
        query.setQuery("*:*");
        //执行查询，得到一个response对象
        QueryResponse response = solrServer.query(query);
        //取查询结果
        SolrDocumentList list = response.getResults();
        System.out.println("查询结果总记录数："+list.getNumFound());
        //遍历打印结果
        for (SolrDocument document : list) {
            System.out.println(document.get("id"));
            System.out.println(document.get("item_title"));
            System.out.println(document.get("item_price"));
        }

    }

    /**
     * 根据id删除
     */
    @Test
    public void test3() throws IOException, SolrServerException {
        //创建一个solrServer
        SolrServer solrServer = new HttpSolrServer(URL);
        //根据id删除
        solrServer.deleteById("test01");
        //提交
        solrServer.commit();
    }


    /**
     * 根据查询删除
     */
    @Test
    public void test4() throws IOException, SolrServerException {
        //创建一个solrServer
        SolrServer solrServer = new HttpSolrServer(URL);
        solrServer.deleteByQuery("title:change.me");
        solrServer.commit();
    }


    @Test
    public void test5() throws IOException, SolrServerException {
        //1.把solrj的jar包添加到工程
        //2.创建一个solrServer对象，创建一个和solr服务的连接。httpSolrServer
        SolrServer  solrServer = new HttpSolrServer(URL);
        //3.创建一个文档对象，solrInputDocument
        SolrInputDocument document = new SolrInputDocument();
        //4.向文档中添加域，必须有一个id域，而且文档中使用的域必须在sechema.xml中定义
        document.addField("id","test001");
        document.addField("item_title","测试商品");
        //5.把文档添加到索引库
        solrServer.add(document);
        //6.commit
        solrServer.commit();
    }
}
