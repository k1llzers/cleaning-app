import {checkNotEmpty, initCounters, initForms, setInputFilter} from '../../utils/form.js'
import {initPopups, showPopup, closePopup} from '../../utils/popup.js'
import {tryPostRequest, tryPutRequest, tryDeleteRequest, tryGetRequest} from '../../utils/api.js'

const userId = document.getElementById('main_part_content').getAttribute('user-id');
const userRole = document.getElementById('main_part_content').getAttribute('user-role');
const ordersTable = document.getElementById('orders_table');
const pagination = document.querySelector('.pagination');
const requestMessageEl = document.querySelector('.request_message');
const emptyEl = document.querySelector('.empty_list');
const ordersTableBody = ordersTable.querySelector('tbody');
const cancelOrderPopup = document.getElementById('cancel_order_popup');
const reviewOrderPopup = document.getElementById('review_order_popup');
const changeStatusPopup = document.getElementById('change_status_popup');
const verifyOrderPopup = document.getElementById('verify_order_popup');
let currentOrder = null;
let currentEmployees = null;
let currentPage = 1;

function showEmpty() {
    emptyEl.classList.remove('not_visible');
    ordersTable.classList.add('not_visible');
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

function addressToString(address) {
    return `${address.city}, ${address.street}, ${address.houseNumber}${address.flatNumber ? ', ' + address.flatNumber : ''}${address.zip ? ', ' + address.zip : ''}`;
}

/*
NOT_VERIFIED,
    VERIFIED,
    NOT_STARTED,
    PREPARING,
    IN_PROGRESS,
    DONE,
    CANCELLED
 */

function createOrderTrButtonsHtml(order) {
    let html = '<td><div class="buttons">';
    html += '<button class="details_button grey not_visible">Details</button>';
    if(userRole === 'USER') {
        if(order.status === 'NOT_VERIFIED' || order.status === 'VERIFIED') {
            html += '<button class="cancel_button red">Cancel</button>';
        }
        else if(order.status === 'DONE' && !order.review) {
            html += '<button class="review_button green">Review</button>';
        }
    }
    if(userRole === 'EMPLOYEE') {
        if(order.status === 'PREPARING' || order.status === 'IN_PROGRESS') {
            html += '<button class="change_status_button green">Change Status</button>';
        }
    }
    if(userRole === 'ADMIN') {
        if(order.status === 'VERIFIED' || order.status === 'PREPARING' || order.status === 'IN_PROGRESS') {
            html += '<button class="change_status_button green">Change Status</button>';
        }
        if(order.status === 'NOT_VERIFIED') {
            html += '<button class="verify_button green">Verify</button>';
        }
    }
    html += '</div></td>';
    return html;
}

async function tryCancel() {
    const orderId = currentOrder.id;
    const url = `/api/orders/${orderId}`;
    const response = await tryDeleteRequest(requestMessageEl, url, 'Order was successfully cancelled!', 'Order wasn\'t cancelled!');
    if(response) {
        getPage(currentPage);
    }
    currentOrder = null;
    closePopup(cancelOrderPopup);
}

/*
orderId;
    @Range(min = 1, max = 5, message = "Cleaning rate should be in range from 1 to 5")
    private Long cleaningRate;
    @Range(min = 1, max = 5, message = "Employee rate should be in range from 1 to 5")
    private Long employeeRate;
    private String details;
 */

function getCleaningRate() {
    let res = 1;
    document.querySelectorAll('#cleaning_rate input').forEach((inputEl) => {
        if(inputEl.checked) {
            res = parseInt(inputEl.value);
        }
    });
    return res;
}

function getEmployeeRate() {
    let res = 1;
    document.querySelectorAll('#employee_rate input').forEach((inputEl) => {
        if(inputEl.checked) {
            res = parseInt(inputEl.value);
        }
    });
    return res;
}

async function tryReview() {
    const orderId = currentOrder.id;
    const url = `/api/orders/update/review`;
    const review = {
        orderId: orderId,
        cleaningRate: getCleaningRate(),
        employeeRate: getEmployeeRate(),
        details: document.getElementById('review_comment').value
    };
    console.log(review);
    const response = await tryPutRequest(requestMessageEl, url, JSON.stringify(review), 'Review was successfully added!', 'Review wasn\'t added!');
    if(response) {
        getPage(currentPage);
        console.log(response.response);
    }
    currentOrder = null;
    closePopup(reviewOrderPopup);
}

function getStatus() {
    let res = '';
    changeStatusPopup.querySelectorAll('option').forEach((optionEl) => {
        if(optionEl.selected) {
            res = optionEl.value;
        }
    });
    return res;
}

async function tryChangeStatus() {
    const orderId = currentOrder.id;
    const status = getStatus();
    const url = `/api/orders/change/status/${orderId}/${status}`;
    console.log(status);
    const response = await tryPutRequest(requestMessageEl, url, '', 'Status was successfully changed!', 'Status wasn\'t changed!');
    if(response.type === 'success') {
        getPage(currentPage);
        console.log(response.response);
    }
    currentOrder = null;
    closePopup(changeStatusPopup);
}

function initButtons(order, orderTr) {
    const cancelButton = orderTr.querySelector('.cancel_button');
    if(cancelButton) {
        cancelButton.addEventListener('click', () => {
            currentOrder = order;
            showPopup(cancelOrderPopup);
        });
    }
    const reviewButton = orderTr.querySelector('.review_button');
    if(reviewButton) {
        reviewButton.addEventListener('click', () => {
            document.querySelector('#cleaning_rate .stars input:nth-of-type(5)').checked = true;
            document.querySelector('#employee_rate .stars input:nth-of-type(5)').checked = true;
            reviewOrderPopup.querySelector('#review_comment').value = '';
            currentOrder = order;
            showPopup(reviewOrderPopup);
        });
    }
    const changeStatusButton = orderTr.querySelector('.change_status_button');
    if(changeStatusButton) {
        changeStatusButton.addEventListener('click', () => {
            if(userRole === 'EMPLOYEE') {
                changeStatusPopup.querySelectorAll('option').forEach( (optionEl) => {
                        optionEl.classList.add('not_visible');
                    }
                );
                changeStatusPopup.querySelector('.in_progress').classList.remove('not_visible');
                changeStatusPopup.querySelector('.in_progress').selected = true;
                changeStatusPopup.querySelector('.done').classList.remove('not_visible');
            }
            if(userRole === 'ADMIN') {
                changeStatusPopup.querySelectorAll('option').forEach( (optionEl) => {
                        optionEl.classList.remove('not_visible');
                    }
                );
                changeStatusPopup.querySelector('.preparing').selected = true;
            }
            currentOrder = order;
            showPopup(changeStatusPopup);
        });
    }
    const verifyButton = orderTr.querySelector('.verify_button');
    if(verifyButton) {
        verifyButton.addEventListener('click', async function() {
            const url = `/api/available/employees/${order.id}`;
            const employeesResponse = await tryGetRequest(requestMessageEl, url, '', 'No employees for order!');
            if(employeesResponse.type !== 'success') {
                return;
            }
            const fullOrder = await tryGetOrder(order.id);
            if(!fullOrder) {
                return;
            }
            const employees = employeesResponse.response;
            currentEmployees = employees;
            console.log(fullOrder);
            verifyOrderPopup.querySelector('.executors').innerHTML = '';
            employees.forEach((employee) => {
                const labelEl = document.createElement('label');
                labelEl.innerHTML = `
                    <input type="checkbox" value="${employee.id}">
                    <p>${employee.name} ${employee.surname} ${employee.patronymic} (${employee.email})</p>
                `;
                verifyOrderPopup.querySelector('.executors').appendChild(labelEl);
            });
            verifyOrderPopup.querySelector('.time').value = dayjs.duration(fullOrder.duration).as('minutes');
            currentOrder = fullOrder;
            showPopup(verifyOrderPopup);
        });
    }
}

async function tryGetOrder(orderId) {
    if(userRole === 'ADMIN') {
        const url = `/api/orders/admin/${orderId}`;
        const response = await tryGetRequest(requestMessageEl, url, '', 'Can\'t get this order!');
        if(response.type === 'success') {
            return response.response;
        }
    }
    if(userRole === 'USER') {
        const url = `/api/orders/user/${orderId}`;
        const response = await tryGetRequest(requestMessageEl, url, '', 'Can\'t get this order!');
        if(response.type === 'success') {
            return response.response;
        }
    }
    if(userRole === 'EMPLOYEE') {
        const url = `/api/orders/employee/${orderId}`;
        const response = await tryGetRequest(requestMessageEl, url, '', 'Can\'t get this order!');
        if(response.type === 'success') {
            return response.response;
        }
    }
    return null;
}

async function tryVerify() {
    currentOrder.duration = dayjs.duration(verifyOrderPopup.querySelector('.time').value, 'minutes').toISOString();
    const executors = [];
    verifyOrderPopup.querySelectorAll('.executors input').forEach((inputEl) => {
        console.log(inputEl, inputEl.checked);
        if(!inputEl.checked) return;
        const employee = currentEmployees.find((emp) => {console.log(emp.id, inputEl.value); return emp.id == inputEl.value;});
        executors.push(employee);
    });
    currentOrder.executors = executors;
    currentOrder.status = 'VERIFIED';
    const url = `/api/orders/admin`;
    console.log(currentOrder);
    const response = await tryPutRequest(requestMessageEl, url, JSON.stringify(currentOrder), 'Order was successfully verified!', 'Order wasn\'t verified!');
    if(response.type === 'success') {
        getPage(currentPage);
        console.log(response.response);
    }
    currentOrder = null
    currentEmployees = null;
    closePopup(verifyOrderPopup);
}

function orderDetails() {

}

function createOrderTr(order) {
    const orderTr = document.createElement('tr');
    //orderTr.setAttribute('data-id', order.id);
    orderTr.innerHTML = `
        <td class="id">${order.id}</td>
        <td class="price">${order.price}</td>
        <td class="order_time">${dayjs(order.orderTime).format('DD.MM.YYYY HH:mm')}</td>
        <td class="address">${addressToString(order.address)}</td>
        <td class="status ${order.status === 'CANCELLED' ? 'error_message' : ''} ${order.status === 'DONE' ? 'success_message' : ''}">${order.status}</td>
        ${createOrderTrButtonsHtml(order)}
    `;
    initButtons(order, orderTr);
    return orderTr;
}

function showPage(page) {
    if (page['orderList'].length === 0) {
        showEmpty();
        return;
    }
    emptyEl.classList.add('not_visible');
    ordersTable.classList.remove('not_visible');
    ordersTableBody.innerHTML = '';
    for (const order of page['orderList']) {
        const orderTr = createOrderTr(order);
        ordersTableBody.appendChild(orderTr);
    }
    showPagination(page['currPage']+1, page['numOfPages']);
}


async function getPage(page) {
    const url = userRole === 'USER' ? `/api/orders/by-user/${userId}?page=${parseInt(page)-1}&size=10` :
        userRole === 'EMPLOYEE' ? `/api/orders/by-executor/${userId}?page=${parseInt(page)-1}&size=10` : `/api/orders/all?page=${parseInt(page)-1}&size=10`;
    const response = await tryGetRequest(requestMessageEl, url, '', 'Can\'t get orders!');
    if(response.type === 'success') {
        currentPage = page;
        showPage(response.response);
    }
    else {
        showEmpty();
    }
}

getPage(1);
initPopups();
initForms();
initCounters();
const cancelOrderPopupButton = cancelOrderPopup.querySelector('.cancel_button');
if(cancelOrderPopupButton) {
    cancelOrderPopupButton.addEventListener('click', () => {
       tryCancel();
    });
}
reviewOrderPopup.querySelector('.review_button').addEventListener('click', () => {
   tryReview();
});
changeStatusPopup.querySelector('.change_status_button').addEventListener('click', () => {
    tryChangeStatus();
});
verifyOrderPopup.querySelector('.verify_button').addEventListener('click', () => {
    tryVerify();
});
