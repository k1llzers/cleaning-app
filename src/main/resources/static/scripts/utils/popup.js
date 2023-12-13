import {clearForm} from "./form.js";

export function initPopups() {
    document.querySelectorAll('.popup').forEach((popup) => {
        popup.addEventListener('click', (e) => {
            const dialogDimensions = popup.getBoundingClientRect();
            if (
                e.clientX < dialogDimensions.left
                || e.clientX > dialogDimensions.right
                || e.clientY < dialogDimensions.top
                || e.clientY > dialogDimensions.bottom
            ) {
                closePopup(popup);
            }
        });
        popup.querySelectorAll('.close_popup_button').forEach((closeButton) => {
            closeButton.addEventListener('click', () => {
                closePopup(popup);
            });
        });
    });
}

export function clearPopup(popup) {
    popup.querySelectorAll('form').forEach((form) => {
        clearForm(form);
    })
}

export function showPopup(popup) {
    document.body.classList.add('popup_active');
    popup.showModal();
}

export function closePopup(popup) {
    popup.close();
    clearPopup(popup);
    document.body.classList.remove('popup_active');
}
