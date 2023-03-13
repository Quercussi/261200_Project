package com.UPBEATg11.OOP_Project;

import entities.CityCrew;
import entities.Territory;
import orchestrator.IllegalConfiguration;
import orchestrator.MissingConfigurationVariable;
import orchestrator.State;
import orchestrator.Upbeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import parsers.StatementParser;
import parsers.SyntaxError;

import java.io.FileNotFoundException;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
