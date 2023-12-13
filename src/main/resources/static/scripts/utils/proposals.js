export function getChosenProposalFromStorage() {
    if(!sessionStorage.getItem('proposals')) return new Map();
    const obj = JSON.parse(sessionStorage.getItem('proposals'));
    const res = new Map();
    obj.forEach((o) => {
        res.set(o.id, o.count);
    });
    return res;
}

export function jsonToChosenProposals(json) {
    const obj = JSON.parse(json);
    const res = new Map();
    obj.forEach((o) => {
        res.set(o.id, o.count);
    });
    return res;
}

export function chosenProposalsToObj(chosenProposals) {
    const res = [];
    chosenProposals.forEach((value, key) => {
        res.push({
            id: key,
            count: value,
        });
    });
    return res;
}

export function saveChosenProposalsToStorage(chosenProposals) {
    sessionStorage.setItem('proposals', JSON.stringify(chosenProposalsToObj(chosenProposals)));
}
