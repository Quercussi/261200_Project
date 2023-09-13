package com.UPBEATg11.OOP_Project;

import entities.CityCrew;
import entities.CountdownClock;
import entities.Territory;
import entities.Tile;
import orchestrator.InvalidToken;
import orchestrator.State;
import orchestrator.Upbeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import parsers.SyntaxError;

import java.security.Principal;
import java.util.Map;

@Controller
public class ExecutionController {
    @Autowired
    SimpMessagingTemplate template;

    public SimpMessagingTemplate getMessagingTemplate() { return template; }

    @MessageMapping("getGameState")
    @SendTo("/topic/gameState")
    public Object getGameState() { return Upbeat.getGameState(); }

    @SubscribeMapping("gameState")
    public Object autoGetGameState() { return Upbeat.getGameState(); }

    @MessageMapping("startGame")
    @SendTo("/topic/gameState")
    public Object gameState() {
        CountdownClock.setSimpMessagingTemplate(template);
        Upbeat.setSimpMessagingTemplate(template);

        if(Upbeat.getGameState() == Upbeat.GameState.joining)
            Upbeat.gameState = Upbeat.GameState.gameStart;

        if(Upbeat.currentState == null)
            Upbeat.setStates();

        getTerritoryManually();
        getStateManually();

        Upbeat.currentState.getCrew().startCountdown();

        return Upbeat.gameState;
    }

    @MessageMapping("getConstructionPlan")
    @SendToUser("/queue/constructionPlan")
    public Object getConstructionPlan(@Payload Map<String,Object> payload, Principal user) {
        String uuid = (String) payload.get("uuid");
        Integer id = (Integer) payload.get("crewId");

        if(uuid == null || id == null)
            return Map.of("isOkay",false,"message","Missing key in the request body");

        CityCrew crew = Upbeat.getCrewWith(id);

        if(crew == null)
            return Map.of("isOkay",false,"message","There is no such crewId within the game.");

        return Map.of("isOkay",true,"message",crew.getConstructionPlanStr());
    }

    @MessageMapping("setConstructionPlan")
    @SendToUser("/queue/constructionPlan")
    public Object setConstructionPlan(@Payload Map<String,Object> payload, Principal user) {
        String uuid = (String) payload.get("uuid");
        Integer id = (Integer) payload.get("crewId");
        String constructionPlan = (String) payload.get("constructionPlan");

        if(uuid == null || id == null || constructionPlan == null)
            return Map.of("isOkay",false,"message","Missing key in the request body");

        CityCrew crew = Upbeat.getCrewWith(id);

        if(crew == null)
            return Map.of("isOkay",false,"message","There is no such crewId within the game.");

        long rev_cost = Upbeat.get_rev_cost();
        if(crew.getBudget() < rev_cost)
            return Map.of("isOkay",false,"message","Not enough budget; budget required: 100","constructionPlan",crew.getConstructionPlanStr());

        try { crew.setConstructionPlan(constructionPlan, uuid); }
        catch (SyntaxError | InvalidToken e) {
            return Map.of("isOkay",false,"message",e.getMessage(),"constructionPlan",crew.getConstructionPlanStr());
        }

        crew.withdraw(rev_cost);
        getTerritoryManually(); // To update crews budget
        return Map.of("isOkay",true,"constructionPlan",crew.getConstructionPlanStr() , "message", "Construction Plan is revised successfully.");
    }

    private void getTerritoryManually() {
        template.convertAndSend("/topic/territory",Map.of("isOkay",true,"territory",Upbeat.game));
    }

    @MessageMapping("getTerritory")
    @SendTo("/topic/territory")
    public Object getTerritory() { return Map.of("isOkay",true,"territory",Upbeat.game == null ? "" : Upbeat.game); }

    @SubscribeMapping("territory")
    public Object autoGetTerritory() { return Map.of("isOkay",true,"territory",Upbeat.game == null ? "" : Upbeat.game); }

    @MessageMapping("execute")
    @SendTo("/topic/territory") // MAYBE I SHOULD CHANGE THIS
    public Object execute(Map<String,Object> payload) {
        String uuid = (String) payload.get("uuid");
        Integer id = (Integer) payload.get("crewId");

        Territory game = Upbeat.game;

        if(uuid == null || id == null) {
            String missingBody = "" + (uuid == null ? " uuid " : "") + (uuid == null ? " id " : "");
            return Map.of("isOkay", false, "message", "Missing key in the request body: " + missingBody, "territory", game);
        }

        CityCrew crew = Upbeat.getCrewWith(id);

        if(crew == null)
            return Map.of("isOkay",false,"message","There is no such crewId within the game.","territory",game);

        State state = Upbeat.currentState;
        if(state.getCrew().getId() != id)
            return Map.of("isOkay",false,"message","It is not your turn.","territory",game);

        try { state.execute(uuid); }
        catch (InvalidToken e) { return Map.of("isOkay",false,"message",e.getMessage(),"territory",game); }

        getStateManually();
        return Map.of("isOkay",true,"territory",game);
    }

    @MessageMapping("getState")
    @SendTo("/topic/state")
    public Object getState() {
        return Upbeat.currentState;
    }

    @SubscribeMapping("state")
    public Object autoGetState() {
        return Upbeat.currentState;
    }

    void getStateManually() {
        template.convertAndSend("/topic/state",Upbeat.currentState);
    }

    @MessageMapping("resign")
    @SendToUser("/queue/resignMessage")
    public Object resign(Map<String,Object> payload, Principal user) {
        String uuid = (String) payload.get("uuid");
        Integer id = (Integer) payload.get("crewId");

        if(uuid == null || id == null)
            return Map.of("isOkay",false,"message","Missing key in the request body");

        CityCrew crew = Upbeat.getCrewWith(id);

        if(crew == null)
            return Map.of("isOkay",false,"message","There is no such crewId within the game.");

        try { crew.resign(uuid); }
        catch (InvalidToken e) { return Map.of("isOkay",false,"message",e.getMessage()); }

        template.convertAndSend("/topic/joinedUsers",Upbeat.crews);
        getStateManually();
        getTerritoryManually();
        return Map.of("isOkay",true,"message","okay...");
    }
}
