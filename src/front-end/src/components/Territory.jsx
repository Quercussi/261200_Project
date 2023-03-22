import HexagonOdd from "./hexagonodd";
import HexagonEven from "./hexagoneven";
import HexagonRow from "./hexrow";
import Draggable from "react-draggable";

export default function Territory() {
  const rows = [];

  for (let i = 1; i <= 20; i++) {
    for (let j = 1; j <= 20; j++) {
      if (j % 2 === 0) {
        rows.push(<HexagonEven />);
      } else {
        rows.push(<HexagonOdd />);
      }
    }
    rows.push(<HexagonRow />);
  }

  return (
    <div>
      <Draggable>
        <div className="area">{rows}</div>
      </Draggable>
    </div>
  );
}
