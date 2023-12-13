import {checkNotEmpty, initForms, setInputFilter} from '../utils/form.js'
import {initPopups, showPopup, closePopup} from '../utils/popup.js'
import {tryPostRequest, tryPutRequest, tryDeleteRequest, tryGetRequest} from '../utils/api.js'
import {
    getChosenProposalFromStorage,
    jsonToChosenProposals,
    chosenProposalsToObj,
    saveChosenProposalsToStorage
} from '../utils/proposals.js'

const chosenProposals = getChosenProposalFromStorage();
const proposalsTable = document.querySelector('#proposals_table')
const emptyList = document.querySelector('.empty_list');
const proposalsTableBody = document.querySelector('#proposals_table tbody');
const createOrderForm = document.getElementById('create_order_form');
const totalCostEl = document.getElementById('price');
let totalCost = 0;
const proposals = [];

class ProposalComponent {
    constructor(proposal, count) {
        this.proposal = proposal;
        this.count = count;
        this.proposalEl = this.createHtmlProposal();
        proposalsTableBody.appendChild(this.proposalEl);
        this.removeButton = this.proposalEl.querySelector('.remove_button');
        this.initButton();
    }

    initButton() {
        this.removeButton.addEventListener('click', () => {
            this.proposalEl.remove();
            chosenProposals.delete(this.proposal.id.toString());
            saveChosenProposalsToStorage(chosenProposals);
            totalCost -= this.proposal.price * this.count;
            totalCostEl.querySelector('.total_cost').textContent = totalCost;
            proposals.splice(proposals.indexOf(this), 1);
            if(proposals.length === 0) {
                proposalsTable.classList.add('not_visible');
                createOrderForm.classList.add('not_visible');
                totalCostEl.classList.add('not_visible');
                emptyList.classList.remove('not_visible');
            }
        });
    }

    createHtmlProposal() {
        const proposalEl = document.createElement('tr');
        proposalEl.innerHTML = `
        <td class="name">${this.proposal.name}</td>
        <td class="description">${this.proposal.shortDescription}</td>
        <td class="count">${this.proposal.type === 'PER_AREA' ? this.count * 40 + ' M<sup>2</sup>' : this.count}</td>
        <td class="price">${this.proposal.price * this.count}</td>
        <td>
            <div class="button_wrapper">
                <button class="remove_button red">Remove</button>
            </div>
        </td>`;
        return proposalEl;
    }
}

async function initProposals() {
    for(const [id, count] of chosenProposals) {
        const url = `/api/commercial-proposals/${id}`;
        const response = await tryGetRequest(null, url, '', '');
        //console.log(response.type);
        if(response.type === 'success') {
            const proposal = response.response;
            totalCost += proposal.price * count;
            proposals.push(new ProposalComponent(proposal, count));
        }
    }
    totalCostEl.querySelector('.total_cost').textContent = totalCost;
}

if(chosenProposals.size > 0) {
    proposalsTable.classList.remove('not_visible');
    createOrderForm.classList.remove('not_visible');
    totalCostEl.classList.remove('not_visible');
}
else {
    emptyList.classList.remove('not_visible');
}
initProposals();

