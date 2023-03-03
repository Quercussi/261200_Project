export default function DoneButton(props) {
  return (
    <button className="done-button" onClick={() => props.setTrigger(false)}>
      <span className="check-icon">☑</span>Done
    </button>
  );
}
