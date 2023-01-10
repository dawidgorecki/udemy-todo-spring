package pl.dawidgorecki.controller;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dawidgorecki.TaskConfigurationProperties;

@RestController
@RequestMapping("/info")
public class InfoController {
    private final DataSourceProperties dataSource;
    private final TaskConfigurationProperties myProp;

    public InfoController(DataSourceProperties dataSourceProperties, TaskConfigurationProperties taskConfigurationProperties) {
        this.dataSource = dataSourceProperties;
        this.myProp = taskConfigurationProperties;
    }

    @GetMapping("/url")
    public String url() {
        return dataSource.getUrl();
    }

    @GetMapping("/prop")
    public boolean myProp() {
        return myProp.getTemplate().isAllowMultipleTasks();
    }
}
