import { useState } from "react";
import ResignButton from "./ResignButton";

export default function ResignBar(props) {
  const [showResignButton, setShowResignButton] = useState(false);

  const arrowUp = (
    <path d="M3 19h18a1.002 1.002 0 00.823-1.569l-9-13c-.373-.539-1.271-.539-1.645 0l-9 13A.999.999 0 003 19z" />
  );
  const arrowDown = (
    <path d="M11.178 19.569a.998.998 0 001.644 0l9-13A.999.999 0 0021 5H3a1.002 1.002 0 00-.822 1.569l9 13z" />
  );

  return props.resignBarTrigger ? (
    <div className="resignBar">
      <ResignButton show={showResignButton} resign={props.resign} />
      <div
        className="resignTab"
        onClick={() => setShowResignButton(!showResignButton)}
      >
        {/* credits to reactsvgicons.com */}
        <svg
          className="resignTabIcon"
          viewBox="0 0 24 24"
          fill="white"
          height="1em"
          width="1em"
        >
          {showResignButton ? arrowUp : arrowDown}
        </svg>
      </div>
    </div>
  ) : (
    ""
  );
}
