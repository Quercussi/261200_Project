import Link from "next/link";
import BackButton from "@/src/components/BackButton";

export default function SetupGame() {
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
      <button className="btnfileconfig">
        FILE <br />
        CONFIG <br /> ✎
      </button>
    </div>
  );
}
