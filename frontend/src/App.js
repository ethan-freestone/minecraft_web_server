import React, { useEffect, useState  } from 'react';

import { getLog, sendCommand } from './requestFunctions'
import Console from './Components/Console/Console' 

function App() {

  const [log, setLog] = useState([]);
  // Fetch logs every half second
  useEffect(() => {
    const interval = setInterval(() => {
      getLog(setLog)
    }, 500);
    return () => clearInterval(interval);
  }, []);

  return (
    <Console onSend={sendCommand} logs={log}/>
  );
}

export default App;
