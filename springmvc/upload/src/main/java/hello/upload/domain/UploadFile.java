package hello.upload.domain;

import lombok.Data;

@Data
public class UploadFile {
    //둘을 구분하는 이유는 사용자 A,B 가 같은 파일을 업로드하면 내 디스크에 같은 파일이 두개 업로드 되면 파일이
    //커질테니 storeFileName 은 uuid 같이 파일명을 안겹치게 만들어야됨.
    //같은 파일 이름이 업로드 되면 기존 파일이름과 충돌날 수 있음.
    private String uploadFileName;//업로드 파일
    private String storeFileName;//시스템을 저장하는 파일

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
