export function get(path, params = null) {
    const max = params?.max
    const sort = params?.sort
    const order = params?.order
    // creates entity
    return fetch(`http://Atlas:8080/${path}${sort || max || order ? '?' : ''}${sort ? `&sort=${sort}` : ''}${max ? `&max=${max}` : ''}${order ? `&order=${order}` : ''}`, {
        method: "GET",
    })
    .then(response =>
        response.json()
    )
    .then(data => {
        return data
    })
    .catch(err => {
        console.log(err);
    });
}

export function getCount(path) {
    return get(`${path}/count`)
}

export function post(path, postBody) {
    // creates entity
    return fetch(`http://Atlas:8080/${path}`, {
        method: "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(postBody)
    })
    .then(response =>
        response.json()
    )
    .then(data => {
        return data
    })
    .catch(err => {
        console.log(err);
    });
}