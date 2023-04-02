import "@/src/styles/globals.css";

import "@/src/components/startgame.css";

// Set-up Page
import "@/src/components/setupgame.css";
import "@/src/components/ConfigSlider.css";
import "@/src/components/DoneButton.css";
import "@/src/components/FileConfig.css";

// Join Page
import "@/src/components/joinpage.css";

// Game Page
import "@/src/components/ConstructionPlanBar.css";
import "@/src/components/game.css";
import "@/src/components/timer.css";
import "@/src/components/Tile.css";
import "@/src/components/CrewBar.css";
import "@/src/components/ResignBar.css";
import "@/src/components/ExecuteButton.css";

export default function App({ Component, pageProps }) {
  return <Component {...pageProps} />;
}
