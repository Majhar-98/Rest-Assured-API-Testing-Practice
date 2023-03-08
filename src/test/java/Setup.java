import org.junit.Before;
import org.testng.annotations.BeforeTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Setup {
    public Properties props;
@BeforeTest
    public void initConfig() throws IOException {
        FileInputStream file = new FileInputStream("./src/test/resources/config.properties");
        props = new Properties();
        props.load(file);

    }
}
