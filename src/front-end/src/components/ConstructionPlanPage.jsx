export default function ConstructionPlanPage(props) {
  return props.trigger ? (
    <div className="constructionplan-popup">
      <div className="constructionplan-popupinner">
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
