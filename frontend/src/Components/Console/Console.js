import React, { useEffect, useState } from 'react';

import Switch from "react-switch";
import { Col, Row } from 'react-flexbox-grid';

import './Console.css';

function Console({ alive, logs, onSend, serverOn }) {
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
                    <Switch className="switch" onChange={handleSwitch} checked={alive}/>
                </Col>
            </Row>
        );
    }
    return (
        <div className="box">
            {consoleHeader()}
            <ul className="list">
                {logs.map(function(line, index) {
                    return (
                    <li className={index % 2 === 0 ? "even" : "odd"}>
                        {line.output}
                    </li>
                    );
                })}
            </ul>
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
