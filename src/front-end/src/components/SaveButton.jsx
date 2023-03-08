export default function SaveButton(props) {
  return (
    <button className="save-button" onClick={() => props.setTrigger(false)}>
      Save
    </button>
  );
}
