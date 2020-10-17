import React, { useEffect, useState  } from 'react';
import { Col, Row } from 'react-flexbox-grid';

import { getAlive, getItems, getLog, getLogCount, sendCommand, serverOn } from './requestFunctions';
import Console from './Components/Console/Console';


function App() {

  const [log, setLog] = useState([]);
  const [logCount, setLogCount] = useState(0);
  const [alive, setAlive] = useState(false);
  const [max, setMax] = useState(20);
  const [items, setItems] = useState([]);

  // Fetch logs/alive-state every half second
  useEffect(() => {
    const interval = setInterval(() => {
      getLog(setLog, max)
      getLogCount(setLogCount)
      getItems(setItems)
      getAlive(setAlive)
    }, 500);
    return () => clearInterval(interval);
  }, [max]);

  const handleChange = (event) => {
      console.log(event.target.value)
  };

  return (
    <Row>
      <Col xs={6}>
        <Console {... {
          alive,
          onSend: sendCommand,
          logs: log,
          logCount,
          serverOn,
          setMax
        }}/>
      </Col>
      <Col xs={6}>
        <p> Hi there </p>
        <form onSubmit={(e) => {
                e.preventDefault()
            }}
                autoComplete="off"
            >
                <select
                    className="input"
                    id="typedown"
                    onChange={handleChange}
                    placeholder="Enter thingy"
                    type="text"
                >
                  {
                    items.map(function(item, index) {
                      return (
                        <option value={item.name}>{item.displayName}</option>
                      );
                    })
                  }
                </select>
            </form>
      </Col>
    </Row>
    
  );
}

export default App;
