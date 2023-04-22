package ch.uzh.ifi.hase.soprafs23.service;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;

import java.io.IOException;

@Service
@Primary
@Transactional
public class BlobUploaderService {

    @Value("${azure.storage.account-name}")
    private String accountName;

    @Value("${azure.storage.account-key}")
    private String accountKey;

    public String upload(MultipartFile file, String containerName, String fileName) throws IOException {
        String connectionString = "AccountName=" + accountName + ";AccountKey=" + accountKey
                + ";EndpointSuffix=core.windows.net;DefaultEndpointsProtocol=https;"; 

        BlobContainerClient container = new BlobContainerClientBuilder()
                .connectionString(connectionString)
                .containerName(containerName)
                .buildClient();

        String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
        BlobClient blob = container.getBlobClient(fileName + "." + fileExtension);

        blob.upload(file.getInputStream(), file.getSize(), true);

        return blob.getBlobUrl();
    }
    
}
