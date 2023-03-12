import ConstructionPlanPage from "../components/ConstructionPlanPage";
import { useState } from "react";
import SaveButton from "../components/SaveButton";
import Timer from "../components/timer";
import { useRouter } from "next/router";

export default function Game() {
  const router = useRouter();
  const data = router.query;
  const uuid = data.uuid;
  const myId = data.crewId;

  const [buttonPopup, SetButtonPopup] = useState(false);
  const [inputValue, setInputValue] = useState("");

  const handleUserInput = (e) => {
    setInputValue(e.target.value);
  };

  const resetInput = () => {
    setInputValue("");
  };

  return (
    <div>
      <Timer time={0} />
      <button className="btnconplan" onClick={() => SetButtonPopup(true)}>
        Construction
        <br />
        Plan
      </button>
      <ConstructionPlanPage trigger={buttonPopup} setTrigger={SetButtonPopup}>
        <SaveButton setTrigger={SetButtonPopup} />
        <button className="reset-button" onClick={resetInput}>
          Reset
        </button>
        <textarea
          type="text"
          className="constructionplaninput"
          value={inputValue}
          onChange={handleUserInput}
        ></textarea>
      </ConstructionPlanPage>
    </div>
  );
}
