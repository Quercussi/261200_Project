import HexagonRow from "./hexrow";
import Draggable from "react-draggable";
import Tile from "./Tile";
import { useEffect, useState } from "react";

export default function Territory(props) {
  const getCrewWithId = (id) => {
    for (let crew of props.territory.crews) if (crew.id === id) return crew;
    return -1;
  };

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

  useEffect(() => initColorScheme(), [props.territory]);

  useEffect(() => {
    if (!props.territory || !props.territory.graph || !props.territory.crews)
      return;

    const tempRow = [];
    props.territory.graph.map((tileRow, idx) => {
      tileRow.map((tile) => {
        const color = colorScheme[tile.ownerId];

        tempRow.push(
          <Tile
            key={"" + tile.row + "," + tile.col}
            coordinate={{ i: tile.row, j: tile.col }}
            color={color}
            playerMark={
              !props.territory.crews
                ? undefined
                : colorScheme[getCrewIdAt(tile.row, tile.col)]
            }
          />
        );
      });

      tempRow.push(<HexagonRow />);
    });

    setRows(tempRow);
  }, [props.territory, colorScheme]);

  const [position, setPosition] = useState({
    x: -17500,
    y: -17500,
  });

  // const handleStop = (event, dragElement) =>
  //   setPosition({ x: dragElement.x, y: dragElement.y });
  // // credit to Shyam on stackoverflow
  // // Link: https://stackoverflow.com/a/67893683

  // useEffect(() => {
  //   if (!props.positionOnCrew) return;
  //   const crew = getCrewWithId(props.positionOnCrew);

  //   const begin_xPos = -17500;
  //   const begin_yPos = -17500;

  //   const tileWidth = 200;
  //   const tileHeight = tileWidth * Math.sin(Math.PI / 3);

  //   const xPos = (crew.col + 0.5) * tileWidth;
  //   const yPos = (crew.row + 0.5) * tileHeight;
  //   setPosition({ x: xPos, y: yPos });
  //   setPosition({ x: 0, y: 0 });
  // }, [props.positionOnCrew]);

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
