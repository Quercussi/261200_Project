import Editor from "@monaco-editor/react";
import { useEffect, useRef } from "react";

export default function ConstructionPlanBar(props) {
  const editorRef = useRef(null);

  function handleEditorDidMount(editor, monaco) {
    editorRef.current = editor;
  }

  useEffect(() => {
    if (editorRef.current == null) return;
    console.log(editorRef.current.getValue());
  }, [editorRef]);

  const compileMessageColor = (compileMsg) => {
    if (compileMsg.isOkay) return "#00FF40";
    if (compileMsg.message === "Repeated construction plan") return "yellow";
    else return "red";
  };

  return (
    <div className="constructionplan-container">
      <Editor
        className="constructionplaninput"
        theme="vs-dark"
        defaultLanguage="markdown"
        defaultValue={props.constructionPlan}
        padding={10}
        onMount={handleEditorDidMount}
        onChange={(val, e) => props.setConstructionPlan(val)}
      />
      <div className="constructionplanEvents-container">
        <div
          className="compile-message"
          style={{ backgroundColor: compileMessageColor(props.compileMessage) }}
        >
          <p>{props.compileMessage.message}</p>
        </div>
        <button
          className="save-button"
          onClick={() => props.postConstructionPlan()}
        >
          REVISE
        </button>
        <button
          className="reset-button"
          onClick={() => editorRef.current.setValue(props.backConstructionPlan)}
        >
          RESET
        </button>
      </div>
    </div>
  );
}
