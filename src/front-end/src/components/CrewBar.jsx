import { useEffect } from "react";
import { useState } from "react";
import CrewStats from "./CrewStats";

export default function CrewBar(props) {
  const [crewsOrder, setCrewOrder] = useState([]);

  useEffect(() => {
    if (!props.crews) return;

    const crews = props.crews;

    const crewsCount = crews.length;
    const tempCrewsOrder = props.crews;

    for (let i = 0; i < crewsCount; i++) {
      if (tempCrewsOrder[0].id === props.crewId) break;
      tempCrewsOrder.push(tempCrewsOrder.shift());
    }

    setCrewOrder(tempCrewsOrder);
  }, [props.crews]);

  return (
    <div className="sideNav">
      {crewsOrder.length === 0
        ? ""
        : crewsOrder.map((crew) => (
            <CrewStats
              key={"" + (crew.id + 1) + props.countdown[crew.id]}
              crew={crew}
              timeLeft={props.countdown[crew.id]}
              isTurn={props.state.crew.id === crew.id}
              setPositionOnCrew={props.setPositionOnCrew}
            />
          ))}
    </div>
  );
}
