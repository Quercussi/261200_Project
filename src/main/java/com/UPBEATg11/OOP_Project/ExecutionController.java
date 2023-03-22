package com.UPBEATg11.OOP_Project;

import entities.CityCrew;
import orchestrator.InvalidToken;
import orchestrator.State;
import orchestrator.Upbeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import parsers.SyntaxError;

import java.security.Principal;
import java.util.Map;

@Controller
public class ExecutionController {
    @Autowired
    private SimpMessagingTemplate template;

    public SimpMessagingTemplate getMessagingTemplate() { return template; }

    @MessageMapping("getGameState")
    @SendTo("/topic/gameState")
    public Object getGameState() { return Upbeat.getGameState(); }

    @MessageMapping("startGame")
    @SendTo("/topic/gameState")
    public Object gameState() {
        if(Upbeat.getGameState() == Upbeat.GameState.joining)
            Upbeat.gameState = Upbeat.GameState.gameStart;

        Upbeat.setStates();
        return Upbeat.gameState;
    }

    @MessageMapping("getConstructionPlan")
    @SendToUser("/queue/constructionPlan")
    public Object getConstructionPlan(@Payload Map<String,Object> payload, Principal user) {
        String uuid = (String) payload.get("token");
        Integer id = (Integer) payload.get("crewId");

        if(uuid == null || id == null)
            return Map.of("isOkay",false,"message","Missing key in the request body");

        CityCrew crew = Upbeat.getCrewWith(id);

        if(crew == null)
            return Map.of("isOkay",false,"message","There is no such crewId within the game.");


        return Map.of("isOkay",true,"message",crew.getConstructionPlanStr());
    }

    @MessageMapping("setConstructionPlan")
    @SendToUser("/queue/compileMessage")
    public Object setConstructionPlan(@Payload Map<String,Object> payload, Principal user) {
        String uuid = (String) payload.get("token");
        Integer id = (Integer) payload.get("crewId");
        String constructionPlan = (String) payload.get("constructionPlan");

        if(uuid == null || id == null || constructionPlan == null)
            return Map.of("isOkay",false,"message","Missing key in the request body");

        CityCrew crew = Upbeat.getCrewWith(id);

        if(crew == null)
            return Map.of("isOkay",false,"message","There is no such crewId within the game.");

        long rev_cost = Upbeat.get_rev_cost();
        if(crew.getBudget() < rev_cost)
            return Map.of("isOkay",false,"message","Not enough budget; budget required: 100");

        try { crew.setConstructionPlan(constructionPlan, uuid); }
        catch (SyntaxError | InvalidToken e) {
            return Map.of("isOkay",false,"message",e.getMessage());
        }

        crew.withdraw(rev_cost);
        return Map.of("isOkay",true,"message","okay...");
    }

    @MessageMapping("execute")
    @SendTo("/topic/alterations")
    public Object execute(Map<String,Object> payload) {
        String uuid = (String) payload.get("token");
        Integer id = (Integer) payload.get("crewId");

        if(uuid == null || id == null)
            return Map.of("isOkay",false,"message","Missing key in the request body");

        CityCrew crew = Upbeat.getCrewWith(id);

        if(crew == null)
            return Map.of("isOkay",false,"message","There is no such crewId within the game.");

        State state = Upbeat.currentState;
        if(state.getCrew().getId() != id)
            return Map.of("isOkay",false,"message","It is not your turn.");

        try { state.execute(uuid); }
        catch (InvalidToken e) { return Map.of("isOkay",false,"message",e.getMessage()); }

        return Map.of("isOkay",true,"message","okay...");
    }

    @MessageMapping("resign")
    @SendToUser("/queue/resignMessage")
    public Object resign(Map<String,Object> payload, Principal user) {
        String uuid = (String) payload.get("token");
        Integer id = (Integer) payload.get("crewId");

        if(uuid == null || id == null)
            return Map.of("isOkay",false,"message","Missing key in the request body");

        CityCrew crew = Upbeat.getCrewWith(id);

        if(crew == null)
            return Map.of("isOkay",false,"message","There is no such crewId within the game.");

        try { crew.resign(uuid); }
        catch (InvalidToken e) { return Map.of("isOkay",false,"message",e.getMessage()); }

        return Map.of("isOkay",true,"message","okay...");
    }
}
