import {checkPassword, checkConfirmPassword, checkEmail, initForms, clearForm} from '../utils/form.js'
import {tryPostRequest, tryPutRequest, tryDeleteRequest, tryGetRequest} from '../utils/api.js'

const applyButton = document.querySelector('.apply_button');
const motivationList = document.querySelector('.motivation_list');
const requestMessageEl = document.querySelector('.request_message');

async function trySendRequest() {
    const url = '/api/employment';
    const body = motivationList.value;
    const response = await tryPostRequest(requestMessageEl, url, JSON.stringify(body), 'We received your request!', 'Something went wrong...');

}

initForms();
applyButton.addEventListener('click', () => {
    trySendRequest();
});
