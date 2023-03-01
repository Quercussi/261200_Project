import { Inter } from "@next/font/google";
import Start from "./startgame";

const inter = Inter({ subsets: ["latin"] });

export default function Home() {
  return (
    <div>
      <Start />
    </div>
  );
}
