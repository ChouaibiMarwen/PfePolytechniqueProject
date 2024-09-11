package com.camelsoft.rayaserver.Repository.File;

import com.camelsoft.rayaserver.Models.File.MediaModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface FilesStorageService {
    public MediaModel save_file(MultipartFile file, String directory) throws IOException;
    public MediaModel save_fileBMP(MultipartFile file, String directory, String filename, String extention);
    public Set<MediaModel> save_all(Set<MultipartFile> file, String directory);
    public void delete_file_by_paths(Long imageid);
    public void delete_file_by_path_from_cdn(String filename,Long imageid);
    public void delete_all_file_by_path(Set<MediaModel> images);


    public MediaModel save_file_local(MultipartFile file, String directory);
    public Set<MediaModel> save_all_local(List<MultipartFile> file, String directory);
    public void delete_file_by_path_local(String filename,Long imageid);
    public void delete_all_file_by_path_local(Set<MediaModel> images);
}
