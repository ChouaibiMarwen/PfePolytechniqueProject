package com.camelsoft.rayaserver.Repository.File;

import com.camelsoft.rayaserver.Models.File.MediaModel;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

public interface FilesStorageService {
    public MediaModel save_file(MultipartFile file, String directory) throws IOException;
    public Set<MediaModel> save_all(Set<MultipartFile> file, String directory);
    public void delete_file_by_paths(Long imageid);
    public void delete_file_by_path_from_cdn(Long imageid) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;
    public void delete_all_file_by_path(Set<MediaModel> images) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;


    public MediaModel save_file_local(MultipartFile file, String directory);
    public Set<MediaModel> save_all_local(List<MultipartFile> file, String directory);
    public void delete_file_by_path_local(String filename,Long imageid);
    public void delete_all_file_by_path_local(Set<MediaModel> images);
}
