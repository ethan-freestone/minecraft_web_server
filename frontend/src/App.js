import React, { useEffect, useState  } from 'react';
import { Card, List, ListItem, TextField } from '@material-ui/core';

import { getLog, sendCommand } from './requestFunctions'

function App() {

  const [log, setLog] = useState([]);
  // Fetch logs every half second
  useEffect(() => {
    const interval = setInterval(() => {
      getLog(setLog)
    }, 500);
    return () => clearInterval(interval);
  }, []);

  const [cmd, setCmd] = React.useState('');
  const handleChange = (event) => {
    setCmd(event.target.value);
  };

  //console.log("LOGS:%o", log)

  return (
    <Card>
      <List>
        {log.map(line =>
            <ListItem> {line} </ListItem>
        )}
      </List>
      <form onSubmit={(e) => {
        e.preventDefault()
        sendCommand(cmd)
        setCmd('')
      }}
        autoComplete="off"
      >
        <TextField id="cmd" label="Enter command" onChange={handleChange} value={cmd} variant="outlined" />
      </form>
    </Card>
  );
}

export default App;
