package com.UPBEATg11.OOP_Project;

import orchestrator.IllegalConfiguration;
import orchestrator.MissingConfigurationVariable;
import orchestrator.Upbeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.util.Map;

@Controller
public class ConfigurationController {
    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/getConfig")
    @SendTo("/topic/config")
    public Object getConfig() {
        try {
            return Upbeat.getConfig();
        } catch (FileNotFoundException | MissingConfigurationVariable | IllegalConfiguration e) {
            return Map.of("message",e.getMessage());
        }
    }

    @MessageMapping("/setConfig")
    @SendTo("/topic/config")
    public Object setConfig(Map<String,Long> newConfig) {
        if(Upbeat.getGameState() != Upbeat.GameState.configSetting)
            return Map.of("message","Too late");

        try {
            Upbeat.setConfig(newConfig);
            return Upbeat.getConfig();

        } catch (FileNotFoundException | MissingConfigurationVariable | IllegalConfiguration e) {
            return Map.of("message",e.getMessage());
        }
    }
}
