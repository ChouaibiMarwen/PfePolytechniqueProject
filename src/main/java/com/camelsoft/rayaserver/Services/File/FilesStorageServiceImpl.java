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
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {
    @Autowired
    private File_Repository repository;
    private static final List<String> image_accepte_type = Arrays.asList("jpeg", "jpg", "png", "gif", "bmp", "tiff", "tif", "ico", "webp", "svg", "heic", "raw");
    private final AmazonS3 space;
    private final String bucketName = "bucketName";
    private static final List<String> image_accepted_types = Arrays.asList(
            "JPEG", "jpeg", "svg", "png", "SVG", "PNG", "JPG", "jpg", "pdf", "mp4",
            "avi", "mpg", "mpeg", "mov", "mkv", "flv", "wmv", "webm", "3gp", "ogv"
    );
    private final Log logger = LogFactory.getLog(FilesStorageServiceImpl.class);

    public FilesStorageServiceImpl() {
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(new BasicAWSCredentials("AccessKey", "SecretKey"));
        this.space = AmazonS3ClientBuilder
                .standard()
                .withCredentials(awsCredentialsProvider)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("serviceendpoint", "signingRegion"))
                .build();
    }

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
    @Override
    public MediaModel save_file(MultipartFile file, String folderName) throws IOException {

        // Create a new PutObjectRequest object.
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        // Check if the folder exists, and create it if it doesn'
        String fileName = ((new Date()).getTime() + file.getOriginalFilename()).replaceAll("\\s+", "");

        String objectKey = folderName.replaceAll("\\s+", "") + "/" + folderName.replaceAll("\\s+", "") + "/" + fileName;

        PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucketName,
                objectKey,
                file.getInputStream(),
                metadata);

        // Set the object's ACL to public read
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read); // Public-read permission

        putObjectRequest.setAccessControlList(acl);
        // Upload the object to DigitalOcean Spaces.
        space.putObject(putObjectRequest);

        String fileNameresult = extractFileName(objectKey, file.getOriginalFilename());
        MediaModel fileresult = new MediaModel(
                fileNameresult,
                objectKey,
                file.getContentType(),
                file.getSize()
        );
        return this.repository.save(fileresult);
    }



    public MediaModel save_file001(MultipartFile file, String folderName) throws IOException {

        // Create a new PutObjectRequest object.
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());
        // Check if the folder exists, and create it if it doesn'
        String fileName = ((new Date()).getTime() + file.getOriginalFilename()).replaceAll("\\s+", "");

        String objectKey = folderName.replaceAll("\\s+", "") + "/" + folderName.replaceAll("\\s+", "") + "/" + fileName;

        PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucketName,
                objectKey,
                file.getInputStream(),
                metadata);

        // Set the object's ACL to public read
        AccessControlList acl = new AccessControlList();
        acl.grantPermission(GroupGrantee.AllUsers, Permission.Read); // Public-read permission

        putObjectRequest.setAccessControlList(acl);
        // Upload the object to DigitalOcean Spaces.
        space.putObject(putObjectRequest);

        String fileNameresult = extractFileName(objectKey, file.getOriginalFilename());
        return  new MediaModel(
                fileNameresult,
                objectKey,
                file.getContentType(),
                file.getSize()
        );
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
    public MediaModel save_fileBMP(MultipartFile file, String directory, String name, String extention) {
        try {
            Path root = Paths.get("WebContent/" + directory);
            if (!Files.exists(root))
                Files.createDirectories(root);

            String namesaved = ((name.substring(0, name.lastIndexOf(".")) + (new Date()).getTime()).replaceAll("\\s+", "") + ".bmp");
            Path filepath = root.resolve(namesaved);
            Resource resourcepast = new UrlResource(filepath.toUri());

            if (resourcepast.exists() || resourcepast.isReadable())
                FileSystemUtils.deleteRecursively(filepath.toFile());

            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            BufferedImage bmpImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            bmpImage.getGraphics().drawImage(originalImage, 0, 0, null);

            // Create a ByteArrayOutputStream to hold the BMP image data
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // Write the BMP image data to the ByteArrayOutputStream
            ImageIO.write(bmpImage, "bmp", baos);
            // Write the BMP image data to the file.
            try (FileOutputStream fos = new FileOutputStream(filepath.toFile())) {
                fos.write(baos.toByteArray());
            }

            Path file_info = root.resolve(namesaved);
            Resource resource = new UrlResource(file_info.toUri());

            if (resource.exists() || resource.isReadable()) {
                MediaModel fileresult = new MediaModel(
                        name,
                        resource.getURI().getPath(),
                        "image/bmp", // Set content type to BMP
                        baos.size() // Size of the BMP image
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
    @Transactional
    public void delete_file_by_paths(Long imageid) {
        if (this.repository.existsById(imageid))
            this.repository.deleteById(imageid);

    }

    @Override
    public void delete_file_by_path_from_cdn(String path, Long imageid) {
        if (this.repository.existsById(imageid))
        {
            space.deleteObject(bucketName, path);
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
    public void delete_all_file_by_path(Set<MediaModel> images) {
        for (MediaModel fileModel: images) {
            if (fileModel.getUrl()!=null)
            space.deleteObject(bucketName, fileModel.getUrl());
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
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());
                // Check if the folder exists, and create it if it doesn'
                String fileName = ((new Date()).getTime() + file.getOriginalFilename()).replaceAll("\\s+", "");

                String objectKey = folderName.replaceAll("\\s+", "") + "/" + folderName.replaceAll("\\s+", "") + "/" + fileName;

                PutObjectRequest putObjectRequest = new PutObjectRequest(
                        bucketName,
                        objectKey,
                        file.getInputStream(),
                        metadata);

                // Set the object's ACL to public read
                AccessControlList acl = new AccessControlList();
                acl.grantPermission(GroupGrantee.AllUsers, Permission.Read); // Public-read permission

                putObjectRequest.setAccessControlList(acl);
                // Upload the object to DigitalOcean Spaces.
                space.putObject(putObjectRequest);

                String fileNameresult = extractFileName(objectKey, file.getOriginalFilename());
                MediaModel fileresult = new MediaModel(
                        fileNameresult,
                        objectKey,
                        file.getContentType(),
                        file.getSize()
                );
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
                        file.getSize()
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

}
