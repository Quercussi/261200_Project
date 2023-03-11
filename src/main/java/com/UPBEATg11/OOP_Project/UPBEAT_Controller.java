package com.UPBEATg11.OOP_Project;

import entities.Alteration;
import entities.CityCrew;
import entities.Territory;
import orchestrator.IllegalConfiguration;
import orchestrator.MissingConfigurationVariable;
import orchestrator.State;
import orchestrator.Upbeat;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import parsers.StatementParser;
import parsers.SyntaxError;

import java.io.FileNotFoundException;
import java.util.*;

//@RestController
//public class UPBEAT_Controller {
//    @GetMapping(value = "/config")
//    public ResponseEntity<Object> getConfig() {
//        try {
//            return new ResponseEntity<>(Upbeat.getConfig(),HttpStatus.OK);
//        } catch (FileNotFoundException | MissingConfigurationVariable | IllegalConfiguration e) {
//            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @GetMapping(value = "/territory")
//    public ResponseEntity<Territory> getTerritory() {
//        return new ResponseEntity<>(Upbeat.getTerritory(),HttpStatus.OK);
//    }
//
//    @GetMapping(value = "/currentState")
//    public ResponseEntity<State> getCurrentState() {
//        return new ResponseEntity<>(Upbeat.currentState,HttpStatus.OK);
//    }
//
//    @PostMapping("/config")
//    public ResponseEntity<String> setConfig(@RequestBody Map<String,Long> config) {
//        try {
//            Upbeat.setConfig(config);
//            return new ResponseEntity<>("okay...",HttpStatus.CREATED);
//
//        } catch (MissingConfigurationVariable | IllegalConfiguration e) {
//            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @PostMapping("/setCrews")
//    public ResponseEntity<List<CityCrew>> setCrews(@RequestBody List<String> nameList) throws SyntaxError {
//        Upbeat.ConstructGame(nameList);
//        List<CityCrew> crewList = new ArrayList<>(Upbeat.crews);
//        return new ResponseEntity<>(crewList,HttpStatus.CREATED);
//    }
//
//    @PostMapping("/setConstructionPlan/{id}")
//    public ResponseEntity<Object> setConstructionPlan(@RequestBody String constructionPlan, @PathVariable int id) {
//        constructionPlan = constructionPlan.substring(1,constructionPlan.length()-1);
//
//        for(CityCrew crew : Upbeat.crews) {
//            if(crew.getId() != id)
//                continue;
//
//            try {
//                crew.setConstructionPlan(new StatementParser(constructionPlan));
//                return new ResponseEntity<>(crew,HttpStatus.CREATED);
//
//            } catch (SyntaxError e) {
//                return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
//            }
//        }
//        return new ResponseEntity<>("Crew not founded",HttpStatus.NOT_FOUND);
//    }
//
//    @PostMapping("/execute")
//    public ResponseEntity<Map<String,Object>> execute() {
//            List<Alteration> alterations = Upbeat.currentState.execute();
//            Map<String,Object> response = Map.of("alterations",alterations,"nextState",Upbeat.currentState);
//            return new ResponseEntity<>(response,HttpStatus.OK);
//    }
//}