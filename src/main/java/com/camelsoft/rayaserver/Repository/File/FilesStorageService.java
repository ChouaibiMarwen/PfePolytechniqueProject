package com.camelsoft.rayaserver.Repository.File;

import com.camelsoft.rayaserver.Models.File.File_model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface FilesStorageService {
    public File_model save_file(MultipartFile file, String directory) throws IOException;
    public File_model save_file(MultipartFile file, String directory,File_model media) throws IOException;
    public File_model save_fileBMP(MultipartFile file, String directory, String filename, String extention);
    public Set<File_model> save_all(List<MultipartFile> file, String directory);
    public void delete_file_by_paths(Long imageid);
    public void delete_file_by_path_from_cdn(String filename,Long imageid);
    public void delete_all_file_by_path(Set<File_model> images);
}
