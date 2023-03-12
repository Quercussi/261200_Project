import Link from "next/link";
import BackButton from "@/src/components/BackButton";
import ConfigSliderBox from "../components/ConfigSliderBox";
import DoneButton from "../components/DoneButton";
import FileConfig from "../components/FileConfig";
import { useEffect, useState } from "react";
import { Client } from "@stomp/stompjs";

let client;

export default function SetupGame() {
  useEffect(() => {
    if (!client) {
      client = new Client({
        brokerURL: "ws://localhost:8080/demo-websocket",

        onConnect: () => {
          client.subscribe("/topic/config", (message) => {
            const body = JSON.parse(message.body);
            setConfig(body);
            console.log(body);
          });
        },
      });

      client.activate();
    }
  }, []);

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

  const getConfigPublic = () => {
    client.publish({ destination: "/app/getConfig" });
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
          NEW <br />
          GAME <br />▶
        </button>
      </Link>
      <button
        className="btnfileconfig"
        onClick={() => {
          getConfigPublic();
          SetButtonPopup(true);
        }}
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
