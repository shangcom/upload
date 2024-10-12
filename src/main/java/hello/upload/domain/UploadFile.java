package hello.upload.domain;

import lombok.Data;

@Data
public class UploadFile {

    private String uploadFileName;
    // 고객이 업로드한 파일 명.

    private String storeFileName;
    // 서버에서 관리하는 파일 명.
    // 고객이 업로드한 이름으로 서버 내부에 저장 X !!!!!!!!!!
    // 고객이 중복 이름으로 업로드할 수 있음.
    // 이름 증복되면 덮어쓰기 됨.
    // UUID 등으로 별도 처리 해줘야 한다.


    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
