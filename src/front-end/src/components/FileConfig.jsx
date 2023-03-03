export default function FileConfig(props) {
  return props.trigger ? (
    <div className="fileconfig-popup">
      <div className="fileconfig-popupinner">
        <p className="crossbtn" onClick={() => props.setTrigger(false)}>
          ✖
        </p>
        {props.children}
      </div>
    </div>
  ) : (
    ""
  );
}
