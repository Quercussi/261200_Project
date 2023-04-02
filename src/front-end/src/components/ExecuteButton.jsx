export default function ExecuteButton(props) {
  return (
    <div>
      {props.trigger ? (
        <button className="execute-button" onClick={() => props.execute()}>
          EXECUTE
        </button>
      ) : (
        ""
      )}
    </div>
  );
}
