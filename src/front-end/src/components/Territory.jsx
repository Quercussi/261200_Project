import React from "react";
import HexagonGrid from "./hexagongrid";
import times from "lodash/times";

const Territory = () => {
  const getHexProps = (hexagon) => {
    return {
      style: {
        backgroundImage: "/image/region.png",
        stroke: "black",
      },
      onClick: () => alert(`Hexagon n.${hexagon} has been clicked`),
    };
  };

  const renderHexagonContent = (hexagon) => {
    return (
      <text
        x="50%"
        y="50%"
        fontSize={100}
        fontWeight="lighter"
        style={{ fill: "black" }}
        textAnchor="middle"
      >
        {hexagon}
      </text>
    );
  };

  let rows = times(100, (id) => id);

  return (
    <HexagonGrid
      gridWidth={450}
      gridHeight={450}
      hexagons={rows}
      hexProps={getHexProps}
      renderHexagonContent={renderHexagonContent}
    />
  );
};

export default Territory;
