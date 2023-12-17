package oop.backend.crawler.abstractcrawler;

import oop.backend.crawler.DataCrawler;
import oop.backend.dtos.eplatform.NiftyTopDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public abstract class GetNiftyTop implements DataCrawler {
    public ResponseEntity<?> getDataFromNiftyGateway(String selection) {
        try {
            // Phần logic chung ở đây
            List<?> data = getData(selection);
            // Xử lý data hoặc trả về nguyên dạng tùy thuộc vào logic chung
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid selection");
        }
    }

    public abstract List<NiftyTopDTO> getData(String selection) throws Exception;
}
