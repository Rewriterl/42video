import com.stelpolvo.video.Neo42VideoApplication;
import com.stelpolvo.video.service.ElasticSearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Neo42VideoApplication.class)
public class ElasticTest {

    @Autowired
    private ElasticSearchService elasticSearchService;

    @Test
    public void test() {
        elasticSearchService.deleteAllVideos();
    }
}
