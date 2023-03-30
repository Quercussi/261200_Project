import { useEffect, useState } from "react";
import ConfigSlider from "./ConfigSlider";

export default function ConfigSliderBox(props) {
  const [m, setm] = useState(props.defaultConfig.m);
  const [n, setn] = useState(props.defaultConfig.n);
  const [init_plan_time, set_init_plan_time] = useState(
    props.defaultConfig.init_plan_time
  );
  const [init_budget, set_init_budget] = useState(
    props.defaultConfig.init_budget
  );
  const [init_center_dep, set_init_center_dep] = useState(
    props.defaultConfig.init_center_dep
  );
  const [plan_rev_time, set_plan_rev_time] = useState(
    props.defaultConfig.plan_rev_time
  );
  const [rev_cost, set_rev_cost] = useState(props.defaultConfig.rev_cost);
  const [max_dep, set_max_dep] = useState(props.defaultConfig.max_dep);
  const [interest_pct, set_interest_pct] = useState(
    props.defaultConfig.interest_pct
  );

  useEffect(() => {
    props.setConfig({
      m: m,
      n: n,
      init_plan_time: init_plan_time,
      init_budget: init_budget,
      init_center_dep: init_center_dep,
      plan_rev_time: plan_rev_time,
      rev_cost: rev_cost,
      max_dep: max_dep,
      interest_pct: interest_pct,
    });
  }, [
    m,
    n,
    init_plan_time,
    init_budget,
    init_center_dep,
    plan_rev_time,
    rev_cost,
    max_dep,
    interest_pct,
  ]);

  return (
    <div className="config-slider-box">
      <ConfigSlider
        title={"Number of rows"}
        defaultValue={m}
        min={2}
        max={100}
        step={1}
        setConfig={setm}
      />
      <ConfigSlider
        title={"Number of columns"}
        defaultValue={n}
        min={2}
        max={100}
        step={1}
        setConfig={setn}
      />
      <ConfigSlider
        title={"Initial planning specification time"}
        defaultValue={init_plan_time}
        min={60}
        max={900}
        step={10}
        setConfig={set_init_plan_time}
      />
      <ConfigSlider
        title={"Initial budget"}
        defaultValue={init_budget}
        min={0}
        max={1e6}
        step={100}
        setConfig={set_init_budget}
      />
      <ConfigSlider
        title={"Inital city center deposit"}
        defaultValue={init_center_dep}
        min={50}
        max={1e4}
        step={10}
        setConfig={set_init_center_dep}
      />
      <ConfigSlider
        title={"Planning revision time"}
        defaultValue={plan_rev_time}
        min={300}
        max={3600}
        step={10}
        setConfig={set_plan_rev_time}
      />
      <ConfigSlider
        title={"Plan revision cost"}
        defaultValue={rev_cost}
        min={0}
        max={1e4}
        step={100}
        setConfig={set_rev_cost}
      />
      <ConfigSlider
        title={"Max deposit"}
        defaultValue={max_dep}
        min={1e3}
        max={1e7}
        step={1000}
        setConfig={set_max_dep}
      />
      <ConfigSlider
        title={"Interest rate percentage"}
        defaultValue={interest_pct}
        min={1}
        max={100}
        step={1}
        setConfig={set_interest_pct}
      />
    </div>
  );
}
