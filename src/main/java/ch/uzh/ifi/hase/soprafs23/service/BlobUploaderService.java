package ch.uzh.ifi.hase.soprafs23.service;

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
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;

@Service
@Primary
@Transactional
public class BlobUploaderService {

    @Value("${azure.storage.account-name}")
    private String accountName;

    @Value("${azure.storage.account-key}")
    private String accountKey;

    public String upload(MultipartFile file, String containerName, String fileName) {
        try {
            getOrCreateContainer(containerName);
            String connectionString = "DefaultEndpointsProtocol=https;" +
                    "AccountName=" + accountName + ";" +
                    "AccountKey=" + accountKey;
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);

            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            CloudBlobContainer container = blobClient.getContainerReference(containerName);

            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            CloudBlockBlob blob = container.getBlockBlobReference(fileName + "." + fileExtension);
            blob.upload(file.getInputStream(), file.getSize());
            return blob.getUri().toURL().toString();

        } catch (Exception e) {
            return null;
        }

    }

    public List<String> uploadImages(MultipartFile[] files, String listingId, List<String> urlsToDelete)
            throws URISyntaxException, NullPointerException {

        for (String urlToDeleteString : urlsToDelete) {
            deleteBlobByUrl(urlToDeleteString);
        }

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!"".equals(file.getOriginalFilename())) {
                String filename = generateUUID();
                String imageUrl = upload(file, listingId, filename);
                imageUrls.add(imageUrl);
            }
        }
        return imageUrls;
    }

    public void deleteImages(List<String> urlsToDelete)
            throws URISyntaxException, NullPointerException {
        for (String urlToDeleteString : urlsToDelete) {
            deleteBlobByUrl(urlToDeleteString);
        }
    }

    public CloudBlobClient getOrCreateContainer(String containerName)
            throws URISyntaxException, java.security.InvalidKeyException, StorageException {

        String connectionString = "DefaultEndpointsProtocol=https;" +
                "AccountName=" + accountName + ";" +
                "AccountKey=" + accountKey;
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(connectionString);

        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

        CloudBlobContainer container = blobClient.getContainerReference(containerName);

        container.createIfNotExists();

        BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

        containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

        container.uploadPermissions(containerPermissions);
        return blobClient;
    }

    private String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private void deleteBlobByUrl(String blobUrl) throws URISyntaxException {
        URI uri = new URI(blobUrl);
        String containerName = uri.getPath().split("/")[1];
        String blobName = uri.getPath().substring(containerName.length() + 2);

        BlobClient blobClient = new BlobServiceClientBuilder()
                .endpoint("https://upsearchstorage.blob.core.windows.net" + "/" + containerName
                        + "?sv=2021-12-02&ss=bfqt&srt=sco&sp=rwdlacupiytfx&se=2024-04-23T17:42:48Z&st=2023-04-23T09:42:48Z&spr=https,http&sig=SA0QntBFC1v%2BYH%2FcM07emZEebxUpTMusGyj7AMCTI7E%3D")
                .buildClient()
                .getBlobContainerClient(containerName)
                .getBlobClient(blobName);

        blobClient.delete();
    }

}
