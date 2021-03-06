package scenarios;

import java.io.File;

import cambio.simulator.models.MainModel;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Lion Wagner
 */
@Disabled
public class ChaosMonkeyExperimentTest {

    @Test
    void ExampleExecutionRun() {
        File f = new File("./Examples/example_architecture_model.json");
        File f2 = new File("./Examples/example_experiment_chaosmonkey.json");
        String[] args = new String[] {"-a", f.getAbsolutePath(), "-e", f2.getAbsolutePath(), "-d"};
        MainModel.main(args);
    }
}
