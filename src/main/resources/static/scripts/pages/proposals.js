import {checkNotEmpty, initForms, initCounters} from '../utils/form.js'
import {initPopups, showPopup, closePopup} from '../utils/popup.js'
import {tryPostRequest, tryPutRequest, tryDeleteRequest} from '../utils/api.js'

const proposals = [];
const proposalsTable = document.getElementById('proposals_table');
const emptyList = document.querySelector('.empty_list');
const addProposalButton = document.getElementById('add_proposal_button');
const addProposalPopup = document.getElementById('add_proposal_popup');
const editProposalPopup = document.getElementById('edit_proposal_popup');
const deleteProposalPopup = document.getElementById('delete_proposal_popup');
const addProposalForm = document.getElementById('add_proposal_form');
const editProposalForm = document.getElementById('edit_proposal_form');
const deleteProposalButton = document.querySelector('#delete_proposal_popup .delete_button');
const requestMessageEl = document.querySelector('.request_message');
const editPerItemEl = editProposalForm.querySelector('.per_item');
const editPerAreaEl = editProposalForm.querySelector('.per_area');
const addPerItemEl = editProposalForm.querySelector('.per_item');
const addPerAreaEl = editProposalForm.querySelector('.per_area');
let currentProposalComponent = null;

class ProposalComponent {
    constructor(proposalEl) {
        this.proposalEl = proposalEl;
        this.proposal = {
            id: proposalEl.getAttribute('data-id'),
            name: proposalEl.querySelector('.name').textContent,
            shortDescription: proposalEl.querySelector('.short_description').textContent,
            fullDescription: proposalEl.querySelector('.full_description').textContent,
            requiredCountOfEmployees: proposalEl.querySelector('.employees').textContent,
            price: proposalEl.querySelector('.price').textContent,
            time: proposalEl.querySelector('.time').textContent,
            type: proposalEl.querySelector('.type').textContent
        };
        this.proposalEl.querySelector('.time').textContent = dayjs.duration(this.proposal.time).as('minutes');
        this.editButton = proposalEl.querySelector('.edit_button');
        this.deleteButton = proposalEl.querySelector('.delete_button');
        this.initButtons();
    }

    initButtons() {
        this.editButton.addEventListener('click', () => {
            currentProposalComponent = this;
            editProposalPopup.querySelector('.name').value = this.proposal.name;
            editProposalPopup.querySelector('.short_description').value = this.proposal.shortDescription;
            editProposalPopup.querySelector('.full_description').value = this.proposal.fullDescription;
            editProposalPopup.querySelector('.employees').value = this.proposal.requiredCountOfEmployees;
            editProposalPopup.querySelector('.price').value = this.proposal.price;
            editProposalPopup.querySelector('.time').value = dayjs.duration(this.proposal.time).as('minutes');
            if(this.proposal.type == 'PER_ITEM') {
                editPerItemEl.selected = true;
                editPerAreaEl.selected = false;
            }
            else {
                editPerItemEl.selected = false;
                editPerAreaEl.selected = true;
            }
            showPopup(editProposalPopup);
        });
        this.deleteButton.addEventListener('click', () => {
            currentProposalComponent = this;
            showPopup(deleteProposalPopup);
        });
    }

    edit(proposal) {
        this.proposal = proposal;
        this.proposalEl.querySelector('.name').textContent = proposal.name;
        this.proposalEl.querySelector('.short_description').textContent = proposal.shortDescription;
        this.proposalEl.querySelector('.full_description').textContent = proposal.fullDescription;
        this.proposalEl.querySelector('.employees').textContent = proposal.requiredCountOfEmployees;
        this.proposalEl.querySelector('.price').textContent = proposal.price;
        this.proposalEl.querySelector('.time').textContent = dayjs.duration(this.proposal.time).as('minutes');
        this.proposalEl.querySelector('.type').textContent = proposal.type;
    }
}

function initProposals() {
    document.querySelectorAll('#proposals_table tbody tr').forEach((proposalEl) => {
        proposals.push(new ProposalComponent(proposalEl));
    });
}

function createProposalElement(proposal) {
    const proposalElement = document.createElement('tr');
    proposalElement.setAttribute('data-id', proposal.id);
    proposalElement.innerHTML = `
        <td class="name">${proposal.name}</td>
        <td class="short_description">${proposal.shortDescription}</td>
        <td class="full_description not_visible">${proposal.fullDescription}</td>
        <td class="employees">${proposal.requiredCountOfEmployees}</td>
        <td class="price">${parseInt(proposal.price)}</td>
        <td class="time">${proposal.time}</td>
        <td class="type">${proposal.type}</td>
        <td>
            <button class="edit_button grey">Edit</button>
            <button class="delete_button red">Delete</button>
        </td>`;
    return proposalElement;
}

async function tryDeleteProposal() {
    const proposalId = currentProposalComponent.proposal.id;
    const url = `/api/commercial-proposals/${proposalId}`;
    const res = await tryDeleteRequest(requestMessageEl, url, 'Proposal was successfully deleted!', 'Proposal wasn\'t deleted!');
    if(res) {
        currentProposalComponent.proposalEl.remove();
        proposals.splice(proposals.indexOf(currentProposalComponent), 1);
        if(proposals.length === 0) {
            proposalsTable.classList.add('not_visible');
            emptyList.classList.remove('not_visible');
        }
    }
    currentProposalComponent = null;
    closePopup(deleteProposalPopup);
}

async function tryEditProposal() {
    const url = `/api/commercial-proposals`;
    const proposalId = currentProposalComponent.proposal.id;
    const price = editProposalForm.querySelector('.price').value ? editProposalForm.querySelector('.price').value : 1;
    const time = editProposalForm.querySelector('.time').value ? dayjs.duration(editProposalForm.querySelector('.time').value, 'minutes').toISOString() : dayjs.duration(1, 'minutes').toISOString();
    const employees = editProposalForm.querySelector('.employees').value ? editProposalForm.querySelector('.employees').value : 1;
    const proposal = {
        id: proposalId,
        name: editProposalForm.querySelector('.name').value,
        shortDescription: editProposalForm.querySelector('.short_description').value,
        fullDescription: editProposalForm.querySelector('.full_description').value,
        requiredCountOfEmployees: employees,
        price: price,
        time: time,
        type: editPerItemEl.selected ? 'PER_ITEM' : 'PER_AREA'
    };
    const res = await tryPutRequest(requestMessageEl, url, JSON.stringify(proposal), 'Proposal was successfully edited!', 'Proposal wasn\'t edited!');
    if(res.type === 'success') {
        currentProposalComponent.edit(res.response);
    }
    currentProposalComponent = null;
    closePopup(editProposalPopup);
}

function addProposal(proposal) {
    if(proposals.length === 0) {
        proposalsTable.classList.remove('not_visible');
        emptyList.classList.add('not_visible');
    }
    const proposalsListEl = document.querySelector('#proposals_table tbody');
    const proposalEl = createProposalElement(proposal);
    proposalsListEl.appendChild(proposalEl);
    proposals.push(new ProposalComponent(proposalEl));
}

async function tryAddProposal() {
    const url = `/api/commercial-proposals`;
    const price = addProposalForm.querySelector('.price').value ? addProposalForm.querySelector('.price').value : 1;
    const time = addProposalForm.querySelector('.time').value ? dayjs.duration(addProposalForm.querySelector('.time').value, 'minutes').toISOString() : dayjs.duration(1, 'minutes').toISOString();
    const employees = addProposalForm.querySelector('.employees').value ? addProposalForm.querySelector('.employees').value : 1;
    const proposal = {
        name: addProposalForm.querySelector('.name').value,
        shortDescription: addProposalForm.querySelector('.short_description').value,
        fullDescription: addProposalForm.querySelector('.full_description').value,
        requiredCountOfEmployees: employees,
        price: price,
        time: time,
        type: addPerItemEl.selected ? 'PER_ITEM' : 'PER_AREA'
    };
    const res = await tryPostRequest(requestMessageEl, url, JSON.stringify(proposal), 'Proposal was successfully added!', 'Proposal wasn\'t added!');
    if(res.type === 'success') {
        addProposal(res.response);
    }
    closePopup(addProposalPopup);
}

initForms();
initPopups();
initProposals();
initCounters();
addProposalButton.addEventListener('click', () => {
    addProposalPopup.querySelector('.employees').value = 1;
    addProposalPopup.querySelector('.time').value = 1;
    addProposalPopup.querySelector('.price').value = 1;
    addPerItemEl.selected = true;
    addPerAreaEl.selected = false;
    showPopup(addProposalPopup);
});
addProposalForm.addEventListener('submit', (e) => {
    e.preventDefault();
    let isFormValid = true;
    for(const notEmptyEl of addProposalForm.querySelectorAll('.not_empty')) {
        isFormValid = isFormValid && checkNotEmpty(notEmptyEl);
    }
    if(isFormValid) {
        tryAddProposal();
    }
});
editProposalForm.addEventListener('submit', (e) => {
    e.preventDefault();
    let isFormValid = true;
    for(const notEmptyEl of editProposalForm.querySelectorAll('.not_empty')) {
        isFormValid = isFormValid && checkNotEmpty(notEmptyEl);
    }
    if(isFormValid) {
        tryEditProposal();
    }
});
deleteProposalButton.addEventListener('click', () => {
    tryDeleteProposal();
});

