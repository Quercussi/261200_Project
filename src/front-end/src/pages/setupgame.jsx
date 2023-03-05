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
      <button className="btnnewgame">
        NEW <br />
        GAME <br />▶
      </button>
      <button className="btnfileconfig" onClick={() => SetButtonPopup(true)}>
        FILE <br />
        CONFIG <br /> ✎{" "}
      </button>
      <FileConfig trigger={buttonPopup} setTrigger={SetButtonPopup}>
        <DoneButton setTrigger={SetButtonPopup} />
        <textarea type="text" className="fileconfiginput"></textarea>
      </FileConfig>
    </div>
  );
}
