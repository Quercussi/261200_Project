import TileStats from "./TileStats";

const freeTileColor = "#99aab5";

export default function Tile(props) {
  const scale = 200;
  const width = scale;
  const height = width * Math.sin(Math.PI / 3);

  const midWidth1 = width / 4;
  const midWidth2 = (3 * width) / 4;
  const midHeight = height / 2;

  const coordinate = props.coordinate;
  const classname = coordinate.j % 2 === 0 ? "hex" : "hex even";

  const getColorCode = (color) => {
    return color === undefined ? freeTileColor : color;
  };

  const cityCenterStar = (x, y) => {
    return (
      <svg
        x={x - 52}
        y={y - 52}
        width="104px"
        height="104px"
        viewBox="0 0 24.00 24.00"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
        transform="rotate(45)"
      >
        <g id="SVGRepo_bgCarrier" stroke-width="0"></g>
        <g
          id="SVGRepo_tracerCarrier"
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke="#CCCCCC"
          stroke-width="0.048"
        >
          {" "}
          <path
            d="M12 3C12 7.97056 16.0294 12 21 12C16.0294 12 12 16.0294 12 21C12 16.0294 7.97056 12 3 12C7.97056 12 12 7.97056 12 3Z"
            stroke={freeTileColor}
            stroke-width="2.4"
            stroke-linecap="round"
            stroke-linejoin="round"
          ></path>{" "}
        </g>
        <g id="SVGRepo_iconCarrier">
          {" "}
          <path
            d="M12 3C12 7.97056 16.0294 12 21 12C16.0294 12 12 16.0294 12 21C12 16.0294 7.97056 12 3 12C7.97056 12 12 7.97056 12 3Z"
            stroke={freeTileColor}
            stroke-width="2.4"
            stroke-linecap="round"
            stroke-linejoin="round"
          ></path>{" "}
        </g>
      </svg>
    );
  };

  return (
    <div className={classname}>
      <svg width={width} height={height}>
        <polygon
          className="polygon"
          points={`${midWidth1},0 ${midWidth2},0 ${width},${midHeight} ${midWidth2},${height} ${midWidth1},${height} 0,${midHeight}`}
          style={{
            fill: getColorCode(props.color),
            opacity: 0.5 + props.opacityGain,
          }}
        />

        {props.playerMark ? (
          <circle
            cx={width / 2}
            cy={midHeight}
            r="40px"
            stroke={freeTileColor}
            strokeWidth="17px"
            fill={props.playerMark}
          />
        ) : (
          ""
        )}

        {props.cityCenterMark ? cityCenterStar(width / 2, midHeight) : ""}
      </svg>

      <TileStats tile={props.tile} />
    </div>
  );
}
