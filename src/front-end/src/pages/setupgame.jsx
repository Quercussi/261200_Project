import Link from "next/link";
import BackButton from "@/src/components/BackButton";
import FileConfig from "../components/FileConfig";
import DoneButton from "../components/DoneButton";
import { useState } from "react";

export default function SetupGame() {
  const [buttonPopup, SetButtonPopup] = useState(false);

  return (
    <div>
      <BackButton />
      <p className="circle-1"></p>
      <p className="circle-2"></p>
      <p className="circle-3"></p>
      <p className="circle-4"></p>
      <p className="circle-5"></p>
      <Link href="/gamepage">
        <button className="btnnewgame">
          NEW <br />
          GAME <br />▶
        </button>
      </Link>
      <button className="btnfileconfig" onClick={() => SetButtonPopup(true)}>
        FILE <br />
        CONFIG <br /> ✎
      </button>
      <FileConfig trigger={buttonPopup} setTrigger={SetButtonPopup}>
        <DoneButton setTrigger={SetButtonPopup} />
        <textarea type="text" className="fileconfiginput">
          m=20&#13;n=15&#13;init_plan_min=5&#13;init_plan_sec=0&#13;init_budget=10000
          &#13;init_center_dep=100&#13;plan_rev_min=30&#13;testplan_rev_sec=0&#13;rev_cost=100
          &#13;max_dep=1000000&#13;interest_pct=5
        </textarea>
      </FileConfig>
    </div>
  );
}
