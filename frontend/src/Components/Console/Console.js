import React, { useEffect, useState } from 'react';

import Switch from "react-switch";
import { Col, Row } from 'react-flexbox-grid';
import InfiniteScroll from 'react-infinite-scroller';

import { get, getCount, post } from '../gormControllerRequestFunctions';

import './Console.css';

function Console() {

    const onSend = (cmd) => post('shell/command', {cmd: cmd})
    const serverOn = () => post('shell/start', {})

    const [logs, setLogs] = useState([]);
    const [logCount, setLogCount] = useState(0);
    const [alive, setAlive] = useState(false);
    const [max, setMax] = useState(20);

    // Fetch logs/alive-state every half second
    useEffect(() => {
        const interval = setInterval(() => {
        const logPromise = get(
            'logs',
            {
            sort: 'dateCreated',
            max: max,
            order: 'desc'
            }
        )
        logPromise.then(response => {
            setLogs(response ? response.reverse() : [])
        })

        const logCountPromise = getCount('logs')
        logCountPromise.then(response => {
            setLogCount(response)
        })

        const alivePromise = get('shell')
        alivePromise.then(response => {
            setAlive(response)
        })
        }, 500);
        return () => clearInterval(interval);
    }, [max]);

    const [cmd, setCmd] = useState('');
    const [offPending, setOffPending] = useState(false)

    const handleChange = (event) => {
        setCmd(event.target.value);
    };

    const handleSwitch = (checked) => {
        if (checked) {
            // We're turning the minecraft server ON
            serverOn()
        } else {
            // We're turning the minecraft server OFF
            onSend("stop")
            setOffPending(true)
        }
    }

    useEffect(() => {
        if (alive === false) {
            // This should only serve to switch the "off pending" state
            setOffPending(false)
        }
    }, [alive]);

    const consoleHeader = () => {
        return (
            <Row>
                <Col xs={8}>
                    <h1> Console </h1>
                </Col>
                <Col xs={4}>
                    <Switch
                        checked={alive}
                        className="switch"
                        disabled={offPending}
                        onChange={handleSwitch}
                    />
                </Col>
            </Row>
        );
    }
    
    return (
        <div className="box">
            {consoleHeader()}
            <div
                className="scrollContainer"
            >
                <InfiniteScroll
                    pageStart={0}
                    loadMore={() => setMax(Math.max(logs.length + 10, 20))}
                    hasMore={logs.length < logCount}
                    isReverse
                    loader={<b>Loading ...</b>}
                    useWindow={false}
                >
                    {logs.map(function(line, index) {
                        return (
                            <div className={index % 2 === 0 ? "even" : "odd"}>
                                {line.output}
                            </div>
                        );
                    })}
                </InfiniteScroll>
            </div>
            
            <form onSubmit={(e) => {
                e.preventDefault()
                onSend(cmd)
                setCmd('')
            }}
                autoComplete="off"
            >
                <input
                    className="input"
                    disabled={!alive || offPending}
                    id="cmd"
                    onChange={handleChange}
                    placeholder="Enter command"
                    value={cmd}
                    type="text"
                />
            </form>
        </div>
    );
}

export default Console;
