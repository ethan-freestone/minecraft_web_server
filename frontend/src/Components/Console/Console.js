import React from 'react';
import './Console.css';

function Console({ logs, onSend }) {
    const [cmd, setCmd] = React.useState('');
    const handleChange = (event) => {
        setCmd(event.target.value);
    };

    return (
        <div className="box">
            <h1 className="header"> Console </h1>
            <ul className="list">
                {logs.map(function(line, index) {
                    console.log("INDEX: %o", index)
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
