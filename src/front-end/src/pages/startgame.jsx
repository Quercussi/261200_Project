import { useState } from "react";
import Link from "next/link";

export default function Start() {
  const [isMousedOver, setMouseOver] = useState(false);

  function handleMouseOver() {
    setMouseOver(true);
  }

  function handleMouseOut() {
    setMouseOver(false);
  }

  return (
    <div className="text">
      <h1>UPBEAT</h1>
      <Link href="/setupgame">
        <button
          className="btnstart"
          style={{
            backgroundColor: isMousedOver ? "#FFB84C" : "#FFAACF",
            border: isMousedOver ? "solid" : "none",
          }}
          onMouseOver={handleMouseOver}
          onMouseOut={handleMouseOut}
        >
          Start
        </button>
      </Link>
    </div>
  );
}
