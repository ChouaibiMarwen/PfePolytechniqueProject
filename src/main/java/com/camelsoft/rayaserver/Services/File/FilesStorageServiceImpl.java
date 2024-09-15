package com.camelsoft.rayaserver.Services.File;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.camelsoft.rayaserver.Models.File.MediaModel;
import com.camelsoft.rayaserver.Repository.File.File_Repository;
import com.camelsoft.rayaserver.Repository.File.FilesStorageService;
import com.camelsoft.rayaserver.Response.Tools.FileUploadResponse;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {
    @Autowired
    private File_Repository repository;
    @Value("${spring.minio.url}")
    private String minioendpoint;
    @Value("${spring.minio.secret-key}")
    private String secretkey;
    @Value("${spring.minio.bucket}")
    private String bucket;
    @Value("${spring.minio.access-key}")
    private String accesskey;
    private static final List<String> image_accepte_type = Arrays.asList("jpeg", "jpg", "png", "gif", "bmp", "tiff", "tif", "ico", "webp", "svg", "heic", "raw");
    private static final List<String> image_accepted_types = Arrays.asList(
            "JPEG", "jpeg", "svg", "png", "SVG", "PNG", "JPG", "jpg", "pdf", "mp4",
            "avi", "mpg", "mpeg", "mov", "mkv", "flv", "wmv", "webm", "3gp", "ogv"
    );
    private final Log logger = LogFactory.getLog(FilesStorageServiceImpl.class);


    public Boolean checkformat(MultipartFile file){
        if(file == null || file.isEmpty())
        {
            return false;
        }
        String extension = file.getContentType().substring(file.getContentType().indexOf("/") + 1).toLowerCase(Locale.ROOT);
        if (!image_accepte_type.contains(extension)) {
            return false;
        }
        return true;
    }
    public Boolean checkformatList(Set<MultipartFile> files){
        for (MultipartFile f : files) {
            if( f == null ||  f.isEmpty())
            {
                return false;
            }
            String extension = f.getContentType().substring(f.getContentType().indexOf("/") + 1).toLowerCase(Locale.ROOT);
            if (!image_accepte_type.contains(extension)) {
                return false;
            }
        }

        return true;
    }
    public Boolean checkformatArrayList(List<MultipartFile> files){
        for (MultipartFile f : files) {
            if( f == null ||  f.isEmpty())
            {
                return false;
            }
            String extension = f.getContentType().substring(f.getContentType().indexOf("/") + 1).toLowerCase(Locale.ROOT);
            if (!image_accepte_type.contains(extension)) {
                return false;
            }
        }

        return true;
    }


    private String extractFileName(String imageUrl, String nonull) {
        // Define a regular expression pattern to match the filename between the last '/' and the file extension
        Pattern pattern = Pattern.compile(".*/(.*?)\\..*");

        // Use a Matcher to find the match
        Matcher matcher = pattern.matcher(imageUrl);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return nonull; // Match not found, handle it accordingly
        }
    }




    @Override
    public MediaModel save_file(MultipartFile file, String directory) {
        try {
            MediaModel fileresult = this.upload_file_to_minio(file,directory);
            return fileresult;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }

    }


    @Override
    @Transactional
    public void delete_file_by_paths(Long imageid) {
        if (this.repository.existsById(imageid))
            this.repository.deleteById(imageid);

    }
    private String[] extractBucketAndObjectKey(String url) {
        // Assuming the URL format is something like http://localhost:9000/bucket-name/object-key
        String[] parts = url.split("/", 4);
        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid URL format");
        }
        return new String[]{parts[2], parts[3]};
    }
    @Override
    public void delete_file_by_path_from_cdn(Long imageid) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (this.repository.existsById(imageid))
        {
            MediaModel file = this.repository.findById(imageid).get();
            String[] bucketAndObject = extractBucketAndObjectKey(file.getUrl());
            String bucketName = bucketAndObject[0];
            String objectKey = bucketAndObject[1];

            // Create MinIO client
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(minioendpoint)
                    .credentials(accesskey, secretkey)
                    .build();

            // Delete the object from MinIO
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .build());
            this.repository.deleteById(imageid);

        }

    }

    public MediaModel findbyid(Long media_id){
        if (this.repository.existsById(media_id))
        {
            return  this.repository.findById(media_id).get();

        }
        return null;
    }

    @Override
    public void delete_all_file_by_path(Set<MediaModel> images) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        for (MediaModel fileModel: images) {
            if (fileModel.getUrl()!=null)
           //impliment delete all minio
                this.delete_file_by_path_from_cdn(fileModel.getId());
            if (this.repository.existsById(fileModel.getId()))
                this.repository.deleteById(fileModel.getId());
        }

    }

    @Override
    public Set<MediaModel> save_all(Set<MultipartFile> files, String folderName) {
        try {
            Set<MediaModel> images = new HashSet<>();
            for (MultipartFile file : files){
                // Create a new PutObjectRequest object.
                MediaModel fileresult = this.upload_file_to_minio(file,folderName);
                images.add(fileresult);
            }
            return images;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }

    }


    /******************************************************************************************/
    @Override
    public MediaModel save_file_local(MultipartFile file, String directory) {

        try {
            String name = ((new Date()).getTime() + file.getOriginalFilename()).replaceAll("\\s+", "");
            String extention = file.getContentType().substring(file.getContentType().indexOf("/") + 1).toLowerCase(Locale.ROOT);
            Path root = Paths.get("WebContent/"+directory);
            if(!Files.exists(root))
                Files.createDirectories(root);


            String namesaved = ((name+(new Date()).getTime()).replaceAll("\\s+","")+"."+extention);
            Path filepath = root.resolve(namesaved);
            Resource resourcepast = new UrlResource(filepath.toUri());
            if (resourcepast.exists() || resourcepast.isReadable())
                FileSystemUtils.deleteRecursively(filepath.toFile());

            Files.copy(file.getInputStream(), root.resolve(namesaved));
            Path file_info = root.resolve(namesaved);
            Resource resource = new UrlResource(file_info.toUri());

            if (resource.exists() || resource.isReadable()) {
                MediaModel fileresult = new MediaModel(
                        name,
                        resource.getURI().getPath(),
                        file.getContentType(),
                        file.getSize(),
                        resource.getURI().getPath()
                );

                return this.repository.save(fileresult);

            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }

    }



    @Override
    public void delete_file_by_path_local(String path,Long imageid) {
        Path root_local= Paths.get(path);
        if(this.repository.existsById(imageid))
            this.repository.deleteById(imageid);
        FileSystemUtils.deleteRecursively(root_local.toFile());
    }

    @Override
    public void delete_all_file_by_path_local(Set<MediaModel> images) {
        for(int i = 0 ; i<images.size();i++){
            Path root_local= Paths.get(images.iterator().next().getUrl());
            if(this.repository.existsById(images.iterator().next().getId()))
                this.repository.deleteById(images.iterator().next().getId());
            FileSystemUtils.deleteRecursively(root_local.toFile());
        }

    }

    @Override
    public Set<MediaModel> save_all_local(List<MultipartFile> file, String directory){
        try {
            Set<MediaModel> images = new HashSet<>();
            for(int i = 0 ; i<file.size();i++){
                String extention = file.get(i).getContentType().substring(file.get(i).getContentType().indexOf("/") + 1).toLowerCase(Locale.ROOT);
                if (extention.contains("+xml") && extention.contains("svg"))
                    extention = "svg";
                logger.error(extention);

                if (!image_accepted_types.contains(extention))
                    throw new RuntimeException("Could not read the file!");
                MediaModel resource_media = this.save_file_local(file.get(i), directory);
                resource_media.setRange(i);
                images.add(resource_media);
            }
            return images;
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }

    }


    /******************************************************************************************/
    private MediaModel upload_file_to_minio(MultipartFile file, String folderPath){
        FileUploadResponse response = new FileUploadResponse();

        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            String name = ((new Date()).getTime() + file.getOriginalFilename()).replaceAll("\\s+", "");

            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(minioendpoint)
                            .credentials(accesskey, secretkey)
                            .build();

            // Make 'asiatrip' bucket if not exist.
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            } else {
                System.out.println("Bucket "+bucket+" already exists.");
            }

            // Upload '/home/user/Photos/asiaphotos.zip' as object name 'asiaphotos-2015.zip' to bucket
            // 'asiatrip'.
            // Upload the file to the bucket.
            String objectName = folderPath + "/" + file.getOriginalFilename().replace(" ","");

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName) // Use the filename as the object name
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());
            System.out.println(
                    "'/home/user/Photos/asiaphotos.zip' is successfully uploaded as "
                            + "object '"+file.getOriginalFilename()+"' to bucket "+bucket+" .");
            // Generate the public URL for the uploaded file
            String presignedUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(objectName)
                            .expiry(7, TimeUnit.DAYS) // Link valid for 7 days
                            .build());
            // Construct the URI for the file
            String uri = String.format("%s/%s/%s", minioendpoint, bucket, objectName);

            System.out.println("File URL: " + presignedUrl);
            System.out.println("File URI: " + uri);
            response.setUri(uri);
            response.setPresignedUrl(presignedUrl);
            MediaModel fileresult = new MediaModel(
                    file.getOriginalFilename().replace(" ",""),
                    uri,
                    file.getContentType(),
                    file.getSize(),
                    presignedUrl
            );

            MediaModel result = this.repository.save(fileresult);
            return result;
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
            System.out.println("HTTP trace: " + e.getMessage());
            response.setError_message(e.getMessage());
            return null;
        }
    }

}
