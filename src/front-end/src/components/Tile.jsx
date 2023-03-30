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

  return (
    <div className={classname}>
      <svg width={width} height={height}>
        <polygon
          className="polygon"
          points={`${midWidth1},0 ${midWidth2},0 ${width},${midHeight} ${midWidth2},${height} ${midWidth1},${height} 0,${midHeight}`}
          style={{
            fill: props.color === undefined ? freeTileColor : props.color,
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
      </svg>
    </div>
  );
}
