import {checkNotEmpty, initForms, setInputFilter} from '../../utils/form.js'
import {initPopups, showPopup, closePopup} from '../../utils/popup.js'
import {tryPostRequest, tryPutRequest, tryDeleteRequest, tryGetRequest} from '../../utils/api.js'

const userId = document.getElementById('main_part_content').getAttribute('user-id');
const ordersTable = document.getElementById('orders_table');
const pagination = document.querySelector('.pagination');
const requestMessageEl = document.querySelector('.request_message');
const emptyEl = document.querySelector('.empty_list');
const ordersTableBody = ordersTable.querySelector('tbody');

function showEmpty() {
    emptyEl.classList.remove('not_visible');
    ordersTable.classList.add('not_visible');
    pagination.classList.add('not_visible');
}

function showPagination(currentPage, pagesCount) {
    if(pagesCount === 1) {
        pagination.classList.add('not_visible');
        return;
    }
    pagination.classList.remove('not_visible');
}

function addressToString(address) {
    return `${address.city}, ${address.street}, ${address.houseNumber}${address.flatNumber ? ', ' + address.flatNumber : ''}${address.zip ? ', ' + address.zip : ''}`;
}

function createOrderTr(order) {
    const orderTr = document.createElement('tr');
    //orderTr.setAttribute('data-id', order.id);
    orderTr.innerHTML = `
        <td class="id">${order.id}</td>
        <td class="price">${order.price}</td>
        <td class="order_time">${dayjs(order.orderTime).format('DD.MM.YYYY HH:mm')}</td>
        <td class="address">${addressToString(order.address)}</td>
        <td class="status ${order.status === 'CANCELLED' ? 'error' : ''} ${order.status === 'DONE' ? 'success' : ''}">${order.status}</td>
        `;
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
}


async function getPage(page) {
    const url = `/api/orders/by-user/${userId}?page=${page}&size=10`;
    const response = await tryGetRequest(requestMessageEl, url, '', 'Can\'t get orders!');
    if(response.type === 'success') {
        showPage(response.response);
    }
    else {
        showEmpty();
    }
}

getPage(0);
