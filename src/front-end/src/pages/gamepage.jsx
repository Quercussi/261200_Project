import ConstructionPlanPage from "../components/ConstructionPlanPage";
import { useState } from "react";
import SaveButton from "../components/SaveButton";
import Timer from "../components/timer";
import { useRouter } from "next/router";
import Territory from "../components/Territory";
import Editor from "@monaco-editor/react";
import React, { useRef } from "react";

export default function Game() {
  const router = useRouter();
  const data = router.query;
  const uuid = data.uuid;
  const myId = data.crewId;

  const [buttonPopup, SetButtonPopup] = useState(false);
  const [inputValue, setInputValue] = useState("");
  const [scale, setScale] = useState(1.2);

  const editorRef = useRef(null);

  function handleEditorDidMount(editor, monaco) {
    editorRef.current = editor;
  }

  const handleUserInput = (e) => {
    setInputValue(e.target.value);
  };

  const resetInput = () => {
    setInputValue("");
  };

  const zoomIn = () => {
    if (scale < 1.5) {
      setScale(scale + 0.2);
    }
  };
  const zoomOut = () => {
    if (scale > 0.3) {
      setScale(scale - 0.2);
    }
  };

  return (
    <div>
      <button className="zoomin" onClick={zoomIn}>
        +
      </button>
      <button className="zoomout" onClick={zoomOut}>
        -
      </button>
      <Timer time={0} />
      <div className="territory">
        <div style={{ transform: `scale(${scale})` }}>
          <Territory />
        </div>
      </div>
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
        <Editor
          className="constructionplaninput"
          width="500px"
          height="350px"
          theme="vs-dark"
          defaultLanguage="java"
          defaultValue="// add some construction plan"
          padding={10}
          onMount={handleEditorDidMount}
        ></Editor>
      </ConstructionPlanPage>
    </div>
  );
}
