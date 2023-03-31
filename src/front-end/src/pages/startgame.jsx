import Link from "next/link";
export default function Start() {
  return (
    <div className="bg">
      <div className="text">
        <h1>UPBEAT</h1>
        <Link href="/setupgame">
          <button className="btnstart">Start</button>
        </Link>
      </div>
    </div>
  );
}
