function successRequest(requestMessageEl, message) {
    if(!requestMessageEl) return;
    requestMessageEl.classList.add('success_message');
    requestMessageEl.textContent = message;
    setTimeout(() => {
        requestMessageEl.textContent = '';
        requestMessageEl.classList.remove('success_message');
    }, 5000);
}

function errorRequest(requestMessageEl, message) {
    if(!requestMessageEl) return;
    requestMessageEl.classList.add('error_message');
    requestMessageEl.textContent = message;
    setTimeout(() => {
        requestMessageEl.textContent = '';
        requestMessageEl.classList.remove('error_message');
    }, 5000);
}

export async function tryPutRequest(requestMessageEl, url, body, successMessage, errorMessage) {
    document.body.classList.add('loading');
    const response = await fetch(url,
        {
            method: 'PUT',
            headers: {"Content-Type": "application/json"},
            body: body
        });
    try {
        const responseJson = await response.json();
        if(responseJson['errorMessage']) {
            errorRequest(requestMessageEl, responseJson['errorMessage']);
        }
        else if(responseJson['errors']) {
            errorRequest(requestMessageEl, responseJson['errors']);
        }
        else {
            successRequest(requestMessageEl, successMessage);
            document.body.classList.remove('loading');
            return {type: 'success', response: responseJson};
        }
    } catch (e) {
        errorRequest(requestMessageEl, errorMessage);
    }
    document.body.classList.remove('loading');
    return {type: 'error', response: null};
}

export async function tryPostRequest(requestMessageEl, url, body, successMessage, errorMessage) {
    document.body.classList.add('loading');
    const response = await fetch(url,
        {
            method: 'POST',
            headers: {"Content-Type": "application/json"},
            body: body
        });
    try {
        const responseJson = await response.json();
        if(responseJson['errorMessage']) {
            errorRequest(requestMessageEl, responseJson['errorMessage']);
        }
        else if(responseJson['errors']) {
            errorRequest(requestMessageEl, responseJson['errors']);
        }
        else {
            successRequest(requestMessageEl, successMessage);
            document.body.classList.remove('loading');
            return {type: 'success', response: responseJson};
        }
    } catch (e) {
        errorRequest(requestMessageEl, errorMessage);
    }
    document.body.classList.remove('loading');
    return {type: 'error', response: null};
}

export async function tryGetRequest(requestMessageEl, url, successMessage, errorMessage) {
    document.body.classList.add('loading');
    const response = await fetch(url,
        {
            method: 'GET',
            headers: {"Content-Type": "application/json"}
        });
    try {
        const responseJson = await response.json();
        if(responseJson['errorMessage']) {
            errorRequest(requestMessageEl, responseJson['errorMessage']);
        }
        else if(responseJson['errors']) {
            errorRequest(requestMessageEl, responseJson['errors']);
        }
        else {
            successRequest(requestMessageEl, successMessage);
            document.body.classList.remove('loading');
            return {type: 'success', response: responseJson};
        }
    } catch (e) {
        errorRequest(requestMessageEl, errorMessage);
    }
    document.body.classList.remove('loading');
    return {type: 'error', response: null};
}

export async function tryDeleteRequest(requestMessageEl, url, successMessage, errorMessage) {
    document.body.classList.add('loading');
    const response = await fetch(url,
        {
            method: 'DELETE',
            headers: {"Content-Type": "application/json"}
        });
    const responseText = await response.text();
    if (responseText === 'true') {
        successRequest(requestMessageEl, successMessage);
        document.body.classList.remove('loading');
        return true;
    }
    else {
        try {
            const responseJson = await response.json();
            if(responseJson['errorMessage']) {
                errorRequest(requestMessageEl, responseJson['errorMessage']);
            }
            else if(responseJson['errors']) {
                errorRequest(requestMessageEl, responseJson['errors']);
            }
            else {
                errorRequest(requestMessageEl, errorMessage);
            }
        }
        catch (e) {
            errorRequest(requestMessageEl, errorMessage);
        }
    }
    document.body.classList.remove('loading');
    return false;
}
