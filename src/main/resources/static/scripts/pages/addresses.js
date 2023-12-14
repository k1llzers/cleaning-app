import {checkNotEmpty, initForms} from '../utils/form.js'
import {initPopups, showPopup, closePopup} from '../utils/popup.js'
import {tryPostRequest, tryPutRequest, tryDeleteRequest} from '../utils/api.js'

const addresses = [];
const addressTable = document.getElementById('addresses_table');
const emptyList = document.querySelector('.empty_list')
const addAddressButton = document.getElementById('add_address_button');
const addAddressPopup = document.getElementById('add_address_popup');
const editAddressPopup = document.getElementById('edit_address_popup');
const deleteAddressPopup = document.getElementById('delete_address_popup');
const addAddressForm = document.getElementById('add_address_form');
const editAddressForm = document.getElementById('edit_address_form');
const deleteAddressButton = document.querySelector('#delete_address_popup .delete_button');
const requestMessageEl = document.querySelector('.request_message');
const userId = document.getElementById('addresses_table').getAttribute('user-id');
let currentAddressComponent = null;

class AddressComponent {
    constructor(addressEl) {
        this.addressEl = addressEl;
        this.address = {
            id: addressEl.getAttribute('data-id'),
            city: addressEl.querySelector('.city').textContent,
            street: addressEl.querySelector('.street').textContent,
            houseNumber: addressEl.querySelector('.house_number').textContent,
            flatNumber: addressEl.querySelector('.flat_number').textContent,
            zip: addressEl.querySelector('.zip').textContent
        };
        this.editButton = addressEl.querySelector('.edit_button');
        this.deleteButton = addressEl.querySelector('.delete_button');
        this.initButtons();
    }

    initButtons() {
        this.editButton.addEventListener('click', () => {
            currentAddressComponent = this;
            editAddressPopup.querySelector('.city').value = this.address.city;
            editAddressPopup.querySelector('.street').value = this.address.street;
            editAddressPopup.querySelector('.house_number').value = this.address.houseNumber;
            editAddressPopup.querySelector('.flat_number').value = this.address.flatNumber;
            editAddressPopup.querySelector('.zip').value = this.address.zip;
            showPopup(editAddressPopup);
        });
        this.deleteButton.addEventListener('click', () => {
            currentAddressComponent = this;
            showPopup(deleteAddressPopup);
        });
    }

    edit(address) {
        this.address = address;
        this.addressEl.querySelector('.city').textContent = address.city;
        this.addressEl.querySelector('.street').textContent = address.street;
        this.addressEl.querySelector('.house_number').textContent = address.houseNumber;
        this.addressEl.querySelector('.flat_number').textContent = address.flatNumber;
        this.addressEl.querySelector('.zip').textContent = address.zip;
    }
}

function initAddresses() {
    document.querySelectorAll('#addresses_table tbody tr').forEach((addressEl) => {
        addresses.push(new AddressComponent(addressEl));
    });
}

function createAddressElement(address) {
    const addressElement = document.createElement('tr');
    addressElement.setAttribute('data-id', address.id);
    addressElement.innerHTML = `
        <td class="city">${address.city}</td>
        <td class="street">${address.street}</td>
        <td class="house_number">${address.houseNumber}</td>
        <td class="flat_number">${address.flatNumber}</td>
        <td class="zip">${address.zip}</td>
        <td>
            <button class="edit_button grey">Edit</button>
            <button class="delete_button red">Delete</button>
        </td>`;
    return addressElement;
}

function addAddress(address) {
    if(addresses.length === 0) {
        addressTable.classList.remove('not_visible');
        emptyList.classList.add('not_visible');
    }
    const addressesListEl = document.querySelector('#addresses_table tbody');
    const addressEl = createAddressElement(address);
    addressesListEl.appendChild(addressEl);
    addresses.push(new AddressComponent(addressEl));
}

async function tryAddAddress() {
    const url = `/api/addresses`;
    const address = {
        city: addAddressForm.querySelector('.city').value,
        street: addAddressForm.querySelector('.street').value,
        houseNumber: addAddressForm.querySelector('.house_number').value,
        flatNumber: addAddressForm.querySelector('.flat_number').value,
        zip: addAddressForm.querySelector('.zip').value
    };
    const res = await tryPostRequest(requestMessageEl, url, JSON.stringify(address), 'Address was successfully added!', 'Address wasn\'t added!');
    if(res.type === 'success') {
        addAddress(res.response);
    }
    closePopup(addAddressPopup);
}

async function tryDeleteAddress() {
    const addressId = currentAddressComponent.address.id;
    const url = `/api/addresses/${addressId}`;
    const res = await tryDeleteRequest(requestMessageEl, url, 'Address was successfully deleted!', 'Address wasn\'t deleted!');
    if(res) {
        currentAddressComponent.addressEl.remove();
        addresses.splice(addresses.indexOf(currentAddressComponent), 1);
        if(addresses.length === 0) {
            addressTable.classList.add('not_visible');
            emptyList.classList.remove('not_visible');
        }
    }
    currentAddressComponent = null;
    closePopup(deleteAddressPopup);
}

async function tryEditAddress() {
    const url = `/api/addresses`;
    const addressId = currentAddressComponent.address.id;
    const address = {
        id: addressId,
        city: editAddressForm.querySelector('.city').value,
        street: editAddressForm.querySelector('.street').value,
        houseNumber: editAddressForm.querySelector('.house_number').value,
        flatNumber: editAddressForm.querySelector('.flat_number').value,
        zip: editAddressForm.querySelector('.zip').value
    };
    const res = await tryPutRequest(requestMessageEl, url, JSON.stringify(address), 'Address was successfully edited!', 'Address wasn\'t edited!');
    if(res.type === 'success') {
        currentAddressComponent.edit(res.response);

    }
    currentAddressComponent = null;
    closePopup(editAddressPopup);
}

initPopups();
initForms();
initAddresses();
addAddressButton.addEventListener('click', () => {
    showPopup(addAddressPopup);
});
addAddressForm.addEventListener('submit', (e) => {
    e.preventDefault();
    let isFormValid = true;
    for(const notEmptyEl of addAddressForm.querySelectorAll('.not_empty')) {
        isFormValid = isFormValid && checkNotEmpty(notEmptyEl);
    }
    if(isFormValid) {
        tryAddAddress();
    }
});
editAddressForm.addEventListener('submit', (e) => {
    e.preventDefault();
    let isFormValid = true;
    for(const notEmptyEl of editAddressForm.querySelectorAll('.not_empty')) {
        isFormValid = isFormValid && checkNotEmpty(notEmptyEl);
    }
    if(isFormValid) {
        tryEditAddress();
    }
});
deleteAddressButton.addEventListener('click', () => {
    tryDeleteAddress();
});
