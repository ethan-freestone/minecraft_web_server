import React, { useEffect, useState  } from 'react';

import { getAlive, getLog, sendCommand, serverOn } from './requestFunctions';
import Console from './Components/Console/Console';


function App() {

  const [log, setLog] = useState([]);
  const [alive, setAlive] = useState(false);

  // Fetch logs/alive-state every half second
  useEffect(() => {
    const interval = setInterval(() => {
      getLog(setLog)
      getAlive(setAlive)
    }, 500);
    return () => clearInterval(interval);
  }, []);

  return (
    <Console {... {alive, onSend: sendCommand, logs: log, serverOn }}/>
  );
}

export default App;
