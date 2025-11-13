const API_BASE = '/api';

let cy = cytoscape({
    container: document.getElementById('cy'),
    style: [
        {
            selector: 'node',
            style: {
                'background-color': '#3498db',
                'label': 'data(label)',
                'color': '#fff',
                'text-valign': 'center',
                'text-halign': 'center',
                'width': '80px',
                'height': '80px',
                'font-size': '14px',
                'font-weight': 'bold'
            }
        },
        {
            selector: 'node[type="INITIAL"]',
            style: {
                'background-color': '#27ae60',
                'shape': 'roundrectangle'
            }
        },
        {
            selector: 'node[type="FINAL"]',
            style: {
                'background-color': '#e74c3c',
                'shape': 'diamond'
            }
        },
        {
            selector: 'edge',
            style: {
                'width': 3,
                'line-color': '#95a5a6',
                'target-arrow-color': '#95a5a6',
                'target-arrow-shape': 'triangle',
                'curve-style': 'bezier',
                'label': 'data(label)',
                'font-size': '12px',
                'text-rotation': 'autorotate',
                'text-margin-y': -10
            }
        }
    ],
    layout: {
        name: 'circle',
        padding: 50
    }
});

let currentTemplate = {
    states: [],
    transitions: []
};

function addState() {
    const name = document.getElementById('stateName').value.trim();
    const type = document.getElementById('stateType').value;

    if (!name) {
        alert('Please enter a state name');
        return;
    }

    if (currentTemplate.states.find(s => s.stateName === name)) {
        alert('State already exists');
        return;
    }

    const state = {
        stateName: name,
        stateType: type,
        positionX: 0,
        positionY: 0
    };

    currentTemplate.states.push(state);

    cy.add({
        group: 'nodes',
        data: {
            id: name,
            label: name,
            type: type
        }
    });

    cy.layout({ name: 'circle' }).run();

    document.getElementById('stateName').value = '';
    document.getElementById('stateType').value = 'NORMAL';
}

function addTransition() {
    const source = document.getElementById('sourceState').value.trim();
    const target = document.getElementById('targetState').value.trim();
    const event = document.getElementById('eventName').value.trim();

    if (!source || !target || !event) {
        alert('Please fill all transition fields');
        return;
    }

    if (!currentTemplate.states.find(s => s.stateName === source)) {
        alert('Source state does not exist');
        return;
    }

    if (!currentTemplate.states.find(s => s.stateName === target)) {
        alert('Target state does not exist');
        return;
    }

    const transition = {
        sourceState: source,
        targetState: target,
        eventName: event,
        orderIndex: currentTemplate.transitions.length
    };

    currentTemplate.transitions.push(transition);

    cy.add({
        group: 'edges',
        data: {
            id: `${source}-${target}-${event}`,
            source: source,
            target: target,
            label: event
        }
    });

    document.getElementById('sourceState').value = '';
    document.getElementById('targetState').value = '';
    document.getElementById('eventName').value = '';
}

async function saveTemplate() {
    const name = document.getElementById('templateName').value.trim();
    const description = document.getElementById('templateDescription').value.trim();
    const initialState = document.getElementById('initialState').value.trim();

    if (!name || !initialState) {
        alert('Please enter template name and initial state');
        return;
    }

    if (currentTemplate.states.length === 0) {
        alert('Please add at least one state');
        return;
    }

    if (!currentTemplate.states.find(s => s.stateName === initialState)) {
        alert('Initial state must exist in the states list');
        return;
    }

    const template = {
        name: name,
        description: description,
        initialState: initialState,
        states: currentTemplate.states,
        transitions: currentTemplate.transitions
    };

    try {
        const response = await fetch(`${API_BASE}/templates`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(template)
        });

        if (response.ok) {
            const saved = await response.json();
            alert('Template saved successfully!');
            console.log('Saved template:', saved);
            loadTemplates();
        } else {
            const error = await response.text();
            alert('Error saving template: ' + error);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error saving template: ' + error.message);
    }
}

async function loadTemplates() {
    try {
        const response = await fetch(`${API_BASE}/templates`);
        if (response.ok) {
            const templates = await response.json();
            displayTemplates(templates);
        } else {
            alert('Error loading templates');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error loading templates: ' + error.message);
    }
}

function displayTemplates(templates) {
    const listDiv = document.getElementById('templateList');
    if (templates.length === 0) {
        listDiv.innerHTML = '<p style="text-align: center; color: #999;">No templates found</p>';
        return;
    }

    listDiv.innerHTML = '';
    templates.forEach(template => {
        const item = document.createElement('div');
        item.className = 'template-item';
        item.innerHTML = `<strong>${template.name}</strong><br><small>${template.description || 'No description'}</small>`;
        item.onclick = () => loadTemplate(template);
        listDiv.appendChild(item);
    });
}

function loadTemplate(template) {
    clearCanvas();

    document.getElementById('templateName').value = template.name;
    document.getElementById('templateDescription').value = template.description || '';
    document.getElementById('initialState').value = template.initialState;

    currentTemplate.states = [...template.states];
    currentTemplate.transitions = [...template.transitions];

    // Add nodes
    template.states.forEach(state => {
        cy.add({
            group: 'nodes',
            data: {
                id: state.stateName,
                label: state.stateName,
                type: state.stateType
            }
        });
    });

    // Add edges
    template.transitions.forEach(transition => {
        cy.add({
            group: 'edges',
            data: {
                id: `${transition.sourceState}-${transition.targetState}-${transition.eventName}`,
                source: transition.sourceState,
                target: transition.targetState,
                label: transition.eventName
            }
        });
    });

    cy.layout({ name: 'circle' }).run();
}

function clearCanvas() {
    cy.elements().remove();
    currentTemplate = {
        states: [],
        transitions: []
    };
    document.getElementById('templateName').value = '';
    document.getElementById('templateDescription').value = '';
    document.getElementById('initialState').value = '';
}

// Load templates on page load
window.addEventListener('load', () => {
    loadTemplates();
});
