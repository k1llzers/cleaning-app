import {checkNotEmpty, initForms} from '../utils/form.js'
import {initPopups, showPopup, closePopup} from '../utils/popup.js'
import {tryPostRequest, tryPutRequest, tryDeleteRequest, tryPutBooleanRequest} from '../utils/api.js'

const requestMessageEl = document.querySelector('.request_message');
const applyApplicantPopup = document.getElementById('apply_applicant_popup');
const cancelApplicantPopup = document.getElementById('cancel_applicant_popup');
const motivationListPopup = document.getElementById('motivation_list_popup');
const applicantsTable = document.getElementById('applicants_table');
let currentUserId = null;
let currentTr = null;

function initApplicants() {
    document.querySelectorAll('#applicants_table tbody tr').forEach((tr) => {
        const userId = tr.getAttribute('data-id');
        tr.querySelector('.cancel_button').addEventListener('click', () => {
           currentUserId = userId;
           currentTr = tr;
           showPopup(cancelApplicantPopup);
        });
        tr.querySelector('.apply_button').addEventListener('click', () => {
            currentUserId = userId;
            currentTr = tr;
            showPopup(applyApplicantPopup);
        });
        tr.querySelector('.motivation_list_button').addEventListener('click', () => {
            currentUserId = userId;
            motivationListPopup.querySelector('textarea').value = tr.querySelector('.motivation_list').textContent;
            showPopup(motivationListPopup);
        });
    });
}

async function tryCancelApplicant() {
    const url = `/api/employment/${currentUserId}/cancel`;
    const response = await tryPutBooleanRequest(requestMessageEl, url, 'Applicant cancelled successfully!', 'Applicant wasn\'t cancelled!');
    if(response) {
        //console.log(currentTr);
        currentTr.remove();
        if(applicantsTable.querySelector('tbody').innerHTML.trim() === '') {
            applicantsTable.classList.add('not_visible');
            document.querySelector('.empty_list').classList.remove('not_visible');
        }
    }
    closePopup(cancelApplicantPopup);
}

async function tryApplyApplicant() {
    const url = `/api/employment/${currentUserId}/succeed`;
    const response = await tryPutBooleanRequest(requestMessageEl, url, 'Applicant employed successfully!', 'Applicant wasn\'t emloyed!');
    if(response) {
        currentTr.remove();
        if(applicantsTable.querySelector('tbody').innerHTML.trim() === '') {
            applicantsTable.classList.add('not_visible');
            document.querySelector('.empty_list').classList.remove('not_visible');
        }
    }
    closePopup(applyApplicantPopup);
}

initPopups();
initForms();
initApplicants();
cancelApplicantPopup.querySelector('.cancel_button').addEventListener('click', () => {
    tryCancelApplicant();
});
applyApplicantPopup.querySelector('.apply_button').addEventListener('click', () => {
    tryApplyApplicant();
});
