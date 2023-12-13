function isRequired(value) {
    return value !== '';
}

function isBetween(length, min, max) {
    return min <= length <= max;
}

function isEmailValid(email) {
    const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

function isPasswordSecure(password) {
    const re = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{12,})");
    return re.test(password);
}

const showError = (input, message) => {
    const formField = input.parentElement;
    formField.classList.remove('success');
    formField.classList.add('error');
    const error = formField.querySelector('.validation_error');
    error.textContent = message;
};

const showSuccess = (input) => {
    const formField = input.parentElement;
    formField.classList.remove('error');
    formField.classList.add('success');
    const error = formField.querySelector('.validation_error');
    error.textContent = '';
}

function isPhoneValid(phone) {
    const uaPhoneRegex = /^(\+38\s?)?((\(0[1-9]{2}\))|(0[1-9]{2}))(\s|-)?[0-9]{3}(\s|-)?[0-9]{2}(\s|-)?[0-9]{2}$/g;
    return uaPhoneRegex.test(phone) || phone === '';
}

export function checkPhone(phoneEl) {
    let valid = false;
    const phone = phoneEl.value.trim();
    if (!isPhoneValid(phone)) {
        showError(phoneEl, 'Phone is not valid.')
    } else {
        showSuccess(phoneEl);
        valid = true;
    }
    return valid;
}

export function checkEmail(emailEl) {
    let valid = false;
    const email = emailEl.value.trim();
    if (!isRequired(email)) {
        showError(emailEl, 'Email cannot be blank.');
    } else if (!isEmailValid(email)) {
        showError(emailEl, 'Email is not valid.')
    } else {
        showSuccess(emailEl);
        valid = true;
    }
    return valid;
}

export function checkPassword(passwordEl) {
    let valid = false;
    const password = passwordEl.value.trim();
    if (!isRequired(password)) {
        showError(passwordEl, 'Password cannot be blank.');
    } else if (!isPasswordSecure(password)) {
        showError(passwordEl, 'Password must has at least 12 characters that include at least 1 lowercase character, 1 uppercase characters, 1 number, and 1 special character in (!@#$%^&*)');
    } else {
        showSuccess(passwordEl);
        valid = true;
    }
    return valid;
}

export function checkConfirmPassword(confirmPasswordEl) {
    let valid = false;
    const passwordEl = confirmPasswordEl.form.querySelector('.password_input');
    const confirmPassword = confirmPasswordEl.value.trim();
    const password = passwordEl.value.trim();
    if (password !== confirmPassword) {
        showError(confirmPasswordEl, 'Confirm password does not match');
    } else {
        showSuccess(confirmPasswordEl);
        valid = true;
    }
    return valid;
}

export function checkNotEmpty(notEmptyEl) {
    let valid = false;
    const val = notEmptyEl.value.trim();
    if (!isRequired(val)) {
        showError(notEmptyEl, 'This field cannot be blank.');
    }
    else {
        showSuccess(notEmptyEl);
        valid = true;
    }
    return valid;
}

function debounce(fn, delay = 500) {
    let timeoutId;
    return (...args) => {
        if (timeoutId) {
            clearTimeout(timeoutId);
        }
        timeoutId = setTimeout(() => {
            fn.apply(null, args)
        }, delay);
    };
}

export function initForms() {
    const forms = document.querySelectorAll('form');
    forms.forEach((form) => {
        form.addEventListener('input', debounce(function (e) {
            if(e.target.classList.contains('password_input')) {
                checkPassword(e.target);
            }
            if(e.target.classList.contains('confirm_password_input')) {
                checkConfirmPassword(e.target);
            }
            if(e.target.classList.contains('email_input')) {
                checkEmail(e.target);
            }
            if(e.target.classList.contains('phone_input')) {
                checkPhone(e.target);
            }
            if(e.target.classList.contains('not_empty')) {
                checkNotEmpty(e.target);
            }
        }));
    });
}

export function clearForm(form) {
    form.querySelectorAll('input').forEach((input) => {
        input.value = "";
    });
    form.querySelectorAll('.form_field').forEach((x) => {
        x.classList.remove('error');
        x.classList.remove('success');
        x.querySelector('.validation_error').textContent = '';
    });
}

export function setInputFilter(textbox, inputFilter, errMsg) {
    [ "input", "keydown", "keyup", "mousedown", "mouseup", "select", "contextmenu", "drop", "focusout" ].forEach(function(event) {
        textbox.addEventListener(event, function(e) {
            if (inputFilter(this.value) || this.readOnly) {
                // Accepted value.
                if ([ "keydown", "mousedown", "focusout" ].indexOf(e.type) >= 0){
                    this.classList.remove("input-error");
                    this.setCustomValidity("");
                }

                this.oldValue = this.value;
                this.oldSelectionStart = this.selectionStart;
                this.oldSelectionEnd = this.selectionEnd;
            }
            else if (this.hasOwnProperty("oldValue")) {
                // Rejected value: restore the previous one.
                if(errMsg) {
                    this.classList.add("input-error");
                    this.setCustomValidity(errMsg);
                    this.reportValidity();
                }
                this.value = this.oldValue;
                this.setSelectionRange(this.oldSelectionStart, this.oldSelectionEnd);
            }
            else {
                // Rejected value: nothing to restore.
                this.value = "";
            }
        });
    });
}
