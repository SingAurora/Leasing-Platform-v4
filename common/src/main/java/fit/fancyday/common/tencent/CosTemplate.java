package fit.fancyday.common.tencent;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.*;
import fit.fancyday.common.propertise.CosProperties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 腾讯云对象存储服务
 *
 * @author asus
 * @date 2022/09/29
 */
public class CosTemplate {
    private CosProperties cosProperties;

    public CosTemplate(CosProperties cosProperties) {
        this.cosProperties = cosProperties;
    }

    /**
     * 获取配置好的客户端对象
     *
     * @return {@link COSClient}
     */
    private COSClient getCosClient() {
        String secretId = cosProperties.getSecretId();
        String secretKey = cosProperties.getSecretKey();
        // 初始化用户身份信息（secretId, secretKey）。
        // 密钥管理：https://console.cloud.tencent.com/cam/capi
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 设置 bucket 的地域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-guangzhou");
        ClientConfig clientConfig = new ClientConfig(region);
        // 这里建议设置使用 https 协议
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }


    /**
     * 显示上传进度
     *
     * @param transfer 转移
     */
    void showTransferProgress(Transfer transfer,String type) {
        // 这里的 Transfer 是异步上传结果 Upload 的父类
        System.out.println(transfer.getDescription());

        // transfer.isDone() 查询是否已经完成
        while (transfer.isDone() == false) {
            try {
                // 每 2 秒获取一次进度
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return;
            }

            TransferProgress progress = transfer.getProgress();
            long sofar = progress.getBytesTransferred();
            long total = progress.getTotalBytesToTransfer();
            double pct = progress.getPercentTransferred();
            System.out.printf(type+" progress: [%d / %d] = %.02f%%\n", sofar, total, pct);
        }

        // 完成了 Completed，或者失败了 Failed
        System.out.println(transfer.getState());
    }

    /**
     * 上传对象
     *
     * @param localFile 本地文件对象
     */
    public void uploadObject(File localFile) {
        COSClient cosClient = getCosClient();
        // 指定文件将要存放的存储桶
        String bucketName = cosProperties.getBucketName();
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        String pathKey = cosProperties.getPathKey();
        // 截取带点的文件类型
        String suffix = localFile.getName().substring(localFile.getName().lastIndexOf("."));
        String name = UUID.randomUUID().toString() + suffix;
        pathKey = pathKey + "/" + name;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, pathKey, localFile);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        cosClient.shutdown();
    }

    /**
     * 查询对象列表
     *
     * @return {@link List}<{@link String}>
     */
    public List<String> queryingObjectList() {
        COSClient cosClient = getCosClient();
        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        String bucketName = cosProperties.getBucketName();
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
        // 设置bucket名称
        listObjectsRequest.setBucketName(bucketName);
        // prefix表示列出的object的key以prefix开始
        String pathKey = cosProperties.getPathKey() + "/";
        listObjectsRequest.setPrefix(pathKey);
        // deliter表示分隔符, 设置为/表示列出当前目录下的object, 设置为空表示列出所有的object
        listObjectsRequest.setDelimiter("/");
        // 设置最大遍历出多少个对象, 一次listobject最大支持1000
        listObjectsRequest.setMaxKeys(1000);
        ObjectListing objectListing = null;
        ArrayList<String> list = new ArrayList<>();
        do {
            try {
                objectListing = cosClient.listObjects(listObjectsRequest);
            } catch (CosServiceException e) {
                e.printStackTrace();
            } catch (CosClientException e) {
                e.printStackTrace();
            }
            // common prefix表示表示被delimiter截断的路径, 如delimter设置为/, common prefix则表示所有子目录的路径
            List<String> commonPrefixs = objectListing.getCommonPrefixes();

            // object summary表示所有列出的object列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                // 文件的路径key
                String key = cosObjectSummary.getKey();
                list.add(key);
                // 文件的etag
                String etag = cosObjectSummary.getETag();
                // 文件的长度
                long fileSize = cosObjectSummary.getSize();
                // 文件的存储类型
                String storageClasses = cosObjectSummary.getStorageClass();
            }

            String nextMarker = objectListing.getNextMarker();

            listObjectsRequest.setMarker(nextMarker);
        } while (objectListing.isTruncated());
        cosClient.shutdown();
        return list;
    }

    /**
     * 删除对象
     *
     * @param fileName 文件名称
     */
    public void deleteObject(String fileName) {
        COSClient cosClient = getCosClient();
        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        String bucketName = cosProperties.getBucketName();
        // 指定被删除的文件在 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示删除位于 folder 路径下的文件 picture.jpg
        String key = cosProperties.getPathKey();
        key = key + "/" + fileName;
        cosClient.deleteObject(bucketName, key);
        cosClient.shutdown();
    }

    /**
     * 这个实例用来后续上传调用高级接口
     *
     * @return {@link TransferManager}
     */
    private TransferManager createTransferManagerUp() {
        COSClient cosClient = getCosClient();

        // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
        ExecutorService threadPool = Executors.newFixedThreadPool(32);

        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        TransferManager transferManager = new TransferManager(cosClient, threadPool);

        // 设置高级接口的配置项
        // 分块上传阈值和分块大小分别为 5MB 和 1MB
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(5 * 1024 * 1024);
        transferManagerConfiguration.setMinimumUploadPartSize(1 * 1024 * 1024);
        transferManager.setConfiguration(transferManagerConfiguration);

        return transferManager;
    }


    /**
     * 这个实例用来后续下载调用高级接口
     *
     * @return {@link TransferManager}
     */
    TransferManager createTransferManagerDown() {
        COSClient cosClient = getCosClient();
        // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
        ExecutorService threadPool = Executors.newFixedThreadPool(32);

        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        TransferManager transferManager = new TransferManager(cosClient, threadPool);

        return transferManager;
    }

    /**
     * 一定要关闭这个实例，防止资源泄露
     *
     * @param transferManager 传输管理器
     */
    void shutdownTransferManager(TransferManager transferManager) {
        // 指定参数为 true, 则同时会关闭 transferManager 内部的 COSClient 实例。
        // 指定参数为 false, 则不会关闭 transferManager 内部的 COSClient 实例。
        transferManager.shutdownNow(true);
    }

    // 上传对象
    public void upload1(File localFile) throws CosServiceException, CosClientException {
        // 使用高级接口必须先保证本进程存在一个 TransferManager 实例，如果没有则创建
        // 详细代码参见本页：高级接口 -> 创建 TransferManager
        TransferManager transferManager = createTransferManagerUp();

        // 指定文件将要存放的存储桶
        String bucketName = cosProperties.getBucketName();
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        String pathKey = cosProperties.getPathKey();
        // 截取带点的文件类型
        String suffix = localFile.getName().substring(localFile.getName().lastIndexOf("."));
        String name = UUID.randomUUID().toString() + suffix;
        pathKey = pathKey + "/" + name;

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, pathKey, localFile);

        // 设置存储类型（如有需要，不需要请忽略此行代码）, 默认是标准(Standard), 低频(standard_ia)
        // 更多存储类型请参见 https://cloud.tencent.com/document/product/436/33417
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);

        //若需要设置对象的自定义 Headers 可参照下列代码,若不需要可省略下面这几行,对象自定义 Headers 的详细信息可参考 https://cloud.tencent.com/document/product/436/13361
//        ObjectMetadata objectMetadata = new ObjectMetadata();

        //若设置Content-Type、Cache-Control、Content-Disposition、Content-Encoding、Expires这五个字自定义 Headers，推荐采用objectMetadata.setHeader()
//        objectMetadata.setHeader(key, value);
        //若要设置 “x-cos-meta-[自定义后缀]” 这样的自定义 Header，推荐采用
//        Map<String, String> userMeta = new HashMap<String, String>();
//        userMeta.put("x-cos-meta-[自定义后缀]", "value");
//        objectMetadata.setUserMetadata(userMeta);

//        putObjectRequest.withMetadata(objectMetadata);

        try {
            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回UploadResult, 失败抛出异常
            Upload upload = transferManager.upload(putObjectRequest);
            UploadResult uploadResult = upload.waitForUploadResult();
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 确定本进程不再使用 transferManager 实例之后，关闭之
        // 详细代码参见本页：高级接口 -> 关闭 TransferManager
        shutdownTransferManager(transferManager);
    }

    public void upload2(File localFile) throws IOException {
        // 使用高级接口必须先保证本进程存在一个 TransferManager 实例，如果没有则创建
        // 详细代码参见本页：高级接口 -> 创建 TransferManager
        TransferManager transferManager = createTransferManagerUp();

        // 指定文件将要存放的存储桶
        String bucketName = cosProperties.getBucketName();
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        String pathKey = cosProperties.getPathKey();
        // 截取带点的文件类型
        String suffix = localFile.getName().substring(localFile.getName().lastIndexOf("."));
        String name = UUID.randomUUID().toString() + suffix;
        pathKey = pathKey + "/" + name;

        // 这里创建一个 ByteArrayInputStream 来作为示例，实际中这里应该是您要上传的 InputStream 类型的流
        FileInputStream inputStream = new FileInputStream(localFile);
        //获取流的长度
        int inputStreamLength = inputStream.available();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 上传的流如果能够获取准确的流长度，则推荐一定填写 content-length
        // 如果确实没办法获取到，则下面这行可以省略，但同时高级接口也没办法使用分块上传了
        objectMetadata.setContentLength(inputStreamLength);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, pathKey, inputStream, objectMetadata);

        // 设置存储类型（如有需要，不需要请忽略此行代码）, 默认是标准(Standard), 低频(standard_ia)
        // 更多存储类型请参见 https://cloud.tencent.com/document/product/436/33417
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);

        try {
            // 高级接口会返回一个异步结果Upload
            // 可同步地调用 waitForUploadResult 方法等待上传完成，成功返回UploadResult, 失败抛出异常
            Upload upload = transferManager.upload(putObjectRequest);
            showTransferProgress(upload,"upload");
            UploadResult uploadResult = upload.waitForUploadResult();
        } catch (
                CosServiceException e) {
            e.printStackTrace();
        } catch (
                CosClientException e) {
            e.printStackTrace();
        } catch (
                InterruptedException e) {
            e.printStackTrace();
        }

        // 确定本进程不再使用 transferManager 实例之后，关闭之
        // 详细代码参见本页：高级接口 -> 关闭 TransferManager
        shutdownTransferManager(transferManager);
    }

    public void download(final File file) {
        // 使用高级接口必须先保证本进程存在一个 TransferManager 实例，如果没有则创建
        TransferManager transferManager = createTransferManagerDown();

        // 指定文件将要存放的存储桶
        String bucketName = cosProperties.getBucketName();
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        String pathKey = cosProperties.getPathKey();
        String name = file.getName();
        pathKey = pathKey + "/" + name;
        String localFilePath = "C:\\Users\\asus\\Downloads\\"+file.getName();
        File downloadFile = new File(localFilePath);

        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, pathKey);
        try {
            // 返回一个异步结果 Donload, 可同步的调用 waitForCompletion 等待下载结束, 成功返回 void, 失败抛出异常
            Download download = transferManager.download(getObjectRequest, downloadFile);
            showTransferProgress(download,"download");
            download.waitForCompletion();
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 确定本进程不再使用 transferManager 实例之后，关闭之
        shutdownTransferManager(transferManager);
    }

}
