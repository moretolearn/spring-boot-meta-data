package com.moretolearn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.SpringVersion;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;


@RestController
public class MetadataController {

    @Value("${spring.application.name}")
    private String appName;
    private final Environment env;

    private final BuildProperties buildProperties;
    
//    private final DataSource dataSource;

    public MetadataController(
            BuildProperties buildProperties,
            Environment env
//            DataSource dataSource
            ) {
        this.buildProperties = buildProperties;
//        this.dataSource = dataSource;
        this.env = env;
    }

    @GetMapping("/metadata")
    public Map<String, Object> metadata() throws SQLException {

        Map<String, Object> response = new HashMap<>();

        /* ---------------- Application ---------------- */
        response.put("application", Map.of(
                "name", appName,
                "timestamp", LocalDateTime.now()
        ));

        /* ---------------- Build ---------------- */
        response.put("build", Map.of(
                "version", buildProperties.getVersion(),
                "artifact", buildProperties.getArtifact(),
                "group", buildProperties.getGroup(),
                "time", buildProperties.getTime()
        ));

        /* ---------------- Runtime ---------------- */
        response.put("runtime", Map.of(
                "javaVersion", System.getProperty("java.version"),
                "jvmName", System.getProperty("java.vm.name"),
                "os", System.getProperty("os.name"),
                "uptimeMs", ManagementFactory.getRuntimeMXBean().getUptime(),
                "springBootVersion", SpringBootVersion.getVersion(),
                "springVersion", SpringVersion.getVersion()
//                "srpingSecurityVersion"
        ));
        
        /* ---------------- Database ---------------- */
//        response.put("database", Map.of(
//                "DB Name", dataSource.getConnection().getMetaData().getDatabaseProductName(),
//                "DB Version", dataSource.getConnection().getMetaData().getDatabaseProductVersion(),
//                "URL", dataSource.getConnection().getMetaData().getURL(),
//                "User", dataSource.getConnection().getMetaData().getUserName()
//               
//        ));
        
        /* ---------------- Container / K8s (Optional) ---------------- */
        response.put("container", Map.of(
//                "pod", env.getProperty("POD_NAME"),
//                "imageTag", env.getProperty("IMAGE_TAG")
        ));
        

        return response;
    }
}

