export function getLog(setLog) {
    // creates entity
    fetch("http://localhost:8080/logs?sort=dateCreated&max=20&order=desc", {
        method: "GET",
    })
    .then(response =>
        response.json()
    )
    .then(data => {
        setLog(data ? data.reverse() : [])
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

