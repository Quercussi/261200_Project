export default function TileStats(props) {
  const tile = props.tile;

  const row = tile.row + 1;
  const col = tile.col + 1;
  const ownerName = tile.ownerName;
  const deposit = tile.deposit;

  return (
    <div className="tile-stats">
      <p>Coordinate: {"(" + row + "," + col + ")"}</p>
      <p>Owner: {ownerName}</p>
      <p>Deposit: {deposit.toFixed(3)}</p>
    </div>
  );
}
