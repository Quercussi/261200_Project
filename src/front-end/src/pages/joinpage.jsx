import Link from "next/link";
import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";

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
      <input
        type="text"
        placeholder="insert your name here..."
        onChange={(e) => setName(e.target.value)}
        value={name}
      />
      <br />
      <button
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
      <h1>Waiting for other users</h1>
      {crews.map((crew, idx) => {
        return (
          <div key={idx}>
            <p>
              {idx + 1} : {crew.name}
            </p>
          </div>
        );
      })}
      <br />
      <Link href={{ pathname: "/gamepage", query: token }}>
        <button>Start game</button>
      </Link>
    </div>
  );

  return hasJoined ? preamble : joinOption;
}
