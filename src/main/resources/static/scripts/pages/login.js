import {checkPassword, checkConfirmPassword, checkEmail, initForms, clearForm} from '../utils/form.js'
import {tryPostRequest, tryPutRequest, tryDeleteRequest, tryGetRequest} from '../utils/api.js'

const loginForm = document.querySelector('.login_form');

async function tryLogin() {
    const requestMessageEl = loginForm.querySelector('.request_message');
    const url = '/api/auth/login';
    const body = {
        username: loginForm.querySelector('.email_input').value,
        password: loginForm.querySelector('.password_input').value
    };
    const response = await tryPostRequest(requestMessageEl, url, JSON.stringify(body), 'You logged in successfully!', 'Your email or password is invalid!');
    if(response.type === 'success') {
        const jwtToken = response.response;
        document.cookie = `accessToken=${jwtToken['accessToken']}`;
        document.cookie = `refreshToken=${jwtToken['refreshToken']}`;
        window.location.replace('/');
    }
}

initForms();
loginForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const emailEl = loginForm.querySelector('.email_input');
    const passwordEl = loginForm.querySelector('.password_input');
    const isFormValid =
        checkEmail(emailEl)
        && checkPassword(passwordEl);
    if (isFormValid) {
        tryLogin();
    }
});
