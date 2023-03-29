export default function ResignButton(props) {
  return (
    <div
      className={
        props.show ? "resignButtonContainer" : "hidingResignButtonContainer"
      }
    >
      <button
        className={props.show ? "resignButton" : "hidingResignButton"}
        onClick={() => {
          props.resign();
        }}
      >
        RESIGN
      </button>
    </div>
  );
}
