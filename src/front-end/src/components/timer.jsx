import { useEffect, useState } from "react";

export default function Timer({ time }) {
  const [second, setSecond] = useState(time);
  const [minute, setMinute] = useState(0);

  useEffect(() => {
    if (second >= 0) {
      setTimeout(() => setSecond(second + 1), 1000);
      if (second % 60 === 0 && second !== 0) {
        setMinute(minute + 1);
        setSecond(0);
      }
    }
  }, [second]);

  return (
    <span className="count-timer">
      {minute}:{second}
    </span>
  );
}
