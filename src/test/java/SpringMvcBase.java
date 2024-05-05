import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {com.solution.Application.class}, webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringMvcBase {

    private static final Logger logger = LoggerFactory.getLogger(SpringMvcBase.class);
    @Autowired
    private WebApplicationContext applicationContext;
    @Autowired
    private Environment environment;
    protected MockMvc mockMvc;

    @Before
    public void before() throws Exception {
        if (environment != null && environment.getActiveProfiles() != null && environment.getActiveProfiles().length > 0) {
            logger.info("current profile [{}]", Arrays.toString(environment.getActiveProfiles()));
        } else {
            logger.info("current profile <not set>");
        }
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }
}