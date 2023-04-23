package ch.uzh.ifi.hase.soprafs23.service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
import com.azure.storage.blob.BlobServiceClientBuilder;

@Service
@Primary
@Transactional
public class BlobUploaderService {

    @Value("${azure.storage.account-name}")
    private String accountName;

    @Value("${azure.storage.account-key}")
    private String accountKey;


    String connectionString = "AccountName=" + accountName + ";AccountKey=" + accountKey
            + ";EndpointSuffix=core.windows.net;DefaultEndpointsProtocol=https;";
    
    public String upload(MultipartFile file, String containerName, String fileName) throws IOException {

        BlobContainerClient container = getOrCreateContainer(containerName);
        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        BlobClient blob = container.getBlobClient(fileName + "." + fileExtension);

        blob.upload(file.getInputStream(), file.getSize(), true);

        return blob.getBlobUrl();
    }

    public List<String> uploadImages(MultipartFile[] files, String listingId, List<String> urlsToDelete) throws IOException, URISyntaxException {
        //delete urls
        for (String urlToDeleteString : urlsToDelete) {
            deleteBlobByUrl(urlToDeleteString);
        }

        List<String> imageUrls = new ArrayList<>();
        BlobContainerClient container = getOrCreateContainer(listingId);
        for (MultipartFile file : files) {
            if(!file.getOriginalFilename().equals("")){
                String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
                BlobClient blob = container.getBlobClient(generateUUID() + "." + fileExtension);
                blob.upload(file.getInputStream(), file.getSize(), true);
                imageUrls.add(blob.getBlobUrl());
            }     
        }

        return imageUrls;
    }

    public BlobContainerClient getOrCreateContainer(String containerName) {
        BlobContainerClient containerClient = new BlobContainerClientBuilder()
            .endpoint("https://upsearchstorage.blob.core.windows.net" + "/" + containerName + "?sv=2021-12-02&ss=bfqt&srt=sco&sp=rwdlacupiytfx&se=2024-04-23T17:42:48Z&st=2023-04-23T09:42:48Z&spr=https,http&sig=SA0QntBFC1v%2BYH%2FcM07emZEebxUpTMusGyj7AMCTI7E%3D")
            .buildClient();
        if (!containerClient.exists()) {
            containerClient.create();
        }
        return containerClient;
    }

    private String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private void deleteBlobByUrl(String blobUrl) throws URISyntaxException{
        URI uri = new URI(blobUrl);
        String accountName = uri.getHost().split("\\.")[0];
        String containerName = uri.getPath().split("/")[1];
        String blobName = uri.getPath().substring(containerName.length() + 2);

        BlobClient blobClient = new BlobServiceClientBuilder()
        .endpoint("https://upsearchstorage.blob.core.windows.net" + "/" + containerName + "?sv=2021-12-02&ss=bfqt&srt=sco&sp=rwdlacupiytfx&se=2024-04-23T17:42:48Z&st=2023-04-23T09:42:48Z&spr=https,http&sig=SA0QntBFC1v%2BYH%2FcM07emZEebxUpTMusGyj7AMCTI7E%3D")
                .buildClient()
                .getBlobContainerClient(containerName)
                .getBlobClient(blobName);

        blobClient.delete();
    }

}
