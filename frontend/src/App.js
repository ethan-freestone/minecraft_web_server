import React, { useEffect, useState  } from 'react';
import { getLog } from './requestFunctions'
function App() {

  const [log, setLog] = useState([]);
  // Fetch logs every half second
  useEffect(() => {
    const interval = setInterval(() => {
      getLog(setLog)
    }, 500);
    return () => clearInterval(interval);
  }, []);

  console.log("LOGS:%o", log)

  return (
    <div>
      {log.map(line =>
          <p> {line} </p>
      )}
    </div>
  );
}

export default App;
