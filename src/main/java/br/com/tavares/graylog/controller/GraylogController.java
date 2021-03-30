package br.com.tavares.graylog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("log")
public class GraylogController {

    @GetMapping("/info")
    public String enviaLogInfo(){
      log.info("Log INFO");
      return "Log INFO";
    }

    @GetMapping("/error")
    public String enviaLogError(){
        log.error("Log ERROR");
        return "Log ERROR";
    }

}

