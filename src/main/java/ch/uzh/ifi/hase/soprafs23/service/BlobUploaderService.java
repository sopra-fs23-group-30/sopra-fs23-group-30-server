package ch.uzh.ifi.hase.soprafs23.service;

import java.net.URI;
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

    public static final String STORAGECONNECTIONSTRING = "DefaultEndpointsProtocol=https;" +
            "AccountName=upsearchstorage;" +
            "AccountKey=USKgaMmYTFVCfN+5V0C583JzmKeaRXFwksdvD8JJ8ys624aF+VjEUeamVrWYKo7zHyvKjxtvR0zM+AStpad7AA==";

    public String upload(MultipartFile file, String containerName, String fileName) {
        try {
            getOrCreateContainer(containerName);
            CloudStorageAccount storageAccount = CloudStorageAccount.parse(STORAGECONNECTIONSTRING);

            // Create the blob client.
            CloudBlobClient blobClient = storageAccount.createCloudBlobClient();
            CloudBlobContainer container = blobClient.getContainerReference(containerName);

            // Create or overwrite the "myimage.jpg" blob with contents from a local file.
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            CloudBlockBlob blob = container.getBlockBlobReference(fileName + "." + fileExtension);
            blob.upload(file.getInputStream(), file.getSize());
            return blob.getUri().toURL().toString();

        } catch (Exception e) {
            return null;
        }

    }

    public List<String> uploadImages(MultipartFile[] files, String listingId, List<String> urlsToDelete) {
        // delete urls
        for (String urlToDeleteString : urlsToDelete) {
            deleteBlobByUrl(urlToDeleteString);
        }

        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().equals("")) {
                String filename = generateUUID();
                String imageUrl = upload(file, listingId, filename);
                imageUrls.add(imageUrl);
            }
        }
        return imageUrls;
    }

    public CloudBlobClient getOrCreateContainer(String containerName) throws Exception {

        // Retrieve storage account from connection-string.
        CloudStorageAccount storageAccount = CloudStorageAccount.parse(STORAGECONNECTIONSTRING);

        // Create the blob client.
        CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

        // Get a reference to a container.
        // The container name must be lower case
        CloudBlobContainer container = blobClient.getContainerReference(containerName);

        // Create the container if it does not exist.
        container.createIfNotExists();

        // Create a permissions object.
        BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

        // Include public access in the permissions object.
        containerPermissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

        // Set the permissions on the container.
        container.uploadPermissions(containerPermissions);
        return blobClient;
    }

    private String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    private void deleteBlobByUrl(String blobUrl) {
        try {
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
        } catch (Exception ex) {

        }
    }

}
