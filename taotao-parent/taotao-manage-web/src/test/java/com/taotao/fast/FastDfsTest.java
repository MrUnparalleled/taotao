package com.taotao.fast;

import com.taotao.common.utils.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

/**
 * 测试文件上传服务器
 */
public class FastDfsTest {

    @Test
    public void testUpload() throws Exception {
        //创建一个配置文件。文件名任意。内容就是tracker服务器的地址
        ClientGlobal.init("E:/git/taotao/taotao-parent/taotao-manage-web/src/main/resources/client.conf");
        //使用全局对象加载配置文件
        TrackerClient trackerClient = new TrackerClient();
        //创建一个TrackClient获得一个TrackerServer对象
        TrackerServer trackerServer = trackerClient.getConnection();
        //创建一个StrorageServer的引用，可以是null
        StorageServer storageServer = null;
        //创建一个StorageClient，参数用TrackerServer和StrorageServer
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        //使用StorageClient上传文件
        String[] jpgs = storageClient.upload_file("C:\\Users\\ucmed\\Desktop\\work\\slide2.jpg", "jpg", null);
        for (String string : jpgs) {
            System.out.println(string);
        }
    }

    @Test
    public void testFastDfClient() throws Exception{
        FastDFSClient fastDFSClient = new FastDFSClient("E:/git/taotao/taotao-parent/taotao-manage-web/src/main/resources/client.conf");
        String s = fastDFSClient.uploadFile("C:\\Users\\ucmed\\Desktop\\c9ad9a551b6d5daa8875133493722110.jpg");
        System.out.println(s);
    }
}
