package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletResponse;
import java.io.*;
import java.net.InetAddress;

@SpringBootApplication
@RestController
public class DemoApplication {

    Logger logger = LoggerFactory.getLogger(DemoApplication.class);

    static long DELAY = 0;

    @Value("${greeting.name}")
    String name;

    @Value("${file.path}")
    String path;

    @GetMapping
    public String show() throws Exception {
        InetAddress addr = InetAddress.getLocalHost();

        return String.format("Host name: %s ,Welcome %s !\n", addr.getHostName(), name);
    }

    @GetMapping("/read")
    public void readFile(ServletResponse response) {


        File file = new File(path);

        try (PrintWriter out = response.getWriter()) {
            out.println("Read file >>> " + path);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
                String content;
                while ((content = reader.readLine()) != null) {
                    out.println(content);
                }
            } else {
                out.println("File does not exist.");
            }

        } catch (Exception e) {
        }


    }

    @GetMapping("/ping")
    public ResponseEntity ping() {
        logger.info("ping......");
        try {
            Thread.sleep(DELAY);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok("pong\n");
    }


    @PutMapping("/delay/{millis}")
    public ResponseEntity updateDelay(@PathVariable long millis) {
        logger.info("Set ping delay to {}", millis);
        DELAY = millis;
        return ResponseEntity.ok().build();
    }

    @GetMapping("/delay")
    public ResponseEntity getDelay() {
        return ResponseEntity.ok(DELAY + "\n");
    }


    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}

