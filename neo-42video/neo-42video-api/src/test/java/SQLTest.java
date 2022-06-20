import com.stelpolvo.video.Neo42VideoApplication;
import com.stelpolvo.video.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Neo42VideoApplication.class)
public class SQLTest {

    @Autowired
    UserDao userDao;

    @Test
    void getUser() {
        System.out.println(userDao.getUserWithRolesAndInfoById(29L));
    }
}
