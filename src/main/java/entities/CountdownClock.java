package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import orchestrator.InvalidToken;
import orchestrator.Upbeat;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Map;

public class CountdownClock {
    private final CityCrew crew;
    private int timeLeft; // in decisecond
    private boolean counting;
    private Thread counterThread;
    private static SimpMessagingTemplate template;
    private String uuid;

    private static int timeRunningOut = 200;

    public CountdownClock(CityCrew crew, int timeLeft, String uuid) {
        this.crew = crew;
        this.timeLeft = timeLeft;
        this.counting = false;
        this.uuid = uuid;

        counterThread = new Thread(new Counter());
    }

    public void startCountdown() {
        if(counting) return;

        counting = true;
        counterThread = new Thread(new Counter());
        counterThread.start();
    }

    public void stopCountdown() { counting = false; }

    public static void setSimpMessagingTemplate(SimpMessagingTemplate template) {
        CountdownClock.template = template;
    }

    public double getTimeLeft() { return timeLeft; }

    @JsonIgnore
    public CityCrew getCrew() { return crew; }

    private class Counter implements Runnable {
        public void run() {
            if(Upbeat.crews.size() <= 1)
                counting = false;

            while(counting) {
                try { Thread.sleep(100); }
                catch (InterruptedException e) { System.out.println(e.getMessage()); }
                timeLeft -= 1;

                if(timeLeft <= timeRunningOut || timeLeft % 10 == 0)
                    template.convertAndSend("/topic/countDown" , Map.of("crewId",crew.getId(),"timeLeft",timeLeft));

                if(timeLeft <= 0) { // Player loses by timeout
                    try { crew.resign(uuid); }
                    catch (InvalidToken e) { System.out.println("In Timeout: " + e.getMessage()); }

                    template.convertAndSend("/topic/joinedUsers",Upbeat.crews);
                    template.convertAndSend("/topic/territory",Map.of("isOkay",true,"territory",Upbeat.game));
                    template.convertAndSend("/topic/state",Upbeat.currentState);
                }
            }
        }
    }
}
