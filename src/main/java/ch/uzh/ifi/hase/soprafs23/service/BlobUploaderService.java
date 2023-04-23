package ch.uzh.ifi.hase.soprafs23.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.common.StorageSharedKeyCredential;

@Service
@Primary
@Transactional
public class BlobUploaderService {

    @Value("${azure.storage.account-name}")
    private String accountName;

    @Value("${azure.storage.account-key}")
    private String accountKey;

    private BlobContainerClient containerClient; 

    String connectionString = "AccountName=" + accountName + ";AccountKey=" + accountKey
            + ";EndpointSuffix=core.windows.net;DefaultEndpointsProtocol=https;";
    
    public String upload(MultipartFile file, String containerName, String fileName) throws IOException {

        BlobContainerClient container = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        BlobClient blob = container.getBlobClient(fileName + "." + fileExtension);

        blob.upload(file.getInputStream(), file.getSize(), true);

        return blob.getBlobUrl();
    }

    public List<String> uploadImages(MultipartFile[] files, String listingId) throws IOException {

        List<String> imageUrls = new ArrayList<>();

        BlobContainerClient container = getOrCreateBlobContainer(listingId);

        for (MultipartFile file : files) {

            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            BlobClient blob = container.getBlobClient(generateUUID() + "." + fileExtension);

            blob.upload(file.getInputStream(), file.getSize(), true);

            imageUrls.add(blob.getBlobUrl());

        }

        return imageUrls;

    }

    private BlobContainerClient getOrCreateBlobContainer(String listingId) {

        StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().endpoint("https://upsearchstorage.blob.core.windows.net/").credential(credential).buildClient();

        blobServiceClient.createBlobContainerIfNotExists(listingId);
        containerClient = blobServiceClient.getBlobContainerClient(listingId);
        containerClient.createIfNotExists();

        return containerClient;

    }

    private String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

}
