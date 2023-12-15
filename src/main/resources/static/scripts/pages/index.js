import {checkNotEmpty, initForms, setInputFilter} from '../utils/form.js'
import {initPopups, showPopup, closePopup} from '../utils/popup.js'
import {tryPostRequest, tryPutRequest, tryDeleteRequest} from '../utils/api.js'

const proposals = [];
const chosenProposals = getChosenProposalFromStorage();
const addProposalPopup = document.getElementById('add_proposal_popup');
const addProposalForm = document.getElementById('add_proposal_form');
const proposalCountInput = document.querySelector('.proposal_count input');
const minusButton = document.querySelector('.proposal_count .minus');
const plusButton = document.querySelector('.proposal_count .plus');
const priceEl = document.querySelector('#add_proposal_form .proposal_price');
let currentProposalComponent = null;

class ProposalComponent {
    constructor(proposalEl) {
        this.proposalEl = proposalEl;
        this.proposal = {
            id: proposalEl.getAttribute('data-id'),
            name: proposalEl.querySelector('.proposal_name').textContent,
            shortDescription: proposalEl.querySelector('.proposal_short_description').textContent,
            fullDescription: proposalEl.querySelector('.proposal_full_description').textContent,
            requiredCountOfEmployees: proposalEl.getAttribute('employees'),
            price: proposalEl.getAttribute('price'),
            time: proposalEl.getAttribute('duration'),
            type: proposalEl.getAttribute('data-type'),
        };
        this.addButton = proposalEl.querySelector('.add_button');
        this.initButton();
    }

    initButton() {
        if(!this.addButton) return;
        this.addButton.addEventListener('click', () => {
            let count = 1;
            if(chosenProposals.get(this.proposal.id)) {
                count = chosenProposals.get(this.proposal.id);
            }
            addProposalPopup.querySelector('.proposal_name').textContent = this.proposal.name;
            addProposalPopup.querySelector('.proposal_full_description').textContent = this.proposal.fullDescription;
            if(this.proposal.type === 'PER_AREA') {
                addProposalPopup.querySelector('.proposal_type').textContent = 'Square meters';
                proposalCountInput.readOnly = true;
                proposalCountInput.value = count * 40;
            }
            else if(this.proposal.type === 'PER_ITEM') {
                addProposalPopup.querySelector('.proposal_type').textContent = 'Count';
                proposalCountInput.readOnly = false;
                proposalCountInput.value = count;
            }
            currentProposalComponent = this;
            changePriceEl();
            showPopup(addProposalPopup);
        });
    }
}

function getChosenProposalFromStorage() {
    if(!sessionStorage.getItem('proposals')) return new Map();
    const obj = JSON.parse(sessionStorage.getItem('proposals'));
    const res = new Map();
    obj.forEach((o) => {
        res.set(o.id, o.count);
    });
    return res;
}

function jsonToChosenProposals(json) {
    const obj = JSON.parse(json);
    const res = new Map();
    obj.forEach((o) => {
        res.set(o.id, o.count);
    });
    return res;
}

function changePriceEl() {
    if(currentProposalComponent != null) {
        if(!proposalCountInput.value) {
            priceEl.textContent = parseFloat(currentProposalComponent.proposal.price);
        }
        else if(currentProposalComponent.proposal.type === 'PER_AREA') {
            priceEl.textContent = parseInt(proposalCountInput.value) / 40 * parseFloat(currentProposalComponent.proposal.price);
        }
        else {
            priceEl.textContent = parseInt(proposalCountInput.value) * parseFloat(currentProposalComponent.proposal.price);
        }
    }
}

function initProposals() {
    document.querySelectorAll('.proposal').forEach((proposalEl) => {
        proposals.push(new ProposalComponent(proposalEl));
    });
}

function chosenProposalsToObj(chosenProposals) {
    const res = [];
    chosenProposals.forEach((value, key) => {
        res.push({
            id: key,
            count: value,
        });
    });
    return res;
}

initProposals();
initPopups();
addProposalForm.addEventListener('submit', (e) => {
   e.preventDefault();
   let count;
   if(proposalCountInput.value) {
       count = parseInt(proposalCountInput.value);
       if(currentProposalComponent.proposal.type === 'PER_AREA') {
           count /= 40;
       }
   }
   else {
       count = 1;
   }
   chosenProposals.set(currentProposalComponent.proposal.id, count);
   sessionStorage.setItem('proposals', JSON.stringify(chosenProposalsToObj(chosenProposals)));
   console.log(sessionStorage.getItem('proposals'));
   currentProposalComponent = null;
   closePopup(addProposalPopup);
});
setInputFilter(proposalCountInput, function (value) {
    const regex = /^([1-9][0-9]{0,2})?$/;
    if(regex.test(value)) {
        changePriceEl();
        return true;
    }
    return false;
});
minusButton.addEventListener('click', () => {
    if(currentProposalComponent.proposal.type === 'PER_AREA') {
        if(parseInt(proposalCountInput.value) >= 80) {
            proposalCountInput.value = parseInt(proposalCountInput.value) - 40;
        }
    }
    else if(proposalCountInput.value > 1) {
        proposalCountInput.value--;
    }
    changePriceEl();
});
plusButton.addEventListener('click', () => {
    if(currentProposalComponent.proposal.type === 'PER_AREA') {
        if(parseInt(proposalCountInput.value) <= 920) {
            proposalCountInput.value = parseInt(proposalCountInput.value) + 40;
        }
    }
    else if(proposalCountInput.value < 999) {
        proposalCountInput.value++;
    }
    changePriceEl();
});
const employmentButton = document.querySelector('#employment button');
if(employmentButton) {
    employmentButton.addEventListener('click', () => {
        window.location.replace('/employment-form');
    });
}
