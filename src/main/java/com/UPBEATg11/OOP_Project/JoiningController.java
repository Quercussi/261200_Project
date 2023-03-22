package com.UPBEATg11.OOP_Project;

import entities.CityCrew;
import entities.Tile;
import orchestrator.Upbeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import parsers.SyntaxError;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class JoiningController {
    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/updateUsers")
    @SendTo("/topic/joinedUsers")
    public Object getUsers() { return Upbeat.crews; }

    @MessageMapping("/join")
    @SendToUser("/queue/token")
    public Object pushUser(@Header("simpSessionId") String sessionId, @Payload String username, Principal user){
        System.out.println(sessionId);

        if(Upbeat.getGameState() == Upbeat.GameState.configSetting)
            Upbeat.gameState = Upbeat.GameState.joining;

        List<CityCrew> crews = Upbeat.crews;
        String uuid = UUID.randomUUID().toString();
        int nextCrewId = crews.isEmpty() ? 1 : crews.get(crews.size() - 1).getId() + 1; // Trust me, this works. I've proved it.
        try {
            if(Upbeat.game == null)
                Upbeat.constructTerritory();
            CityCrew newCrew = Upbeat.randomizedInitCrew(username, nextCrewId, uuid, sessionId);
            if(newCrew == null)
                return Map.of("isOkay",false,"message","No more players can join.");

            crews.add(newCrew);
            template.convertAndSend("/topic/joinedUsers",crews);
            template.convertAndSend("/topic/gameState",Upbeat.getGameState());
        } catch (SyntaxError | RuntimeException e)
        {/*It just cannot create an error*/ System.out.println(e.getMessage());}

        return Map.of("isOkay",true,"token",uuid,"crewId",nextCrewId);
    }

    @EventListener
    public void handleSessionDisconnect(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        CityCrew disconnectedCrew = Upbeat.getCrewWithSession(sessionId);
        if(disconnectedCrew == null)
            return;

        // Lazy Resignation
        Upbeat.vacantTile.remove(disconnectedCrew.getCityCenter());
        Upbeat.crews.remove(disconnectedCrew);
        if(Upbeat.getGameState() == Upbeat.GameState.gameStart)
            Upbeat.losers.add(disconnectedCrew);

        disconnectedCrew.setCityCenter(null);
        for(Tile tile : disconnectedCrew.getOwnedTiles())
            tile.setOwner(null);

        template.convertAndSend("/topic/joinedUsers",Upbeat.crews);
    }
}
