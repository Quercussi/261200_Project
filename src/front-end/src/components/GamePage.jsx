import { useEffect, useState } from "react";
import Territory from "../components/Territory";
import React, { useRef } from "react";
import CrewBar from "./CrewBar";
import ConstructionPlanBar from "./ConstructionPlanBar";
import { DragSizing } from "react-drag-sizing";
import ResignBar from "./ResignBar";

export default function Game(props) {
  const [scale, setScale] = useState(0.3);
  const [executeButtonTrigger, setExecuteButtonTrigger] = useState(false);

  useEffect(() => {
    const state = props.state;
    if (!state.crew) setExecuteButtonTrigger(false);
    else setExecuteButtonTrigger(props.state.crew.id === props.crewId);
  }, [props.state]);

  const zoomIn = () => {
    if (scale < 1.5) setScale(scale * 1.2);
  };
  const zoomOut = () => {
    setScale(scale * 0.8);
    if (scale < 0) setScale(0.0005);
  };

  const [positionOnCrew, setPositionOnCrew] = useState(undefined);

  return (
    <div>
      <div className="flex-container">
        {!props.crewId ? (
          ""
        ) : (
          <DragSizing
            border="right"
            style={{ minWidth: "10vw", maxWidth: "60vw", width: "23vw" }}
          >
            <ConstructionPlanBar
              backConstructionPlan={props.backConstructionPlan}
              constructionPlan={props.constructionPlan}
              setConstructionPlan={props.setConstructionPlan}
              postConstructionPlan={props.postConstructionPlan}
              compileMessage={props.compileMessage}
            />
          </DragSizing>
        )}

        <div
          className="territory"
          onWheel={(event) => {
            if (event.deltaY > 0) zoomOut();
            else zoomIn();
          }}
        >
          <div style={{ transform: `scale(${scale})` }}>
            <Territory
              territory={props.territory}
              draggingScale={scale}
              positionOnCrew={positionOnCrew}
            />
          </div>
          <ResignBar resign={props.resign} />
          {executeButtonTrigger ? (
            <button className="execute-button" onClick={() => props.execute()}>
              EXECUTE
            </button>
          ) : (
            ""
          )}
        </div>

        <DragSizing
          border="left"
          style={{ minWidth: "10vw", maxWidth: "30vw", width: "23vw" }}
        >
          <CrewBar
            crews={props.territory.crews}
            state={props.state}
            countdown={props.countdown}
            crewId={props.crewId}
            setPositionOnCrew={setPositionOnCrew}
          />
        </DragSizing>
      </div>
    </div>
  );
}
