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
      <div>
        <div className="compile-message">
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
