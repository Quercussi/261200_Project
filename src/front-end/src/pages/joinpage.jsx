import Link from "next/link";
import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import BackButton1 from "@/src/components/BackButton1";

let client;

export default function JoinPage() {
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
            // client.publish({ destination: "/app/updateUsers" });
            // client.publish({ destination: "/app/getGameState" });
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

          setIsConnected(true);
        },
      });

      client.activate();
    }
  }, []);

  const [isConnected, setIsConnected] = useState(false);
  const [hasJoined, setHasJoined] = useState(false);
  const [name, setName] = useState("");
  const [crews, setCrews] = useState([]);
  const [token, setToken] = useState({});
  const [gameState, setGameState] = useState("configSetting");

  const joinGameHandler = () => {
    client.publish({ destination: "/app/join", body: name });
    setHasJoined(true);
  };

  const gameStartHandler = () => {
    if (client.connected) client.publish({ destination: "/app/startGame" });
  };

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

  const preamble = (
    <div>
      <h1 className="waiting">Waiting for other users...</h1>
      {crews.map((crew, idx) => {
        return (
          <div key={idx} className="crewname square">
            <p>
              {idx + 1} : {crew.name}
            </p>
          </div>
        );
      })}
      <br />
      <Link href={{ pathname: "/gamepage", query: token }}>
        <button className="btnstartgame" onClick={() => gameStartHandler()}>
          Start game
        </button>
      </Link>
    </div>
  );

  return hasJoined ? preamble : joinOption;
}
