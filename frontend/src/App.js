import React, { useEffect, useState  } from 'react';

import { getAlive, getLog, getLogCount, sendCommand, serverOn } from './requestFunctions';
import Console from './Components/Console/Console';


function App() {

  const [log, setLog] = useState([]);
  const [logCount, setLogCount] = useState(0);
  const [alive, setAlive] = useState(false);
  const [max, setMax] = useState(20);

  // Fetch logs/alive-state every half second
  useEffect(() => {
    const interval = setInterval(() => {
      getLog(setLog, max)
      getLogCount(setLogCount)
      getAlive(setAlive)
    }, 500);
    return () => clearInterval(interval);
  }, [max]);

  return (
    <Console {... {
      alive,
      onSend: sendCommand,
      logs: log,
      logCount,
      serverOn,
      setMax
    }}/>
  );
}

export default App;
