export function getLog(setLog, max = null) {
    // creates entity
    fetch(`http://Atlas:8080/logs?sort=dateCreated${max ? `&max=${max}` : ''}&order=desc`, {
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

export function getAlive(setAlive) {
    // creates entity
    fetch("http://Atlas:8080/shell", {
        method: "GET",
    })
    .then(response =>
        response.json()
    )
    .then(status => {
        setAlive(status)
        return status
    })
    .catch(err => {
        console.log(err);
    });
}

export function getLogCount(setCount) {
    // creates entity
    fetch("http://Atlas:8080/logs/count", {
        method: "GET",
    })
    .then(response =>
        response.json()
    )
    .then(count => {
        setCount(count)
        return count
    })
    .catch(err => {
        console.log(err);
    });
}

export function sendCommand(e) {
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


export function serverOn() {
    // creates entity
    fetch("http://Atlas:8080/shell/start", {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
    .then(response =>
        response.json()
    )
    .catch(err => {
        console.log(err);
    });
}
