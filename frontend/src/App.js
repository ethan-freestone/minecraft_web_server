import React, { useEffect, useState  } from 'react';
import Switch from "react-switch";
import { Col, Row } from 'react-flexbox-grid';

import { getAlive, getLog, sendCommand } from './requestFunctions';
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
    <Row>
      <Col xs={6}>
        <Console onSend={sendCommand} logs={log}/>
      </Col>
      <Col>
        <Switch checked={alive}/>
      </Col>
    </Row>
    
  );
}

export default App;
