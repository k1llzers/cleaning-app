import {checkNotEmpty, initCounters, initForms, setInputFilter} from '../utils/form.js'
import {initPopups, showPopup, closePopup} from '../utils/popup.js'
import {tryPostRequest, tryPutRequest, tryDeleteRequest, tryGetRequest, tryPutBooleanRequest} from '../utils/api.js'

const employeesTable = document.getElementById('employees_table');
const pagination = document.querySelector('.pagination');
const requestMessageEl = document.querySelector('.request_message');
const emptyEl = document.querySelector('.empty_list');
const unemploymentPopup = document.getElementById('unemployment_popup');
let currentPage = 1;
let currentUserId = 1;

function showEmpty() {
    emptyEl.classList.remove('not_visible');
    employeesTable.classList.add('not_visible');
    pagination.classList.add('not_visible');
}

function createPaginationElement(text, className) {
    const paginationEl = className === 'pagination_dots' ? document.createElement('span') : document.createElement('button');
    paginationEl.innerText = text;
    paginationEl.classList.add(className);
    if(className === 'pagination_link_chosen') {
        paginationEl.disabled = true;
        paginationEl.classList.add('grey');
    }
    if (className === 'pagination_link') {
        paginationEl.classList.add('grey');
        paginationEl.addEventListener('click', () => {
            getPage(parseInt(text, 10));
        });
    }
    return paginationEl;
}

function updatePagination(current, pagesCount) {
    const left = current - 1;
    const right = current + 1;
    pagination.innerHTML = '';
    const fragment = document.createDocumentFragment();
    if (pagesCount === 0) {
        return;
    }
    const last = parseInt(pagesCount, 10);
    if (current === 1) {
        fragment.appendChild(createPaginationElement(current, 'pagination_link_chosen'));
        if (last <= 3) {
            for (let i = 2; i <= last; i++) {
                fragment.appendChild(createPaginationElement(i, 'pagination_link'));
            }
        } else {
            fragment.appendChild(createPaginationElement(2, 'pagination_link'));
            fragment.appendChild(createPaginationElement('...', 'pagination_dots'));
            fragment.appendChild(createPaginationElement(last, 'pagination_link'));
        }
        pagination.appendChild(fragment);
        return;
    }
    if (current === last) {
        if (last <= 3) {
            for (let i = 1; i < last; i++) {
                fragment.appendChild(createPaginationElement(i, 'pagination_link'));
            }
        } else {
            fragment.appendChild(createPaginationElement(1, 'pagination_link'));
            fragment.appendChild(createPaginationElement('...', 'pagination_dots'));
            fragment.appendChild(createPaginationElement(last - 1, 'pagination_link'));
        }
        fragment.appendChild(createPaginationElement(current, 'pagination_link_chosen'));
        pagination.appendChild(fragment);
        return;
    }
    fragment.appendChild(createPaginationElement(1, 'pagination_link'));
    if (left === 2) {
        fragment.appendChild(createPaginationElement(2, 'pagination_link'));
    } else if (left > 2) {
        fragment.appendChild(createPaginationElement('...', 'pagination_dots'));
        fragment.appendChild(createPaginationElement(left, 'pagination_link'));
    }
    fragment.appendChild(createPaginationElement(current, 'pagination_link_chosen'));
    if (right === last - 1) {
        fragment.appendChild(createPaginationElement(right, 'pagination_link'));
    } else if (right < last - 1) {
        fragment.appendChild(createPaginationElement(right, 'pagination_link'));
        fragment.appendChild(createPaginationElement('...', 'pagination_dots'));
    }
    fragment.appendChild(createPaginationElement(last, 'pagination_link'));
    pagination.appendChild(fragment);
}

function showPagination(currentPage, pagesCount) {
    if(pagesCount === 1) {
        pagination.classList.add('not_visible');
        return;
    }
    pagination.classList.remove('not_visible');
    updatePagination(currentPage, pagesCount);
}



function createEmployeeTr(employee) {
    const employeeTr = document.createElement('tr');
    //employeeTr.setAttribute('data-id', employee.id);
    employeeTr.innerHTML = `
        <td class="name">${employee.name}</td>
        <td class="surname">${employee.surname}</td>
        <td class="patronym">${employee.patronymic}</td>
        <td class="email">${employee.email}</td>
        <td><div class="buttons"><button class="unemployment_button red">Unemploy</button></div></td>
    `;
    employeeTr.querySelector('.unemployment_button').addEventListener('click', () => {
        currentUserId = employee.id;
        showPopup(unemploymentPopup);
    });
    return employeeTr;
}

function showPage(page) {
    if (page['orderList'].length === 0) {
        if(page['currPage'] > 0) {
            getPage(page['currPage']);
            return;
        }
        showEmpty();
        return;
    }
    emptyEl.classList.add('not_visible');
    employeesTable.classList.remove('not_visible');
    employeesTable.querySelector('tbody').innerHTML = '';
    for (const order of page['orderList']) {
        const orderTr = createEmployeeTr(order);
        employeesTable.querySelector('tbody').appendChild(orderTr);
    }
    showPagination(page['currPage']+1, page['numOfPages']);
}


async function getPage(page) {
    const url = `/api/users/findAllByRole?role=EMPLOYEE&page=${parseInt(page)-1}&size=10`;
    const response = await tryGetRequest(requestMessageEl, url, '', 'Can\'t get employees!');
    if(response.type === 'success') {
        currentPage = page;
        showPage(response.response);
    }
    else {
        showEmpty();
    }
}

async function tryUnemploy() {
    const url = `/api/employment/${currentUserId}/unemployment`;
    const response = await tryPutBooleanRequest(requestMessageEl, url, 'Employee was successfully unemployed!', 'Employee wasn\'t unemployed!');
    if(response) {
        getPage(currentPage);
    }
    closePopup(unemploymentPopup);
}

getPage(1);
initPopups();
initForms();
unemploymentPopup.querySelector('.unemploy_button').addEventListener('click', () => {
    tryUnemploy();
});
if(document.getElementById('applicants_button')) {
    document.getElementById('applicants_button').addEventListener('click', () => {
        window.location.replace("/applicants");
    });
}

