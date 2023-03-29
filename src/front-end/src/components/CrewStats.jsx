import { useEffect, useState } from "react";

export default function CrewStats(props) {
  const crew = props.crew;
  const name = crew.name;
  const col = crew.col + 1;
  const row = crew.row + 1;
  const budget = crew.budget;
  const timeLeft = props.timeLeft;

  const ownedTiles = crew.ownedTiles;
  const [totalDeposit, setTotalDeposit] = useState(0);

  const timeToString = (time) => {
    // time must be in decisecond
    const minute = Math.floor(time / 600);
    const second = (time % 600) / 10;

    const strMinute = (minute < 10 ? "0" : "") + minute;
    const strSecond =
      (second < 10 ? "0" : "") +
      (time >= 200 ? Math.floor(second) : second.toFixed(1));

    return strMinute + ":" + strSecond;
  };

  useEffect(() => {
    setTotalDeposit(
      Math.floor(ownedTiles.reduce((sum, tile) => sum + tile.deposit, 0))
    );
  }, [props.crew]);

  const onTurn = (
    <svg
      width="20px"
      height="80px"
      style={{
        position: "relative",
        top: "50%",
        transform: "translateY(-50%)",
      }}
    >
      <polygon
        className="polygon"
        points={"0,0 0,80 20,40"}
        style={{ fill: "white" }}
      />
    </svg>
  );

  return (
    <div className="navStats" onClick={() => props.setPositionOnCrew(crew.id)}>
      <div
        style={{
          width: "3vh",
          height: "100%",
          marginRight: "1vw",
          backgroundColor: props.crew.color,
        }}
      >
        {props.isTurn ? onTurn : ""}
      </div>
      <div style={{ marginBottom: "2vh" }}>
        <h1 style={{ margin: 0 }}>{name}</h1>
        <p> Coordinate: {"(" + row + "," + col + ")"}</p>
        <p> Budget: {budget}</p>
        <p> Tile Count: {ownedTiles.length}</p>
        <p> Total Deposit: {totalDeposit}</p>
        <p style={{ color: timeLeft > 200 ? "#eff0f6" : "red" }}>
          Time Left: {timeToString(timeLeft)}
        </p>
      </div>
    </div>
  );
}
