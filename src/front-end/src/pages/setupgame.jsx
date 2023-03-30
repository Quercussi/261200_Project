import Link from "next/link";
import BackButton from "@/src/components/BackButton";
import ConfigSliderBox from "../components/ConfigSliderBox";
import DoneButton from "../components/DoneButton";
import FileConfig from "../components/FileConfig";
import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";

let client;

export default function SetupGame() {
  const [gameState, setGameState] = useState("configSetting");
  const [buttonPopup, SetButtonPopup] = useState(false);
  const [config, setConfig] = useState({
    m: 20,
    n: 15,
    init_plan_time: 300,
    init_budget: 10000,
    init_center_dep: 100,
    plan_rev_time: 1800,
    rev_cost: 100,
    max_dep: 1000000,
    interest_pct: 5,
  });

  // INITIATE CLIENT
  useEffect(() => {
    if (!client) {
      client = new Client({
        brokerURL: "ws://localhost:8080/g14-websocket",

        onConnect: async () => {
          await client.subscribe("/topic/config", (message) => {
            const body = JSON.parse(message.body);
            if (!body.isOkay) return;
            const tempConfig = body.config;
            tempConfig.init_plan_time =
              tempConfig.init_plan_min * 60 + tempConfig.init_plan_sec;
            tempConfig.plan_rev_time =
              tempConfig.plan_rev_min * 60 + tempConfig.plan_rev_sec;
            setConfig(tempConfig);

            console.log(tempConfig);
          });
          await client.subscribe("/topic/gameState", (message) => {
            const body = JSON.parse(message.body);
            console.log(body);
            setGameState(body);
          });

          // INITIALIZE ENDPOINTS
          await client.subscribe("/app/gameState", (message) => {
            const body = JSON.parse(message.body);
            console.log(body);
            setGameState(body);
          });

          await client.subscribe("/app/config", (message) => {
            const body = JSON.parse(message.body);
            if (!body.isOkay) return;
            const tempConfig = body.config;
            tempConfig.init_plan_time =
              tempConfig.init_plan_min * 60 + tempConfig.init_plan_sec;
            tempConfig.plan_rev_time =
              tempConfig.plan_rev_min * 60 + tempConfig.plan_rev_sec;
            setConfig(tempConfig);
          });

          await client.publish({ destination: "/app/getGameState" });
        },
      });

      client.activate();
    }
  }, []);

  const getConfigPublic = () => {
    if (client.connected) client.publish({ destination: "/app/getConfig" });
  };

  const postConfig = () => {
    config.plan_rev_sec = config.plan_rev_time % 60;
    config.plan_rev_min = Math.floor(config.plan_rev_time / 60);
    config.init_plan_sec = config.init_plan_time % 60;
    config.init_plan_min = Math.floor(config.init_plan_time / 60);

    if (client.connected)
      client.publish({
        destination: "/app/setConfig",
        body: JSON.stringify(config),
      });
  };

  // IN CASE THE WEBSOCKET IS INITIATED
  useEffect(() => {
    if (!client.connected) return;
    client.subscribe("/app/gameState", (message) => {
      const body = JSON.parse(message.body);
      console.log(body);
      setGameState(body);
    });

    client.subscribe("/app/config", (message) => {
      const body = JSON.parse(message.body);
      if (!body.isOkay) return;
      const tempConfig = body;
      tempConfig.init_plan_time =
        tempConfig.init_plan_min * 60 + tempConfig.init_plan_sec;
      tempConfig.plan_rev_time =
        tempConfig.plan_rev_min * 60 + tempConfig.plan_rev_sec;
      setConfig(tempConfig);
    });
  }, []);

  return (
    <div>
      <BackButton />
      <p className="circle-1"></p>
      <p className="circle-2"></p>
      <p className="circle-3"></p>
      <p className="circle-4"></p>
      <p className="circle-5"></p>
      <Link href="/joinpage">
        <button className="btnnewgame">
          {gameState === "configSetting"
            ? "NEW"
            : gameState === "joining"
            ? "JOIN"
            : "WATCH"}{" "}
          <br />
          GAME <br />▶
        </button>
      </Link>
      <button
        className="btnfileconfig"
        onClick={() => {
          getConfigPublic();
          SetButtonPopup(true);
        }}
        disabled={gameState !== "configSetting"}
      >
        FILE <br />
        CONFIG <br /> ✎
      </button>
      <FileConfig trigger={buttonPopup} setTrigger={SetButtonPopup}>
        <ConfigSliderBox setConfig={setConfig} defaultConfig={config} />
        <DoneButton setTrigger={SetButtonPopup} postConfig={postConfig} />
      </FileConfig>
    </div>
  );
}
