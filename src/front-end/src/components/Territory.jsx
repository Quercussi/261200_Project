import Draggable from "react-draggable";
import Tile from "./Tile";
import { useEffect, useState } from "react";

export default function Territory(props) {
  const getCrewIdAt = (row, col) => {
    for (let crew of props.territory.crews)
      if (crew.row === row && crew.col === col) return crew.id;
    return -1;
  };

  const [rows, setRows] = useState([]);
  const [colorScheme, setColorScheme] = useState({});

  const initColorScheme = () => {
    if (!props.territory || !props.territory.crews) return;
    const tempColorScheme = {};
    props.territory.crews.map((crew) => {
      tempColorScheme[crew.id] = crew.color;
    });
    setColorScheme(tempColorScheme);
  };

  const hexagonRow = <div className="hex-row" />;

  useEffect(() => initColorScheme(), [props.territory]);

  useEffect(() => {
    if (!props.territory || !props.territory.graph || !props.territory.crews)
      return;

    let crewsMap = {};
    for (let crew of props.territory.crews) crewsMap[crew.id] = crew;
    console.log(crewsMap);

    let maxDep = 1;
    for (let row of props.territory.graph)
      for (let tile of row) if (tile.deposit > maxDep) maxDep = tile.deposit;

    const tempRow = [];
    props.territory.graph.map((tileRow) => {
      tileRow.map((tile) => {
        const color = colorScheme[tile.ownerId];
        const opacityGain = Math.sqrt(tile.deposit / maxDep) * 0.5;

        const owner = !tile.ownerId
          ? { cityCenter: { row: -1, col: -1 } }
          : crewsMap[tile.ownerId];
        const cityCenter = owner.cityCenter;

        tempRow.push(
          <Tile
            key={"" + tile.row + "," + tile.col}
            tile={tile}
            coordinate={{ i: tile.row, j: tile.col }}
            color={color}
            playerMark={
              !props.territory.crews
                ? undefined
                : colorScheme[getCrewIdAt(tile.row, tile.col)]
            }
            cityCenterMark={
              !props.territory.crews
                ? false
                : tile.row === cityCenter.row && tile.col === cityCenter.col
            }
            opacityGain={opacityGain}
          />
        );
      });

      tempRow.push(hexagonRow);
    });

    setRows(tempRow);
  }, [props.territory, colorScheme]);

  const [position, setPosition] = useState({
    x: -17500,
    y: -17500,
  });

  return (
    <div>
      {/* <p style={{ color: "#424549" }}>Look on my works, ye Mighty</p> */}
      <Draggable
        style={{ border: "10px" }}
        scale={props.draggingScale}
        defaultPosition={{ x: -17500, y: -17500 }}
      >
        <div className="area">{rows}</div>
      </Draggable>
    </div>
  );
}
