package Scenarios;

import de.rss.fachstudie.MiSim.entities.networking.NetworkRequestSendEvent;
import de.rss.fachstudie.MiSim.export.CSVData;
import desmoj.core.simulator.Experiment;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import testutils.RandomTieredModel;
import testutils.TestUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Lion Wagner
 */
@Disabled
public class PerformanceTests {
    static class TestResult implements CSVData {
        public long execution_duration_ms;
        public int simulated_duration_ms;
        public int number_of_services;
        public int number_of_interconnections;
        public long number_of_sendEvents;
    }

    static List<TestResult> performanceTest(int max_service_count, int tier_count, int iterations) {
        List<TestResult> testResults = new LinkedList<>();

        System.out.printf("Running %s tiers, %s services%n", tier_count, max_service_count);
        System.out.printf("Running %s iterations.%n", iterations);
        for (int i = 0; i < iterations; i++) {
            System.out.printf("Run #%s%n", i + 1);

            RandomTieredModel model = new RandomTieredModel("TestModel", max_service_count, tier_count);
            int simulatedDuration = TestUtils.nextNonNegative(10801); //10800 are 3h of realtime with the default unit SECONDS
            Experiment e = TestUtils.getExampleExperiment(model, simulatedDuration);
            long start = System.currentTimeMillis();
            e.start();
            e.finish();
            long duration = System.currentTimeMillis() - start;

            TestResult result = new TestResult();
            result.simulated_duration_ms = simulatedDuration * 1000;
            result.execution_duration_ms = duration;
            result.number_of_services = model.getAll_microservices().size();
            result.number_of_interconnections = model.getAll_microservices().stream().mapToInt(ms -> ms.getOperations().length).sum();
            result.number_of_sendEvents = NetworkRequestSendEvent.getCounterSendEvents();
            NetworkRequestSendEvent.resetCounterSendEvents();

            testResults.add(result);
            TestUtils.resetModel(model);
        }
//        System.out.println(times);
        return testResults;
    }

    @Test
    void SystemScalingPerformanceTest() {
        List<TestResult> data = new LinkedList<>();

        //warmup
        System.out.println("Warmup");
        performanceTest(15, 15, 3);
        System.out.println("Warmup Done");

        for (int tierCount = 1; tierCount <= 10; tierCount++) {
            for (int max_service_per_tier = 1; max_service_per_tier <= 50; max_service_per_tier++) {
                List<TestResult> result = performanceTest(max_service_per_tier, tierCount, 10);
                data.addAll(result);
            }
        }

        TestUtils.writeOutput(data, "./performance_test_result.csv");
    }

    @Test
    void SystemGeneratorImpactTest() {
        List<TestResult> testResults = new LinkedList<>();

        //warmup
        System.out.println("Warmup");
        performanceTest(15, 15, 3);
        System.out.println("Warmup Done");

        double maxMsgPerSec = 2000.0;

        for (int msgPerSec = 1; msgPerSec <= maxMsgPerSec; msgPerSec += 10) {
            System.out.printf("progress %s%n", msgPerSec / maxMsgPerSec);

            RandomTieredModel model = new RandomTieredModel("Large TestModel", 500, 15);
            int genCount = Math.max(1, TestUtils.nextNonNegative(msgPerSec));
            double interval = msgPerSec == 1 ? (double) msgPerSec / genCount : 1;
            model.setGenerator_count(genCount);
            model.setGenerator_interval(interval);
            int simulatedDuration = TestUtils.nextNonNegative(10801); //10800 are 3h of realtime with the default unit SECONDS
            Experiment exp = TestUtils.getExampleExperiment(model, simulatedDuration);

            long start = System.currentTimeMillis();
            exp.start();
            exp.finish();
            long duration = System.currentTimeMillis() - start;


            TestResult result = new TestResult();
            result.simulated_duration_ms = simulatedDuration * 1000;
            result.execution_duration_ms = duration;
            result.number_of_services = model.getAll_microservices().size();
            result.number_of_interconnections = model.getAll_microservices().stream().mapToInt(ms -> ms.getOperations().length).sum();
            result.number_of_sendEvents = NetworkRequestSendEvent.getCounterSendEvents();

            testResults.add(result);
            TestUtils.resetModel(model);

        }

        TestUtils.writeOutput(testResults, "./message_scale_test.csv");
    }
}
