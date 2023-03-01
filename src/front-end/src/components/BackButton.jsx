import Link from "next/link";

export default function BackButton() {
  return (
    <Link href="/">
      <img
        src="image/back.png"
        className="backbtn"
        width="100"
        height="100"
      ></img>
    </Link>
  );
}
