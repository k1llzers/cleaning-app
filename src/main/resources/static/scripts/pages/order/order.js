import {checkNotEmpty, initForms, setInputFilter} from '../../utils/form.js'
import {initPopups, showPopup, closePopup} from '../../utils/popup.js'
import {tryPostRequest, tryPutRequest, tryDeleteRequest, tryGetRequest} from '../../utils/api.js'
import {
    getChosenProposalFromStorage,
    jsonToChosenProposals,
    chosenProposalsToObj,
    saveChosenProposalsToStorage
} from '../../utils/proposals.js'

const userId = document.getElementById('main_part_content').getAttribute('user-id');
const chooseAddress = document.getElementById('choose_address');
const writeAddress = document.getElementById('write_address');
const addressSelect = document.getElementById('address_select');
const timeTable = document.getElementById('time_table');
const orderButton = document.querySelector('.order_button');
const addAddressForm = document.getElementById('add_address_form');
const addressComponents = [];
const proposals = [];
const timeComponents = [];
const chosenProposals = getChosenProposalFromStorage();

function calculateDuration() {
    let res = 0;
    proposals.forEach(({proposal, count}) => {
       res += dayjs.duration(proposal.time).as('milliseconds') * count;
    });
    return res;
}

function calculateCount() {
    let res = 1;
    proposals.forEach(({proposal, count}) => {
        res = Math.max(proposal.requiredCountOfEmployees, res);
    });
    return res;
}

class AddressComponent {
    constructor(address) {
        this.address = address;
        this.addressEl = createHtmlAddress(address);
        addressSelect.appendChild(this.addressEl);
    }
}

class TimeComponent {
    constructor(timeEl, time) {
        this.timeEl = timeEl;
        this.time = time;
    }
}

function createHtmlAddress(address) {
    const addressElement = document.createElement('option');
    addressElement.setAttribute('data-id', address.id);
    addressElement.textContent = `${address.city}, ${address.street}, ${address.houseNumber}${address.flatNumber ? ', ' + address.flatNumber : ''}${address.zip ? ', ' + address.zip : ''}`;
    return addressElement;
}

async function initAddresses() {
    const url = `/api/addresses/by-user/${userId}`;
    const response = await tryGetRequest(null, url, '', '');
    const addresses = response.response;
    if(response.type === 'success' && addresses.length > 0) {
        chooseAddress.classList.remove('disabled');
        chooseAddress.open = true;
        writeAddress.open = false;
        addresses.forEach((address) => {addressComponents.push(new AddressComponent(address))});
    }
    else {
        chooseAddress.classList.add('disabled');
        chooseAddress.open = false;
        writeAddress.open = true;
    }
}

async function initProposals() {
    for (const [id, count] of chosenProposals) {
        const url = `/api/commercial-proposals/${id}`;
        const response = await tryGetRequest(null, url, '', '');
        if(response.type === 'success') {
            proposals.push({proposal: response.response, count: count});
        }
    }
}

function initTimeTableTHead(times) {
    const thead = document.createElement('thead');
    const tr = document.createElement('tr');
    tr.appendChild(document.createElement('th'));
    for (const date in times) {
        const th = document.createElement('th');
        th.textContent = dayjs(date).format('DD.MM.YYYY');
        tr.appendChild(th);
    }
    thead.appendChild(tr);
    timeTable.appendChild(thead);
}

function initTimeTableTBody(times) {
    const tbody = document.createElement('tbody');
    let checked = false;
    for(let i = 9; i <= 20; i++) {
        const tr = document.createElement('tr');
        const tdHeader = document.createElement('td');
        tdHeader.textContent = dayjs().startOf('day').add(i, 'hour').format('HH:mm');
        tr.appendChild(tdHeader);
        for(const date in times) {
            const td = document.createElement('td');
            const input = document.createElement('input');
            input.setAttribute('type', 'radio');
            input.setAttribute('name', 'time');
            timeComponents.push(new TimeComponent(input, dayjs(date).startOf('day').add(i, 'hour').format('YYYY-MM-DDTHH:mm:ss.SSS') + 'Z'));//toISOString()format('YYYY-MM-DD HH:mm:ss.SSS')
            if(!times[date].some((x) => {return dayjs(`${date} ${x}`).hour() === i;})) {
                input.disabled = true;
            }
            else if(!checked) {
                checked = true;
                input.checked = true;
            }
            td.appendChild(input);
            tr.appendChild(td);
        }
        tbody.appendChild(tr);
    }
    timeTable.appendChild(tbody);
    if(!checked) {
        document.querySelector('#time .error_message').textContent = 'Sorry but we don\'t have free time for next week. Please try next time';
        orderButton.disabled = true;
    }
}

async function initTimeTable(count, duration) {
    const url = `/api/available/time/${count}/${dayjs.duration(duration).toISOString()}`;
    const response = await tryGetRequest(null, url, '', '');
    if(response.type === 'success') {
        const times = response.response;
        console.log(times);
        initTimeTableTHead(times);
        initTimeTableTBody(times);
    }
}

function calculateTotalCost() {
    let res = 0;
    proposals.forEach(({proposal, count}) => {
        res += proposal.price * count;
    });
    return res;
}

async function tryOrder(order) {
    console.log(JSON.stringify(order));
    const url = `/api/orders`;
    const requestMessageEl = document.querySelector('.request_message');
    const response = await tryPostRequest(requestMessageEl, url, JSON.stringify(order), 'Your order was successfully created!', 'Your order wasn\'t created!');
    if(response.type === 'success') {
        sessionStorage.clear();
        sessionStorage.setItem('order', 'success');
        window.location.replace('/order/success');
        //console.log(response.response);
        ///console.log(dayjs(response.response.orderTime).toISOString());
    }
}

if(chosenProposals.size === 0) {
    window.location.replace('/');
}
chooseAddress.querySelector('summary').addEventListener('click', (e) => {
    e.preventDefault();
    if(chooseAddress.classList.contains('disabled')) {
        chooseAddress.open = false;
        return;
    }
    chooseAddress.open = true;
    writeAddress.open = false;
});
writeAddress.querySelector('summary').addEventListener('click', (e) => {
    e.preventDefault();
    writeAddress.open = true;
    chooseAddress.open = false;
});

function getCheckedTime() {
    for(const timeComponent of timeComponents) {
        if(timeComponent.timeEl.checked) return timeComponent.time;
    }
    return null;
}

function proposalsToObj() {
    const res = {};
    proposals.forEach(({proposal, count}) => {
        res[proposal.id] = count;
    });
    return res;
}

function getAddress() {
    if(chooseAddress.open) {
        for(const addressComponent of addressComponents) {
            if(addressComponent.addressEl.selected) return addressComponent.address;
        }
    }
    else {
        return {
            city: addAddressForm.querySelector('.city').value,
            street: addAddressForm.querySelector('.street').value,
            houseNumber: addAddressForm.querySelector('.house_number').value,
            flatNumber: addAddressForm.querySelector('.flat_number').value,
            zip: addAddressForm.querySelector('.zip').value
        };
    }
    return null;
}

initForms();
(async () => {
    await initAddresses();
    await initProposals();
    const duration = calculateDuration();
    const count = calculateCount();
    await initTimeTable(count, duration);
    const totalPrice = calculateTotalCost();
    document.querySelector('#price .total_cost').textContent = calculateTotalCost();
    orderButton.addEventListener('click', () => {
        let isFormValid = true;
        if(writeAddress.open) {
            for(const notEmptyEl of addAddressForm.querySelectorAll('.not_empty')) {
                isFormValid = isFormValid && checkNotEmpty(notEmptyEl);
            }
        }
        if(!isFormValid) return;
        const orderTime = getCheckedTime();
        if(!orderTime) {
            document.querySelector('.request_message').textContent = 'Please select time!';
            document.querySelector('.request_message').classList.add('error');
            setTimeout(() => {
                document.querySelector('.request_message').textContent = '';
                document.querySelector('.request_message').classList.remove('error');
            }, 5000);
            return;
        }
        const address = getAddress();
        if(!address) {
            document.querySelector('.request_message').textContent = 'Please choose or write address!';
            document.querySelector('.request_message').classList.add('error');
            setTimeout(() => {
                document.querySelector('.request_message').textContent = '';
                document.querySelector('.request_message').classList.remove('error');
            }, 5000);
            return;
        }
        const order = {
            price: totalPrice,
            orderTime: orderTime,
            clientId: userId,
            comment: document.querySelector('#comment textarea').value,
            address: address,
            duration: dayjs.duration(duration).toISOString(),
            proposals: proposalsToObj()
        };
        //console.log(order);
        tryOrder(order);
    });
    //console.log(dayjs.duration(duration).toISOString());
    //console.log(count);
})();
