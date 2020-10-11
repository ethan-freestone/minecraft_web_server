import React, { useEffect, useState  } from 'react';

import { getAlive, getLog, sendCommand, serverOn } from './requestFunctions';
import Console from './Components/Console/Console';


function App() {

  const [log, setLog] = useState([]);
  const [alive, setAlive] = useState(false);
  const [max, setMax] = useState(20);

  // Fetch logs/alive-state every half second
  useEffect(() => {
    console.log("max: %o", max)
    const interval = setInterval(() => {
      getLog(setLog, max)
      getAlive(setAlive)
    }, 500);
    return () => clearInterval(interval);
  }, [max]);

  return (
    <Console {... {
      alive,
      onSend: sendCommand,
      logs: log,
      serverOn,
      setMax
    }}/>
  );
}

export default App;
