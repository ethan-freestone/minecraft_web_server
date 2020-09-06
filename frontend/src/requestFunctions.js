export function getLog(setLog) {
    // creates entity
    fetch("http://localhost:8080/shell", {
        "method": "GET"
    })
    .then(response =>
        response.json()
    )
    .then(data => {
        setLog(data.log)
        return data
    })
    .catch(err => {
        console.log(err);
    });
}

