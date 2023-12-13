import {checkPassword, checkConfirmPassword, checkEmail, initForms, clearForm} from '../utils/form.js'
import {tryPostRequest, tryPutRequest, tryDeleteRequest, tryGetRequest} from '../utils/api.js'

const signupForm = document.querySelector('.signup_form');

async function trySignup() {
    const user = {
        name: signupForm.querySelector('.name').value,
        surname: signupForm.querySelector('.surname').value,
        patronymic: signupForm.querySelector('.patronym').value,
        email: signupForm.querySelector('.email_input').value,
        password: signupForm.querySelector('.password_input').value
    };
    const requestMessageEl = signupForm.querySelector('.request_message');
    const url = '/api/users';
    const response = await tryPostRequest(requestMessageEl, url, JSON.stringify(user), 'You was successfully registered!', 'You wasn\'t registered!');
    if(response.type === 'success') {
        clearForm(signupForm);
    }
}

initForms();
signupForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const emailEl = signupForm.querySelector('.email_input');
    const passwordEl = signupForm.querySelector('.password_input');
    const confirmPasswordEl = signupForm.querySelector('.confirm_password_input');
    const isFormValid =
        checkEmail(emailEl)
        && checkPassword(passwordEl)
        && checkConfirmPassword(confirmPasswordEl);
    if (isFormValid) {
        trySignup();
    }
});
