import {checkPassword, checkConfirmPassword, checkEmail, checkPhone, initForms} from '../utils/form.js'
import {initPopups, showPopup, closePopup} from '../utils/popup.js'

const editButton = document.getElementById('edit_button');
const cancelButton = document.querySelector('.account_info .cancel_button');
const addressesButton = document.getElementById('addresses_button');
const ordersButton = document.getElementById('orders_button');
const deleteAccountPopup = document.getElementById('delete_account_dialog');
const changePasswordPopup = document.getElementById('change_password_dialog');
//const deleteAccountButton = document.getElementById('delete_account_button');
const changePasswordForm = document.getElementById('change_password_form');
const deleteButtonPopup = document.querySelector('#delete_account_dialog .delete_button');
const accountInfoForm = document.querySelector('.account_info');
const changePasswordButton = document.getElementById('change_password_button');
const userId = document.querySelector('.account_info').getAttribute('data-id');
const role = document.querySelector('.account_info').getAttribute('role');
let editable = false;
const nameInput = document.getElementById('name_input');
const surnameInput = document.getElementById('surname_input');
const patronymInput = document.getElementById('patronym_input');
const emailInput = document.getElementById('email_input');
const phoneInput = document.getElementById('phone_input');
let name = nameInput.value;
let surname = surnameInput.value;
let patronym = patronymInput.value;
let email = emailInput.value;
let phone = phoneInput.value;

function edit() {
    editButton.disabled = true;
    cancelButton.classList.remove('not_visible');
    document.querySelectorAll('.account_info input').forEach((x) => {
        x.classList.remove('not_editable');
        x.removeAttribute('readonly');
    });
    document.querySelector('.account_info button').classList.remove('not_visible');
    editable = !editable;
}

function cancel() {
    editButton.disabled = false;
    cancelButton.classList.add('not_visible');
    document.querySelectorAll('.account_info input').forEach((x) => {
        x.classList.add('not_editable');
        x.setAttribute('readonly', 'true');
    });
    document.querySelectorAll('.account_info .form_field').forEach((x) => {
        x.classList.remove('error');
        x.classList.remove('success');
        x.querySelector('.validation_error').textContent = '';
    });
    document.getElementById('name_input').value = name;
    document.getElementById('surname_input').value = surname;
    document.getElementById('patronym_input').value = patronym;
    document.getElementById('email_input').value = email;
    document.getElementById('phone_input').value = phone;
    document.querySelector('.account_info button').classList.add('not_visible');
    editable = !editable;
}

function createJsonUser() {
    const user =
    {
        id: userId,
        name: nameInput.value,
        surname: surnameInput.value,
        patronymic: patronymInput.value,
        email: emailInput.value,
        phoneNumber: phoneInput.value,
        role: role
    };
    return JSON.stringify(user);
}

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

async function tryPutRequest(requestMessageEl, url, body, successMessage, errorMessage) {
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
        else {
            successRequest(requestMessageEl, successMessage);
            document.body.classList.remove('loading');
            return true;
        }
    } catch (e) {
        errorRequest(requestMessageEl, errorMessage);
    }
    document.body.classList.remove('loading');
    return false;
}

async function updateAccount() {
    const res = await tryPutRequest(
        accountInfoForm.querySelector('.request_message'),
        '/api/users',
        createJsonUser(),
        'Account was successfully updated!',
        'Account wasn\'t  updated!'
    );
    if(res) {
        name = nameInput.value;
        surname = surnameInput.value;
        patronym = patronymInput.value;
        email = emailInput.value;
        phone = phoneInput.value;
    }
    cancel();
}

async function changePassword() {
    const passwordInput = changePasswordPopup.querySelector('.password_input');
    const password = passwordInput.value;
    const res = await tryPutRequest(
        changePasswordPopup.querySelector('.request_message'),
        '/api/users/pass',
        JSON.stringify({id: userId, password: password}),
        'Password was successfully changed!',
        'Password wasn\'t  changed!'
    );
    if(res) {
        setTimeout(() => {
            closePopup(changePasswordPopup);
        }, 5000);
    }
}

initPopups();
initForms();
editButton.addEventListener('click', edit);
cancelButton.addEventListener('click', cancel);
if(ordersButton) {
    ordersButton.addEventListener('click', () => {
        window.location.replace('/orders');
    });
}
if(addressesButton) {
    addressesButton.addEventListener('click', () => {
        window.location.replace('/addresses');
    });
}
/*deleteAccountButton.addEventListener('click', () => {
    showPopup(deleteAccountPopup);
});*/
deleteButtonPopup.addEventListener('click', () => {
    closePopup(deleteAccountPopup);
});
changePasswordButton.addEventListener('click', () => {
    showPopup(changePasswordPopup);
});
accountInfoForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const emailEl = accountInfoForm.querySelector('.email_input');
    const phoneEl = accountInfoForm.querySelector('.phone_input');
    const isFormValid =
        checkEmail(emailEl)
        && checkPhone(phoneEl);
    if (isFormValid) {
        updateAccount();
    }
});
changePasswordForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const passwordEl = changePasswordForm.querySelector('.password_input');
    const confirmPasswordEl = changePasswordForm.querySelector('.confirm_password_input');
    const isFormValid =
        checkPassword(passwordEl)
        && checkConfirmPassword(confirmPasswordEl);
    if (isFormValid) {
        changePassword();
    }
});
