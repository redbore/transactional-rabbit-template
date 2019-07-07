package transactional.rabbit.template;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpringBootTest
public class ProducerServiceTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private ProducerService producerService;

    @Autowired
    private RabbitTemplate testRabbitTemplate;

    @Test
    public void testNormalExecution() {
        producerService.normalExecution();
        sleep(1000);
        Assert.assertNotNull(testRabbitTemplate.receiveAndConvert());
    }

    @Test
    public void testExecutionWithException() {
        Assert.assertThrows(RuntimeException.class, () -> producerService.executionWithException());
        sleep(1000);
        Assert.assertNull(testRabbitTemplate.receiveAndConvert());
    }

    @Test
    public void testMultipleTransactions() {
        producerService.normalExecution();
        Assert.assertThrows(RuntimeException.class, () -> producerService.executionWithException());
        producerService.normalExecution();
        sleep(1000);
        Assert.assertNotNull(testRabbitTemplate.receiveAndConvert());
        Assert.assertNotNull(testRabbitTemplate.receiveAndConvert());
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
