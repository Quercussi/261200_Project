import { useState } from "react";

export default function ConfigSlider(props) {
  const [defaultValue, setValue] = useState(props.defaultValue);

  const getValue = () => {
    var minute = Math.floor(defaultValue / 60);
    var second = defaultValue % 60;
    if (second < 10) second = "0" + second;
    return props.title.includes("time") ? `${minute}\:${second}` : defaultValue;
  };

  return (
    <div className="config-setting-module">
      <p className="config-title">{props.title}</p>
      <div className="config-box">
        <input
          type="range"
          className="config-slider"
          min={props.min}
          max={props.max}
          step={props.step}
          value={defaultValue}
          onChange={(e) => {
            setValue(e.target.value);
            props.setConfig(e.target.value);
          }}
        />
        <label className="config-value">{getValue()}</label>
      </div>
    </div>
  );
}
