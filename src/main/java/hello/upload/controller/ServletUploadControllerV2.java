package hello.upload.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    /**
     * 멀티 파트 요청을 받았을 때, HttpServletRequest는 스프링이 자동으로 StandardMultipartHttpServletRequest로
     * 감싸서 처리함. 만약 멀티파트 요청이 아닌 일반 요청이 들어왔다면,
     * request.getParts() 같은 메서드들에서 ServletException, IOException 발생할 수 있음.
     * 즉, 일반 요청 들어오면 멀티파트 기능 필요한 메서드들은 동작하지 않음.
     */
    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws ServletException, IOException {
        log.info("request= {}", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName= {}", itemName);

        Collection<Part> parts = request.getParts();
        log.info("parts= {}", parts);
        for (Part part : parts) {
            log.info("=== PART ===");
            log.info("name= {}", part.getName());
            Collection<String> headerNames = part.getHeaderNames();
            for (String headerName : headerNames) {
                log.info("header {}: {}", headerName, part.getHeader(headerName) );
            }
            // 편의 메서드
            // content-disposition; filename
            log.info("submittedFilename= {}", part.getSubmittedFileName());
            log.info("size= {}", part.getSize());

            // 데이터 읽기
            InputStream inputStream = part.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            log.info("body= {}", body);

            // 파일에 저장하기
            if (StringUtils.hasText(part.getSubmittedFileName())) {
                // 파일 제목 존재 여부로 클라이언트가 파일을 업로드했는지 확인.
                String fullPath = fileDir + part.getSubmittedFileName();
                // 파일이 저장될 전체 정로 생성 : 파일이 저장될 디렉토리 경로와 업로드된 파일명 결합
                log.info("파일 저장 fullPath= {}", fullPath);
                part.write(fullPath);
                // 업로드된 파일을 서버의 지정된 경로에 저장.
            }
        }
        return "upload-form";
    }
}
