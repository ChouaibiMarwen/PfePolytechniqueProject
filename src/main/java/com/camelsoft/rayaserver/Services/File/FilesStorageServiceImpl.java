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
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {
    @Autowired
    private File_Repository repository;
    private static final List<String> image_accepte_type = Arrays.asList("jpeg", "jpg", "png", "gif", "bmp", "tiff", "tif", "ico", "webp", "svg", "heic", "raw");
    private final MinioClient minioClient;
    private final String bucketName = "rayabucket";
    private static final List<String> image_accepted_types = Arrays.asList(
            "JPEG", "jpeg", "svg", "png", "SVG", "PNG", "JPG", "jpg", "pdf", "mp4",
            "avi", "mpg", "mpeg", "mov", "mkv", "flv", "wmv", "webm", "3gp", "ogv"
    );
    private final Log logger = LogFactory.getLog(FilesStorageServiceImpl.class);

    private String extractFileName(String imageUrl, String nonull) {
        // Define a regular expression pattern to match the filename between the last '/' and the file extension
        Pattern pattern = Pattern.compile(".*/(.*?)\\..*");

        // Use a Matcher to find the match
        Matcher matcher = pattern.matcher(imageUrl);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            // If no match is found, return the original filename
            return nonull != null ? nonull : "unknown";
        }
    }




    public FilesStorageServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public Boolean checkformat(MultipartFile file) {
        return file != null && !file.isEmpty() && image_accepte_type.contains(file.getContentType().split("/")[1].toLowerCase(Locale.ROOT));
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

    public Boolean checkFormatArrayList(List<MultipartFile> files) {
        return files.stream().allMatch(this::checkformat);
    }
    @Override
    public void delete_file_by_path_from_cdn(String filename, Long imageid) {
        try {
            if (repository.existsById(imageid)) {
                minioClient.removeObject(
                        RemoveObjectArgs.builder().bucket(bucketName).object(filename).build()
                );
                repository.deleteById(imageid);
            }
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error occurred while deleting the file: " + e.getMessage(), e);
        }
    }

    @Override
    public MediaModel save_file_local(MultipartFile file, String directory) {
        try {
            String fileName = ((new Date()).getTime() + file.getOriginalFilename()).replaceAll("\\s+", "");
            String objectKey = directory.replaceAll("\\s+", "") + "/" + fileName;

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            String fileNameResult = extractFileName(objectKey, file.getOriginalFilename());
            MediaModel fileResult = new MediaModel(
                    fileNameResult,
                    objectKey,
                    file.getContentType(),
                    file.getSize()
            );
            return repository.save(fileResult);
        } catch (MinioException e) {
            throw new RuntimeException("Error occurred while uploading the file: " + e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while handling the file input stream: " + e.getMessage(), e);
        }
    }

    @Override
    public Set<MediaModel> save_all_local(List<MultipartFile> files, String directory) {
        try {
            Set<MediaModel> images = new HashSet<>();
            for (MultipartFile file : files) {
                String fileName = ((new Date()).getTime() + file.getOriginalFilename()).replaceAll("\\s+", "");
                String objectKey = directory.replaceAll("\\s+", "") + "/" + fileName;

                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectKey)
                                .stream(file.getInputStream(), file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );

                String fileNameResult = extractFileName(objectKey, file.getOriginalFilename());
                MediaModel fileResult = new MediaModel(
                        fileNameResult,
                        objectKey,
                        file.getContentType(),
                        file.getSize()
                );
                images.add(fileResult);
            }
            return images;
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error occurred while storing files: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete_file_by_path_local(String filename, Long imageid) {
        try {
            if (repository.existsById(imageid)) {
                Path filePath = Paths.get(filename);
                Files.deleteIfExists(filePath);
                repository.deleteById(imageid);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while deleting the local file: " + e.getMessage(), e);
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
    public void delete_file_by_paths(Long id) {
        Optional<MediaModel> mediaModel = repository.findById(id);
        if (mediaModel.isPresent()) {
            try {
                // Remove file from MinIO storage
                minioClient.removeObject(
                        RemoveObjectArgs.builder().bucket(bucketName).object(mediaModel.get().getUrl()).build()
                );
                // Remove file entry from the database
                repository.deleteById(id);
            } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RuntimeException("Error occurred while deleting the file: " + e.getMessage(), e);
            }
        } else {
            throw new RuntimeException("File with ID " + id + " not found.");
        }
    }

    public MediaModel findbyid(Long media_id){
        if (this.repository.existsById(media_id))
        {
            return  this.repository.findById(media_id).get();

        }
        return null;
    }




    /******************************************************************************************/
  /*  @Override
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
*/


   /* @Override
    public void delete_file_by_path_local(String path,Long imageid) {
        Path root_local= Paths.get(path);
        if(this.repository.existsById(imageid))
            this.repository.deleteById(imageid);
        FileSystemUtils.deleteRecursively(root_local.toFile());
    }
*/
 /*   @Override
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

    }*/
    /******************************************************************************************/

}
