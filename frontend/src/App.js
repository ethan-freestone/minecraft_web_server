import React, { useEffect, useState  } from 'react';
import { Col, Row } from 'react-flexbox-grid';

import { get } from './Components/gormControllerRequestFunctions';
import Console from './Components/Console/Console';


function App() {

  const [items, setItems] = useState([]);

  // Fetch logs/alive-state every half second
  useEffect(() => {
    const interval = setInterval(() => {
      const itemsPromise = get(
        'minecraftItem',
        {
          max: 10
        }
      )
      itemsPromise.then(response => {
        setItems(response)
      })
    }, 500);
    return () => clearInterval(interval);
  });

  const handleChange = (event) => {
      console.log(event.target.value)
  };

  return (
    <Row>
      <Col xs={6}>
        <Console/>
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
                    items?.map(function(item, index) {
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
