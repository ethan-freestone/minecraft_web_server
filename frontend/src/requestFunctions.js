export function getLog(setLog) {
    // creates entity
    fetch("http://Atlas:8080/shell", {
        "method": "GET"
    })
    .then(response =>
        response.json()
    )
    .then(data => {
        setLog(data.log ? data.log : [])
        return data
    })
    .catch(err => {
        console.log(err);
    });
}

export function sendCommand(e) {
    console.log("COMMAND TO SEND: %o", e)
    // creates entity
    fetch("http://Atlas:8080/shell/command", {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            "cmd": e
        })
    })
    .then(response =>
        response.json()
    )
    .catch(err => {
        console.log(err);
    });
    
    
}

