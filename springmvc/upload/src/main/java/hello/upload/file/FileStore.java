package hello.upload.file;


import hello.upload.domain.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.UUID;

@Component
public class FileStore {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename){
        return fileDir + filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            //multipartFile 이 비어있지 않으면
            if(!multipartFile.isEmpty()){
                 storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    //MultipartFile 은 스프링이 제공.
    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        //MultipartFile 을 받아서 UploadFile 파일로 바꿔줌.
        if(multipartFile.isEmpty()){
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));

        return new UploadFile(originalFilename, storeFileName);
    }

    //서버에 저장하는 파일명
    private String createStoreFileName(String originalFilename){
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        //저장할 파일 이름
        return uuid+"."+ext;
    }

    //확장자를 꺼내는 작업
    private String extractExt(String originalFilename){
        int pos = originalFilename.lastIndexOf(".");//이렇게 하면 위치를 가져올 수 있음.
        return originalFilename.substring(pos+1); //. 다음에 +1이니까 .png 를 가져올 수 있음.
    }
}
