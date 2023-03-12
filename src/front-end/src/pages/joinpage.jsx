import Link from "next/link";
import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";
import BackButton1 from "@/src/components/BackButton1";

let client;

export default function JoinPage() {
  useEffect(() => {
    if (!client) {
      client = new Client({
        brokerURL: "ws://localhost:8080/demo-websocket",

        onConnect: () => {
          client.subscribe("/topic/joinedUsers", (message) => {
            const body = JSON.parse(message.body);
            console.log(body);
            setCrews(body);
          });

          client.subscribe("/user/queue/token", (message) => {
            const body = JSON.parse(message.body);
            console.log(body);
            setToken(body);
          });
        },
      });

      client.activate();
    }
  }, []);

  const [hasJoined, setHasJoined] = useState(false);
  const [name, setName] = useState("");
  const [crews, setCrews] = useState([]);
  const [token, setToken] = useState({});

  const joinOption = (
    <div>
      <h1 className="joingame">JOIN GAME!</h1>
      <BackButton1 />
      <input
        className="inputname"
        type="text"
        placeholder="insert your name here..."
        onChange={(e) => setName(e.target.value)}
        value={name}
      />
      <br />
      <button
        className="btnjoin"
        onClick={() => {
          client.publish({ destination: "/app/join", body: name });
          client.publish({ destination: "/app/updateUsers" });
          setHasJoined(true);
        }}
      >
        join
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
        <button className="btnstartgame">Start game</button>
      </Link>
    </div>
  );

  return hasJoined ? preamble : joinOption;
}
