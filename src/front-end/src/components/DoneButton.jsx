export default function DoneButton(props) {
  return (
    <button
      className="done-button"
      onClick={() => {
        props.postConfig();
        props.setTrigger(false);
      }}
    >
      <span className="check-icon">☑</span>Done
    </button>
  );
}
