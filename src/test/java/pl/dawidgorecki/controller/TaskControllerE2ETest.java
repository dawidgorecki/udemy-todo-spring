package pl.dawidgorecki.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.dawidgorecki.model.Task;
import pl.dawidgorecki.repository.TaskRepository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerE2ETest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository repository;

    @Test
    void httpGet_returnsAllTasks() {
        // given
        int initial = repository.findAll().size();
        repository.save(new Task("foo", LocalDateTime.now()));
        repository.save(new Task("bar", LocalDateTime.now()));

        // when
        Task[] result = restTemplate.getForObject("http://localhost:" + port + "/tasks", Task[].class);

        // then
        assertThat(result).hasSize(initial + 2);
    }

    @Test
    void httpGet_returnGivenTask() {
        // given
        Integer id = repository.save(new Task("foo", LocalDateTime.now())).getId();

        // when
        Task result = restTemplate.getForObject("http://localhost:" + port + "/tasks/" + id, Task.class);

        // then
        assertThat(result).hasFieldOrPropertyWithValue("description", "foo");
    }

    @Test
    void httpPost_createTask() {
        // given
        Task toCreate = new Task("test", LocalDateTime.now());

        // when
        ResponseEntity<Task> response = restTemplate.postForEntity("http://localhost:" + port + "/tasks", toCreate, Task.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.hasBody()).isTrue();
        assertThat(response.getBody().getDescription()).isEqualTo("test");
    }
}