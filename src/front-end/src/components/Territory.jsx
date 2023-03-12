import React from "react";
import HexagonGrid from "./hexagongrid";
import times from "lodash.times";

const Territory = () => {
  const getHexProps = (hexagon) => {
    return {
      style: {
        stroke: "black",
      },
    };
  };

  let rows = times(100, (id) => id);

  return (
    <HexagonGrid
      gridWidth={450}
      gridHeight={450}
      hexagons={rows}
      hexProps={getHexProps}
    />
  );
};

export default Territory;
