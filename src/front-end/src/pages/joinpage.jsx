import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import BackButton1 from "@/src/components/BackButton1";

import React from "react";
import Game from "../components/GamePage";

let client;

export default function JoinPage() {
  // INITIATE CLIENT
  useEffect(() => {
    if (!client) {
      client = new Client({
        brokerURL: "ws://localhost:8080/g14-websocket",

        onConnect: () => {
          console.log("...CONNECTED...");

          client.subscribe("/user/queue/token", (message) => {
            const body = JSON.parse(message.body);
            console.log(body);
            setToken(body);
          });

          client.subscribe("/user/queue/constructionPlan", (message) => {
            const body = JSON.parse(message.body);
            console.log(body);
            if (body.isOkay || body.message === "Repeated construction plan")
              setBackConstructionPlan(body.constructionPlan);
            setCompileMessage({ isOkay: body.isOkay, message: body.message });
          });

          client.subscribe("/topic/joinedUsers", async (message) => {
            const body = await JSON.parse(message.body);
            setCrews(body);
            console.log(body);
          });

          client.subscribe("/topic/gameState", (message) => {
            const body = JSON.parse(message.body);
            console.log(body);
            setGameState(body);
          });

          client.subscribe("/topic/territory", (message) => {
            const body = JSON.parse(message.body);
            console.log(body);
            setTerritory(body.territory);

            updateCountdown(body.territory);
          });

          client.subscribe("/topic/state", async (message) => {
            const body = await JSON.parse(message.body);
            setState(body);
            console.log(body);
          });

          client.subscribe("/topic/countDown", (message) => {
            const body = JSON.parse(message.body);
            const tempCountdown = structuredClone(countdown);
            tempCountdown[body.crewId] = body.timeLeft;

            setCountDown(tempCountdown);
          });

          // INITIALIZE ENDPOINTS
          client.subscribe("/app/gameState", (message) => {
            const body = JSON.parse(message.body);
            console.log(body);
            setGameState(body);
          });

          client.subscribe("/app/territory", (message) => {
            const body = JSON.parse(message.body);
            console.log(body);
            setTerritory(body.territory);

            updateCountdown(body.territory);
          });

          client.subscribe("/app/joinedUsers", async (message) => {
            const body = await JSON.parse(message.body);
            setCrews(body);
            console.log(body);
          });

          client.subscribe("/app/state", async (message) => {
            const body = await JSON.parse(message.body);
            setState(body);
            console.log(body);
          });

          setIsConnected(true);
        },
      });

      client.activate();
    }
  }, []);

  // IN CASE THE WEBSOCKET IS INITIATED
  useEffect(() => {
    if (!client.connected) return;
    client.subscribe("/app/gameState", (message) => {
      const body = JSON.parse(message.body);
      console.log(body);
      setGameState(body);
    });

    client.subscribe("/app/territory", (message) => {
      const body = JSON.parse(message.body);
      console.log(body);
      setTerritory(body.territory);

      updateCountdown(body.territory);
    });

    client.subscribe("/app/joinedUsers", async (message) => {
      const body = await JSON.parse(message.body);
      setCrews(body);
      console.log(body);
    });

    client.subscribe("/app/state", async (message) => {
      const body = await JSON.parse(message.body);
      setState(body);
      console.log(body);
    });
  }, []);

  const [isConnected, setIsConnected] = useState(Boolean(client));
  const [hasJoined, setHasJoined] = useState(false);
  const [name, setName] = useState("");
  const [crews, setCrews] = useState([]);
  const [token, setToken] = useState({});
  const [gameState, setGameState] = useState("configSetting");
  const [state, setState] = useState({});

  const joinGameHandler = () => {
    client.publish({ destination: "/app/join", body: name });
    setHasJoined(true);
  };

  const gameStartHandler = () => {
    if (client.connected) client.publish({ destination: "/app/startGame" });
  };

  // ---- JOIN PAGE ---- //

  const joinOption = (
    <div>
      <h1 className="joingame">JOIN GAME!</h1>
      <BackButton1 />
      <input
        className="inputname"
        type="text"
        placeholder="enter your name here..."
        onChange={(e) => setName(e.target.value)}
        value={name}
      />
      <br />
      <button
        className="btnjoin"
        onClick={() => joinGameHandler()}
        disabled={!isConnected}
      >
        {isConnected ? "join" : "connecting..."}
      </button>
    </div>
  );

  // ---- PREAMBLE ---- //

  const preamble = (
    <div>
      <h1 className="waiting">Waiting for other users...</h1>
      {crews.map((crew, idx) => {
        return (
          <div key={crew.color + crew.id} className="crewname square">
            <p>
              {idx + 1} : {crew.name}
            </p>
          </div>
        );
      })}
      <br />
      <button className="btnstartgame" onClick={() => gameStartHandler()}>
        Start game
      </button>
    </div>
  );

  // ---- GAME START ---- //
  const [territory, setTerritory] = useState({ graph: [[]], crews: [] });
  const [backConstructionPlan, setBackConstructionPlan] = useState(
    "# Add construction plan here\ndone"
  );
  const [frontConstructionPlan, setFrontConstructionPlan] = useState(
    "# Add construction plan here\ndone"
  );
  const [compileMessage, setCompileMessage] = useState({
    isOkay: true,
    message: "",
  });
  const [countdown, setCountDown] = useState({});

  const postConstructionPlan = () => {
    client.publish({
      destination: "/app/setConstructionPlan",
      body: JSON.stringify({
        uuid: token.uuid,
        crewId: token.crewId,
        constructionPlan: frontConstructionPlan,
      }),
    });
  };
  const executionHandler = () =>
    client.publish({
      destination: "/app/execute",
      body: JSON.stringify({ uuid: token.uuid, crewId: token.crewId }),
    });
  const resignationHandler = () =>
    client.publish({
      destination: "/app/resign",
      body: JSON.stringify({ uuid: token.uuid, crewId: token.crewId }),
    });

  const updateCountdown = (territory) => {
    const tempCountdown = countdown;
    for (const crew of territory.crews) tempCountdown[crew.id] = crew.timeLeft;
    setCountDown(tempCountdown);
  };

  return gameState === "gameStart" ? (
    <div>
      <Game
        crewId={token.crewId}
        territory={territory}
        state={state}
        countdown={countdown}
        backConstructionPlan={backConstructionPlan}
        constructionPlan={frontConstructionPlan}
        setConstructionPlan={setFrontConstructionPlan}
        postConstructionPlan={postConstructionPlan}
        compileMessage={compileMessage}
        execute={executionHandler}
        resign={resignationHandler}
      />
    </div>
  ) : hasJoined ? (
    preamble
  ) : (
    joinOption
  );
}
