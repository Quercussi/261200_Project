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
import org.springframework.messaging.simp.annotation.SubscribeMapping;
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

    @SubscribeMapping("joinedUsers")
    public Object autoGetUsers() { return Upbeat.crews; }

    @MessageMapping("/join")
    @SendToUser("/queue/token")
    public Object pushUser(@Header("simpSessionId") String sessionId, @Payload String username, Principal user){
        System.out.println("A new session connected: " + sessionId);

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

            //Set color for crews
            int colorCount = CityCrew.defaultColorScheme.length;
            int crewCount = crews.size();
            for(int i = 0; i < colorCount && i < crewCount; i++)
                crews.get(i).setColor(CityCrew.defaultColorScheme[i]);

            for(int i = colorCount; i < crewCount; i++) {
                StringBuilder colorSB = new StringBuilder("#");
                int colorInt = Upbeat.rand.nextInt(0xFFFFFF+1);
                colorSB.append(Integer.toHexString(colorInt));
                crews.get(i).setColor(colorSB.toString());
            }

            template.convertAndSend("/topic/joinedUsers",crews);
            template.convertAndSend("/topic/gameState",Upbeat.getGameState());
        } catch (SyntaxError | RuntimeException e)
        {/*It just cannot create an error*/ System.out.println("Error from convertAndSend: " + e.getMessage());}

        return Map.of("isOkay",true,"uuid",uuid,"crewId",nextCrewId);
    }

    @MessageMapping("/echoBack")
    @SendToUser("/queue/echoAck")
    public Object pushUser(@Payload Map<String,Object> payload, Principal user){
        String uuid = (String) payload.get("uuid");
        Integer id = (Integer) payload.get("crewId");

        if(uuid == null || id == null)
            return Map.of("isOkay",false,"message","Missing key in the request body");

        CityCrew crew = Upbeat.getCrewWith(id);

        if(crew == null)
            return Map.of("isOkay",false,"message","There is no such crewId within the game.");

        if(!crew.correctUUID(uuid))
            return Map.of("isOkay",false,"message","Incorrect UUID");

        Upbeat.echoCheck.put(id,true);

        return Map.of("isOkay",true);
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
        disconnectedCrew.getOwnedTiles().clear();

        if(Upbeat.currentState.getCrew() == disconnectedCrew)
            Upbeat.currentState.incrementState();

        template.convertAndSend("/topic/joinedUsers",Upbeat.crews);
        template.convertAndSend("/topic/territory",Map.of("isOkay",true,"territory",Upbeat.game));
        template.convertAndSend("/topic/state",Upbeat.currentState);
    }
}
